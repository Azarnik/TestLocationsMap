package com.test.epm.locationmap.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.test.epm.locationmap.R
import com.test.epm.locationmap.data.local.entity.BaseLocation
import com.test.epm.locationmap.data.local.entity.DefaultLocation
import com.test.epm.locationmap.data.local.entity.UserLocation
import com.test.epm.locationmap.ui.detail.DetailActivity
import com.test.epm.locationmap.ui.locationlist.LocationListActivity
import com.test.epm.locationmap.ui.maps.dialog.LocationNameDialog
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject

class MapActivity : DaggerAppCompatActivity(), OnMapReadyCallback,
        MapContract.View, LocationNameDialog.NameEnteredListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM = 10f
    }

    @Inject
    lateinit var presenter: MapPresenter

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: LatLng? = null

    private val defaultMarkerList: MutableList<Marker> = mutableListOf()
    private val userMarkerList: MutableList<Marker> = mutableListOf()

    private var isFirstStart: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        if (savedInstanceState != null)
            isFirstStart = false

        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        presenter.attachView(this)
        presenter.fetchAllData()
        this.googleMap.setOnMapLongClickListener({
            presenter.openCreateLocationDialog(it)
        })

        this.googleMap.setOnInfoWindowClickListener { presenter.openLocationDetails(it.tag as BaseLocation) }
        verifyDeviceLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_show_list -> {
            presenter.openLocationList()
            true
        }
        R.id.action_update_from_server -> {
            presenter.updateDefaultLocations()
            true
        }
        else -> false
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showLocationListUi() {
        if (lastKnownLocation != null)
            with(Intent(this, LocationListActivity::class.java)) {
                putExtra(LocationListActivity.ARGS_LATITUDE, lastKnownLocation!!.latitude)
                putExtra(LocationListActivity.ARGS_LONGITUDE, lastKnownLocation!!.longitude)
                startActivity(this)
            }
    }

    override fun showLocationDetailsUi(id: Long, isDefault: Boolean) {
        with(Intent(this, DetailActivity::class.java)) {
            putExtra(DetailActivity.ARGS_LOCATION_ID, id)
            putExtra(DetailActivity.ARGS_IS_DEFAULT_LOCATION, isDefault)
            startActivity(this)
        }
    }

    override fun showDefaultLocationsMarkers(locationsList: List<DefaultLocation>) {
        showMarkerOnMap(locationsList, 180f, defaultMarkerList)
    }

    override fun showUserLocationsMarkers(locationsList: List<UserLocation>) {
        showMarkerOnMap(locationsList, 90f, userMarkerList)
    }

    override fun clearDefaultMarkers() {
        clearMarkers(defaultMarkerList)
    }

    override fun clearUserMarkers() {
        clearMarkers(userMarkerList)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    verifyDeviceLocation()
                } else {
                    showPermissionDeniedAlert()
                }
            }
        }
    }

    override fun onNameEntered(name: String, lat: Float, lng: Float) =
            presenter.addUserLocation(name, lat, lng)

    override fun showAddLocationDialog(latLng: LatLng) {
        val dialog = LocationNameDialog.newInstance(latLng)
        dialog.show(supportFragmentManager, "LocationNameDialog")
    }

    override fun showUpdateDefaultLocationError() {
        showMessage(getString(R.string.error_while_updating_from_server))
    }

    override fun showLoadingDefaultLocationsError() {
        showMessage(getString(R.string.error_while_loading_default_locations))
    }

    override fun showLoadingUserLocationsError() {
        showMessage(getString(R.string.error_while_loading_user_locations))
    }

    override fun showDefaultLocationsUpdated() {
        showMessage(getString(R.string.default_locations_updated))
    }

    override fun showAddUserLocationError() {
        showMessage(getString(R.string.error_add_user_location))
    }

    override fun showNewLocationAdded() {
        showMessage(getString(R.string.new_location_added))
    }

    fun isFirstStart(): Boolean = isFirstStart

    private fun clearMarkers(markerList: MutableList<Marker>) {
        markerList.forEach { it.remove() }
        markerList.clear()
    }

    private fun showPermissionDeniedAlert() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage(R.string.permission_denied_message)
        alertBuilder.setPositiveButton(R.string.ok, null)
        alertBuilder.setOnDismissListener { verifyDeviceLocation() }
        alertBuilder.show()
    }

    private fun showMarkerOnMap(locationsList: List<BaseLocation>, markerColor: Float, markerList: MutableList<Marker>) {
        clearMarkers(markerList)
        for (location in locationsList) {
            val position = LatLng(location.lat.toDouble(), location.lng.toDouble())
            val marker = googleMap.addMarker(
                    MarkerOptions()
                            .position(position).title(location.name)
                            .icon(BitmapDescriptorFactory.defaultMarker(markerColor)))
            marker.tag = location
            markerList.add(marker)
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(coordinator, message, Snackbar.LENGTH_LONG).show()
    }

    private fun verifyDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true

            val locationResult: Task<Location> = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(this, {
                if (it.isSuccessful && it.result != null) {
                    lastKnownLocation = LatLng(it.result.latitude, it.result.longitude)
                    if (isFirstStart)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, DEFAULT_ZOOM))

                }
            })
        }
    }
}

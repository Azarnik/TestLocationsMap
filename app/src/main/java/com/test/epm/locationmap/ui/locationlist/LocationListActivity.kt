package com.test.epm.locationmap.ui.locationlist

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import com.test.epm.locationmap.R
import com.test.epm.locationmap.ui.detail.DetailActivity
import com.test.epm.locationmap.ui.locationlist.entity.LocationDistance
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_location_list.*
import javax.inject.Inject

class LocationListActivity : DaggerAppCompatActivity(), LocationListContract.View {
    companion object {
        const val ARGS_LATITUDE = "ARGS_LATITUDE"
        const val ARGS_LONGITUDE = "ARGS_LONGITUDE"
    }

    @Inject
    lateinit var presenter: LocationListPresenter

    private var adapter: LocationRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        adapter = LocationRecyclerViewAdapter({ location -> presenter.openLocationDetail(location.location) })

        location_recycle_view.layoutManager = LinearLayoutManager(this)
        location_recycle_view.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun showLocationsList(locationsList: List<LocationDistance>) {
        adapter?.updateItems(locationsList);
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
        presenter.getAllLocations()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showLocationDetailUI(id: Long, isDefault: Boolean) {
        with(Intent(this, DetailActivity::class.java)) {
            putExtra(DetailActivity.ARGS_LOCATION_ID, id)
            putExtra(DetailActivity.ARGS_IS_DEFAULT_LOCATION, isDefault)
            startActivity(this)
        }
    }

    override fun showGetLocationsError() {
        Snackbar.make(constraint_location_list, getString(R.string.error_while_loading_locations), Snackbar.LENGTH_LONG).show()
    }

}
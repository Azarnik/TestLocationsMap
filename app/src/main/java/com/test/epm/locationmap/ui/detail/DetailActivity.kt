package com.test.epm.locationmap.ui.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.test.epm.locationmap.R
import com.test.epm.locationmap.data.local.entity.BaseLocation
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject

class DetailActivity : DaggerAppCompatActivity(), DetailContract.View {
    companion object {
        const val ARGS_LOCATION_ID = "ARGS_LOCATION_ID"
        const val ARGS_IS_DEFAULT_LOCATION = "ARGS_IS_DEFAULT_LOCATION"
    }

    @Inject
    lateinit var presenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        with(supportActionBar) {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
        presenter.getLocationDetails()
    }

    override fun onPause() {
        super.onPause()
        presenter.updateDescription(description.text.toString())
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_delete_location -> {
            showDeleteConfirmationDialog()
            true
        }
        else -> false
    }

    override fun showLocationDetail(location: BaseLocation) {
        supportActionBar?.title = location.name
        description.setText(location.description)
    }

    override fun showGetLocationDetailsError() {
        Snackbar.make(constraint_detail, getString(R.string.error_while_loading_location_details), Snackbar.LENGTH_LONG).show()
    }

    override fun showCannotDeleteDefaultLocation() {
        Snackbar.make(constraint_detail, getString(R.string.cannot_delete_default_location), Snackbar.LENGTH_LONG).show()
    }

    override fun showDeleteConfirmationDialog() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle(getString(R.string.delete_location))
        alertBuilder.setPositiveButton(R.string.delete, { _, _ -> presenter.deleteLocation() })
        alertBuilder.setNegativeButton(R.string.cancel, null)
        alertBuilder.show()
    }

    override fun closeDetailsScreen() {
        finish()
    }
}

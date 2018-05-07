package com.test.epm.locationmap.ui.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.test.epm.locationmap.R
import com.test.epm.locationmap.data.local.entity.BaseLocation
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_location_list.*
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

    override fun showLocationDetail(location: BaseLocation) {
        supportActionBar?.title = location.name
        description.setText(location.description)
    }

    override fun showGetLocationError() {
        Snackbar.make(constraint_location_list, getString(R.string.error_while_loading_location_details), Snackbar.LENGTH_LONG).show()
    }
}

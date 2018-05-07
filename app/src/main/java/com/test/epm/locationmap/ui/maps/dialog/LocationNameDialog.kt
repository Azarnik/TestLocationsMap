package com.test.epm.locationmap.ui.maps.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import com.test.epm.locationmap.R

class LocationNameDialog : DialogFragment() {
    companion object {
        private const val LATITUDE = "LATITUDE"
        private const val LONGITUDE = "LONGITUDE"

        fun newInstance(latLng: LatLng): LocationNameDialog {
            val fragment = LocationNameDialog()
            val args = Bundle()
            args.putFloat(LATITUDE, latLng.latitude.toFloat())
            args.putFloat(LONGITUDE, latLng.longitude.toFloat())
            fragment.arguments = args
            return fragment
        }
    }

    interface NameEnteredListener {
        fun onNameEntered(name: String, lat: Float, lng: Float)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val lat = arguments!!.getFloat(LATITUDE)
        val lng = arguments!!.getFloat(LONGITUDE)
        val builder = AlertDialog.Builder(activity)
        val editText =
                activity!!.layoutInflater.inflate(R.layout.dialog_name, null) as EditText
        builder.setView(editText)
        val listener: NameEnteredListener = activity as NameEnteredListener
        builder.setMessage(getString(R.string.message_location_name_dialog))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    listener.onNameEntered(editText.text.toString(), lat, lng)
                    dismiss()
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    dismiss()
                }
        return builder.create()
    }

}

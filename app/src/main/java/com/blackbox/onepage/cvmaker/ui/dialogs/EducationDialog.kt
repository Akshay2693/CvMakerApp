package com.blackbox.onepage.cvmaker.ui.dialogs

/**
 * Created by Doctor on 19/12/2016.
 */

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.AppCompatEditText
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.models.EducationInfo
import com.blackbox.onepage.cvmaker.rxBus.RxBus
import com.blackbox.onepage.cvmaker.ui.activities.CVCreaterActivity

class EducationDialog : DialogFragment() {

    private lateinit var _rxBus: RxBus

    companion object {
        val TAG = "EducationDialog"


        fun show(activity: Activity) {
            EducationDialog().show(activity.fragmentManager, TAG)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.dialog_education, null)

        _rxBus = (activity as CVCreaterActivity).rxBusSingleton

        val institute = view.findViewById(R.id.input_institute) as AppCompatEditText
        val startYear = view.findViewById(R.id.input_institute) as AppCompatEditText
        val endYear = view.findViewById(R.id.input_institute) as AppCompatEditText
        val degree = view.findViewById(R.id.input_institute) as AppCompatEditText

        return AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(getString(R.string.txt_done)) {
                    dialog, which ->

                    var info: EducationInfo = EducationInfo(null, institute.text.toString()
                            , startYear.text.toString()
                            , endYear.text.toString()
                            , degree.text.toString())

                    if (_rxBus.hasObservers()) {
                        _rxBus.send(info)
                    }

                }.setNegativeButton(getString(R.string.txt_cancel)) {
            dialog, which ->

        }
                .create()
    }
}
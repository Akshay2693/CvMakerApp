package com.blackbox.onepage.cvmaker.ui.activities

import android.app.ProgressDialog
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.db.AppDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class CVViewerActivity : AppCompatActivity() {

    val TAG: String = "CVViewerActivity"

    val threadExecutor: Executor = Executors.newFixedThreadPool(5)

    val db: AppDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "user-database").build()

    private var progressDialog: ProgressDialog? = null
    private var message: String? = null

    @Synchronized fun show() {
        progressDialog!!.setMessage(message)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.show()
    }

    @Synchronized fun dismiss() {
        if (progressDialog == null) {
            return
        }

        progressDialog!!.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_viewer)

        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            message = getString(R.string.message_loading)
        }

        show()

    }


}

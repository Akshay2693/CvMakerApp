package com.blackbox.onepage.cvmaker.ui

import android.arch.persistence.room.Room
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.db.AppDatabase
import com.blackbox.onepage.cvmaker.models.User
import com.linkedin.platform.LISessionManager
import com.linkedin.platform.errors.LIAuthError
import com.linkedin.platform.listeners.AuthListener
import com.linkedin.platform.utils.Scope
import com.thebluealliance.spectrum.SpectrumDialog
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class CVCreaterActivity : AppCompatActivity() {

    val TAG: String = "CVCreaterActivity"

    val PACKAGE = "com.blackbox.onepage.cvmaker"

    val threadExecutor: Executor = Executors.newFixedThreadPool(5)

    val db: AppDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "user-database").build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_creater)

        generateHashKey()

        val thisActivity = this

        LISessionManager.getInstance(applicationContext).init(thisActivity, buildScope(), object : AuthListener {
            override fun onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                Toast.makeText(getApplicationContext(), "success" +
                        LISessionManager
                                .getInstance(getApplicationContext())
                                .getSession().getAccessToken().toString(),
                        Toast.LENGTH_LONG).show();
            }

            override fun onAuthError(error: LIAuthError) {
                // Handle authentication errors
                Log.e(TAG, "Error: " + error.toString());
            }
        }, true)

        var background = findViewById(R.id.main_content) as CoordinatorLayout

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            pickColor(background)
        }
    }

    fun saveData(user: User) {
        try {
            threadExecutor.execute {
                Log.i(TAG, "Saving Data..");
                db.userDao().saveUser(user)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pickColor(view: View) {
        SpectrumDialog.Builder(this)
                .setColors(R.array.demo_colors)
                .setSelectedColorRes(R.color.md_blue_100)
                .setDismissOnColorSelected(false)
                .setOutlineWidth(2)
                .setOnColorSelectedListener(SpectrumDialog.OnColorSelectedListener { positiveResult, color ->
                    if (positiveResult) {
                        view.setBackgroundColor(color)
                        Toast.makeText(this, "Color selected: #" + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT).show()
                    }
                }).build().show(supportFragmentManager, "dialog_demo_2")
    }

    fun buildScope(): Scope {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(applicationContext).onActivityResult(this, requestCode, resultCode, data)
    }

    fun generateHashKey() {
        try {
            val info = packageManager.getPackageInfo(PACKAGE, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.i(TAG, Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("Name not found", e.message, e)

        } catch (e: NoSuchAlgorithmException) {
           e.printStackTrace()
        }

    }
}

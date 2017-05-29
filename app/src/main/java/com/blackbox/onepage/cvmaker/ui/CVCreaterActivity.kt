package com.blackbox.onepage.cvmaker.ui

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.blackbox.onepage.cvmaker.R
import com.blackbox.onepage.cvmaker.db.AppDatabase
import com.blackbox.onepage.cvmaker.models.User
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class CVCreaterActivity : AppCompatActivity() {

    val TAG: String = "CVCreaterActivity"

    val threadExecutor: Executor = Executors.newFixedThreadPool(5)

    val db: AppDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "user-database").build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_creater)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
}

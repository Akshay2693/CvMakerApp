package com.blackbox.onepage.cvmaker

import android.app.Application
import android.arch.persistence.room.Room
import com.blackbox.onepage.cvmaker.db.AppDatabase


/**
 * Created by umair on 26/05/2017.
 */

class MainApplication: Application()
{

    override fun onCreate() {
        super.onCreate()

    }

    fun getDB(): AppDatabase? {
        return Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user-database").build()
    }
}

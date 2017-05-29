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

    public fun getDB(): AppDatabase? {
        return Room.databaseBuilder(this, AppDatabase::class.java, "user-database").build()
    }
}

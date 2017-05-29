package com.blackbox.onepage.cvmaker.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.blackbox.onepage.cvmaker.models.User


/**
 * Created by umair on 26/05/2017.
 */
@Database(entities = arrayOf(User::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
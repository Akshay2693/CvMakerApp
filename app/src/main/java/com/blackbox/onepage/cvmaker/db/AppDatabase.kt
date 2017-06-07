package com.blackbox.onepage.cvmaker.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.blackbox.onepage.cvmaker.models.BasicInfo
import com.blackbox.onepage.cvmaker.models.EducationInfo


/**
 * Created by umair on 26/05/2017.
 */
@Database(entities = arrayOf(BasicInfo::class,EducationInfo::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): BasicInfoDao
    abstract fun educationDao(): EducationDao

    companion object{
        private val databaseName = "user-database"

        var dbInstance:AppDatabase? = null
        fun getInstance(context: Context):AppDatabase?{
            if(dbInstance == null)
                dbInstance = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
            return dbInstance;
        }
    }
}
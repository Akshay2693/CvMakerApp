package com.blackbox.onepage.cvmaker.db

import android.arch.persistence.room.*
import com.blackbox.onepage.cvmaker.models.BasicInfo


/**
 * Created by umair on 26/05/2017.
 */
@Dao
interface BasicInfoDao
{

    @Query("SELECT * FROM info")
    fun getAll(): List<BasicInfo>

    @Insert
    fun insertAll(vararg basicInfos: BasicInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(basicInfo: BasicInfo)

    @Delete
    fun delete(basicInfo: BasicInfo)

}
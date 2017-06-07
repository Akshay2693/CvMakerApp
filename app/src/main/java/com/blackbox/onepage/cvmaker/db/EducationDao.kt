package com.blackbox.onepage.cvmaker.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.blackbox.onepage.cvmaker.models.EducationInfo


/**
 * Created by umair on 26/05/2017.
 */
@Dao
interface EducationDao
{

    @Query("SELECT * FROM education")
    fun getAll(): List<EducationInfo>

    @Insert
    fun save(info: EducationInfo)

    @Delete
    fun delete(info: EducationInfo)

}
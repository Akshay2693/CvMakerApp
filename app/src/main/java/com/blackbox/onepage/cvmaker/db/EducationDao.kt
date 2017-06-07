package com.blackbox.onepage.cvmaker.db

import android.arch.persistence.room.*
import com.blackbox.onepage.cvmaker.models.EducationInfo


/**
 * Created by umair on 26/05/2017.
 */
@Dao
interface EducationDao
{

    @Query("SELECT * FROM education")
    fun getAll(): List<EducationInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(info: EducationInfo)

    @Delete
    fun delete(info: EducationInfo)

}
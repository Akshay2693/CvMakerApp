package com.blackbox.onepage.cvmaker.db

import android.arch.persistence.room.*
import com.blackbox.onepage.cvmaker.models.User


/**
 * Created by umair on 26/05/2017.
 */
@Dao
interface UserDao
{

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert
    fun insertAll(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User)

    @Delete
    fun delete(user: User)

}
package com.blackbox.onepage.cvmaker.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey



/**
 * Created by umair on 26/05/2017.
 */
@Entity(tableName = "user")
class User(
        @ColumnInfo(name = "first_name") var firstName: String? = "",
        @ColumnInfo(name = "last_name") var lastName: String? = "",
        @ColumnInfo(name = "website") var website: String? = "",
        @ColumnInfo(name = "phone") var phone: String? = "",
        @ColumnInfo(name = "email") var email: String? = "",
        @ColumnInfo(name = "title") var title: String? = "",
        @ColumnInfo(name = "description") var description: String? = ""
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
package com.blackbox.onepage.cvmaker.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable


/**
 * Created by umair on 26/05/2017.
 */
@Entity(tableName = "info")
class BasicInfo(
        @ColumnInfo(name = "id") @PrimaryKey var id: String? = "",
        @ColumnInfo(name = "first_name") var firstName: String? = "",
        @ColumnInfo(name = "last_name") var lastName: String? = "",
        @ColumnInfo(name = "title") var title: String? = "",
        @ColumnInfo(name = "summary") var summary: String? = "",
        @ColumnInfo(name = "industry") var industry: String? = "",
        @ColumnInfo(name = "pictureUrl") var pictureUrl: String? = ""
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<BasicInfo> = object : Parcelable.Creator<BasicInfo> {
            override fun createFromParcel(source: Parcel): BasicInfo = BasicInfo(source)
            override fun newArray(size: Int): Array<BasicInfo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id!!)
        dest.writeString(firstName)
        dest.writeString(lastName)
        dest.writeString(title)
        dest.writeString(summary)
        dest.writeString(industry)
        dest.writeString(pictureUrl)
    }
}
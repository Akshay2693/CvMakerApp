package com.blackbox.onepage.cvmaker.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable


/**
 * Created by umair on 26/05/2017.
 */
@Entity(tableName = "education")
class EducationInfo(
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Int? = 0,
        @ColumnInfo(name = "institute") var institute: String? = "",
        @ColumnInfo(name = "start_year") var startYear: String? = "",
        @ColumnInfo(name = "end_year") var endYear: String? = "",
        @ColumnInfo(name = "degree") var degree: String? = ""
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<EducationInfo> = object : Parcelable.Creator<EducationInfo> {
            override fun createFromParcel(source: Parcel): EducationInfo = EducationInfo(source)
            override fun newArray(size: Int): Array<EducationInfo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id!!)
        dest.writeString(institute)
        dest.writeString(startYear)
        dest.writeString(endYear)
        dest.writeString(degree)
    }
}

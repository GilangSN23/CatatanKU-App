package com.gsn.catatanku.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Note (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "course")
    var course: String? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "detail")
    var detail: String? = null,

    @ColumnInfo(name = "foto")
    var foto: String? = null
) : Parcelable
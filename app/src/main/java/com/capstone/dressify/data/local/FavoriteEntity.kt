package com.capstone.dressify.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    var title: String = "",
    var image: String? = null,
    var isCheck: Boolean = false
) : Parcelable
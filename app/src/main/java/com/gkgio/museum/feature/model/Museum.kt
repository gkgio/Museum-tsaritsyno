package com.gkgio.museum.feature.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Museum(
    val id: String,
    val exhibitions: List<Exhibition>?,
    val imageUrl: String,
    val latitude: String,
    val longitude: String,
    val title: String
) : Parcelable
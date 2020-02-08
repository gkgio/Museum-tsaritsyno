package com.gkgio.museum.feature.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    val id: String,
    val author: String
) : Parcelable
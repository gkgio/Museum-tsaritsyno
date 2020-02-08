package com.gkgio.museum.feature.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exhibition(
    val id: String,
    val description: String?,
    val imageUrl: String,
    val time: String,
    val title: String,
    val type: String,
    val items: List<Item>?
) : Parcelable
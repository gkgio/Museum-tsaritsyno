package com.gkgio.museum.feature.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    val id: String,
    val author: String,
    val ibeaconUuid: String,
    val images: List<String>,
    val description: String?,
    val ibeaconMinorId: String,
    val ibeaconMajorId: String,
    val audioFiles: List<String>,
    val title: String?
) : Parcelable
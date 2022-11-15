package com.irlab.testappkotlin.server

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemList(
    val title: String,
    val name: String,
    val like: String,
    val postNum: String,
    val contents: String,
    val postTime: String,
) : Parcelable

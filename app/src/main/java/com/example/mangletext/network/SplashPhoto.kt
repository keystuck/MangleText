package com.example.mangletext.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class URLsForPhoto(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
): Parcelable {

}

@Parcelize
data class SplashPhoto(
    val id: String,
    val width: Int,
    val height: Int,
    val urls: URLsForPhoto
): Parcelable{

}
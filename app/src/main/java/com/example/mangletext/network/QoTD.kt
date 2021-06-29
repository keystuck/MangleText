package com.example.mangletext.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject


@Parcelize
data class Result(
    val contents: Contents
): Parcelable{

}

@Parcelize
data class Contents(
    val quotes: Array<QoTD>
): Parcelable{
}

@Parcelize
data class QoTD(
    val quote: String = "No quote available",
    val length: Int = 0,
    val author: String = ""
) : Parcelable {

}
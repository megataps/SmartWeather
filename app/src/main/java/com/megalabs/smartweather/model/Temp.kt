package com.megalabs.smartweather.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Temp (
	@Json(name = "day") val day : Double,
	@Json(name = "min") val min : Double,
	@Json(name = "max") val max : Double,
	@Json(name = "night") val night : Double,
	@Json(name = "eve") val eve : Double,
	@Json(name = "morn") val morn : Double
): Parcelable
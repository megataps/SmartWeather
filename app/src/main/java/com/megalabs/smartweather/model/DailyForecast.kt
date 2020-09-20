package com.megalabs.smartweather.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class DailyForecast (
	@Json(name = "city") val city : City,
	@Json(name = "cod") val cod : Int,
	@Json(name = "message") val message : Double,
	@Json(name = "cnt") val cnt : Int,
	@Json(name = "list") val list : List<DailyItem>
): Parcelable
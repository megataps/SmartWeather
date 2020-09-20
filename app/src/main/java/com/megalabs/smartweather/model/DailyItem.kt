package com.megalabs.smartweather.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class DailyItem (
	@Json(name = "dt") val dt : Long,
	@Json(name = "sunrise") val sunrise : Long,
	@Json(name = "sunset") val sunset : Long,
	@Json(name = "temp") val temp : Temp,
	@Json(name = "feels_like") val feels_like : FeelsLike,
	@Json(name = "pressure") val pressure : Int,
	@Json(name = "humidity") val humidity : Int,
	@Json(name = "weather") val weather : List<Weather>,
	@Json(name = "speed") val speed : Double,
	@Json(name = "deg") val deg : Int,
	@Json(name = "clouds") val clouds : Int,
	@Json(name = "pop") val pop : Double? = 0.0 ,
	@Json(name = "rain") val rain : Double? = 0.0
): Parcelable
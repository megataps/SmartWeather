package com.megalabs.smartweather.exception

class ApiException(
    val errorCode: String,
    detailMessage: String
) : RuntimeException(detailMessage)
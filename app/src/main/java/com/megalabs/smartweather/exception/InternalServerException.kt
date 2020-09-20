package com.megalabs.smartweather.exception

class InternalServerException(
    errorMsg: String
) : RuntimeException(errorMsg)
package com.megalabs.smartweather.exception

class NotFoundException(
    errorMsg: String
) : RuntimeException(errorMsg)
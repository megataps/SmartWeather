package com.megalabs.smartweather.exception

class UnknownException(
    val throwable: Throwable
) : RuntimeException(throwable)
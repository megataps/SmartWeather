package com.megalabs.smartweather.exception

class AuthenticationException(
    errorMsg: String
) : RuntimeException(errorMsg)
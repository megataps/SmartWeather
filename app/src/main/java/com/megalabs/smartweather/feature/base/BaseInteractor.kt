package com.megalabs.smartweather.feature.base

import android.os.Parcelable
import com.megalabs.smartweather.exception.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.android.parcel.Parcelize
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseInteractor: KoinComponent {

    private val moshi: Moshi by inject()

    protected fun transformException(throwable: Throwable): Exception {
        // A network error happened
        if (throwable is IOException ||
            throwable is SocketTimeoutException ||
            throwable is UnknownHostException
        ) {
            return NetworkException(throwable)
        }

        // We had non-200 http error
        if (throwable is HttpException) {
            return handleServerException(throwable)
        }

        // We don't know what happened. We need to simply convert to an unknown error
        return UnknownException(throwable)
    }

    private fun handleServerException(httpException: HttpException): RuntimeException {
        // This condition is used to handle offline search that city hasn't been cached
        if (httpException.code() == 504 && httpException.message?.contains("(only-if-cached)") == true) {
            return NetworkException(httpException)
        }

        val httpResponse = httpException.response()
        val errorResponse: ErrorResponse? = getErrorResponse(httpResponse)
        val errorMsg = errorResponse?.serverMessage ?: ""

        if (httpException.code() == 401) {
            return AuthenticationException(errorMsg)
        }

        if (httpException.code() == 404) {
            return NotFoundException(errorMsg)
        }

        if (httpException.code() == 502 || httpException.code() == 500) {
            return InternalServerException(errorMsg)
        }

        return ApiException(errorResponse?.cod ?: "", errorMsg)
    }

    private fun getErrorResponse(httpResponse: Response<*>?): ErrorResponse? {
        val bufferedSource = httpResponse?.errorBody()?.source()
        var errorResponse: ErrorResponse? = null
        bufferedSource?.let {
            errorResponse = moshi.adapter(ErrorResponse::class.java).fromJson(bufferedSource)
        }
        return errorResponse
    }
}

@Parcelize
@JsonClass(generateAdapter = true)
internal data class ErrorResponse(
    @Json(name = "cod") val cod: String,
    @Json(name = "message") val serverMessage: String,
    val translatedMessage: String? = ""
): Parcelable
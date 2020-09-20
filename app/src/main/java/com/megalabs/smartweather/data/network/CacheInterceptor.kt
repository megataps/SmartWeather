package com.megalabs.smartweather.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response

// TODO
//https://stackoverflow.com/questions/37119429/android-retrofit-2-differences-between-addinterceptor-addnetworkinterceptor-f
class CacheInterceptor(
    private val context: Context
) : Interceptor {

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager?.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            var isConnected: Boolean? = false
            val activeNetwork: NetworkInfo? = connectivityManager?.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected) {
                isConnected = true
            }
            return isConnected ?: false
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (isNetworkAvailable(context)) {
            /*
            *  If there is Internet, get the cache that was stored 60 seconds ago.
            *  If the cache is older than 60 seconds, then discard it,
            *  and indicate an error in fetching the response.
            *  The 'max-age' attribute is responsible for this behavior.
            */
            request = request.newBuilder().header(
                "Cache-Control",
                "public, max-age=" + 60
            ).build()
        } else {
            /*
             *  If there is no Internet, get the cache that was stored 7 days ago.
             *  If the cache is older than 7 days, then discard it,
             *  and indicate an error in fetching the response.
             *  The 'max-stale' attribute is responsible for this behavior.
             *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
             */
            request = request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
            ).build()
        }

        return chain.proceed(request)
    }
}
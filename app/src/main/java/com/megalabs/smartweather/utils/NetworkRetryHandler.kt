package com.megalabs.smartweather.utils

import android.annotation.SuppressLint
import android.util.ArrayMap
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.megalabs.smartweather.R
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import kotlinx.android.synthetic.main.fragment_no_internet.view.*
import org.koin.core.KoinComponent
import timber.log.Timber

class NetworkRetryHandler: KoinComponent {

    fun askNetworkingAvailable(error: Throwable): Flowable<String> {
        Timber.e("LLLNNN>>>askNetworkingAvailable")
        return Flowable.create({ emitter ->
            val dialog = MaterialDialog(getRunningActivity()!!)
                .customView(
                    R.layout.fragment_no_internet,
                    scrollable = false,
                    noVerticalPadding = true,
                    dialogWrapContent = true)


            val customView = dialog.getCustomView()
            customView.retryButton.setOnClickListener{
                dialog.dismiss()
                emitter.onNext("retry-again")
                emitter.onComplete()
            }

            dialog.show {
                cancelOnTouchOutside(false)
                cornerRadius(32f)
            }
        }, BackpressureStrategy.LATEST)

    }

    @SuppressLint("PrivateApi")
    private fun getRunningActivity(): AppCompatActivity? {

        try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val activityThread = activityThreadClass.getMethod("currentActivityThread")
                .invoke(null)
            val activitiesField =
                activityThreadClass.getDeclaredField("mActivities")
            activitiesField.isAccessible = true
            val activities =
                activitiesField[activityThread] as ArrayMap<*, *>
            for (activityRecord in activities.values) {
                val activityRecordClass: Class<*> = activityRecord.javaClass
                val pausedField =
                    activityRecordClass.getDeclaredField("paused")
                pausedField.isAccessible = true
                if (!pausedField.getBoolean(activityRecord)) {
                    val activityField = activityRecordClass.getDeclaredField("activity")
                    activityField.isAccessible = true
                    return activityField[activityRecord] as AppCompatActivity
                }
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
        throw Exception("Didn't find the running activity")
    }
}
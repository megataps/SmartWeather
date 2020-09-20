package com.megalabs.smartweather.mock

import com.megalabs.smartweather.extension.SchedulerProvider
import io.reactivex.rxjava3.schedulers.Schedulers

class TestSchedulerProvider: SchedulerProvider {
    override fun io() = Schedulers.trampoline()

    override fun ui() = Schedulers.trampoline()

    override fun computation() = Schedulers.trampoline()
}
package com.megalabs.smartweather.app

import androidx.multidex.MultiDexApplication
import com.megalabs.smartweather.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

// TODO:
// https://developer.android.com/studio/build/multidex#keep
class SmartWeatherApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SmartWeatherApp)
            koin.loadModules(weatherAppModule)
            koin.createRootScope()
        }
    }
}

val weatherAppModule = listOf(
    networkModule,
    apiModule,
    repositoryModule,
    interactorModule,
    viewModelModule,)

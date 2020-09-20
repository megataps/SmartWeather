package com.megalabs.smartweather.feature.splash

import android.os.Bundle
import com.megalabs.smartweather.R
import com.megalabs.smartweather.feature.base.BaseActivity

class SplashActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SplashFragment.newInstance())
                .commitNow()
        }
    }
}
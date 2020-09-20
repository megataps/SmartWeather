package com.megalabs.smartweather.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.megalabs.smartweather.R
import com.megalabs.smartweather.feature.search.view.SearchFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment.newInstance())
                .commitNow()
        }
    }
}
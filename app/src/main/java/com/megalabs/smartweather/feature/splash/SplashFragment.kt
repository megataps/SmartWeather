package com.megalabs.smartweather.feature.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.megalabs.smartweather.R
import com.megalabs.smartweather.feature.MainActivity
import com.megalabs.smartweather.feature.base.BaseFragment
import com.scottyab.rootbeer.RootBeer

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    companion object {
        fun newInstance() =
            SplashFragment()
    }

    override fun onResume() {
        super.onResume()

        val rootBeer = RootBeer(this.requireContext())
        if (rootBeer.isRooted) {
            showErrorDialog(R.string.rooting_device_message, true)
        } else {
            // TODO: Preload data if needed
            // viewModel.loadData()
            launchMainActivity()
        }
    }

    override fun bindEvents(rootView: View?, savedInstanceState: Bundle?) {
    }

    private fun launchMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.finish()
    }
}
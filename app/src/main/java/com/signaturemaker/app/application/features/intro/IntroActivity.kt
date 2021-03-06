/*
 __ _                   _                                 _
/ _(_) __ _ _ __   __ _| |_ _   _ _ __ ___    /\/\   __ _| | _____ _ __
\ \| |/ _` | '_ \ / _` | __| | | | '__/ _ \  /    \ / _` | |/ / _ \ '__|
_\ \ | (_| | | | | (_| | |_| |_| | | |  __/ / /\/\ \ (_| |   <  __/ |
\__/_|\__, |_| |_|\__,_|\__|\__,_|_|  \___| \/    \/\__,_|_|\_\___|_|
      |___/

Copyright (C) 2018  Raúl Rodríguez Concepción www.tuppersoft.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/
package com.signaturemaker.app.application.features.intro

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntroFragment
import com.signaturemaker.app.R
import com.signaturemaker.app.application.features.main.MainActivity
import com.signaturemaker.app.application.utils.Constants
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import com.tuppersoft.skizo.android.core.extension.saveSharedPreference

class IntroActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (loadSharedPreference(Constants.FIRST_TIME, true)) {
            saveSharedPreference(Constants.FIRST_TIME, false)

            val f1 = AppIntroFragment.newInstance(
                resources.getString(R.string.tittle_slider1), "fontTypeMedium",
                resources.getString(R.string.tittle_body_slider1), "normal",
                R.drawable.ic_pencil_icon, ContextCompat.getColor(this, R.color.background_sliders),
                ContextCompat.getColor(this, R.color.colorWhite), ContextCompat.getColor(this, R.color.colorWhite)
            )
            val f2 = AppIntroFragment.newInstance(
                resources.getString(R.string.tittle_slider2), "fontTypeMedium",
                resources.getString(R.string.tittle_body_slider2), "normal",
                R.drawable.ic_share_icon, ContextCompat.getColor(this, R.color.background_sliders),
                ContextCompat.getColor(this, R.color.colorWhite), ContextCompat.getColor(this, R.color.colorWhite)
            )
            val f3 = AppIntroFragment.newInstance(
                resources.getString(R.string.tittle_slider3), "fontTypeMedium",
                resources.getString(R.string.tittle_body_slider3), "normal",
                R.drawable.ic_sign_icon, ContextCompat.getColor(this, R.color.background_sliders),
                ContextCompat.getColor(this, R.color.colorWhite), ContextCompat.getColor(this, R.color.colorWhite)
            )
            val f4 = AppIntroFragment.newInstance(
                resources.getString(R.string.tittle_slider4), "fontTypeMedium",
                resources.getString(R.string.tittle_body_slider4), "normal",
                R.drawable.ic_check_icon, ContextCompat.getColor(this, R.color.background_sliders),
                ContextCompat.getColor(this, R.color.colorWhite), ContextCompat.getColor(this, R.color.colorWhite)
            )
            addSlide(f1)
            addSlide(f2)
            addSlide(f3)
            addSlide(f4)

            showSkipButton(false)
            isProgressButtonEnabled = true

            setVibrate(true)
            setVibrateIntensity(30)
        } else {
            launchActivityMain()
        }
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        launchActivityMain()
    }

    private fun launchActivityMain() {
        val myIntent = Intent(this, MainActivity::class.java)
        this.startActivity(myIntent)
        finish()
    }
}


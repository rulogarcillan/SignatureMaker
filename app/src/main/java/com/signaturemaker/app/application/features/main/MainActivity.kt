/*
 __ _                   _                                 _
/ _(_) __ _ _ __   __ _| |_ _   _ _ __ ___    /\/\   __ _| | _____ _ __
\ \| |/ _` | '_ \ / _` | __| | | | '__/ _ \  /    \ / _` | |/ / _ \ '__|
_\ \ | (_| | | | | (_| | |_| |_| | | |  __/ / /\/\ \ (_| |   <  __/ |
\__/_|\__, |_| |_|\__,_|\__|\__,_|_|  \___| \/    \/\__,_|_|\_\___|_|
      |___/

Copyright (C) 2018  Raúl Rodríguez Concepción www.wepica.com

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
package com.signaturemaker.app.application.features.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.platform.BaseActivity
import com.signaturemaker.app.application.core.platform.FilesUtils
import com.signaturemaker.app.application.core.platform.PermissionsUtils
import com.signaturemaker.app.application.features.files.ClickInterface
import com.signaturemaker.app.application.features.files.ListFilesFragment
import com.signaturemaker.app.application.features.sing.SingBoardFragment
import kotlinx.android.synthetic.main.activity_main.adView
import kotlinx.android.synthetic.main.activity_main.containerFiles

class MainActivity : BaseActivity(), ClickInterface {

    private var flagAdvertising: Boolean = false
    private var layoutMain: RelativeLayout? = null
    private var listFragment: ListFilesFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utils.loadAllPreferences(this)

        val ft = supportFragmentManager.beginTransaction()
        val signBoard: SingBoardFragment = SingBoardFragment.newInstance()
        ft.replace(
            R.id.container,
            signBoard,
            SingBoardFragment::class.java.simpleName
        )
        ft.commit()
        signBoard.setInterface(this)

        createTableView()

        startChangelog(false)



        initAdvertising()
    }

    override fun onStart() {
        super.onStart()
        if (Utils.deleteExit) {
            PermissionsUtils.getInstance().callRequestPermissionWrite(this)
        }
    }

    public override fun onRestart() {
        super.onRestart()
        showAdvertising()
    }

    override fun onResume() {
        super.onResume()
        flagAdvertising = Utils.disableAds
        hideAdvertising()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Utils.deleteExit && PermissionsUtils.hasPermissionWriteRead(this)) {
            FilesUtils.deleteAllFiles(this)
        }
    }

    override fun buttonClicked() {
        if (containerFiles != null) {
            listFragment?.reloadFiles()
        }
    }

    private fun createTableView() {
        if (containerFiles != null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.containerFiles, ListFilesFragment(),
                ListFilesFragment::class.java.simpleName
            )
                .commit()
        }
    }

    /**
     * hide advertising if options is selected
     */
    private fun hideAdvertising() {
        if (Utils.disableAds && adView != null) {
            layoutMain?.removeView(adView)
        }
    }

    /**
     * Init advertising
     */
    private fun initAdvertising() {
        adView.visibility = View.GONE
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adView.visibility = View.VISIBLE
            }
        }
    }

    /**
     * show advertising if options change
     */
    private fun showAdvertising() {
        if (Utils.disableAds != flagAdvertising && !Utils.disableAds) {
            val intent = Intent()
            intent.setClass(this, this.javaClass)
            finish()
            this.startActivity(intent)
        }
    }
}
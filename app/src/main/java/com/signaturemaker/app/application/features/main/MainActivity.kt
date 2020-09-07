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
package com.signaturemaker.app.application.features.main

import android.Manifest.permission
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.platform.BaseActivity
import com.signaturemaker.app.application.core.platform.FilesUtils
import com.signaturemaker.app.application.core.platform.PermissionsUtils
import com.signaturemaker.app.application.features.files.ListFilesFragment
import com.signaturemaker.app.application.features.menu.SettingViewModel
import com.signaturemaker.app.application.utils.Constants
import com.signaturemaker.app.data.repositories.SharedPreferencesRepository
import com.signaturemaker.app.databinding.ActivityMainBinding
import com.tuppersoft.skizo.core.extension.gone
import com.tuppersoft.skizo.core.extension.loadSharedPreference
import com.tuppersoft.skizo.core.extension.logd
import com.tuppersoft.skizo.core.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_toolbar.view.tvTittle
import java.io.File

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var flagAdvertising: Boolean = false
    private val settingViewModel: SettingViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.idToolbar.mtbToolbar)

        Utils.loadAllPreferences(this)

        initObserver()
        initMigrate()
        //createTableView()
        initAdvertising()
    }

    override fun onStart() {
        super.onStart()
        if (Utils.deleteExit) {
            PermissionsUtils.callRequestPermissionWrite(this)
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
            FilesUtils.deleteAllFiles()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            else -> return true
        }
        return true
    }

    private fun createTableView() {
        if (binding.containerFiles != null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.containerFiles, ListFilesFragment(),
                ListFilesFragment::class.java.simpleName
            ).commit()
        }
    }

    /**
     * hide advertising if options is selected
     */
    private fun hideAdvertising() {
        if (Utils.disableAds) {
            binding.layoutMain.removeView(binding.adView)
            // binding.adView.getChildAt(0).gone()
        }
    }

    /**
     * Init advertising
     */
    private fun initAdvertising() {
        binding.adView.visibility = View.GONE
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                binding.adView.visibility = View.VISIBLE
            }
        }
    }

    /**
     * show advertising if options change
     */
    private fun showAdvertising() {
        if (Utils.disableAds != flagAdvertising) {
            val intent = Intent()
            intent.setClass(this, this.javaClass)
            finish()
            this.startActivity(intent)
        }
    }

    private fun initObserver() {
        settingViewModel.title.observe(this, ::setTitleToolbar)
        settingViewModel.backButton.observe(this, ::showBackButton)
        settingViewModel.cancelButton.observe(this, ::showCancelButton)
        settingViewModel.showToolbar.observe(this, ::showToolbarOrHide)
    }

    private fun setTitleToolbar(title: String) {
        binding.idToolbar.mtbToolbar.tvTittle.text = title
        if (title.isNotEmpty()) {
            binding.idToolbar.mtbToolbar.tvTittle.visible()
        } else {
            binding.idToolbar.mtbToolbar.tvTittle.gone()
        }
        supportActionBar?.title = ""
    }

    private fun showBackButton(flag: Boolean) {

        val actionBar = supportActionBar
        actionBar?.let {
            actionBar.setHomeButtonEnabled(flag)
            actionBar.setDisplayHomeAsUpEnabled(flag)
            actionBar.setDisplayShowHomeEnabled(flag)

        }
    }

    private fun showCancelButton(flag: Boolean) {
        binding.idToolbar.mtbToolbar.menu?.findItem(R.id.idCancel)?.isVisible = flag
    }

    private fun showToolbarOrHide(flag: Boolean) {
        if (flag) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                "Migrate start".logd()
                migrateFiles()
                FilesUtils.removeFile(loadOldPath())
                "Migrate finish".logd()
                createTableView()
            } else {
                createTableView()
                "Cancel migrate - permission is denied".logd()
            }
        }

    private fun initMigrate() {
        if (SharedPreferencesRepository.loadPreference(this, Constants.NEED_MIGRATE, true) && isNeedMigrate()) {
            "Need migrate files".logd()
            permissions()
        } else {
            "No need migrate files".logd()
            createTableView()
        }
        SharedPreferencesRepository.savePreference(this, Constants.NEED_MIGRATE, false)
    }

    private fun loadOldPath(): String = loadSharedPreference(Constants.ID_PREF_PATH, Constants.DEFAULT_OLD_PATH)

    private fun isNeedMigrate(): Boolean = File(loadOldPath()).exists()

    private fun migrateFiles() {
        FilesUtils.moveFiles(loadOldPath(), this)
    }

    private fun permissions() {
        requestPermissionLauncher.launch(permission.WRITE_EXTERNAL_STORAGE)
    }
}

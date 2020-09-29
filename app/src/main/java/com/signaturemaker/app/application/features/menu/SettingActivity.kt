package com.signaturemaker.app.application.features.menu

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.platform.BaseActivity
import com.signaturemaker.app.databinding.SettingActivityBinding
import com.tuppersoft.skizo.android.core.extension.gone
import com.tuppersoft.skizo.android.core.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_toolbar.view.tvTittle

/**
 * Created by Raúl Rodríguez Concepción on 10/05/2020.

 * raulrcs@gmail.com
 */

@AndroidEntryPoint
class SettingActivity : BaseActivity() {

    private lateinit var binding: SettingActivityBinding
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
        binding = SettingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.inSettingToolbar.mtbToolbar)
    }

    private fun initObserver() {
        settingViewModel.title.observe(this, ::setTitleToolbar)
        settingViewModel.backButton.observe(this, ::showBackButton)
        settingViewModel.showToolbar.observe(this, ::showToolbarOrHide)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top)
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

    private fun setTitleToolbar(title: String) {
        binding.inSettingToolbar.mtbToolbar.tvTittle.text = title
        if (title.isNotEmpty()) {
            binding.inSettingToolbar.mtbToolbar.tvTittle.visible()
        } else {
            binding.inSettingToolbar.mtbToolbar.tvTittle.gone()
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

    private fun showToolbarOrHide(flag: Boolean) {
        if (flag) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }
}



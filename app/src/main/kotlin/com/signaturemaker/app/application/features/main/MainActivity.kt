package com.signaturemaker.app.application.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.platform.BaseActivity
import com.signaturemaker.app.application.core.util.InAppUpdateHelper
import com.signaturemaker.app.application.ui.navigation.SignatureMakerApp
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignatureMakerApp()
        }

        // Check for app updates (flexible: downloads in background)
        InAppUpdateHelper.checkForUpdate(this) {
            InAppUpdateHelper.completeUpdate(this)
        }
    }

    override fun onDestroy() {
        if (Utils.deleteExit) {
            mainViewModel.removeAllFiles(Utils.path)
        }
        super.onDestroy()
    }
}

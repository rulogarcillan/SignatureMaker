package com.signaturemaker.app.application.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import com.signaturemaker.app.application.core.extensions.Utils
import com.signaturemaker.app.application.core.platform.BaseActivity
import com.signaturemaker.app.application.ui.navigation.SignatureMakerApp
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignatureMakerApp()
        }

        // ============================================
        // CRASHLYTICS TESTING
        // Descomenta UNA de estas líneas para probar crashes
        // ============================================

        // Test 1: Crash simple (descomentar para probar)
        // testCrashSimple()

        // Test 2: Crash con datos personalizados (descomentar para probar)
        // testCrashWithCustomData()

        // Test 3: Excepción no fatal - NO crashea (descomentar para probar)
        // testNonFatalException()

        // Test 4: NullPointerException (descomentar para probar)
        // testNullPointerCrash()

        // Test 5: Crash con breadcrumbs/logs (descomentar para probar)
        // testCrashWithBreadcrumbs()

        // Test 6: IndexOutOfBoundsException (descomentar para probar)
        // testIndexOutOfBoundsCrash()

        // ============================================
        // CÓMO PROBAR:
        // 1. Descomenta UNA función de test arriba
        // 2. Compila y ejecuta la app en modo Debug
        // 3. La app crasheará (excepto test 3)
        // 4. Espera 5-10 minutos
        // 5. Ve a Firebase Console > Crashlytics
        // 6. Deberías ver el reporte del crash
        // ============================================
    }

    override fun onDestroy() {
        if (Utils.deleteExit) {
            mainViewModel.removeAllFiles(Utils.path)
        }
        super.onDestroy()
    }
}

package com.signaturemaker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.signaturemaker.app.navigation.SignatureMakerNavigation
import com.signaturemaker.app.ui.theming.SignatureMakerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMainActivity()
        }
    }
}


@Composable
fun ComposeMainActivity(navController: NavHostController = rememberNavController()) {
    SignatureMakerAppTheme {
        SignatureMakerNavigation(navController = navController)
    }
}
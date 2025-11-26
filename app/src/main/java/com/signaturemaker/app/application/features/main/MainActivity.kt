package com.signaturemaker.app.application.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection.Ltr
import androidx.compose.ui.unit.LayoutDirection.Rtl
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.signaturemaker.app.BuildConfig
import com.signaturemaker.app.R
import com.signaturemaker.app.application.features.main.MainActivityMenu.Gallery
import com.signaturemaker.app.application.features.main.MainActivityMenu.Sign
import com.signaturemaker.app.application.core.platform.BaseActivity
import com.signaturemaker.app.application.features.gallery.GalleryDestination
import com.signaturemaker.app.application.features.sign.SignDestination
import com.signaturemaker.app.application.ui.navigation.SignatureMakerNavigation
import com.signaturemaker.app.application.ui.designsystem.SMTheme
import com.signaturemaker.app.application.ui.designsystem.components.SMIcon
import com.signaturemaker.app.application.ui.designsystem.components.SMIconButton
import com.signaturemaker.app.application.ui.designsystem.components.SMLineSeparator
import com.signaturemaker.app.application.ui.designsystem.components.SMText
import com.signaturemaker.app.application.ui.snackbar.rememberSnackbarController
import com.signaturemaker.app.application.ui.snackbar.LocalSnackbarController
import com.signaturemaker.app.application.ui.theming.SignatureMakerAppTheme

/*
 * Main Activity
 */
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMainActivity()
        }
    }
}

/**
 * Main Activity Composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeMainActivity(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val mainActivityUIState = remember { MainActivityUIState.create() }

    val mainActivityState = rememberMainActivityState(
        navController = navController
    )

    // Create SnackbarController for the entire app
    val snackbarController = rememberSnackbarController()

    SignatureMakerAppTheme {
        CompositionLocalProvider(LocalSnackbarController provides snackbarController) {
            ModalNavigationDrawer(
                modifier = modifier.safeDrawingPadding(),
                drawerState = mainActivityState.drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerShape = RectangleShape,
                        drawerContainerColor = SMTheme.material.colorScheme.surface,
                        modifier = Modifier
                            .focusGroup()
                            .fillMaxHeight()
                    ) {
                        DrawerTopSection(onCloseDrawerClick = { mainActivityState.onDrawerClick() })
                        DrawerTitleSection()
                        DrawerOptionsSection(mainActivityUIState, mainActivityState)
                        SMLineSeparator(modifier = Modifier.padding(top = SMTheme.spacing.spacing250))
                    }
                },
            ) {
                Scaffold(
                    modifier = Modifier.safeDrawingPadding(),
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarController.snackbarHostState)
                    },
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = SMTheme.material.colorScheme.primary,
                                actionIconContentColor = contentColorFor(SMTheme.material.colorScheme.primary),
                                titleContentColor = contentColorFor(SMTheme.material.colorScheme.primary)
                            ),
                            title = { SMText(text = stringResource(R.string.app_title)) },
                            navigationIcon = {
                                SMIconButton(
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = contentColorFor(SMTheme.material.colorScheme.primary)
                                    ),
                                    onClick = { mainActivityState.onDrawerClick() },
                                    contentDescription = "",
                                    imageVector = Icons.Default.Menu
                                )
                            }
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(
                                top = it.calculateTopPadding(),
                                start = it.calculateStartPadding(Ltr),
                                bottom = it.calculateEndPadding(Rtl)
                            )
                            .fillMaxSize()
                    ) {
                        SignatureMakerNavigation(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerTopSection(onCloseDrawerClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SMTheme.spacing.spacing250),
        horizontalArrangement = Arrangement.End
    ) {
        SMIconButton(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.close_menu),
            onClick = onCloseDrawerClick
        )
    }
}

@Composable
private fun DrawerTitleSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SMTheme.spacing.spacing250),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SMIcon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.logo),
            tint = SMTheme.material.colorScheme.primary,
            modifier = Modifier
                .size(SMTheme.size.size550)
                .aspectRatio(1f),
        )
        SMText(text = stringResource(R.string.app_title))
        SMText(text = BuildConfig.VERSION_NAME, style = SMTheme.material.typography.bodySmall)
        SMLineSeparator(modifier = Modifier.padding(top = SMTheme.spacing.spacing250))
    }
}

@Composable
private fun DrawerOptionsSection(
    mainActivityUIState: MainActivityUIState,
    mainActivityState: MainActivityState
) {
    val selected = mainActivityState.menuSelected
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        mainActivityUIState.menu.forEach {
            DrawerOptionItem(
                text = stringResource(it.titleResId),
                startIcon = it.icon,
                modifier = Modifier.padding(bottom = SMTheme.spacing.spacing100),
                onClick = {
                    mainActivityState.onDrawerClick()
                    mainActivityState.changeMenuSelected(it)
                    when (it) {
                        is Sign -> mainActivityState.navigateTo(SignDestination.route)
                        is Gallery -> mainActivityState.navigateTo(GalleryDestination.route)
                        else -> {}
                    }
                },
                selected = selected == it
            )
            println(mainActivityState.menuSelected)
        }
    }
}

@Composable
private fun DrawerOptionItem(
    text: String,
    startIcon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    val colorIsSelected = if (selected) {
        SMTheme.material.colorScheme.primary.copy(alpha = 0.6f)
    } else {
        SMTheme.material.colorScheme.surface
    }

    val contentColorIsSelected = if (selected) {
        SMTheme.material.colorScheme.contentColorFor(SMTheme.material.colorScheme.primary)
    } else {
        SMTheme.material.colorScheme.contentColorFor(SMTheme.material.colorScheme.surface)
    }

    Surface(
        shape = RoundedCornerShape(
            topEnd = SMTheme.radius.radius1000,
            bottomEnd = SMTheme.radius.radius1000
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(end = SMTheme.size.size150),
        onClick = onClick,
        color = colorIsSelected,
        contentColor = contentColorIsSelected,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SMTheme.size.size150),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(SMTheme.spacing.spacing150))
            SMIcon(
                imageVector = startIcon,
                contentDescription = null,
                modifier = Modifier.size(SMTheme.size.size300)
            )
            Spacer(modifier = Modifier.width(SMTheme.spacing.spacing250))
            SMText(text = text)
        }
    }
}

package com.signaturemaker.app.application.features.main

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.signaturemaker.app.BuildConfig
import com.signaturemaker.app.R
import com.signaturemaker.app.application.ui.designsystem.SMTheme
import com.signaturemaker.app.application.ui.designsystem.components.SMIcon
import com.signaturemaker.app.application.ui.designsystem.components.SMIconButton
import com.signaturemaker.app.application.ui.designsystem.components.SMLineSeparator
import com.signaturemaker.app.application.ui.designsystem.components.SMText
import com.signaturemaker.app.application.ui.snackbar.LocalSnackbarController
import com.signaturemaker.app.application.ui.snackbar.rememberSnackbarController
import com.signaturemaker.app.application.ui.theming.SignatureMakerAppTheme

// ============================================
// MAIN SCREEN
// ============================================

/**
 * Main Screen - Main composable for application navigation and drawer
 *
 * Provides:
 * - Top navigation bar
 * - Navigation drawer with menu options
 * - Scaffold structure for nested navigation content
 * - Global snackbar support
 *
 * @param modifier Modifier to be applied to the root element
 * @param onNavigationAction Callback for handling drawer navigation actions
 * @param content Content to be displayed in the main area (navigation destinations)
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onNavigationAction: (MainScreenAction) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val mainState = rememberMainState()
    val menuConfig = rememberMainMenuConfig()

    // Create SnackbarController for the entire app
    val snackbarController = rememberSnackbarController()

    SignatureMakerAppTheme {
        CompositionLocalProvider(LocalSnackbarController provides snackbarController) {
            MainScreenContent(
                mainState = mainState,
                menuConfig = menuConfig,
                onNavigationAction = onNavigationAction,
                snackbarController = snackbarController,
                content = content,
                modifier = modifier
            )
        }
    }
}

/**
 * Main Screen Content - Layout and structure
 */
@Composable
private fun MainScreenContent(
    mainState: MainUIState,
    menuConfig: MainMenuConfig,
    onNavigationAction: (MainScreenAction) -> Unit,
    snackbarController: com.signaturemaker.app.application.ui.snackbar.SnackbarController,
    content: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalNavigationDrawer(
        modifier = modifier.safeDrawingPadding(),
        drawerState = mainState.drawerState,
        drawerContent = {
            NavigationDrawerContent(
                mainState = mainState,
                menuConfig = menuConfig,
                onAction = onNavigationAction
            )
        },
    ) {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            snackbarHost = {
                SnackbarHost(hostState = snackbarController.snackbarHostState)
            },
            topBar = {
                MainTopBar(onMenuClick = { mainState.onDrawerClick() })
            }
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}

// ============================================
// TOP BAR
// ============================================

/**
 * Main Top Bar - Application top app bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopBar(
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SMTheme.material.colorScheme.primary,
            actionIconContentColor = contentColorFor(SMTheme.material.colorScheme.primary),
            titleContentColor = contentColorFor(SMTheme.material.colorScheme.primary)
        ),
        title = {
            SMText(text = stringResource(R.string.app_title))
        },
        navigationIcon = {
            SMIconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = contentColorFor(SMTheme.material.colorScheme.primary)
                ),
                onClick = onMenuClick,
                contentDescription = stringResource(R.string.close_menu),
                imageVector = Icons.Default.Menu
            )
        }
    )
}

// ============================================
// NAVIGATION DRAWER
// ============================================

/**
 * Navigation Drawer Content - Complete drawer layout
 */
@Composable
private fun NavigationDrawerContent(
    mainState: MainUIState,
    menuConfig: MainMenuConfig,
    onAction: (MainScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        drawerShape = RectangleShape,
        drawerContainerColor = SMTheme.material.colorScheme.surface,
        modifier = modifier
            .focusGroup()
            .fillMaxHeight()
    ) {
        DrawerTopSection(onCloseDrawerClick = { mainState.onDrawerClick() })
        DrawerTitleSection()
        DrawerOptionsSection(
            menuConfig = menuConfig,
            mainState = mainState,
            onAction = onAction
        )
        SMLineSeparator(modifier = Modifier.padding(top = SMTheme.spacing.spacing250))
    }
}

/**
 * Drawer Top Section - Close button
 */
@Composable
private fun DrawerTopSection(
    onCloseDrawerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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

/**
 * Drawer Title Section - App logo and version
 */
@Composable
private fun DrawerTitleSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
        SMText(
            text = BuildConfig.VERSION_NAME,
            style = SMTheme.material.typography.bodySmall
        )
        SMLineSeparator(modifier = Modifier.padding(top = SMTheme.spacing.spacing250))
    }
}

/**
 * Drawer Options Section - Menu items list
 */
@Composable
private fun DrawerOptionsSection(
    menuConfig: MainMenuConfig,
    mainState: MainUIState,
    onAction: (MainScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = mainState.menuSelected
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        menuConfig.menuItems.forEach { menuItem ->
            DrawerOptionItem(
                text = stringResource(menuItem.titleResId),
                startIcon = menuItem.icon,
                selected = selected == menuItem,
                modifier = Modifier.padding(bottom = SMTheme.spacing.spacing100),
                onClick = {
                    mainState.onDrawerClick()
                    mainState.changeMenuSelected(menuItem)
                    onAction(menuItem.toAction())
                }
            )
        }
    }
}

// ============================================
// DRAWER OPTION ITEM
// ============================================

/**
 * Drawer Option Item - Individual menu item
 */
@Composable
private fun DrawerOptionItem(
    text: String,
    startIcon: ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val backgroundColor = if (selected) {
        SMTheme.material.colorScheme.primary.copy(alpha = 0.6f)
    } else {
        SMTheme.material.colorScheme.surface
    }

    val contentColor = if (selected) {
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
        color = backgroundColor,
        contentColor = contentColor,
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



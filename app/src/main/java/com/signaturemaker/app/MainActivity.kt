package com.signaturemaker.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.signaturemaker.app.base.BaseActivity
import com.signaturemaker.app.features.gallery.GalleryDestination
import com.signaturemaker.app.features.sign.SignDestination
import com.signaturemaker.app.navigation.SignatureMakerNavigation
import com.signaturemaker.app.ui.designsystem.SMTheme
import com.signaturemaker.app.ui.designsystem.components.SMIcon
import com.signaturemaker.app.ui.designsystem.components.SMIconButton
import com.signaturemaker.app.ui.designsystem.components.SMImage
import com.signaturemaker.app.ui.designsystem.components.SMLineSeparator
import com.signaturemaker.app.ui.designsystem.components.SMText
import com.signaturemaker.app.ui.theming.SignatureMakerAppTheme

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMainActivity()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ComposeMainActivity(navController: NavHostController = rememberNavController()) {

    val mainActivityUIState = remember { MainActivityUIState.create() }

    val mainActivityState = rememberMainActivityState(
        navController = navController
    )

    SignatureMakerAppTheme {
        ModalNavigationDrawer(
            modifier = Modifier.safeDrawingPadding(),
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
                topBar = {

                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = SMTheme.material.colorScheme.primary,
                            actionIconContentColor = contentColorFor(SMTheme.material.colorScheme.primary),
                            titleContentColor = contentColorFor(SMTheme.material.colorScheme.primary)
                        ),
                        title = { SMText(text = "Signature Maker") },
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

                }) {
                Box(
                    modifier = Modifier
                        .padding(
                            top = it.calculateTopPadding(),
                            start = it.calculateStartPadding(LayoutDirection.Ltr),
                            bottom = it.calculateEndPadding(LayoutDirection.Rtl)
                        )
                        .fillMaxSize()
                ) {
                    SignatureMakerNavigation(navController = navController)
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
            .padding(SMTheme.spacing.spacing250), horizontalArrangement = Arrangement.End
    ) {
        SMIconButton(
            imageVector = Icons.Default.Close,
            contentDescription = "Close menu",
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
        SMImage(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier
                .size(64.dp)
                .aspectRatio(1f),
            alignment = Alignment.Center
        )
        SMText(text = "Signature Maker")
        SMText(text = "v4.0.0", style = SMTheme.material.typography.bodySmall)
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
                text = it.title,
                startIcon = it.icon,
                modifier = Modifier.padding(bottom = SMTheme.spacing.spacing100),
                onClick = {
                    mainActivityState.onDrawerClick()
                    mainActivityState.changeMenuSelected(it)
                    when (it) {
                        is MainActivityMenu.Sign -> mainActivityState.navigateTo(SignDestination.route)
                        is MainActivityMenu.Gallery -> mainActivityState.navigateTo(GalleryDestination.route)
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
    onClick: () -> Unit,
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


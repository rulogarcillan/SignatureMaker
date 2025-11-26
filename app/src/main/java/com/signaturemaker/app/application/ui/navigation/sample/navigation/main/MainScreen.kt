@file:Suppress("MagicNumber", "LongMethod")

package com.kubit.charts.storybook.ui.screens.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.kubit.charts.storybook.R
import com.kubit.charts.storybook.domain.resources.StringResources
import com.kubit.charts.storybook.domain.resources.StringResourcesProvider
import com.kubit.charts.storybook.theme.KubitTheme
import com.kubit.charts.storybook.ui.screens.main.MainScreenAction.NavigateToComponentList
import com.kubit.charts.storybook.ui.screens.main.MainScreenAction.NavigateToSamples
import com.kubit.charts.storybook.ui.storybookcomponents.StorybookScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MainScreen(
    onNavigationAction: (MainScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val strings = StringResourcesProvider.get()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    StorybookScreen(
        modifier = modifier,
        title = strings.mainTitle,
        showTitle = false
    ) { paddingValues ->

        ModernMainContent(
            onNavigationAction = onNavigationAction,
            paddingValues = paddingValues,
            isVisible = isVisible,
            strings = strings,
        )
    }
}

@Composable
private fun ModernMainContent(
    onNavigationAction: (MainScreenAction) -> Unit,
    paddingValues: PaddingValues,
    isVisible: Boolean,
    strings: StringResources,
    modifier: Modifier = Modifier,
) {
    val animationProgress by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(800), label = ""
    )

    val scrollState = rememberScrollState()
    val navigationBarsPadding = WindowInsets.navigationBars.asPaddingValues()

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .weight(1f)
                .alpha(animationProgress),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            HeroSection(
                animationProgress = animationProgress,
                strings = strings
            )
            Spacer(modifier = Modifier.height(4.dp))

            MainCards(
                onNavigationAction = onNavigationAction,
                animationProgress = animationProgress,
                strings = strings
            )
        }
        Box(
            modifier = Modifier
                .padding(navigationBarsPadding.calculateBottomPadding())
        ) {
            Footer()
        }
    }
}

@Composable
private fun HeroSection(
    animationProgress: Float,
    strings: StringResources
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scale(animationProgress),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = strings.mainTitle,
            style = KubitTheme.material.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            ),
            color = KubitTheme.material.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Text(
            text = strings.powerfulChartLibrary,
            style = KubitTheme.material.typography.bodyLarge.copy(
                fontSize = 18.sp
            ),
            color = KubitTheme.material.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Box(
            modifier = Modifier
                .height(4.dp)
                .width(64.dp)
                .padding(top = 16.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            KubitTheme.material.colorScheme.primary,
                            KubitTheme.material.colorScheme.secondary
                        )
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}

@Composable
private fun MainCards(
    onNavigationAction: (MainScreenAction) -> Unit,
    animationProgress: Float,
    strings: StringResources
) {
    val context = LocalContext.current

    // Definir lista de datos para las tarjetas
    val cardData = listOf(
        CardData(
            title = strings.componentsTitle,
            subtitle = strings.componentsSubtitle,
            description = strings.componentsDescription,
            icon = ImageVector.vectorResource(R.drawable.dashboard),
            gradientColors = persistentListOf(
                KubitTheme.material.colorScheme.primary,
                KubitTheme.material.colorScheme.primaryContainer
            ),
            onClick = { onNavigationAction(NavigateToComponentList) },
            animationDelay = 0f
        ),
        CardData(
            title = strings.samplesTitle,
            subtitle = strings.samplesSubtitle,
            description = strings.samplesDescription,
            icon = ImageVector.vectorResource(R.drawable.bottom_sheets),
            gradientColors = persistentListOf(
                KubitTheme.material.colorScheme.primary,
                KubitTheme.material.colorScheme.primaryContainer
            ),
            onClick = { onNavigationAction(NavigateToSamples) },
            animationDelay = 0.1f
        ),
        CardData(
            title = strings.viewOnGithub,
            subtitle = strings.contributeText,
            description = strings.githubDescription,
            icon = ImageVector.vectorResource(R.drawable.github),
            gradientColors = persistentListOf(
                KubitTheme.material.colorScheme.tertiary,
                KubitTheme.material.colorScheme.tertiaryContainer
            ),
            onClick = {
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    strings.githubRepositoryUrl.toUri()
                )
                context.startActivity(intent)
            },
            animationDelay = 0.2f
        )
    )

    cardData.forEach { card ->
        ModernCard(
            title = card.title,
            subtitle = card.subtitle,
            description = card.description,
            icon = card.icon,
            gradientColors = card.gradientColors,
            onClick = card.onClick,
            animationDelay = card.animationDelay,
            animationProgress = animationProgress
        )
    }
}

// Data class para las tarjetas
@Immutable
private data class CardData(
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val gradientColors: ImmutableList<Color>,
    val onClick: () -> Unit,
    val animationDelay: Float
)

@Composable
private fun ModernCard(
    title: String,
    subtitle: String,
    description: String,
    icon: ImageVector,
    gradientColors: ImmutableList<Color>,
    onClick: () -> Unit,
    animationDelay: Float,
    animationProgress: Float
) {
    val cardScale by animateFloatAsState(
        targetValue = if (animationProgress > 0.5f) 1f else 0.8f,
        animationSpec = tween(600, delayMillis = (animationDelay * 1000).toInt()), label = ""
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = KubitTheme.material.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors.map { it.copy(alpha = 0.1f) }
                    )
                )
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = gradientColors[0].copy(alpha = 0.2f)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = gradientColors[0]
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content Section
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = KubitTheme.material.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = KubitTheme.material.colorScheme.onSurface
                )

                Text(
                    text = subtitle,
                    style = KubitTheme.material.typography.bodyMedium,
                    color = gradientColors[0],
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = description,
                    style = KubitTheme.material.typography.bodySmall,
                    color = KubitTheme.material.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Arrow Icon
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = gradientColors[0],
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun Footer() {
    val strings = StringResourcesProvider.get()

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val annotatedText = buildAnnotatedString {
                append(strings.builtWithLove)
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(strings.jetpackCompose)
                }
            }

            Text(
                text = annotatedText,
                style = KubitTheme.material.typography.bodySmall,
                color = KubitTheme.material.colorScheme.onSurfaceVariant
            )
        }
    }
}

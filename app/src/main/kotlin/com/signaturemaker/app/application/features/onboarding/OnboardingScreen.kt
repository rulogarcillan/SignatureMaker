package com.signaturemaker.app.application.features.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.signaturemaker.app.R
import com.signaturemaker.app.application.ui.designsystem.SMTheme
import com.signaturemaker.app.application.ui.designsystem.components.SMButton
import com.signaturemaker.app.application.ui.designsystem.components.SMIcon
import com.signaturemaker.app.application.ui.designsystem.components.SMText
import kotlinx.coroutines.launch

/**
 * Lightweight onboarding screen with 3 slides + a final CTA.
 * Uses HorizontalPager for swipe navigation.
 *
 * Design: clean, minimal, uses SMTheme tokens for consistency with the app.
 */
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            icon = Icons.Default.Draw,
            titleRes = R.string.tittle_slider1,
            bodyRes = R.string.tittle_body_slider1,
            accentColor = MaterialTheme.colorScheme.primary
        ),
        OnboardingPage(
            icon = Icons.Default.Send,
            titleRes = R.string.tittle_slider2,
            bodyRes = R.string.tittle_body_slider2,
            accentColor = MaterialTheme.colorScheme.tertiary
        ),
        OnboardingPage(
            icon = Icons.Default.AttachFile,
            titleRes = R.string.tittle_slider3,
            bodyRes = R.string.tittle_body_slider3,
            accentColor = MaterialTheme.colorScheme.secondary
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
    ) {
        // Skip button (top-right), hidden on last page
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = SMTheme.spacing.spacing250,
                    vertical = SMTheme.spacing.spacing150
                ),
            horizontalArrangement = Arrangement.End
        ) {
            if (!isLastPage) {
                SMButton(
                    text = stringResource(R.string.onboarding_skip),
                    onClick = onFinish,
                    colors = ButtonDefaults.textButtonColors(),
                    elevation = null
                )
            } else {
                // Invisible spacer to keep layout stable
                Spacer(modifier = Modifier.height(SMTheme.size.size450))
            }
        }

        // Pager content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            OnboardingPageContent(
                page = pages[page],
                modifier = Modifier.fillMaxSize()
            )
        }

        // Bottom section: indicators + button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = SMTheme.spacing.spacing300,
                    vertical = SMTheme.spacing.spacing350
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Page indicators
            PageIndicator(
                pageCount = pages.size,
                currentPage = pagerState.currentPage
            )

            Spacer(modifier = Modifier.height(SMTheme.spacing.spacing350))

            // Action button
            SMButton(
                text = if (isLastPage) {
                    stringResource(R.string.onboarding_get_started)
                } else {
                    stringResource(R.string.onboarding_next)
                },
                onClick = {
                    if (isLastPage) {
                        onFinish()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                endImage = if (!isLastPage) Icons.AutoMirrored.Filled.ArrowForward else null,
                shape = RoundedCornerShape(SMTheme.spacing.spacing200)
            )
        }
    }
}

// ============================================
// PAGE CONTENT
// ============================================

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    val accentColor = page.accentColor

    Column(
        modifier = modifier.padding(horizontal = SMTheme.spacing.spacing350),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Decorative signature-like lines behind the icon
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(160.dp)
        ) {
            // Background circle
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.08f))
            )

            // Decorative dashed lines (signature feel)
            Canvas(modifier = Modifier.size(160.dp)) {
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 12f), 0f)
                val center = size.width / 2
                val radius = size.width * 0.42f

                drawCircle(
                    color = accentColor.copy(alpha = 0.15f),
                    radius = radius,
                    center = Offset(center, center),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 1.5f,
                        pathEffect = pathEffect
                    )
                )
            }

            // Icon
            SMIcon(
                imageVector = page.icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(SMTheme.size.size550)
            )
        }

        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing400))

        // Title
        SMText(
            text = stringResource(page.titleRes),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(SMTheme.spacing.spacing200))

        // Body
        SMText(
            text = stringResource(page.bodyRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = SMTheme.spacing.spacing250)
        )
    }
}

// ============================================
// PAGE INDICATOR
// ============================================

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage

            val width by animateDpAsState(
                targetValue = if (isSelected) 24.dp else 8.dp,
                animationSpec = tween(300),
                label = "indicator_width"
            )

            val color by animateColorAsState(
                targetValue = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                animationSpec = tween(300),
                label = "indicator_color"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .width(width)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

// ============================================
// DATA MODEL
// ============================================

private data class OnboardingPage(
    val icon: ImageVector,
    val titleRes: Int,
    val bodyRes: Int,
    val accentColor: Color
)

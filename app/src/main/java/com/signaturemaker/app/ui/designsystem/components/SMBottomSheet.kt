package com.signaturemaker.app.ui.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import com.signaturemaker.app.ui.designsystem.SMTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SMModalBottomSheet(
    onDismissRequest: () -> Unit,
    //modalContentDescription: String,
    sheetState: SheetState,
    openBottomSheet: Boolean,
    modifier: Modifier = Modifier,
    isFullScreen: Boolean = false,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit
) {

    //val context = LocalContext.current

    if (openBottomSheet) {
        ModalBottomSheet(
            modifier = modifier
                .then(if (isFullScreen) Modifier.fillMaxHeight() else Modifier)
                .semantics {
                    // context.sendAnnounceEventToAccessibilityManager(modalContentDescription)
                },
            dragHandle = dragHandle,
            sheetState = sheetState,
            containerColor = SMTheme.material.colorScheme.surface,
            scrimColor = SMTheme.material.colorScheme.scrim.copy(alpha = 0.4f),
            shape = RoundedCornerShape(
                topStart = SMTheme.radius.radius250,
                topEnd = SMTheme.radius.radius250
            ),
            content = content,
            onDismissRequest = onDismissRequest,
            windowInsets = windowInsets
        )
    }
}
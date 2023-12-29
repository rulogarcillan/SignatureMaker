package com.signaturemaker.app.features.sign

import androidx.annotation.DrawableRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch

@Stable
data class SignState(
    val showSignHere: Boolean,
    val showSignHereChange: (show: Boolean) -> Unit,
    val bottomSheetState: BottomSheetState,
    val strokeColorState: StrokeColorState,
    val backgroundState: BackgroundState
) {
    var clearFunction: (() -> Unit)? = null
}


@OptIn(ExperimentalMaterial3Api::class)
@Stable
data class BottomSheetState(
    val sheetState: SheetState,
    val isShowBottomSheet: Boolean,
    val showBottomSheet: () -> Unit,
    val closeBottomSheet: () -> DisposableHandle,
)

@Stable
data class StrokeColorState(
    val selectedColor: Color,
    val changeColor: (color: Color) -> Unit
)

@Stable
data class BackgroundState(
    @DrawableRes val selectedImage: Int,
    val changeImage: (image: Int) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberSignState(
    selectedColor: Color,
    @DrawableRes selectedImage: Int,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { true }),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): SignState {

    var isShowBottomSheet by rememberSaveable { mutableStateOf(false) }
    var _selectedColor by remember { mutableStateOf(selectedColor) }
    var _selectedImage by rememberSaveable { mutableIntStateOf(selectedImage) }
    var showSignHere by rememberSaveable { mutableStateOf(true) }

    val showBottomSheet = {
        isShowBottomSheet = true
    }
    val closeBottomSheet = {
        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) isShowBottomSheet = false
        }
    }

    val changeColor = { color: Color ->
        _selectedColor = color
    }

    val changeImage = { image: Int ->
        _selectedImage = image
    }

    val changeShowSignHere = { show: Boolean ->
        showSignHere = show
    }


    return remember(
        isShowBottomSheet,
        showBottomSheet,
        sheetState,
        closeBottomSheet
    ) {
        SignState(
            showSignHere = showSignHere,
            showSignHereChange = changeShowSignHere,
            bottomSheetState = BottomSheetState(
                sheetState = sheetState,
                isShowBottomSheet = isShowBottomSheet,
                showBottomSheet = showBottomSheet,
                closeBottomSheet = closeBottomSheet
            ),
            strokeColorState = StrokeColorState(
                selectedColor = _selectedColor,
                changeColor = changeColor
            ),
            backgroundState = BackgroundState(
                selectedImage = _selectedImage,
                changeImage = changeImage
            )
        )
    }
}
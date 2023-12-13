package com.app.opticool.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.opticool.data.response.EyeglassesResponseItem
import com.app.opticool.ui.common.EyeglassState
@Composable
fun DetailScreen(
    uiState: EyeglassState,
    retryAction: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is EyeglassState.Loading -> LoadingScreen()
        is EyeglassState.Success -> DetailScreen(
            eyeglass = uiState.eyeglass,
            modifier = modifier.fillMaxWidth()
        )
        is EyeglassState.Error ->  ErrorScreen()
    }
}

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    eyeglass: EyeglassesResponseItem
) {
    Text(text = eyeglass.name)
}
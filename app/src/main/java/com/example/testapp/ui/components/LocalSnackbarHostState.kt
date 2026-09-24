package com.example.testapp.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.launch

val LocalSnackbarHostState = staticCompositionLocalOf { SnackbarHostState() }

@Composable
fun rememberShowMessage(): (String) -> Unit {
    val hostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()

    return remember(hostState, scope) {
        { message ->
            scope.launch {
                hostState.currentSnackbarData?.dismiss()
                hostState.showSnackbar(message)
            }
        }
    }
}

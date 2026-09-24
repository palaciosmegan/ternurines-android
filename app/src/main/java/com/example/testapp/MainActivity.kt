package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.testapp.data.AccessibilityRepository
import com.example.testapp.navigation.AppRoot
import com.example.testapp.ui.theme.TestAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var accessibilityRepository: AccessibilityRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by accessibilityRepository.settings.collectAsStateWithLifecycle()

            TestAppTheme(highContrast = settings.highContrast, largeText = settings.largeView) {
                AppRoot()
            }
        }
    }
}

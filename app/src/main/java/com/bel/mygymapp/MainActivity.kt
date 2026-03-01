package com.bel.mygymapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bel.mygymapp.domain.preferences.AppTheme
import com.bel.mygymapp.domain.preferences.ThemePreferences
import com.bel.mygymapp.ui.navigation.AppNavigation
import com.bel.mygymapp.ui.theme.MyGymAppTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val themePreferences: ThemePreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentTheme by themePreferences.themeFlow.collectAsState(initial = AppTheme.SYSTEM)
            
            MyGymAppTheme(appTheme = currentTheme) {
                AppNavigation()
            }
        }
    }
}
package com.example.gerenciador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gerenciador.presentation.navigation.AppNavigation // 1. Importe sua navegação
import com.example.gerenciador.ui.theme.GerenciadorTheme
import dagger.hilt.android.AndroidEntryPoint // 2. Importe o Hilt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GerenciadorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Chamando o AppNavigation
                    AppNavigation()
                }
            }
        }

    }
}
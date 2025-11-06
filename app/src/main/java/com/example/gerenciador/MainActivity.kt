package com.example.gerenciador

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.gerenciador.ui.theme.GerenciadorTheme

class MainActivity : ComponentActivity() {

    private companion object {
        const val TAG = "GERENCIADOR_DEBUG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TESTE SIMPLES - sem API por enquanto
        Log.d(TAG, "🎯 APP INICIADO - Permissão de internet adicionada")

        // COMENTE o teste da API temporariamente
        // testApiSimples()

        setContent {
            GerenciadorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(
                        text = "✅ App Funcionando!\nPermissão de Internet Adicionada",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    // COMENTE este método temporariamente
    /*
    private fun testApiSimples() {
        // ... código comentado
    }
    */
}
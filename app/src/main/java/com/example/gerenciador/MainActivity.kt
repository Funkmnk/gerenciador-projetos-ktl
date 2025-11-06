package com.example.gerenciador

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gerenciador.ui.theme.GerenciadorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private companion object {
        const val TAG = "GERENCIADOR_DEBUG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "🎯 APP INICIADO - Testando API GitHub com Compose")

        setContent {
            GerenciadorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Green
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🔧 Testando API GitHub...\nVerifique o Logcat!",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        // TESTE DA API
        testApiGitHub()
    }

    private fun testApiGitHub() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "🔍 PASSO 1: Criando repository...")

                val repository = com.example.gerenciador.data.repository.GitHubRepository()
                Log.d(TAG, "✅ Repository criado com sucesso")

                Log.d(TAG, "🔍 PASSO 2: Testando conexão com api.github.com...")

                // Teste com repositório alternativo
                val result = repository.getIssuesSafe("kotlin", "kotlinx.coroutines")
                Log.d(TAG, "📡 Chamada API concluída")

                if (result.isSuccess) {
                    val issues = result.getOrNull() ?: emptyList()
                    Log.d(TAG, "🎉 SUCESSO TOTAL! ${issues.size} issues encontradas")

                    runOnUiThread {
                        setContent {
                            GerenciadorTheme {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color.Cyan
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "✅ API GitHub FUNCIONANDO!\n${issues.size} issues encontradas",
                                            color = Color.Black,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }

                } else {
                    val error = result.exceptionOrNull()
                    Log.e(TAG, "❌ ERRO NA RESPOSTA: ${error?.message}")

                    runOnUiThread {
                        setContent {
                            GerenciadorTheme {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = Color.Yellow
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "❌ Erro API: ${error?.message}",
                                            color = Color.Black,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "💥 ERRO CRÍTICO: ${e.message}")

                runOnUiThread {
                    setContent {
                        GerenciadorTheme {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = Color.Red
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "💥 Erro: ${e.message}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
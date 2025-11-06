package com.example.gerenciador

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.gerenciador.presentation.navigation.AppNavigation
import com.example.gerenciador.ui.theme.GerenciadorTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TESTE TEMPORÁRIO - REMOVA DEPOIS
        testGitHubApi()

        setContent {
            GerenciadorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    private fun testGitHubApi() {
        lifecycleScope.launch {
            try {
                val repository = com.example.gerenciador.data.repository.GitHubRepository()
                val result = repository.getIssuesSafe("google", "material-design-icons")

                if (result.isSuccess) {
                    val issues = result.getOrNull() ?: emptyList()
                    Log.d("API_TEST", "✅ Sucesso! ${issues.size} issues encontradas")
                    issues.forEach { issue ->
                        Log.d("API_TEST", "Issue: ${issue.title}")
                    }
                } else {
                    val error = result.exceptionOrNull()
                    Log.e("API_TEST", "❌ Erro na API: ${error?.message}")
                }
            } catch (e: Exception) {
                Log.e("API_TEST", "❌ Erro geral: ${e.message}")
            }
        }
    }
}
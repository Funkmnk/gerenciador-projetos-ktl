package com.example.gerenciador.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciador.data.model.Project
import com.example.gerenciador.data.model.ProjectStatus
import com.example.gerenciador.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportRepositoryViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImportUiState())
    val uiState: StateFlow<ImportUiState> = _uiState.asStateFlow()

    fun onOwnerChange(owner: String) {
        _uiState.update { it.copy(owner = owner, errorMessage = null) }
    }

    fun onRepoChange(repo: String) {
        _uiState.update { it.copy(repo = repo, errorMessage = null) }
    }

    /**
     * Importa as issues do GitHub e cria um novo projeto com elas
     */
    fun importRepository() {
        val state = _uiState.value

        // Validação básica
        if (state.owner.isBlank() || state.repo.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Por favor, preencha owner e repositório.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // 1. Criar o projeto primeiro
                val novoProjetoId = repository.insertProject(
                    Project(
                        nome = "${state.owner}/${state.repo}",
                        descricao = "Projeto importado do GitHub",
                        cliente = state.owner,
                        deadline = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000), // +30 dias
                        status = ProjectStatus.EM_ANDAMENTO
                    )
                )

                // 2. Importar as issues como tasks
                val result = repository.importIssuesFromGitHub(
                    owner = state.owner,
                    repo = state.repo,
                    projectId = novoProjetoId
                )

                if (result.isSuccess) {
                    val tasksImportadas = result.getOrDefault(0)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "✅ Projeto criado! $tasksImportadas tarefas importadas.",
                            importCompleted = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "❌ Erro: ${result.exceptionOrNull()?.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "❌ Erro inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}

/**
 * Estado da UI de Importação
 */
data class ImportUiState(
    val owner: String = "",
    val repo: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val importCompleted: Boolean = false
)
package com.example.gerenciador.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciador.data.model.Project // ✅ Import necessário
import com.example.gerenciador.data.model.Task // ✅ Import necessário
import com.example.gerenciador.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn // ✅ Import necessário
import kotlinx.coroutines.flow.onEach // ✅ Import necessário
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val repository: ProjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // --- 1. CORREÇÃO: Ler o ID como LONG ---
    private val navProjectId: Long = savedStateHandle.get<Long>("projectId") ?: 0L

    // States existentes do projeto
    private val _project = MutableStateFlow<Project?>(null)
    val project: StateFlow<Project?> = _project.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // --- TASK 4.3 & 4.4: GITHUB IMPORT ---
    private val _showGitHubImportDialog = MutableStateFlow(false)
    val showGitHubImportDialog: StateFlow<Boolean> = _showGitHubImportDialog.asStateFlow()

    private val _githubImportState = MutableStateFlow(GitHubImportState())
    val githubImportState: StateFlow<GitHubImportState> = _githubImportState.asStateFlow()

    init {
        // O ID agora é Long, tudo vai funcionar
        if (navProjectId != 0L) {
            loadProjectDetails()
            loadTasks()
        } else {
            _errorMessage.update { "Erro fatal: ID do projeto não recebido." }
        }
    }

    // --- FUNÇÕES DO GITHUB IMPORT ---
    fun onGitHubOwnerChange(owner: String) {
        _githubImportState.update { it.copy(owner = owner) }
    }

    fun onGitHubRepoChange(repo: String) {
        _githubImportState.update { it.copy(repo = repo) }
    }

    fun openGitHubImportDialog() {
        _showGitHubImportDialog.value = true
        _errorMessage.value = null // Limpa erros antigos
    }

    fun closeGitHubImportDialog() {
        _showGitHubImportDialog.value = false
        _githubImportState.value = GitHubImportState() // Limpa o formulário
    }

    fun importGitHubIssues() {
        val state = _githubImportState.value

        if (state.owner.isBlank() || state.repo.isBlank()) {
            _errorMessage.value = "Preencha Dono e Repositório."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Chama o Repositório (que espera um Long)
                val result = repository.importIssuesFromGitHub(
                    owner = state.owner,
                    repo = state.repo,
                    projectId = navProjectId // ✅ Agora é Long
                )

                // Sucesso
                _errorMessage.value = "✅ ${result.getOrThrow()} tarefas importadas!"
                closeGitHubImportDialog()
                // O Flow do loadTasks() vai cuidar de atualizar a lista sozinho

            } catch (e: Exception) {
                // Erro
                _errorMessage.value = "❌ Erro ao importar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- FUNÇÕES EXISTENTES ---
    private fun loadProjectDetails() {
        viewModelScope.launch {
            try {
                val projectDetails = repository.getProjectById(navProjectId) // ✅ Agora é Long
                _project.value = projectDetails
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao carregar projeto: ${e.message}"
            }
        }
    }

    // --- 2. CORREÇÃO: loadTasks() agora é REATIVO ---
    private fun loadTasks() {
        // Isso fica "escutando" o banco de dados.
        // Se o importGitHubIssues() adicionar tasks, a UI atualiza sozinha.
        repository.getTasksByProject(navProjectId) // ✅ Agora é Long
            .onEach { taskList ->
                _tasks.update { taskList }
            }
            .launchIn(viewModelScope)
    }

    // Função para o Snackbar (que o ProjectDetailsScreen precisa)
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Data class para estado do formulário GitHub
    data class GitHubImportState(
        val owner: String = "",
        val repo: String = ""
    )
}
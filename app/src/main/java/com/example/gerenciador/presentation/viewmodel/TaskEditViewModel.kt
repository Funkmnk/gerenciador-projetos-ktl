package com.example.gerenciador.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciador.data.model.Task
import com.example.gerenciador.data.model.TaskStatus
import com.example.gerenciador.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    private val repository: ProjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Pega os IDs da rota de navegação
    private val projectId: Long = checkNotNull(savedStateHandle["projectId"])
    private val taskId: Long = savedStateHandle.get<Long>("taskId") ?: -1L

    // Determina se estamos em modo de Edição
    val isEditMode = (taskId != -1L)

    // State para os campos da UI
    private val _taskUiState = MutableStateFlow(TaskUiState())
    val taskUiState: StateFlow<TaskUiState> = _taskUiState.asStateFlow()

    // --- INÍCIO DA ALTERAÇÃO ---
    // Adiciona o state de erro
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Limpa a mensagem de erro (usado pela UI após exibir o Snackbar)
     */
    fun clearErrorMessage() {
        _errorMessage.update { null }
    }
    // --- FIM DA ALTERAÇÃO ---

    init {
        if (isEditMode) {
            loadTaskDetails()
        }
    }

    // Se for modo de edição, carrega os dados da task
    private fun loadTaskDetails() {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId) //
            if (task != null) {
                _taskUiState.update {
                    it.copy(
                        titulo = task.titulo,
                        descricao = task.descricao,
                        status = task.status,
                        taskOriginal = task // Guarda o original para Update/Delete
                    )
                }
            }
        }
    }

    // Chamado quando o usuário digita
    fun onTituloChange(titulo: String) {
        _taskUiState.update { it.copy(titulo = titulo) }
    }
    fun onDescricaoChange(descricao: String) {
        _taskUiState.update { it.copy(descricao = descricao) }
    }
    fun onStatusChange(status: TaskStatus) {
        _taskUiState.update { it.copy(status = status) }
    }

    // Botão SALVAR / ATUALIZAR
    fun saveTask() {
        viewModelScope.launch {
            val state = _taskUiState.value

            // --- ALTERAÇÃO (Bônus: Validação para testar o erro) ---
            if (state.titulo.isBlank()) {
                _errorMessage.update { "O título não pode estar vazio!" }
                return@launch // Para a execução
            }
            // --- FIM DA ALTERAÇÃO ---

            if (isEditMode && state.taskOriginal != null) {
                // Modo UPDATE (Task 3.3)
                val taskAtualizada = state.taskOriginal.copy(
                    titulo = state.titulo,
                    descricao = state.descricao,
                    status = state.status
                )
                repository.updateTask(taskAtualizada) //
            } else {
                // Modo CREATE (Task 3.2)
                val novaTask = Task(
                    projectId = projectId,
                    titulo = state.titulo,
                    descricao = state.descricao,
                    status = state.status
                )
                repository.insertTask(novaTask) //
            }
        }
    }

    // Botão DELETAR (Task 3.4)
    fun deleteTask() {
        viewModelScope.launch {
            val state = _taskUiState.value
            if (isEditMode && state.taskOriginal != null) {
                repository.deleteTask(state.taskOriginal) //
            }
        }
    }
}

// Data class para guardar o estado dos campos da UI
data class TaskUiState(
    val titulo: String = "",
    val descricao: String = "",
    val status: TaskStatus = TaskStatus.PENDENTE,
    val taskOriginal: Task? = null // Referência para a task em edição
)
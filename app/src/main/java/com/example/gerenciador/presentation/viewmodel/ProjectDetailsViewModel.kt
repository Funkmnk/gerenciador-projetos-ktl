package com.example.gerenciador.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciador.data.model.Project
import com.example.gerenciador.data.model.Task
import com.example.gerenciador.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val repository: ProjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _project = MutableStateFlow<Project?>(null)
    val project: StateFlow<Project?> = _project.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val projectId: Long = checkNotNull(savedStateHandle["projectId"])

    init {
        loadProjectDetails()
        loadTasks()
    }

    private fun loadProjectDetails() {
        viewModelScope.launch {
            _isLoading.update { true }
            try {
                val projectDetails = repository.getProjectById(projectId)
                _project.update { projectDetails }
            } catch (e: Exception) {
                _errorMessage.update { "Erro ao carregar projeto: ${e.message}" }
            } finally {
                _isLoading.update { false } // Pode ser mais granular se quiser
            }
        }
    }

    private fun loadTasks() {
        repository.getTasksByProject(projectId)
            .onEach { taskList ->
                _tasks.update { taskList }
            }
            .launchIn(viewModelScope) // Mantém a coroutine viva
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            // O Flow do loadTasks() vai atualizar a UI sozinho
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun clearErrorMessage() {
        _errorMessage.update { null }
    }
}
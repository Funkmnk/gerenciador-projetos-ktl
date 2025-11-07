package com.example.gerenciador.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciador.data.model.Project
import com.example.gerenciador.data.model.ProjectStatus
import com.example.gerenciador.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
// Importações do hilt
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(private val repository: ProjectRepository) : ViewModel() {

    // State para lista de projetos
    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    // State para projeto selecionado
    private val _selectedProject = MutableStateFlow<Project?>(null)
    val selectedProject: StateFlow<Project?> = _selectedProject.asStateFlow()

    // State para loading e erros
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadAllProjects()
    }

    // Carregar todos os projetos
    private fun loadAllProjects() {
        viewModelScope.launch {
            repository.getAllProjects().collect { projectsList ->
                _projects.update { projectsList }
            }
        }
    }

    // Inserir novo projeto
    fun insertProject(project: Project) {
        viewModelScope.launch {
            try {
                _isLoading.update { true }
                repository.insertProject(project)
                _errorMessage.update { null }
            } catch (e: Exception) {
                _errorMessage.update { "Erro ao criar projeto: ${e.message}" }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    // Atualizar projeto
    fun updateProject(project: Project) {
        viewModelScope.launch {
            try {
                _isLoading.update { true }
                repository.updateProject(project)
                _errorMessage.update { null }
            } catch (e: Exception) {
                _errorMessage.update { "Erro ao atualizar projeto: ${e.message}" }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    // Deletar projeto
    fun deleteProject(project: Project) {
        viewModelScope.launch {
            try {
                _isLoading.update { true }
                repository.deleteProject(project)
                _errorMessage.update { null }
            } catch (e: Exception) {
                _errorMessage.update { "Erro ao deletar projeto: ${e.message}" }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    // Selecionar projeto
    fun selectProject(project: Project?) {
        _selectedProject.update { project }
    }

    // Limpar mensagem de erro
    fun clearErrorMessage() {
        _errorMessage.update { null }
    }

    // Buscar projetos por status
    fun getProjectsByStatus(status: ProjectStatus) {
        viewModelScope.launch {
            repository.getProjectsByStatus(status).collect { projectsList ->
                _projects.update { projectsList }
            }
        }
    }

    // Buscar projetos próximos do deadline
    fun getUpcomingDeadlines() {
        viewModelScope.launch {
            repository.getUpcomingDeadlines().collect { projectsList ->
                _projects.update { projectsList }
            }
        }
    }
}
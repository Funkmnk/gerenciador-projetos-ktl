package com.example.gerenciador.data.repository

import com.example.gerenciador.data.dao.ProjectDao
import com.example.gerenciador.data.dao.TaskDao
import com.example.gerenciador.data.model.Project
import com.example.gerenciador.data.model.ProjectStatus
import com.example.gerenciador.data.model.Task
import com.example.gerenciador.data.model.TaskStatus
import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao
) {

    // === MÉTODOS PARA PROJECTS ===

    // Inserir novo projeto
    suspend fun insertProject(project: Project): Long {
        return projectDao.insert(project)
    }

    // Atualizar projeto existente
    suspend fun updateProject(project: Project): Int {
        return projectDao.update(project)
    }

    // Deletar projeto
    suspend fun deleteProject(project: Project): Int {
        return projectDao.delete(project)
    }

    // Buscar projeto por ID
    suspend fun getProjectById(id: Long): Project? {
        return projectDao.getById(id)
    }

    // Obter todos os projetos (Flow para atualizações em tempo real)
    fun getAllProjects(): Flow<List<Project>> {
        return projectDao.getAll()
    }

    // Obter projetos por status
    fun getProjectsByStatus(status: ProjectStatus): Flow<List<Project>> {
        return projectDao.getByStatus(status.name)
    }

    // Obter projetos próximos do deadline
    fun getUpcomingDeadlines(): Flow<List<Project>> {
        return projectDao.getUpcomingDeadlines()
    }

    // Obter projetos por cliente
    fun getProjectsByClient(clientName: String): Flow<List<Project>> {
        return projectDao.getByClient(clientName)
    }

    // Contar total de projetos
    fun getProjectsCount(): Flow<Int> {
        return projectDao.getCount()
    }

    // Buscar projetos (para futura implementação de search)
    fun searchProjects(query: String): Flow<List<Project>> {
        return projectDao.getAll() // Por enquanto retorna todos, depois implementamos a busca
    }

    // === MÉTODOS PARA TASKS ===

    // Inserir nova tarefa
    suspend fun insertTask(task: Task): Long {
        return taskDao.insert(task)
    }

    // Atualizar tarefa existente
    suspend fun updateTask(task: Task): Int {
        return taskDao.update(task)
    }

    // Deletar tarefa
    suspend fun deleteTask(task: Task): Int {
        return taskDao.delete(task)
    }

    // Buscar tarefa por ID
    suspend fun getTaskById(id: Long): Task? {
        return taskDao.getById(id)
    }

    // Obter todas as tarefas de um projeto
    fun getTasksByProject(projectId: Long): Flow<List<Task>> {
        return taskDao.getByProject(projectId)
    }

    // Obter tarefas por status
    fun getTasksByStatus(projectId: Long, status: TaskStatus): Flow<List<Task>> {
        return taskDao.getByStatus(projectId, status.name)
    }

    // Obter tarefas pendentes
    fun getPendingTasks(): Flow<List<Task>> {
        return taskDao.getPendingTasks()
    }

    // Contar tarefas por projeto
    fun getTaskCountByProject(projectId: Long): Flow<Int> {
        return taskDao.getCountByProject(projectId)
    }

    // Contar tarefas por status
    fun getTaskCountByStatus(projectId: Long, status: TaskStatus): Flow<Int> {
        return taskDao.getCountByStatus(projectId, status.name)
    }

    // Deletar tarefas de um projeto
    suspend fun deleteTasksByProject(projectId: Long): Int {
        return taskDao.deleteByProject(projectId)
    }
}
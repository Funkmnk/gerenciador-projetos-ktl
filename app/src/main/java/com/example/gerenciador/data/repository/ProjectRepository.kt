package com.example.gerenciador.data.repository

import com.example.gerenciador.data.dao.ProjectDao
import com.example.gerenciador.data.dao.TaskDao  // ← ADICIONE ESTE IMPORT
import com.example.gerenciador.data.model.Project
import com.example.gerenciador.data.model.ProjectStatus
import com.example.gerenciador.data.model.Task
import com.example.gerenciador.data.model.TaskStatus
import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao  // ← ADICIONE ESTE PARÂMETRO
) {

    // ... métodos existentes do Project ...

    // === NOVOS MÉTODOS PARA TASKS ===

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
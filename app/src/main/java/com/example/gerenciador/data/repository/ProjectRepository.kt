package com.example.gerenciador.data.repository

import com.example.gerenciador.data.dao.ProjectDao
import com.example.gerenciador.data.model.Project
import com.example.gerenciador.data.model.ProjectStatus
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

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
}
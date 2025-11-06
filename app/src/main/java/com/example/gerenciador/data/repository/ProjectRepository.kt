package com.example.gerenciador.data.repository

import com.example.gerenciador.data.dao.ProjectDao
import com.example.gerenciador.data.dao.TaskDao
import com.example.gerenciador.data.model.Project
import com.example.gerenciador.data.model.ProjectStatus
import com.example.gerenciador.data.model.Task
import com.example.gerenciador.data.model.TaskStatus
import com.example.gerenciador.data.remote.GitHubApi
import com.example.gerenciador.data.adapter.GitHubAdapter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
    private val gitHubApi: GitHubApi // <- NOVO: Injetar a API
) {

    // === MÉTODOS EXISTENTES (MANTIDOS) ===

    // ... todos os seus métodos atuais permanecem EXATAMENTE como estão ...

    // === MÉTODOS PARA PROJECTS ===
    suspend fun insertProject(project: Project): Long {
        return projectDao.insert(project)
    }

    suspend fun updateProject(project: Project): Int {
        return projectDao.update(project)
    }

    suspend fun deleteProject(project: Project): Int {
        return projectDao.delete(project)
    }

    suspend fun getProjectById(id: Long): Project? {
        return projectDao.getById(id)
    }

    fun getAllProjects(): Flow<List<Project>> {
        return projectDao.getAll()
    }

    fun getProjectsByStatus(status: ProjectStatus): Flow<List<Project>> {
        return projectDao.getByStatus(status.name)
    }

    fun getUpcomingDeadlines(): Flow<List<Project>> {
        return projectDao.getUpcomingDeadlines()
    }

    fun getProjectsByClient(clientName: String): Flow<List<Project>> {
        return projectDao.getByClient(clientName)
    }

    fun getProjectsCount(): Flow<Int> {
        return projectDao.getCount()
    }

    fun searchProjects(query: String): Flow<List<Project>> {
        return projectDao.getAll()
    }

    // === MÉTODOS PARA TASKS ===
    suspend fun insertTask(task: Task): Long {
        return taskDao.insert(task)
    }

    suspend fun updateTask(task: Task): Int {
        return taskDao.update(task)
    }

    suspend fun deleteTask(task: Task): Int {
        return taskDao.delete(task)
    }

    suspend fun getTaskById(id: Long): Task? {
        return taskDao.getById(id)
    }

    fun getTasksByProject(projectId: Long): Flow<List<Task>> {
        return taskDao.getByProject(projectId)
    }

    fun getTasksByStatus(projectId: Long, status: TaskStatus): Flow<List<Task>> {
        return taskDao.getByStatus(projectId, status.name)
    }

    fun getPendingTasks(): Flow<List<Task>> {
        return taskDao.getPendingTasks()
    }

    fun getTaskCountByProject(projectId: Long): Flow<Int> {
        return taskDao.getCountByProject(projectId)
    }

    fun getTaskCountByStatus(projectId: Long, status: TaskStatus): Flow<Int> {
        return taskDao.getCountByStatus(projectId, status.name)
    }

    suspend fun deleteTasksByProject(projectId: Long): Int {
        return taskDao.deleteByProject(projectId)
    }

    // === NOVOS MÉTODOS PARA INTEGRAÇÃO COM GITHUB API ===

    /**
     * Importa issues do GitHub e converte em tasks para um projeto
     * @param owner Dono do repositório (ex: "google")
     * @param repo Nome do repositório (ex: "material-design-icons")
     * @param projectId ID do projeto onde as tasks serão adicionadas
     * @return Result com o número de tasks importadas ou erro
     */
    suspend fun importIssuesFromGitHub(owner: String, repo: String, projectId: Long): Result<Int> {
        return try {
            // 1. Fazer chamada para a API do GitHub
            val response = gitHubApi.getIssues(owner, repo)

            if (response.isSuccessful) {
                val issues = response.body() ?: emptyList()

                // 2. Converter issues do GitHub em tasks usando o adapter
                val tasks = GitHubAdapter.toTaskList(issues, projectId)

                // 3. Salvar todas as tasks no banco local
                tasks.forEach { task ->
                    taskDao.insert(task)
                }

                // 4. Retornar sucesso com quantidade de tasks importadas
                Result.success(tasks.size)
            } else {
                // Erro HTTP (404, 500, etc)
                Result.failure(Exception("Erro HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            // Erro de rede, timeout, etc
            Result.failure(Exception("Erro de conexão: ${e.message}"))
        }
    }

    /**
     * Testa a conexão com a API do GitHub
     * @return true se a API está acessível, false caso contrário
     */
    suspend fun testGitHubConnection(owner: String = "google", repo: String = "material-design-icons"): Boolean {
        return try {
            val response = gitHubApi.getIssues(owner, repo)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
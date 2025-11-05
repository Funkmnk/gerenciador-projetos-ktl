package com.example.gerenciador.data.adapter

import com.example.gerenciador.data.model.Task
import com.example.gerenciador.data.model.TaskStatus
import com.example.gerenciador.data.remote.model.GitHubIssue
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object GitHubAdapter {

    private val githubDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC") // GitHub dates are in UTC
    }

    /**
     * Converte uma GitHubIssue para uma Task
     */
    fun toTask(issue: GitHubIssue, projectId: Long): Task {
        return Task(
            projectId = projectId,
            titulo = issue.title,
            descricao = issue.body ?: "Sem descrição",
            dataCriacao = parseGitHubDateToTimestamp(issue.createdAt),
            dataConclusao = if (issue.state == "closed") parseGitHubDateToTimestamp(issue.updatedAt) else null,
            status = toTaskStatus(issue.state)
        )
    }

    /**
     * Converte uma lista de GitHubIssues para Tasks
     */
    fun toTaskList(issues: List<GitHubIssue>, projectId: Long): List<Task> {
        return issues.map { toTask(it, projectId) }
    }

    /**
     * Parse seguro para datas do GitHub - retorna timestamp (Long)
     */
    private fun parseGitHubDateToTimestamp(dateString: String): Long {
        return try {
            githubDateFormat.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis() // Fallback para timestamp atual
        }
    }

    private fun toTaskStatus(issueState: String): TaskStatus {
        return when (issueState.lowercase()) {
            "closed" -> TaskStatus.CONCLUIDA
            "open" -> TaskStatus.PENDENTE
            else -> TaskStatus.PENDENTE
        }
    }

    /**
     * Método utilitário para formatar timestamp para exibição
     */
    fun formatTimestampForDisplay(timestamp: Long): String {
        return try {
            val displayFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            displayFormat.format(timestamp)
        } catch (e: Exception) {
            "Data inválida"
        }
    }
}
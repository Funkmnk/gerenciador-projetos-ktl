
package com.example.gerenciador.util

import com.example.gerenciador.data.model.Project
import com.example.gerenciador.data.model.Task
import com.example.gerenciador.data.model.TaskStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Criamos um 'object' (Singleton) para não precisar injetar
object ShareUtil {

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    /**
     * Gera um relatório em texto de um projeto e suas tarefas.
     * Esta é a lógica da Task 5.1
     */
    fun createProjectReport(project: Project, tasks: List<Task>): String {
        val stringBuilder = StringBuilder()

        // 1. Título e Cliente
        stringBuilder.appendLine("🚀 Relatório do Projeto: ${project.nome}")
        stringBuilder.appendLine("Cliente: ${project.cliente}")

        // 2. Deadline
        val deadlineFormatado = try {
            dateFormatter.format(Date(project.deadline))
        } catch (e: Exception) {
            "N/A"
        }
        stringBuilder.appendLine("Prazo: $deadlineFormatado")

        // 3. Status Geral
        val concluidas = tasks.count { it.status == TaskStatus.CONCLUIDA }
        val total = tasks.size
        stringBuilder.appendLine("Progresso: $concluidas de $total tarefas concluídas.")
        stringBuilder.appendLine() // Linha em branco

        // 4. Lista de Tarefas
        if (tasks.isEmpty()) {
            stringBuilder.appendLine("Nenhuma tarefa cadastrada.")
        } else {
            stringBuilder.appendLine("--- Tarefas ---")
            tasks.forEach { task ->
                // Formato: • Título da Tarefa (STATUS)
                stringBuilder.appendLine("• ${task.titulo} (${task.status})")
            }
        }

        // Retorna o texto completo
        return stringBuilder.toString()
    }

    /**
     * Gera um texto simples para compartilhar uma única tarefa.
     */
    fun createTaskReport(task: Task, projectName: String): String {
        return """
            Confira o status da minha tarefa:
            Projeto: $projectName
            Tarefa: ${task.titulo}
            Status: ${task.status}
        """.trimIndent()
    }
}
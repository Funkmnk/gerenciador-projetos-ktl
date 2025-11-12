package com.example.gerenciador.presentation.navigation

sealed class Screen(val route: String) {
    object ProjectList : Screen("project_list")
    object AddProject : Screen("add_project?projectId={projectId}") {
        // Helper function para construir a rota de edição
        fun withId(id: Long): String {
            return "add_project?projectId=$id"
        }
        // Helper function para a rota de criação (sem ID)
        fun create(): String {
            return "add_project"
        }
    }

    object ProjectDetails : Screen("project_details/{projectId}") {
        // Helper function para construir a rota
        fun withId(id: Long): String {
            return "project_details/$id"
        }
    }

    // --- ALTERAÇÕES AQUI ---
    // Adicionamos a nova rota para Criar/Editar Tarefas.
    // Ela depende do projectId (na rota) e do taskId (opcional, como query)
    object TaskEdit : Screen("project_details/{projectId}/task_edit?taskId={taskId}") {
        // Rota para CRIAR uma nova task (passa -1L como ID da task)
        fun create(projectId: Long): String {
            return "project_details/$projectId/task_edit?taskId=-1"
        }

        // Rota para EDITAR uma task existente
        fun withId(projectId: Long, taskId: Long): String {
            return "project_details/$projectId/task_edit?taskId=$taskId"
        }
    }
    // Removemos TaskList e AddTask, pois TaskEdit substitui a necessidade deles
}
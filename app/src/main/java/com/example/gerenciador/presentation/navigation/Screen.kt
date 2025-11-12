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

    object TaskList : Screen("task_list")
    object AddTask : Screen("add_task")
}
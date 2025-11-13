package com.example.gerenciador.presentation.navigation

sealed class Screen(val route: String) {
    object ProjectList : Screen("project_list")

    // Rota para Criar/Editar Projetos
    object AddProject : Screen("add_project?projectId={projectId}") {
        fun create(): String {
            return "add_project"
        }
        fun withId(id: Long): String {
            return "add_project?projectId=$id"
        }
    }

    // Rota de Detalhes do Projeto
    object ProjectDetails : Screen("project_details/{projectId}") {
        fun withId(id: Long): String {
            return "project_details/$id"
        }
    }

    // --- ✅ CORREÇÃO APLICADA AQUI ---
    // Rotas de Task separadas para mais clareza

    // Rota para Criar/Editar Tarefas
    // (Substitui a lógica confusa que estava antes)
    object TaskEdit : Screen("task_edit?projectId={projectId}&taskId={taskId}") {
        // Rota para CRIAR (precisa do projectId, mas taskId é opcional)
        fun create(projectId: Long): String {
            return "task_edit?projectId=$projectId"
        }
        // Rota para EDITAR (precisa dos dois IDs)
        fun withId(projectId: Long, taskId: Long): String {
            return "task_edit?projectId=$projectId&taskId=$taskId"
        }
    }
}
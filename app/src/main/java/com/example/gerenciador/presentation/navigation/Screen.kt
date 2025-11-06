package com.example.gerenciador.presentation.navigation

sealed class Screen(val route: String) {
    object ProjectList : Screen("project_list")
    object AddProject : Screen("add_project")
    object ProjectDetails : Screen("project_details")
    object TaskList : Screen("task_list")
    object AddTask : Screen("add_task")
}
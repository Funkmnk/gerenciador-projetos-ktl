package com.example.gerenciador.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gerenciador.presentation.screens.ProjectListScreen
// Project Screen
import com.example.gerenciador.presentation.screens.AddProjectScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
// Importar a tela de Detalhes
import com.example.gerenciador.presentation.screens.ProjectDetailsScreen
// Importar a nova tela de Edição/Criação de Task
import com.example.gerenciador.presentation.screens.TaskEditScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ProjectList.route
    ) {
        composable(route = Screen.ProjectList.route) {
            ProjectListScreen(
                onAddProject = {
                    // TODO: Navegar para tela de adicionar projeto
                    navController.navigate(Screen.AddProject.create())
                },
                onProjectClick = { projectId ->
                    navController.navigate(Screen.ProjectDetails.withId(projectId))
                }
            )
        }

        composable(
            route = Screen.AddProject.route, // A rota com argumento opcional
            arguments = listOf(
                navArgument("projectId") {
                    type = NavType.LongType
                    defaultValue = -1L // Um valor padrão que significa "nenhum ID"
                }
            )
        ) { backStackEntry ->
            // A gente pega o ID que veio pela rota
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: -1L

            AddProjectScreen(
                navController = navController,
                projectId = if (projectId == -1L) null else projectId // Passa null se for -1
            )
        }

        composable(
            route = Screen.ProjectDetails.route, // Rota com ID obrigatório
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            ProjectDetailsScreen(
                navController = navController
            )
        }

        // --- NOVA TELA ADICIONADA AQUI ---
        composable(
            route = Screen.TaskEdit.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType },
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L // O valor padrão para "Criar"
                }
            )
        ) {
            TaskEditScreen(navController = navController)
        }
    }
}
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
                    // TODO: Navegar para detalhes do projeto
                    navController.navigate(Screen.AddProject.withId(projectId))
                }
            )
        }

        // TODO: Adicionar outras telas quando estiverem prontas

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

        /*
        composable(
            route = "${Screen.ProjectDetails.route}/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
            // ProjectDetailsScreen(projectId = projectId, ...)
        }
        */
    }
}
package com.example.gerenciador.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gerenciador.presentation.screens.ProjectListScreen
// Project Screen
import com.example.gerenciador.presentation.screens.AddProjectScreen

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
                    navController.navigate(Screen.AddProject.route)
                },
                onProjectClick = { projectId ->
                    // TODO: Navegar para detalhes do projeto
                    // navController.navigate("${Screen.ProjectDetails.route}/$projectId")
                }
            )
        }

        // TODO: Adicionar outras telas quando estiverem prontas

        composable(route = Screen.AddProject.route) {
            AddProjectScreen(
                navController = navController
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
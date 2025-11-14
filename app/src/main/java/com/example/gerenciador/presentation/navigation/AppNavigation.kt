package com.example.gerenciador.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gerenciador.presentation.screens.ProjectListScreen
import com.example.gerenciador.presentation.screens.AddProjectScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.gerenciador.presentation.screens.ProjectDetailsScreen
import com.example.gerenciador.presentation.screens.TaskEditScreen
import com.example.gerenciador.presentation.screens.ImportRepositoryScreen

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
                    navController.navigate(Screen.AddProject.create())
                },
                onProjectClick = { projectId ->
                    navController.navigate(Screen.ProjectDetails.withId(projectId))
                },
                onImportFromGitHub = {
                    navController.navigate(Screen.ImportRepository.route)
                }
            )
        }

        composable(
            route = Screen.AddProject.route,
            arguments = listOf(
                navArgument("projectId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: -1L

            AddProjectScreen(
                navController = navController,
                projectId = if (projectId == -1L) null else projectId
            )
        }

        composable(
            route = Screen.ProjectDetails.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) {
            ProjectDetailsScreen(navController = navController)
        }

        composable(
            route = Screen.TaskEdit.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType },
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            TaskEditScreen(navController = navController)
        }

        // 🆕 NOVA ROTA - IMPORTAR DO GITHUB
        composable(route = Screen.ImportRepository.route) {
            ImportRepositoryScreen(navController = navController)
        }
    }
}
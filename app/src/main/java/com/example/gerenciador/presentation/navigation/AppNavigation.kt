package com.example.gerenciador.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gerenciador.presentation.screens.AddProjectScreen
import com.example.gerenciador.presentation.screens.ProjectDetailsScreen
import com.example.gerenciador.presentation.screens.ProjectListScreen
import com.example.gerenciador.presentation.screens.TaskEditScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ProjectList.route
    ) {
        // ... (composable 'ProjectList' e 'AddProject' continuam iguais) ...

        composable(route = Screen.ProjectList.route) {
            ProjectListScreen(
                onAddProject = {
                    navController.navigate(Screen.AddProject.create())
                },
                onProjectClick = { projectId ->
                    navController.navigate(Screen.ProjectDetails.withId(projectId))
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
            ProjectDetailsScreen(
                navController = navController
            )
        }

        // --- ✅ CORREÇÃO APLICADA AQUI ---
        // Atualiza a rota para bater com o novo Screen.kt
        composable(
            route = Screen.TaskEdit.route, // Usa a nova rota
            arguments = listOf(
                navArgument("projectId") { // ID do projeto (obrigatório)
                    type = NavType.LongType
                    defaultValue = -1L // (Fallback, embora 'create' sempre deva passar)
                },
                navArgument("taskId") { // ID da task (opcional)
                    type = NavType.LongType
                    defaultValue = -1L // Valor padrão para "Criar nova task"
                }
            )
        ) {
            // O mesmo TaskEditScreen funciona aqui
            TaskEditScreen(navController = navController)
        }
    }
}
package com.example.gerenciador.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gerenciador.data.model.Task
import com.example.gerenciador.presentation.navigation.Screen // A gente vai precisar disso
import com.example.gerenciador.presentation.viewmodel.ProjectDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreen(
    navController: NavController,
    // Hilt injeta o ViewModel e o SavedStateHandle pra gente, tudo automático
    viewModel: ProjectDetailsViewModel = hiltViewModel()
) {
    // 1. Coleta os states do ViewModel que a gente criou
    val project by viewModel.project.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Boa prática: cria a cópia local estável
    val currentProject = project

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Mostra o nome do projeto (ou "Carregando...")
                    Text(currentProject?.nome ?: "Carregando Detalhes...")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    // --- O "Pulo do Gato" da nossa Opção A ---
                    // Botão de Editar que leva para a tela de Edição
                    IconButton(onClick = {
                        if (currentProject != null) {
                            // Navega para a tela de Edição passando o ID
                            navController.navigate(Screen.AddProject.withId(currentProject.id))
                        }
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar Projeto"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: Implementar Task 3.2
                    // navController.navigate(Screen.AddTask.withId(currentProject.id))
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Tarefa")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading && currentProject == null) {
                // Loading inicial
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (tasks.isEmpty() && !isLoading) {
                // Estado Vazio
                Text(
                    text = "Nenhuma tarefa encontrada. Toque em + para adicionar.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // 3. A Lista de Tarefas (Task 3.1)
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onClick = {
                                // TODO: Implementar Task 3.3
                                // navController.navigate(Screen.EditTask.withId(task.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Um Composable simples para mostrar um item da lista de tarefas.
 * (Isso implementa o "Read" da Task 3.1)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = task.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = task.descricao.ifEmpty { "Sem descrição." },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )
            Text(
                text = "Status: ${task.status.name}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
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
import androidx.compose.material.icons.filled.Download // (Ícone do passo anterior)
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gerenciador.data.model.Task
import com.example.gerenciador.presentation.navigation.Screen // Import principal
import com.example.gerenciador.presentation.screens.composables.GitHubImportDialog
import com.example.gerenciador.presentation.viewmodel.ProjectDetailsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreen(
    navController: NavController,
    viewModel: ProjectDetailsViewModel = hiltViewModel()
) {
    // ... (States e Snackbar permanecem iguais) ...
    val project by viewModel.project.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val showImportDialog by viewModel.showGitHubImportDialog.collectAsState()
    val importState by viewModel.githubImportState.collectAsState()
    val currentProject = project
    val projectId = currentProject?.id ?: 0L
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentProject?.nome ?: "Detalhes do Projeto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    // Botão de Importar
                    IconButton(onClick = { viewModel.openGitHubImportDialog() }) {
                        Icon(Icons.Default.Download, "Importar do GitHub")
                    }
                    // Botão de Editar Projeto
                    IconButton(onClick = {
                        if (projectId != 0L) {
                            // (Chamada para AddProject continua a mesma)
                            navController.navigate(Screen.AddProject.withId(projectId))
                        }
                    }) {
                        Icon(Icons.Default.Edit, "Editar Projeto")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (projectId != 0L) {
                        // --- ✅ CORREÇÃO APLICADA AQUI ---
                        // Chamando a nova rota de criação de task
                        navController.navigate(Screen.TaskEdit.create(projectId))
                    }
                }
            ) {
                Icon(Icons.Default.Add, "Adicionar Tarefa")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ... (Lógica de Loading e Estado Vazio permanece a mesma) ...
            if (isLoading && tasks.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (tasks.isEmpty() && !isLoading) {
                Text(
                    text = "Nenhuma tarefa encontrada. Toque em + para adicionar.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // Lista de Tarefas
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onClick = {
                                // --- ✅ CORREÇÃO APLICADA AQUI ---
                                // Chamando a nova rota de edição de task
                                navController.navigate(Screen.TaskEdit.withId(projectId, task.id))
                            }
                        )
                    }
                }
            }

            // Dialog do GitHub Import (permanece igual)
            GitHubImportDialog(
                showDialog = showImportDialog,
                owner = importState.owner,
                repo = importState.repo,
                onOwnerChange = viewModel::onGitHubOwnerChange,
                onRepoChange = viewModel::onGitHubRepoChange,
                onDismiss = viewModel::closeGitHubImportDialog,
                onConfirm = viewModel::importGitHubIssues
            )
        }
    }
}

// O TaskItem (Composable) permanece o mesmo
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit
) {
    // ... (código do TaskItem) ...
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
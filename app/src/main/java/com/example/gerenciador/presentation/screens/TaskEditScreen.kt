package com.example.gerenciador.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gerenciador.data.model.TaskStatus
import com.example.gerenciador.presentation.viewmodel.TaskEditViewModel
import kotlinx.coroutines.flow.collectLatest // Importação necessária

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    navController: NavController,
    viewModel: TaskEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.taskUiState.collectAsState()
    val isEditMode = viewModel.isEditMode
    var showDeleteDialog by remember { mutableStateOf(false) }

    // --- INÍCIO DA ALTERAÇÃO ---
    // Pega o erro e o estado do Snackbar
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Observador que mostra o Snackbar quando o erro muda
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearErrorMessage() // Limpa o erro
        }
    }
    // --- FIM DA ALTERAÇÃO ---

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Tarefa" else "Nova Tarefa") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        // --- INÍCIO DA ALTERAÇÃO ---
        // Adiciona o "lugar" onde o Snackbar vai aparecer
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        // --- FIM DA ALTERAÇÃO ---
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = viewModel::onTituloChange,
                label = { Text("Título da Tarefa") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.descricao,
                onValueChange = viewModel::onDescricaoChange,
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // TODO: Bônus - Trocar isso por um DropdownMenu
            Text("Status: ${uiState.status}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // --- ALTERAÇÃO ---
                    // Só chamamos o saveTask.
                    // Se o título estiver vazio, o ViewModel vai nos mandar um erro.
                    // Se salvar certo, o usuário vai ver a UI atualizar.
                    viewModel.saveTask()

                    // Se não for modo de edição, provavelmente queremos voltar
                    if (!isEditMode && uiState.titulo.isNotBlank()) {
                        navController.popBackStack()
                    }
                    // Se for modo de edição, ficamos na tela
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "ATUALIZAR TAREFA" else "SALVAR TAREFA")
            }

            // Botão de excluir
            if (isEditMode) {
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("EXCLUIR TAREFA")
                }
            }
        }

        // Dialog de Confirmação
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirmar Exclusão") },
                text = { Text("Tem certeza que deseja excluir esta tarefa?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteTask()
                            showDeleteDialog = false
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Excluir")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
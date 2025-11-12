package com.example.gerenciador.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gerenciador.data.model.Project // Importe o modelo
import com.example.gerenciador.presentation.viewmodel.ProjectViewModel // Importe o ViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.DisposableEffect
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectScreen(
    navController: NavController,
    viewModel: ProjectViewModel = hiltViewModel(), projectId: Long?
) {
    // Determina o modo de edição
    val isEditMode = projectId != null

    // Pega o projeto que está sendo editado
    val projectToEdit by viewModel.selectedProject.collectAsState()
    val currentProject = projectToEdit

    // Estados para guardar o que o usuário digita
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") } // Vamos tratar como texto por enquanto

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(projectId) {
        if (isEditMode && projectId != null) {
            viewModel.loadProjectById(projectId)
        } else {
            viewModel.clearSelectedProject()
        }
    }

    LaunchedEffect(projectToEdit) {
        if (isEditMode && projectToEdit != null) {
            nome = projectToEdit!!.nome
            descricao = projectToEdit!!.descricao
            cliente = projectToEdit!!.cliente
            deadline = try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                sdf.format(Date(projectToEdit!!.deadline))
            } catch (e: Exception) {
                ""
            }
        }
    }

    // Limpa o State
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedProject()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // Título dinâmico
                title = { Text(if (isEditMode) "Editar Projeto" else "Novo Projeto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // --- CORREÇÃO (Bônus ArrowBack) ---
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        // --- FIM DA CORREÇÃO ---
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Projeto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Prazo (ex: 2025-12-31)") }, // TODO: Usar um DatePicker no futuro
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // TODO: Adicionar validação dos campos

                    // Converte a data (String) para Long (Timestamp)
                    val deadlineTimestamp = try {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        sdf.parse(deadline)?.time ?: System.currentTimeMillis()
                    } catch (e: Exception) {
                        System.currentTimeMillis()
                    }

                    if (isEditMode && currentProject != null) { // Usando a cópia local
                        // Modo UPDATE
                        val projetoAtualizado = currentProject.copy( // Usando a cópia local
                            nome = nome,
                            descricao = descricao,
                            cliente = cliente,
                            deadline = deadlineTimestamp
                        )
                        viewModel.updateProject(projetoAtualizado)

                    } else {
                        val novoProjeto = Project(
                            nome = nome,
                            descricao = descricao,
                            cliente = cliente,
                            deadline = deadlineTimestamp
                        )
                        viewModel.insertProject(novoProjeto)
                    }

                    // Volta para a tela anterior
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Texto dinamico
                Text(if (isEditMode) "ATUALIZAR PROJETO" else "SALVAR PROJETO")
            }

            // Botão de excluir (só aparece no modo de edição)
            if (isEditMode) {
                Spacer(modifier = Modifier.height(8.dp)) // Um respiro

                OutlinedButton(
                    onClick = {
                        // Abre o dialog de confirmação
                        showDeleteDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error // Vermelho
                    )
                ) {
                    Text("EXCLUIR PROJETO")
                }
            }
        }

        // Dialog de Confirmação de Exclusão
        if (showDeleteDialog && isEditMode && currentProject != null) { // Usando a cópia local
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirmar Exclusão") },
                text = { Text("Tem certeza que deseja excluir o projeto \"${currentProject.nome}\"? Esta ação não pode ser desfeita.") }, // Usando a cópia local

                // Botão de Confirmação (Excluir)
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Chama o ViewModel para deletar
                            viewModel.deleteProject(currentProject) // Usando a cópia local
                            // Fecha o dialog
                            showDeleteDialog = false
                            // Volta para a tela de lista
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error // Cor vermelha
                        )
                    ) {
                        Text("Excluir")
                    }
                },

                // Botão de Cancelar
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false } // Só fecha o dialog
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
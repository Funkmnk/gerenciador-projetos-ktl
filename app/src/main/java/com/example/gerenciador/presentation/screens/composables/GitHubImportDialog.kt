package com.example.gerenciador.presentation.screens.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Dialog (pop-up) para importar Issues do GitHub. (Task 4.3)
 */
@Composable
fun GitHubImportDialog(
    showDialog: Boolean,
    owner: String,
    repo: String,
    // isLoading: Boolean, // Descomente para mostrar um loading no dialog
    onOwnerChange: (String) -> Unit,
    onRepoChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Importar Tarefas do GitHub") },
            text = {
                Column {
                    Text("Digite o dono e o nome do repositório público.")
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = owner,
                        onValueChange = onOwnerChange,
                        label = { Text("Dono (ex: google)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = repo,
                        onValueChange = onRepoChange,
                        label = { Text("Repositório (ex: material-design-icons)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Importar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}
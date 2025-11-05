package com.example.gerenciador.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val titulo: String,
    val descricao: String,
    val dataCriacao: Long = System.currentTimeMillis(), // ← MUDE para Long
    val dataConclusao: Long? = null, // ← MUDE para Long
    val status: TaskStatus = TaskStatus.PENDENTE
)

enum class TaskStatus {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDA,
    CANCELADA
}
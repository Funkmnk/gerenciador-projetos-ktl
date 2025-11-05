package com.example.gerenciador.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

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
    val dataCriacao: LocalDateTime = LocalDateTime.now(),
    val dataConclusao: LocalDateTime? = null,
    val status: TaskStatus = TaskStatus.PENDENTE
)

enum class TaskStatus {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDA,
    CANCELADA
}
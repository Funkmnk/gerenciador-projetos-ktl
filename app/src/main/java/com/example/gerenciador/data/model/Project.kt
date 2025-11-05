package com.example.gerenciador.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val descricao: String,
    val cliente: String,
    val deadline: LocalDateTime,
    val dataCriacao: LocalDateTime = LocalDateTime.now(),
    val status: ProjectStatus = ProjectStatus.EM_ANDAMENTO
)

enum class ProjectStatus {
    EM_ANDAMENTO,
    CONCLUIDO,
    CANCELADO
}
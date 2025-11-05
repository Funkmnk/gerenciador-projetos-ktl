package com.example.gerenciador.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val descricao: String,
    val cliente: String,
    val deadline: Long, // ← MUDE para Long (timestamp)
    val dataCriacao: Long = System.currentTimeMillis(), // ← MUDE para Long
    val status: ProjectStatus = ProjectStatus.EM_ANDAMENTO
)

enum class ProjectStatus {
    EM_ANDAMENTO,
    CONCLUIDO,
    CANCELADO
}
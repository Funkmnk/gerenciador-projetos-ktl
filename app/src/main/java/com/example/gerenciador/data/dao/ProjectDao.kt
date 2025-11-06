package com.example.gerenciador.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gerenciador.data.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    // Inserir um novo projeto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: Project): Long

    // Atualizar um projeto existente
    @Update
    suspend fun update(project: Project): Int

    // Deletar um projeto
    @Delete
    suspend fun delete(project: Project): Int

    // Buscar projeto por ID
    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getById(id: Long): Project?

    // Buscar todos os projetos (Flow para atualizações em tempo real)
    @Query("SELECT * FROM projects ORDER BY dataCriacao DESC")
    fun getAll(): Flow<List<Project>>

    // Buscar projetos por status
    @Query("SELECT * FROM projects WHERE status = :status ORDER BY deadline ASC")
    fun getByStatus(status: String): Flow<List<Project>>

    // Buscar projetos próximos do deadline (próximos 7 dias)
    @Query("SELECT * FROM projects WHERE deadline BETWEEN :currentTime AND :currentTime + 604800000 ORDER BY deadline ASC") // 604800000 = 7 dias em milissegundos
    fun getUpcomingDeadlines(currentTime: Long = System.currentTimeMillis()): Flow<List<Project>>

    // Buscar projetos por cliente
    @Query("SELECT * FROM projects WHERE cliente LIKE '%' || :clientName || '%' ORDER BY nome ASC")
    fun getByClient(clientName: String): Flow<List<Project>>

    // Contar total de projetos
    @Query("SELECT COUNT(*) FROM projects")
    fun getCount(): Flow<Int>
}
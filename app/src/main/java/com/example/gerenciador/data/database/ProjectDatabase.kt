package com.example.gerenciador.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.gerenciador.data.dao.ProjectDao
import com.example.gerenciador.data.dao.TaskDao
import com.example.gerenciador.data.model.Project
import com.example.gerenciador.data.model.Task

@Database(
    entities = [Project::class, Task::class],
    version = 1,
    exportSchema = false
)
// REMOVA esta linha: @TypeConverters(DateConverter::class)
abstract class ProjectDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: ProjectDatabase? = null

        fun getDatabase(context: Context): ProjectDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProjectDatabase::class.java,
                    "project_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
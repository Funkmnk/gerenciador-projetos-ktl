package com.example.gerenciador.di

import android.content.Context
import com.example.gerenciador.data.database.ProjectDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProjectDatabase(@ApplicationContext context: Context): ProjectDatabase {
        return ProjectDatabase.getDatabase(context)
    }

    @Provides
    fun provideProjectDao(database: ProjectDatabase) = database.projectDao()

    @Provides
    fun provideTaskDao(database: ProjectDatabase) = database.taskDao()
}
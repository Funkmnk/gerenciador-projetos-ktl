package com.example.gerenciador.di

import com.example.gerenciador.data.remote.ApiClient
import com.example.gerenciador.data.remote.GitHubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGitHubApi(): GitHubApi {
        return ApiClient.gitHubApi
    }
}
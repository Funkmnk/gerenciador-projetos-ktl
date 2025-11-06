package com.example.gerenciador.data.repository

import com.example.gerenciador.data.remote.ApiClient
import com.example.gerenciador.data.remote.GitHubApi
import com.example.gerenciador.data.remote.model.GitHubIssue

class GitHubRepository {

    private val gitHubApi: GitHubApi = ApiClient.gitHubApi

    suspend fun getIssuesSafe(owner: String, repo: String): Result<List<GitHubIssue>> {
        return try {
            val response = gitHubApi.getIssues(owner, repo)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
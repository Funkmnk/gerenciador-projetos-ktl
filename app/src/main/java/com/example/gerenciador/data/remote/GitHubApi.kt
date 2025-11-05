package com.example.gerenciador.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    /**
     * Busca issues de um repositório específico
     * Exemplo: /repos/usuario/repositorio/issues
     */
    @GET("repos/{owner}/{repo}/issues")
    suspend fun getIssues(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("state") state: String = "open", // open, closed, all
        @Query("per_page") perPage: Int = 100
    ): Response<List<GitHubIssue>>

    /**
     * Busca repositórios por linguagem (exemplo adicional)
     */
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc"
    ): Response<GitHubSearchResponse>
}
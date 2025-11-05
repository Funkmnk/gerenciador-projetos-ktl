package com.example.gerenciador.data.remote.model

import com.google.gson.annotations.SerializedName

data class GitHubIssue(
    @SerializedName("id")
    val id: Long,

    @SerializedName("number")
    val number: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String?,

    @SerializedName("state")
    val state: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("html_url")
    val htmlUrl: String,

    @SerializedName("user")
    val user: GitHubUser
)

data class GitHubUser(
    @SerializedName("login")
    val login: String,

    @SerializedName("avatar_url")
    val avatarUrl: String
)

data class GitHubSearchResponse(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("items")
    val items: List<GitHubRepository>
)

data class GitHubRepository(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("html_url")
    val htmlUrl: String,

    @SerializedName("stargazers_count")
    val stargazersCount: Int,

    @SerializedName("language")
    val language: String?
)
package com.example.repositorioskotlin.Model

import com.google.gson.annotations.SerializedName

data class Repo (
    @SerializedName("name")
    var name: String,

    @SerializedName("owner")
    var owner: Owner,

    @SerializedName("html_url")
    var htmlUrl: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("created_at")
    var createdAt: String
)
package com.example.repositorioskotlin.Network

import com.example.repositorioskotlin.Model.Repo
import retrofit2.Call

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // No usar suspend
    // https://stackoverflow.com/questions/75139082/jsonioexception-interfaces-cant-be-instantiated-register-an-instancecreator-o
    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username: String?): Call<ArrayList<Repo>>

    @GET("users/{username}/repos")
    suspend fun listReposResponse(@Path("username") username: String?): Response<ArrayList<Repo>>

    @GET("ficheros/repos.json")
    fun getRepos(): Call<ArrayList<Repo>>
}
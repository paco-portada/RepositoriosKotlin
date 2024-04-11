package com.example.repositorioskotlin.Network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiAdapter {
    private var API_SERVICE: ApiService? = null
    const val BASE_URL = "https://api.github.com/"
    //const val BASE_URL = "https://api.githubmal.com/"

    // const val BASE_URL = "http://192.168.97.163/"

    // const val BASE_URL = "https://dam.org.es/"
    // Añadir al manifiesto
    // android:usesCleartextTraffic="true"

    @get:Synchronized
    val instance: ApiService?

        get() {
            if (API_SERVICE == null) {

                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()

                val gson = GsonBuilder()
                    .setDateFormat("dd-MM-yyyy")
                    .create()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build()

                API_SERVICE = retrofit.create(ApiService::class.java)
            }
            return API_SERVICE
        }
}
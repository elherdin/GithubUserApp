package com.example.submissionawal.data.retrofit

import com.example.submissionawal.BuildConfig
import com.example.submissionawal.data.response.DetailUserResponse
import com.example.submissionawal.data.response.GithubResponse
import com.example.submissionawal.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") query: String,
        @Header("Authorization") key: String = BuildConfig.KEY
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUsers(
        @Path("username") username: String,
        @Header("Authorization") key: String = BuildConfig.KEY
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String,
        @Header("Authorization") key: String = BuildConfig.KEY
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String,
        @Header("Authorization") key: String = BuildConfig.KEY
    ): Call<List<ItemsItem>>
}


package com.dicoding.picodiploma.githubuserapp.api

import com.dicoding.picodiploma.githubuserapp.response.DetailUserRespon
import com.dicoding.picodiploma.githubuserapp.response.User
import com.dicoding.picodiploma.githubuserapp.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserRespon>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}
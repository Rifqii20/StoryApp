package com.dicoding.rifqi.storyapp.data.api

import com.dicoding.rifqi.storyapp.data.request.LoginBody
import com.dicoding.rifqi.storyapp.data.request.LoginRequest
import com.dicoding.rifqi.storyapp.data.request.RegisterBody
import com.dicoding.rifqi.storyapp.data.request.RegisterRequest
import com.dicoding.rifqi.storyapp.data.response.ApiResponse
import com.dicoding.rifqi.storyapp.data.response.LoginResponse
import com.dicoding.rifqi.storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(
        @Body request: RegisterRequest
    ): Call<ApiResponse>

    @POST("login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<ApiResponse>
}
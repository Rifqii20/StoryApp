package com.dicoding.rifqi.storyapp.data.response

import com.google.gson.annotations.SerializedName
import retrofit2.Callback

data class ApiResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

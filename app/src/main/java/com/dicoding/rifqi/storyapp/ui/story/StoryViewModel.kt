package com.dicoding.rifqi.storyapp.ui.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.rifqi.storyapp.data.Resource
import com.dicoding.rifqi.storyapp.data.api.ApiConfig
import com.dicoding.rifqi.storyapp.data.preference.UserPreference
import com.dicoding.rifqi.storyapp.data.response.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(private val pref: UserPreference) : ViewModel() {
    private val _uploadStory = MutableLiveData<Resource<String>>()
    val uploadStory: LiveData<Resource<String>> = _uploadStory

    suspend fun uploadImage(
        imageMultipart: MultipartBody.Part, description: RequestBody,
    ) {
        _uploadStory.postValue(Resource.Loading())
        val client = ApiConfig.getApiService().uploadImage(
            auth = "Bearer ${pref.getToken().first()}",
            imageMultipart,
            description
        )

        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    _uploadStory.postValue(Resource.Success(response.body()?.message))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ApiResponse::class.java
                    )
                    _uploadStory.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(
                    StoryViewModel::class.java.simpleName,
                    "onFailure upload"
                )
                _uploadStory.postValue(Resource.Error(t.message))
            }
        })
    }
}
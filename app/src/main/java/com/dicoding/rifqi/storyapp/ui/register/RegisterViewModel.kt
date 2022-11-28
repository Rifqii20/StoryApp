package com.dicoding.rifqi.storyapp.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.rifqi.storyapp.data.Resource
import com.dicoding.rifqi.storyapp.data.api.ApiConfig
import com.dicoding.rifqi.storyapp.data.preference.UserPreference
import com.dicoding.rifqi.storyapp.data.request.RegisterRequest
import com.dicoding.rifqi.storyapp.data.response.ApiResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {
    private val _userInfo = MutableLiveData<Resource<String>>()
    val userInfo: LiveData<Resource<String>> = _userInfo

    fun register(name: String, email: String, password: String) {
        _userInfo.postValue(Resource.Loading())
        val client = ApiConfig.getApiService().register(RegisterRequest(name, email, password))

        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message.toString()
                    _userInfo.postValue(Resource.Success(message))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ApiResponse::class.java
                    )
                    _userInfo.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(
                    RegisterViewModel::class.java.simpleName,
                    "onFailure register"
                )
                _userInfo.postValue(Resource.Error(t.message))
            }
        })
    }
}
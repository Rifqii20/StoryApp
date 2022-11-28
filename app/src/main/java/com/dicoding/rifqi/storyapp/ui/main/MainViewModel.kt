package com.dicoding.rifqi.storyapp.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.rifqi.storyapp.data.Resource
import com.dicoding.rifqi.storyapp.data.api.ApiConfig
import com.dicoding.rifqi.storyapp.data.preference.UserPreference
import com.dicoding.rifqi.storyapp.data.response.ListStory
import com.dicoding.rifqi.storyapp.data.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel(){
    private val _stories = MutableLiveData<Resource<ArrayList<ListStory>>>()
    val stories: LiveData<Resource<ArrayList<ListStory>>> = _stories

    suspend fun getStories(token: String) {
        _stories.postValue(Resource.Loading())
        val client =
            ApiConfig.getApiService().getAllStories(token =  "Bearer ${pref.getToken().first()}")

        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {

                if (response.isSuccessful) {
                    response.body()?.let {
                        val listStory = it.listStory
                        _stories.postValue(Resource.Success(ArrayList(listStory)))
                    }
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        StoryResponse::class.java
                    )
                    _stories.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(
                    MainViewModel::class.java.simpleName,
                    "onFailure getStories"
                )
                _stories.postValue(Resource.Error(t.message))
            }
        })
    }

    fun clearDataSession() {
        viewModelScope.launch {
            pref.clearDataSetting()
        }
    }
}
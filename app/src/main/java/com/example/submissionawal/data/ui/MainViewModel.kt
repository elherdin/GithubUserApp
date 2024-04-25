package com.example.submissionawal.data.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.submissionawal.data.local.SettingPreferences
import com.example.submissionawal.data.response.GithubResponse
import com.example.submissionawal.data.response.ItemsItem
import com.example.submissionawal.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {

    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchDataFromApi()
    }

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    private fun fetchDataFromApi() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(query = "arif")
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                Log.e(TAG, "onFailure ${t.message}")
            }
        })
    }

    fun searchUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

    class Factory(private val preferences: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(preferences) as T
    }

}

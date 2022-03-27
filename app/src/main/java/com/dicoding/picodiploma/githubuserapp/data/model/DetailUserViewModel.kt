package com.dicoding.picodiploma.githubuserapp.data.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.picodiploma.githubuserapp.api.RetrofitClient
import com.dicoding.picodiploma.githubuserapp.data.local.FavoriteUser
import com.dicoding.picodiploma.githubuserapp.data.local.FavoriteUserDao
import com.dicoding.picodiploma.githubuserapp.data.local.UserDatabase
import com.dicoding.picodiploma.githubuserapp.response.DetailUserRespon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application): AndroidViewModel(application) {
    val user = MutableLiveData<DetailUserRespon>()

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String){
        RetrofitClient.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<DetailUserRespon>{
                override fun onResponse(
                    call: Call<DetailUserRespon>,
                    response: Response<DetailUserRespon>
                ) {
                    if (response.isSuccessful){
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserRespon>, t: Throwable) {
                    Log.d("Failure", t.message!!)
                }

            })
    }

    fun getUserDetail(): LiveData<DetailUserRespon>{
        return user
    }

    fun addToFavorite(username: String, id: Int, avatar_url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                username, id, avatar_url
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}
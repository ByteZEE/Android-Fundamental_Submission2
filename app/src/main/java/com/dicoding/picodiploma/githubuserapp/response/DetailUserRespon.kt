package com.dicoding.picodiploma.githubuserapp.response

data class DetailUserRespon(
    val login : String = "",
    val id : Int = 0,
    val avatar_url : String = "",
    val followers_url : String = "",
    val following_url : String = "",
    val name : String = "",
    val company : String? = "",
    val location : String? = "",
    val public_repos : Int = 0,
    val followers : Int = 0,
    val following : Int = 0
)

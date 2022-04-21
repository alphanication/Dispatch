package com.example.dispatch.domain.models

data class UserDetails(
    var uid: String = "",
    var fullname: String = "",
    var dateBirth: String = "",
    var email: String = "",
    var password: String = "",
    var photoProfileUrl: String = ""
)
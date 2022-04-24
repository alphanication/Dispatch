package com.example.dispatch.domain.models

data class UserDetailsPublic(
    var uid: String = "",
    var fullname: String = "",
    var dateBirth: String = "",
    var email: String = "",
    var photoProfileUrl: String = ""
)
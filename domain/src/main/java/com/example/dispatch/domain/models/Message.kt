package com.example.dispatch.domain.models

data class Message(
    val message: String = "",
    val timestamp: Long = 0,
    val fromUserUid: String = "",
    val toUserUid: String = ""
)
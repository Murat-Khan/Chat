package com.murat.chat.model

data class Message (
    var senderId : String,
    var receiverId : String,
    var message : String,
    )
package com.example.fairbaseregistration

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInfo(
    val name: String = "",
    val age: String? = ""
)
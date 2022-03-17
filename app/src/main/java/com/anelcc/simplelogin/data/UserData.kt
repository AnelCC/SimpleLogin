package com.anelcc.simplelogin.data

data class UserData (
    var userId: String? = null,
    var name: String? = null,
    var username: String? = null,
    var email: String? = null,
    var website: String? = null) {
    fun toMap() = mapOf(
        "userID" to userId,
        "name" to name,
        "username" to username,
        "website" to website
    )
}
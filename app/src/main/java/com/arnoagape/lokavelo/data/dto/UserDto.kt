package com.arnoagape.lokavelo.data.dto

import java.io.Serializable

data class UserDto(
    val id: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val phoneNumber: String? = null,
    val bio: String? = null,
    val address: String? = null,
    val fcmToken: String? = null
) : Serializable
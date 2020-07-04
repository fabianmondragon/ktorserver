package com.idetidev.usercase.model

import java.io.Serializable
import io.ktor.auth.Principal

data class User (
    val userId: Int,
    val username: String,
    val email: String,
    val displayName: String,
    val passwordHash: String
): Serializable, Principal



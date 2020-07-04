package com.idetidev.repository

import com.idetidev.usercase.model.User

interface Repository {
    suspend fun addUser(email: String,
                        displayName: String,
                        passwordHash: String, userName: String): User?
    suspend fun findUser(userId: Int): User?
    suspend fun findUserByEmail(email: String): User?
    suspend fun deleteUser(userId: Int)
}
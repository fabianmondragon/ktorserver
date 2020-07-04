package com.idetidev.repository

import com.idetidev.data.model.DatabaseFactory.dbQuery
import com.idetidev.data.model.UserDB
import com.idetidev.usercase.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement

class RepositoryImpl : Repository {
    override suspend fun addUser (email: String,
                                  displayName: String,
                                  passwordHash: String, userName: String) : User? {
        var statement : InsertStatement<Number>? = null // 1
        dbQuery { // 2
            // 3
            statement = UserDB.insert { user ->
                user[UserDB.email] = email
                user[UserDB.displayName] = displayName
                user[UserDB.passwordHash] = passwordHash
                user[UserDB.userName] = userName
            }
        }
        // 4
        return rowToUser(statement?.resultedValues?.get(0))
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }
        return User(
            userId = row[UserDB.userId],
            email = row[UserDB.email],
            displayName = row[UserDB.displayName],
            passwordHash = row[UserDB.passwordHash],
            username = row[UserDB.userName]
        )
    }


    override suspend fun findUser(userId: Int)= dbQuery{
        UserDB.select { UserDB.userId.eq(userId) }
            .map { rowToUser(it) }.singleOrNull()
    }

    override suspend fun findUserByEmail(email: String) = dbQuery {
        UserDB.select { UserDB.email.eq(email) }
            .map { rowToUser(it) }.singleOrNull()
    }

    override suspend fun deleteUser(userId: Int) {

    }
}
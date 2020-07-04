package com.idetidev.data.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserDB: Table() {
    val userId : Column<Int> = integer("id").autoIncrement().primaryKey()
    val email = varchar("email", 128).uniqueIndex()
    val userName = varchar("user_name", 64).uniqueIndex()
    val displayName = varchar("display_name", 256)
    val passwordHash = varchar("password_hash", 64)

}
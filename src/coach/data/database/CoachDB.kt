package com.idetidev.coach.data.database

import com.idetidev.data.model.UserDB

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object CoachDB : Table("coach") {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val fk_user: Column<Int> = integer("user_fk") references (UserDB.userId)
}
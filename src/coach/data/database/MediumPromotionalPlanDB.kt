package com.idetidev.coach.data.database

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object MediumPromotionalPlanDB : Table("medium_promotional_plan") {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val description: Column<String> = varchar("description", 500)
    val validity_month: Column<Int> = integer("validity_month")
    val validity_year: Column<Int> = integer("validity_year")
    val state: Column<Boolean> = bool("state")
}
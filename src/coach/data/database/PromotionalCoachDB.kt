package com.idetidev.coach.data.database

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


object PromotionalCoachDB : Table("promotional_coach") {
    val id: Column<Int> = integer("id").autoIncrement().primaryKey()
    val basic_promotional_fk = integer("basic_promotional_fk") references (BasicPromotionalPlanDB.id)
    val medium_promotional_fk = integer("medium_promotional_fk") references (MediumPromotionalPlanDB.id)
    val coach_fk = integer("coach_fk") references (CoachDB.id)
}

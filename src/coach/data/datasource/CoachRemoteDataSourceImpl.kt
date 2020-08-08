package com.idetidev.coach.data.datasource

import com.idetidev.coach.data.database.CoachDB
import com.idetidev.coach.data.database.PromotionalCoachDB
import com.idetidev.coach.data.models.response.PromotinoalCoachResult
import com.idetidev.coach.data.models.response.PromotionalCoach
import com.idetidev.data.model.DatabaseFactory
import com.idetidev.data.model.UserDB
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class CoachRemoteDataSourceImpl : CoachRemoteDataSource {

    override suspend fun getPromotionalCoachList(tecnology: String, language: String): MutableCollection<PromotinoalCoachResult> {
        var listPromotionalCoachResult: MutableCollection<PromotinoalCoachResult> = ArrayList()
        DatabaseFactory.dbQuery {
            val promotional_coach_result = PromotionalCoachDB.innerJoin(CoachDB, { CoachDB.id }, { PromotionalCoachDB.coach_fk }).innerJoin(UserDB, { CoachDB.fk_user }, { UserDB.userId })
                    .slice(PromotionalCoachDB.id, CoachDB.id, CoachDB.id).selectAll().map {
                        listPromotionalCoachResult.add(PromotinoalCoachResult(idCoach = it[CoachDB.id], idUser = it[CoachDB.id], userName = "fabi"))
                    }
        }
        return listPromotionalCoachResult
    }
}
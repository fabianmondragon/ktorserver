package com.idetidev.coach.data.repository

import com.idetidev.coach.data.datasource.CoachRemoteDataSource
import com.idetidev.coach.data.models.response.PromotinoalCoachResult
import com.idetidev.coach.data.models.response.PromotionalCoach
import com.idetidev.coach.repository.data.CoachRepository

class CoachRepositoryImpl (val datasource:CoachRemoteDataSource) : CoachRepository {

    override suspend fun getPromotionalCoachList(tecnology: String, language: String): MutableCollection<PromotinoalCoachResult>? {
        return datasource.getPromotionalCoachList(tecnology, language)
    }
}
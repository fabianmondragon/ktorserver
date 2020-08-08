package com.idetidev.coach.data.datasource

import com.idetidev.coach.data.models.response.PromotinoalCoachResult
import com.idetidev.coach.data.models.response.PromotionalCoach

interface CoachRemoteDataSource {
    suspend fun getPromotionalCoachList(tecnology: String,
                                        language: String): MutableCollection<PromotinoalCoachResult>?
}
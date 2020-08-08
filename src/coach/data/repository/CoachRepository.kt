package com.idetidev.coach.repository.data

import com.idetidev.coach.data.models.response.PromotinoalCoachResult
import com.idetidev.coach.data.models.response.PromotionalCoach

interface CoachRepository {
    suspend fun getPromotionalCoachList (tecnology: String,
    language:String): MutableCollection<PromotinoalCoachResult>?
}
package com.idetidev.routes

import com.idetidev.API_VERSION
import com.idetidev.auth.JwtService
import com.idetidev.auth.MySession
import com.idetidev.coach.data.models.response.PromotinoalCoachResult
import com.idetidev.coach.repository.data.CoachRepository
import com.idetidev.repository.Repository
import com.idetidev.usercase.model.response.BaseResponse
import com.idetidev.usercase.model.response.CustomError
import com.idetidev.usercase.model.response.UserResponse
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set

const val PROMOTIONAL_COACH = "$API_VERSION/getPromotionalCoach"

@KtorExperimentalLocationsAPI
@Location(PROMOTIONAL_COACH)
class PromotionalCoach

@KtorExperimentalLocationsAPI
fun Route.coach(db: CoachRepository, jwtService: JwtService, hashFunction: (String) -> String) {
    get<PromotionalCoach> {
        val parameters = call.receive<Parameters>()
        val tecnology = parameters["tecnology"]
        val language = parameters["language"]
        try {
            val promotionalCoachList = db.getPromotionalCoachList(tecnology = tecnology!!, language = language!!)
            val baseResponse = BaseResponse(promotionalCoachList, HttpStatusCode.OK, 200)
            call.respond(baseResponse)
        } catch (e: Throwable) {
            application.log.error("Failed - get PromotionalCoah ${e.message}")
            val errorMessage = CustomError("Failed - get PromotionalCoah: ", e.message!!)
            val baseResponse: BaseResponse<CustomError> = BaseResponse(errorMessage, HttpStatusCode.InternalServerError, 500)
            call.respond(baseResponse)
        }
    }
}


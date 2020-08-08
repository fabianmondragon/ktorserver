package com.idetidev

import com.idetidev.auth.JwtService
import com.idetidev.auth.MySession
import com.idetidev.auth.hash
import com.idetidev.coach.data.datasource.CoachRemoteDataSourceImpl
import com.idetidev.coach.data.repository.CoachRepositoryImpl
import com.idetidev.coach.repository.data.CoachRepository
import com.idetidev.data.model.DatabaseFactory
import com.idetidev.repository.RepositoryImpl
import com.idetidev.routes.coach
import com.idetidev.routes.users
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.jwt
import io.ktor.gson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations) {
    }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    DatabaseFactory.init()
    val db = RepositoryImpl()
    val coachRepository = CoachRepositoryImpl(CoachRemoteDataSourceImpl())
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    install(Authentication) {
        jwt {
            verifier(jwtService.verifier)
            realm = "Todo Server"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimString = claim.asInt()
                val user = db.findUser(claimString)
                user
            }
        }
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {
        users(db, jwtService, hashFunction)
        authenticate {
            coach(coachRepository, jwtService, hashFunction)
        }

    }
}

const val API_VERSION = "/v1"


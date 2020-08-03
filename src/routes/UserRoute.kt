package com.idetidev.routes


import com.idetidev.API_VERSION
import com.idetidev.auth.JwtService
import com.idetidev.auth.MySession
import com.idetidev.repository.Repository
import com.idetidev.usercase.model.response.BaseResponse
import com.idetidev.usercase.model.response.CustomError
import com.idetidev.usercase.model.response.JWTResponse
import com.idetidev.usercase.model.response.UserResponse

import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.delete
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.sessions
import io.ktor.sessions.set

const val USERS = "$API_VERSION/users"
const val USER_LOGIN = "$USERS/login"
const val USER_LOGOUT = "$USERS/logout"
const val USER_CREATE = "$USERS/create"
const val USER_DELETE = "$USERS/delete"

@KtorExperimentalLocationsAPI
@Location(USER_LOGIN)
class UserLoginRoute

@KtorExperimentalLocationsAPI
@Location(USER_LOGOUT)
class UserLogoutRoute

@KtorExperimentalLocationsAPI
@Location(USER_CREATE)
class UserCreateRoute

@KtorExperimentalLocationsAPI
@Location(USER_DELETE)
class UserDeleteRoute

@KtorExperimentalLocationsAPI
fun Route.users(db: Repository, jwtService: JwtService, hashFunction: (String) -> String) {
    post<UserLoginRoute> {
        val signinParameters = call.receive<Parameters>()
        val userName = signinParameters["userName"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val password = signinParameters["password"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val hash = hashFunction(password)
        try {
            val currentUser = db.findUser(userName, hash)
            currentUser?.let {
                call.sessions.set(MySession(it.userId))
                val token = jwtService.generateToken(it)

                val userResponse = UserResponse (displayName = it.displayName, userName = it.username, token = token)
                val baseResponse: BaseResponse <UserResponse> = BaseResponse(userResponse, HttpStatusCode.OK, 200)
                db.saveToken (token, it.username)
                call.respond(baseResponse)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to loggin user", e)
            val errorMessage = CustomError ("Problem with loggin", e.message!! )
            val baseResponse: BaseResponse <CustomError> = BaseResponse(errorMessage, HttpStatusCode.BadRequest, 400)
            call.respond(baseResponse)
        }
    }
    post<UserLogoutRoute> {
        val signinParameters = call.receive<Parameters>()
        val email = signinParameters["email"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")

        try {
            val currentUser = db.findUserByEmail(email)
            currentUser?.userId?.let {
                call.sessions.clear(call.sessions.findName(MySession::class))
                call.respond(HttpStatusCode.OK)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
        }
    }
    delete<UserDeleteRoute> {
        val signinParameters = call.receive<Parameters>()
        val email = signinParameters["email"] ?: return@delete call.respond(HttpStatusCode.Unauthorized, "Missing Fields")

        try {
            val currentUser = db.findUserByEmail(email)
            currentUser?.userId?.let {
                db.deleteUser(it)
                call.sessions.clear(call.sessions.findName(MySession::class))
                call.respond(HttpStatusCode.OK)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            call.respond(HttpStatusCode.BadRequest, "Problems retrieving User")
        }
    }
    post<UserCreateRoute> {
        val signupParameters = call.receive<Parameters>()
        val password = signupParameters["password"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val displayName = signupParameters["displayName"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val email = signupParameters["email"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val userName = signupParameters["userName"] ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing Fields")
        val hash = hashFunction(password)

        try {
            val newUser = db.addUser(email, displayName, hash, userName)
            newUser?.userId?.let {
                call.sessions.set(MySession(it))
                val baseResponse  = BaseResponse( null,HttpStatusCode.Created, 200)
                call.respond(baseResponse)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to register user", e)
            val errorMessage = CustomError ("Problems creating User", e.message!! )
            val baseResponse: BaseResponse <CustomError> = BaseResponse(errorMessage, HttpStatusCode.BadRequest, 400)
            call.respond(baseResponse)
        }
    }
}


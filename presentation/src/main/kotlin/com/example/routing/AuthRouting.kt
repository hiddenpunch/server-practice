package com.example.routing

import com.example.routing.dto.mapper.AuthMapper
import com.example.routing.dto.request.SignInRequest
import com.example.routing.dto.request.SignUpRequest
import com.example.service.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.mapstruct.factory.Mappers

object AuthRouting {
    private val mapper = Mappers.getMapper(AuthMapper::class.java)
    fun Route.authRouting(authService: AuthService) {
        post("/signup") {
            val request = call.receive<SignUpRequest>()
            authService.signUp(mapper.toSignUpCommand(request)).fold(
                {
                    when (it) {
                        is AuthService.SignUpFailure.DuplicatedEmail -> call.respond(HttpStatusCode.Conflict, "${it.email} already exists")
                        is AuthService.SignUpFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                    }
                },
                {
                    call.respond(HttpStatusCode.Created)
                }
            )
        }
        post("/signin") {
            val request = call.receive<SignInRequest>()
            authService.signIn(mapper.toSignInCommand(request)).fold(
                {
                    when (it) {
                        is AuthService.SignInFailure.WrongCredential -> call.respond(HttpStatusCode.Unauthorized, it.message)
                        is AuthService.SignInFailure.InternalError -> call.respond(HttpStatusCode.InternalServerError, it.message)
                    }
                },
                {
                    call.respond(HttpStatusCode.OK, mapper.toSignInResponse(it))
                }
            )
        }
    }
}
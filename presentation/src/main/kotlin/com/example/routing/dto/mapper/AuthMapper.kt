package com.example.routing.dto.mapper

import com.example.routing.dto.request.SignInRequest
import com.example.routing.dto.request.SignUpRequest
import com.example.routing.dto.response.SignInResponse
import com.example.service.AuthService
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface AuthMapper {
    fun toSignUpCommand(
        request: SignUpRequest
    ): AuthService.SignUpCommand

    fun toSignInCommand(
        request: SignInRequest
    ): AuthService.SignInCommand

    fun toSignInResponse(
        result: AuthService.SignInResult
    ): SignInResponse
}
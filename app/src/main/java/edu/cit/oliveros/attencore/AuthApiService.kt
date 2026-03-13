package edu.cit.oliveros.attencore

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class SocialAuthRequest(
    val token: String,
    val provider: String,
    val email: String? = null
)

data class AuthResponse(
    val jwtToken: String,
    val refreshToken: String,
    val expiresAt: Long,
    val user: UserProfile
)

data class UserProfile(
    val id: String,
    val email: String,
    val name: String?
)

interface AuthApiService {
    @POST("auth/verify-social-token")
    suspend fun verifySocialToken(@Body request: SocialAuthRequest): Response<AuthResponse>

    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body refreshToken: String): Response<AuthResponse>
}

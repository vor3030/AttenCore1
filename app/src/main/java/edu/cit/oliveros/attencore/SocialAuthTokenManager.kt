package edu.cit.oliveros.attencore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Manages secure storage of JWT tokens and user session data using EncryptedSharedPreferences.
 */
class SocialAuthTokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_JWT_TOKEN = "jwt_token"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_AUTH_PROVIDER = "auth_provider"
        private const val KEY_TOKEN_EXPIRY = "token_expiry"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_FULL_NAME = "full_name"
    }

    /**
     * Saves the authentication session data.
     */
    fun saveSession(token: String, email: String, provider: String, expiryTimestamp: Long,
                    userType: String = "STUDENT", fullName: String = "") {
        sharedPreferences.edit().apply {
            putString(KEY_JWT_TOKEN, token)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_AUTH_PROVIDER, provider)
            putLong(KEY_TOKEN_EXPIRY, expiryTimestamp)
            putString(KEY_USER_TYPE, userType)
            putString(KEY_FULL_NAME, fullName)
            apply()
        }
    }

    /**
     * Retrieves the stored JWT token.
     */
    fun getToken(): String? = sharedPreferences.getString(KEY_JWT_TOKEN, null)

    /**
     * Retrieves the stored user email.
     */
    fun getEmail(): String? = sharedPreferences.getString(KEY_USER_EMAIL, null)

    /**
     * Retrieves the authentication provider (e.g., "google", "facebook").
     */
    fun getProvider(): String? = sharedPreferences.getString(KEY_AUTH_PROVIDER, null)

    /**
     * Retrieves the user type ("STUDENT" or "FACULTY").
     */
    fun getUserType(): String = sharedPreferences.getString(KEY_USER_TYPE, "STUDENT") ?: "STUDENT"

    /**
     * Retrieves the stored full name.
     */
    fun getFullName(): String = sharedPreferences.getString(KEY_FULL_NAME, "") ?: ""

    /**
     * Checks if the current session is valid (token exists and not expired).
     */
    fun isSessionValid(): Boolean {
        val token = getToken()
        val expiry = sharedPreferences.getLong(KEY_TOKEN_EXPIRY, 0)
        return token != null && System.currentTimeMillis() < expiry
    }

    /**
     * Clears all session data (Logout).
     */
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
}
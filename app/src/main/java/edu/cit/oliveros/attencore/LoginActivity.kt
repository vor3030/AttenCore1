package edu.cit.oliveros.attencore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var tokenManager: SocialAuthTokenManager
    private lateinit var callbackManager: CallbackManager
    
    // Microsoft MSAL
    private var mAccount: ISingleAccountPublicClientApplication? = null
    
    // Apple (Firebase)
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tokenManager = SocialAuthTokenManager(this)
        firebaseAuth = FirebaseAuth.getInstance()

        setupFacebookLogin()
        setupMicrosoftMSAL()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<AppCompatButton>(R.id.loginButton)
        val registerLink = findViewById<TextView>(R.id.registerLink)
        
        val googleButton = findViewById<ImageButton>(R.id.googleButton)
        val facebookButton = findViewById<ImageButton>(R.id.facebookButton)
        val microsoftButton = findViewById<ImageButton>(R.id.microsoftButton)
        val appleButton = findViewById<ImageButton>(R.id.appleButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            handleSuccessfulLogin(email, "custom", "mock_jwt_token")
        }

        googleButton.setOnClickListener {
            signInWithGoogle()
        }

        facebookButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        }

        microsoftButton.setOnClickListener {
            signInWithMicrosoft()
        }

        appleButton.setOnClickListener {
            signInWithApple()
        }

        registerLink.setOnClickListener {
            // Navigate to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInWithGoogle() {
        val credentialManager = CredentialManager.create(this)

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.google_web_client_id))
            .setAutoSelectEnabled(true)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )
                handleGoogleSignInResult(result)
            } catch (e: GetCredentialException) {
                Log.e("LoginActivity", "Google sign in failed", e)
                Toast.makeText(this@LoginActivity, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleGoogleSignInResult(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                handleSuccessfulLogin(googleIdTokenCredential.id, "google", googleIdTokenCredential.idToken)
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("LoginActivity", "Received an invalid google id token response", e)
            }
        }
    }

    private fun setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleSuccessfulLogin("fb_user", "facebook", result.accessToken.token)
            }
            override fun onCancel() {}
            override fun onError(error: FacebookException) {}
        })
    }

    private fun setupMicrosoftMSAL() {
        PublicClientApplication.createSingleAccountPublicClientApplication(
            this,
            R.raw.auth_config_single_account,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    mAccount = application
                }
                override fun onError(exception: MsalException) {
                    Log.e("MSAL", "Error creating MSAL application", exception)
                }
            })
    }

    private fun signInWithMicrosoft() {
        mAccount?.signIn(this, null, arrayOf("user.read"), object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) {
                val token = authenticationResult.accessToken
                val email = authenticationResult.account.username
                handleSuccessfulLogin(email, "microsoft", token)
            }
            override fun onError(exception: MsalException) {
                Log.e("MSAL", "Authentication failed", exception)
            }
            override fun onCancel() {}
        })
    }

    private fun signInWithApple() {
        val provider = OAuthProvider.newBuilder("apple.com")
        provider.scopes = listOf("email", "name")
        
        firebaseAuth.startActivityForSignInWithProvider(this, provider.build())
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                handleSuccessfulLogin(user?.email ?: "", "apple", "firebase_token")
            }
            .addOnFailureListener { e ->
                Log.e("AppleAuth", "Apple sign in failed", e)
                Toast.makeText(this, "Apple Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSuccessfulLogin(email: String, provider: String, token: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider)
            putExtra("token", token)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}

package edu.cit.oliveros.attencore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var tokenManager: SocialAuthTokenManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
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

        setupGoogleSignIn()
        setupFacebookLogin()
        setupMicrosoftMSAL()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<AppCompatButton>(R.id.loginButton)
        
        val googleButton = findViewById<ImageButton>(R.id.googleButton)
        val facebookButton = findViewById<ImageButton>(R.id.facebookButton)
        val microsoftButton = findViewById<ImageButton>(R.id.microsoftButton)
        val appleButton = findViewById<ImageButton>(R.id.appleButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            handleSuccessfulLogin(email, "custom", "mock_jwt_token")
        }

        googleButton.setOnClickListener {
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
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
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.google_web_client_id))
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                handleGoogleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))
            }
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            handleSuccessfulLogin(account?.email ?: "", "google", account?.idToken ?: "")
        } catch (e: ApiException) {
            Log.e("LoginActivity", "Google sign in failed", e)
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

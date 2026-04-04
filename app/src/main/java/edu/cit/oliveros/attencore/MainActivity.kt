package edu.cit.oliveros.attencore

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    private lateinit var tokenManager: SocialAuthTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tokenManager = SocialAuthTokenManager(this)

        // Check if we just arrived from a successful login
        val email = intent.getStringExtra("email")
        val provider = intent.getStringExtra("provider")
        val token = intent.getStringExtra("token")

        if (token != null && email != null && provider != null) {
            // Save the new session (mocking expiry to 1 hour from now)
            val expiry = System.currentTimeMillis() + (60 * 60 * 1000)
            tokenManager.saveSession(token, email, provider, expiry)
        }

        // If session is valid, we could redirect to a Home screen or update UI
        if (tokenManager.isSessionValid()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Find the app icon and apply bounce animation
        val appIcon = findViewById<ImageView>(R.id.appIcon)
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation)
        appIcon.startAnimation(bounceAnimation)

        // Find the Get Started button
        val getStartedButton = findViewById<AppCompatButton>(R.id.getStartedButton)

        // Set click listener for Get Started button
        getStartedButton.setOnClickListener {
            // Navigate to Login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
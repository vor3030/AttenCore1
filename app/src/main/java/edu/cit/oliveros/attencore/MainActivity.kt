package edu.cit.oliveros.attencore

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout to your home screen XML
        setContentView(R.layout.activity_main)

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







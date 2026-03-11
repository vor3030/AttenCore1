package edu.cit.oliveros.attencore

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout to your login screen XML
        setContentView(R.layout.activity_login)

        // Find the input fields and button
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<AppCompatButton>(R.id.loginButton)

        // Set click listener for Login button
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Simple validation
            if (email.isEmpty()) {
                emailInput.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordInput.error = "Password is required"
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordInput.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            // Show success message
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

            // TODO: Add actual authentication here
        }
    }
}
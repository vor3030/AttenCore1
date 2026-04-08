package edu.cit.oliveros.attencore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class RegisterActivity : AppCompatActivity() {

    private lateinit var tokenManager: SocialAuthTokenManager
    private var selectedUserType = "STUDENT" // Default selection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tokenManager = SocialAuthTokenManager(this)

        val fullNameInput = findViewById<EditText>(R.id.fullNameInput)
        val studentIdInput = findViewById<EditText>(R.id.studentIdInput)
        val emailInput = findViewById<EditText>(R.id.registerEmailInput)
        val passwordInput = findViewById<EditText>(R.id.registerPasswordInput)
        val registerButton = findViewById<AppCompatButton>(R.id.registerButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        val studentButton = findViewById<AppCompatButton>(R.id.studentButton)
        val facultyButton = findViewById<AppCompatButton>(R.id.facultyButton)

        // Set initial state - Student selected
        studentButton.isSelected = true
        studentButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light))

        // Student button click
        studentButton.setOnClickListener {
            selectedUserType = "STUDENT"
            studentIdInput.hint = "Student ID"
            studentButton.isSelected = true
            facultyButton.isSelected = false
            studentButton.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_light_teal))
            facultyButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        }

        // Faculty button click
        facultyButton.setOnClickListener {
            selectedUserType = "FACULTY"
            studentIdInput.hint = "Faculty ID"
            facultyButton.isSelected = true
            studentButton.isSelected = false
            facultyButton.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_light_teal))
            studentButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        }

        registerButton.setOnClickListener {
            val fullName = fullNameInput.text.toString().trim()
            val studentId = studentIdInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validation
            if (!validateInputs(fullName, studentId, email, password)) {
                return@setOnClickListener
            }

            // Call registration endpoint
            registerUser(fullName, studentId, email, password, selectedUserType)
        }

        loginLink.setOnClickListener {
            // Navigate back to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateInputs(fullName: String, studentId: String, email: String, password: String): Boolean {
        // Check if fields are empty
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Full name is required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (studentId.isEmpty()) {
            Toast.makeText(this, "Student/Faculty ID is required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isEmpty()) {
            Toast.makeText(this, getString(R.string.email_required), Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, getString(R.string.password_required), Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show()
            return false
        }

        // Check password length
        if (password.length < 6) {
            Toast.makeText(this, getString(R.string.error_short_password), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser(fullName: String, studentId: String, email: String, password: String, userType: String) {
        Log.d("RegisterActivity", "Registering user: email=$email, fullName=$fullName, userType=$userType, pwdLen=${password.length}")

        // Save session (mock registration)
        tokenManager.saveSession(
            token = "mock_jwt_token",
            email = email,
            provider = "email",
            expiryTimestamp = System.currentTimeMillis() + 3600000
        )

        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()

        // Route directly to your unified HomeActivity
        val intent = Intent(this, HomeActivity::class.java).apply {
            // Pass the necessary data to HomeActivity
            putExtra("fullName", fullName)
            putExtra("userType", userType) // HomeActivity checks this to set the correct layout

            // Passing these just in case you need them in HomeActivity later
            putExtra("email", email)
            putExtra("studentId", studentId)

            // Clear the back stack so pressing 'Back' doesn't go to the register screen
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
        finish()
    }
}

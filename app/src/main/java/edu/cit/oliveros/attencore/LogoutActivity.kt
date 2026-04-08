package edu.cit.oliveros.attencore

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class LogoutActivity : AppCompatActivity() {

    private lateinit var tokenManager: SocialAuthTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        tokenManager = SocialAuthTokenManager(this)

        // Find UI Elements
        val tvLogoutName = findViewById<TextView>(R.id.tvLogoutName)
        val tvLogoutId = findViewById<TextView>(R.id.tvLogoutId)
        val tvLogoutTime = findViewById<TextView>(R.id.tvLogoutTime)
        val tvLogoutDuration = findViewById<TextView>(R.id.tvLogoutDuration)

        val cbClearCache = findViewById<CheckBox>(R.id.cbClearCache)
        val btnConfirmLogout = findViewById<AppCompatButton>(R.id.btnConfirmLogout)
        val btnCancelLogout = findViewById<AppCompatButton>(R.id.btnCancelLogout)

        // Receive data from Intent
        val passedName = intent.getStringExtra("fullName") ?: tokenManager.getEmail() ?: "User"
        val passedId = intent.getStringExtra("studentId") ?: "N/A"

        // Mocking the time for now (You can track real login time later in SharedPreferences)
        val mockLoginTime = "8:00 AM"
        val mockDuration = "2 hours 45 minutes"

        // Set Data to UI
        tvLogoutName.text = passedName
        tvLogoutId.text = passedId
        tvLogoutTime.text = mockLoginTime
        tvLogoutDuration.text = mockDuration

        // --- BUTTON CLICKS ---

        btnCancelLogout.setOnClickListener {
            // User changed their mind, just close this screen and return to Dashboard
            finish()
        }

        btnConfirmLogout.setOnClickListener {
            // Perform the actual logout
            tokenManager.clearSession()

            if (cbClearCache.isChecked) {
                // Clear app cache logic would go here
                Toast.makeText(this, "Cache cleared.", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show()

            // Route back to Login and clear the back stack
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}
package edu.cit.oliveros.attencore

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class ProfileActivity : AppCompatActivity() {

    private lateinit var tokenManager: SocialAuthTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tokenManager = SocialAuthTokenManager(this)

        // Find Input Fields
        val etProfileName = findViewById<EditText>(R.id.etProfileName)
        val etProfileId = findViewById<EditText>(R.id.etProfileId)
        val etProfileEmail = findViewById<EditText>(R.id.etProfileEmail)

        // Find Buttons
        val btnChangePhoto = findViewById<AppCompatButton>(R.id.btnChangePhoto)
        val btnSaveChanges = findViewById<AppCompatButton>(R.id.btnSaveChanges)
        val btnCancel = findViewById<AppCompatButton>(R.id.btnCancel)

        // --- RETRIEVE DATA FROM INTENT ---
        val passedName = intent.getStringExtra("fullName") ?: ""
        val passedEmail = intent.getStringExtra("email") ?: tokenManager.getEmail() ?: ""

        // Pre-fill the form with the known data!
        etProfileName.setText(passedName)
        etProfileEmail.setText(passedEmail)
        // Note: You can also pass studentId from the intent if you have it!

        // --- CLICK LISTENERS ---

        btnChangePhoto.setOnClickListener {
            Toast.makeText(this, "Open Gallery feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        btnSaveChanges.setOnClickListener {
            // Get the new updated text
            val newName = etProfileName.text.toString()

            // TODO: Here is where you would save the new data to Firebase/Database

            Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show()

            // Close the Profile activity and return to the Dashboard
            finish()
        }

        btnCancel.setOnClickListener {
            // Simply close the activity without saving to return to the previous screen
            finish()
        }
    }
}
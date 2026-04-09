package edu.cit.oliveros.attencore

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var tokenManager: SocialAuthTokenManager

    // 1. Declare these at the class level so ALL functions can see them!
    private var currentUserFullName: String = ""
    private var currentUserType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = SocialAuthTokenManager(this)

        // Capture data passed from Login/Register into our class variables
        currentUserType = intent.getStringExtra("userType") ?: "STUDENT"
        currentUserFullName = intent.getStringExtra("fullName") ?: tokenManager.getEmail() ?: "User"

        // 2. Set the Content View FIRST! (Crucial to prevent crashes)
        if (currentUserType == "FACULTY") {
            setContentView(R.layout.activity_home_faculty)
            setupFacultyDashboard()
        } else {
            setContentView(R.layout.activity_home_student)
            setupStudentDashboard()
        }

        // 3. NOW we can safely find the Toolbar because the layout has been loaded
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)

        // ADD THIS LINE HERE to set the hamburger icon:
        toolbar.overflowIcon = androidx.core.content.ContextCompat.getDrawable(this, R.drawable.ic_menu)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "" // Keeps the default title from covering your custom text
    }

    private fun setupStudentDashboard() {
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val dateText = findViewById<TextView>(R.id.dateText)

        // Link the Student Stats IDs
        val tvClassesAttended = findViewById<TextView>(R.id.tvClassesAttended)
        val tvAttendanceRate = findViewById<TextView>(R.id.tvAttendanceRate)

        // Use the class variable here
        welcomeText.text = "Welcome, $currentUserFullName"
        dateText.text = getCurrentDateTime()

        // Set placeholder data for now (Will be replaced by DB data later)
        tvClassesAttended.text = "0"
        tvAttendanceRate.text = "0%"

        val scanQrCard = findViewById<LinearLayout>(R.id.scanQrCard)
        val myAttendanceCard = findViewById<LinearLayout>(R.id.myAttendanceCard)
        val classScheduleCard = findViewById<LinearLayout>(R.id.classScheduleCard)
        val myProfileCard = findViewById<LinearLayout>(R.id.myProfileCard)

        scanQrCard.setOnClickListener {
            // TODO: Navigate to QR Scanner
        }
        myAttendanceCard.setOnClickListener {
            // TODO: Navigate to Attendance Records
        }
        classScheduleCard.setOnClickListener {
            // TODO: Navigate to Class Schedule
        }
        myProfileCard.setOnClickListener {
            // Also navigate to Profile from the card click
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("fullName", currentUserFullName)
            intent.putExtra("userType", currentUserType)
            startActivity(intent)
        }
    }

    private fun setupFacultyDashboard() {
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val dateText = findViewById<TextView>(R.id.dateText)

        // 1. Link the Faculty Overview Stats IDs
        val tvOverviewClasses = findViewById<TextView>(R.id.tvOverviewClasses)
        val tvOverviewStudents = findViewById<TextView>(R.id.tvOverviewStudents)
        val tvOverviewPresent = findViewById<TextView>(R.id.tvOverviewPresent)

        // 2. Link the Class List IDs (For the two static cards we made)
        val tvClassCode1 = findViewById<TextView>(R.id.tvClassCode1)
        val tvClassName1 = findViewById<TextView>(R.id.tvClassName1)
        val btnEnrolled1 = findViewById<AppCompatButton>(R.id.btnEnrolled1)

        val tvClassCode2 = findViewById<TextView>(R.id.tvClassCode2)
        val tvClassName2 = findViewById<TextView>(R.id.tvClassName2)
        val btnEnrolled2 = findViewById<AppCompatButton>(R.id.btnEnrolled2)

        // Use the class variable here
        welcomeText.text = "Welcome, Prof $currentUserFullName"
        dateText.text = getCurrentDateTime()

        // Set Placeholder Data for Stats
        tvOverviewClasses.text = "0"
        tvOverviewStudents.text = "0"
        tvOverviewPresent.text = "0%"

        // Set Placeholder Data for Classes
        tvClassCode1.text = "CSIT284 - G8"
        tvClassName1.text = "Mobile Development"
        btnEnrolled1.text = "0 Students Enrolled"

        tvClassCode2.text = "CSIT226 - G10"
        tvClassName2.text = "Information Management"
        btnEnrolled2.text = "0 Students Enrolled"

        // Setup Click Listeners for Faculty Cards
        val createScheduleCard = findViewById<LinearLayout>(R.id.createScheduleCard)
        val generateQrCard = findViewById<LinearLayout>(R.id.generateQrCard)
        val viewReportsCard = findViewById<LinearLayout>(R.id.viewReportsCard)
        val myProfileCard = findViewById<LinearLayout>(R.id.myProfileCard)

        createScheduleCard.setOnClickListener {
            // TODO: Navigate to Create Schedule
        }
        generateQrCard.setOnClickListener {
            // TODO: Navigate to Generate QR
        }
        viewReportsCard.setOnClickListener {
            // TODO: Navigate to Reports
        }
        myProfileCard.setOnClickListener {
            // Also navigate to Profile from the card click
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("fullName", currentUserFullName)
            intent.putExtra("userType", currentUserType)
            startActivity(intent)
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("EEEE, MMM dd, yyyy • h:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    // This function draws the menu using the dashboard_menu.xml
    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    // This function listens for what you click in the dropdown menu
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                // Navigate to the Profile Screen
                val intent = Intent(this, ProfileActivity::class.java)

                // Pass the safe class variables to Profile
                intent.putExtra("fullName", currentUserFullName)
                intent.putExtra("userType", currentUserType)
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                // Launch the new Logout Confirmation Screen!
                val intent = Intent(this, LogoutActivity::class.java)

                // Pass the safe class variables to Logout
                intent.putExtra("fullName", currentUserFullName)
                // intent.putExtra("studentId", currentStudentId) // Add this later if you track ID
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
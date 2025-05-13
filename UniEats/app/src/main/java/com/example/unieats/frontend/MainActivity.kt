package com.example.unieats.frontend

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.unieats.R
import com.example.unieats.frontend.login.loginFragment
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display if this method is part of your custom utility or library
        enableEdgeToEdge()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Set content view to activity_main layout
        setContentView(R.layout.activity_main)

        // Apply insets to adjust for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Only load the fragment if it's the first time (when savedInstanceState is null)
        if (savedInstanceState == null) {
            loadFragment(loginFragment()) // Ensure the container ID matches your XML layout
        }
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment) // Make sure container ID matches the layout
        transaction.commit()
    }
}

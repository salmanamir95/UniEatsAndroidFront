package com.example.unieats.frontend.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.unieats.R
import com.example.unieats.backend.repository.UserRepository
import com.example.unieats.frontend.register.RegisterFragment

class loginFragment: Fragment() {
    private lateinit var email : String
    private lateinit var pass : String
    private lateinit var logBtn : Button
    private lateinit var regBtn : Button

    private fun validateInputs(email: String, password: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        val passwordPattern = ".{6,}".toRegex()

        return when {
            email.isEmpty() || password.isEmpty() -> {
                Toast.makeText(context, "Complete the Required Fields", Toast.LENGTH_SHORT).show()
                false
            }
            !email.matches(emailPattern) -> {
                Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                false
            }
            !password.matches(passwordPattern) -> {
                Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regBtn = view.findViewById(R.id.regBtn)
        logBtn = view.findViewById(R.id.loginBtn)
        logBtn.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.email).text.toString().trim()
            val password = view.findViewById<EditText>(R.id.pass).text.toString().trim()

            if (validateInputs(email, password)) {
                val repository = UserRepository()
                repository.loginUser(
                    email = email,
                    password = password,
                    onSuccess = { user ->
                        Toast.makeText(context, "Welcome ${user.name}", Toast.LENGTH_LONG).show()
                        // Add navigation logic here
                    },
                    onFailure = { errorMsg ->
                        Toast.makeText(context, "Login failed: $errorMsg", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }

        regBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
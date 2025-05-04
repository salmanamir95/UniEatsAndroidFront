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
import com.example.unieats.frontend.register.RegisterFragment

class loginFragment: Fragment() {
    private lateinit var email : String
    private lateinit var pass : String
    private lateinit var logBtn : Button
    private lateinit var regBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logBtn = view.findViewById(R.id.loginBtn)
        regBtn = view.findViewById(R.id.regBtn)
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val passwordPattern = ".{6,}"

        logBtn.setOnClickListener {
            email = view.findViewById<EditText>(R.id.email).text.toString().trim()
            pass = view.findViewById<EditText>(R.id.pass).text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, "Complete the Required Fields", Toast.LENGTH_SHORT).show()
            } else if (!email.matches(emailPattern.toRegex())) {
                Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else if (!pass.matches(passwordPattern.toRegex())) {
                Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                // Call login
                val repository = com.example.unieats.backend.repository.UserRepository()
                repository.loginUser(email, pass,
                    onSuccess = { user ->
                        Toast.makeText(context, "Welcome ${user.email}", Toast.LENGTH_LONG).show()
                        // TODO: Navigate to home screen or user dashboard here
                    },
                    onFailure = { errorMsg ->
                        Toast.makeText(context, "Login failed: $errorMsg", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }


        regBtn?.let{
            btn -> btn.setOnClickListener(){
            val trans = parentFragmentManager.beginTransaction()
            trans.replace(R.id.fragment_container, RegisterFragment()) // Make sure container ID matches
            trans.commit()
            }
        }
    }
}
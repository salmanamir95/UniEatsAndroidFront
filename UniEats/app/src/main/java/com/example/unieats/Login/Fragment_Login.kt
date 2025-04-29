package com.example.unieats.Login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.unieats.R

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var loginViewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Observe LiveData from ViewModel
//        loginViewModel.userData.observe(viewLifecycleOwner, Observer { user ->
//            // Update the UI with user data
//            usernameTextView.text = user.username
//            emailTextView.text = user.email
//        })

        // Simulate user login
//        loginButton.setOnClickListener {
//            val username = usernameEditText.text.toString()
//            val password = passwordEditText.text.toString()
//            loginViewModel.login(username, password)
//        }
    }
}
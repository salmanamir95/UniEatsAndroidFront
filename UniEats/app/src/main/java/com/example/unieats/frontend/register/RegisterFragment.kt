package com.example.unieats.frontend.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.unieats.R
import com.example.unieats.backend.repository.UserRepository
import com.example.unieats.frontend.login.LoginFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private var selectedDesignation: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = RegisterViewModel(UserRepository()) // Manual instantiation, no Hilt

        val spinner = view.findViewById<Spinner>(R.id.designReg)
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val passwordPattern = ".{6,}"

        val options = listOf("---Select Any---", "Admin", "Student", "Staff")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDesignation = if (position == 0) "" else options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedDesignation = ""
            }
        }

        view.findViewById<Button>(R.id.logreg).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

        view.findViewById<Button>(R.id.regBtnreg).setOnClickListener {
            val email = view.findViewById<EditText>(R.id.emailReg).text.toString().trim()
            val pass = view.findViewById<EditText>(R.id.passReg).text.toString().trim()
            val name = view.findViewById<EditText>(R.id.nameReg).text.toString().trim()
            val age = view.findViewById<EditText>(R.id.ageReg).text.toString().trim().toIntOrNull()

            when {
                email.isEmpty() || pass.isEmpty() || name.isEmpty() || selectedDesignation.isEmpty() || age == null ->
                    toast("Complete all fields")
                !email.matches(emailPattern.toRegex()) ->
                    toast("Invalid email format")
                !pass.matches(passwordPattern.toRegex()) ->
                    toast("Password must be at least 6 characters")
                age < 17 || age > 79 ->
                    toast("Age must be between 17 and 79")
                else -> viewModel.registerUser(email, pass, name, age, selectedDesignation)
            }
        }

        lifecycleScope.launch {
            viewModel.registerState.collectLatest { state ->
                when (state) {
                    is RegisterState.Loading -> toast("Registering...")
                    is RegisterState.Success -> {
                        toast("Registration successful!")
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, LoginFragment())
                            .commit()
                    }
                    is RegisterState.Error -> toast("Registration failed: ${state.message}")
                    else -> {}
                }
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

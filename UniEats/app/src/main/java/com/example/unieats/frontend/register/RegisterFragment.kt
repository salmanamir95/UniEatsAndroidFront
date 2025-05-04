package com.example.unieats.frontend.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.unieats.R
import com.example.unieats.backend.repository.UserRepository
import com.example.unieats.frontend.login.loginFragment

class RegisterFragment : Fragment() {

    private var selectedDesignation: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(R.id.designReg)
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val passwordPattern = ".{6,}"

        val options = listOf("---Select Any---", "Admin", "Student", "Staff")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

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
                .replace(R.id.fragment_container, loginFragment())
                .commit()
        }

        view.findViewById<Button>(R.id.regBtnreg).setOnClickListener {
            val email = view.findViewById<EditText>(R.id.emailReg).text.toString().trim()
            val pass = view.findViewById<EditText>(R.id.passReg).text.toString().trim()
            val name = view.findViewById<EditText>(R.id.nameReg).text.toString().trim()
            val ageText = view.findViewById<EditText>(R.id.ageReg).text.toString().trim()

            val age = ageText.toIntOrNull()

            when {
                email.isEmpty() || pass.isEmpty() || name.isEmpty() || selectedDesignation.isEmpty() || age == null -> {
                    Toast.makeText(context, "Complete all fields", Toast.LENGTH_SHORT).show()
                }
                !email.matches(emailPattern.toRegex()) -> {
                    Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                }
                !pass.matches(passwordPattern.toRegex()) -> {
                    Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
                age < 17 || age > 79 -> {
                    Toast.makeText(context, "Age must be between 17 and 79", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val user = Register(email, name, pass, age, selectedDesignation)
                    val repository = UserRepository()

                    repository.registerUser(
                        user,
                        onSuccess = {
                            Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, loginFragment())
                                .commit()
                        },
                        onFailure = {
                            Toast.makeText(context, "Registration failed: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }
    }
}

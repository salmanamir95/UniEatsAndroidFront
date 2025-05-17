package com.example.unieats.frontend.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.unieats.R
import com.example.unieats.backend.repository.UserRepository
import com.example.unieats.databinding.FragmentLoginBinding
import com.example.unieats.frontend.dashboard.AppUser
import com.example.unieats.frontend.dashboard.UserNavigator
import com.example.unieats.frontend.register.RegisterFragment
import com.example.unieats.utils.EmailValidator
import com.example.unieats.utils.PasswordValidator

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = LoginViewModel(UserRepository())
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.loginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (validateInputs(email, password)) {
                viewModel.loginUser(email, password)
            }
        }

        binding.regBtn.setOnClickListener {
            navigateToRegistration()
        }
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is LoginState.Loading -> showLoading(true)
                is LoginState.Success -> handleLoginSuccess(state.user)
                is LoginState.Error -> handleLoginError(state.message)
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return when {
            !EmailValidator.isValid(email) -> {
                showError("Invalid email format")
                false
            }
            !PasswordValidator.isValid(password) -> {
                showError("Password must be at least 6 characters")
                false
            }
            else -> true
        }
    }

    private fun handleLoginSuccess(user: AppUser) {
        showLoading(false)
        Toast.makeText(requireContext(), "Welcome ${user.name}", Toast.LENGTH_LONG).show()
        UserNavigator.navigateToDashboard(user, parentFragmentManager)
    }

    private fun handleLoginError(errorMessage: String) {
        showLoading(false)
        showError(errorMessage)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loginBtn.isEnabled = !isLoading
        binding.loginBtn.text = if (isLoading) "..." else "Welcome"
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun navigateToRegistration() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RegisterFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

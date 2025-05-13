package com.example.unieats.frontend.dashboard.admin.UserManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unieats.databinding.FragmentMenuManagementBinding
import com.example.unieats.databinding.FragmentUserManagementBinding

class UserManagementFragment: Fragment() {
    private var _binding: FragmentUserManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserManagementBinding.inflate(inflater, container, false)

        binding.userTitle.text = "User Management Screen"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
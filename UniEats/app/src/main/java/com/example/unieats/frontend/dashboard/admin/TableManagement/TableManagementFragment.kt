package com.example.unieats.frontend.dashboard.admin.TableManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unieats.databinding.FragmentMenuManagementBinding

class TableManagementFragment: Fragment() {
    private var _binding: FragmentMenuManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuManagementBinding.inflate(inflater, container, false)

        binding.menuTitle.text = "Table Management Screen"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
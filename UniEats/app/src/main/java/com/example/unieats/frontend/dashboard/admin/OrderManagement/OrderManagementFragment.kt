package com.example.unieats.frontend.dashboard.admin.OrderManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unieats.databinding.FragmentMenuManagementBinding
import com.example.unieats.databinding.FragmentOrderManagementBinding

class OrderManagementFragment: Fragment() {
    private var _binding: FragmentOrderManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderManagementBinding.inflate(inflater, container, false)

        binding.orderTitle.text = "Order Management Screen"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
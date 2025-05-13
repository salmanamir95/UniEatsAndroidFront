package com.example.unieats.frontend.dashboard.admin.ReviewManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unieats.databinding.FragmentMenuManagementBinding
import com.example.unieats.databinding.FragmentReviewManagementBinding

class ReviewManagementFragment: Fragment() {
    private var _binding: FragmentReviewManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewManagementBinding.inflate(inflater, container, false)

        binding.reviewTitle.text = "Review Management Screen"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
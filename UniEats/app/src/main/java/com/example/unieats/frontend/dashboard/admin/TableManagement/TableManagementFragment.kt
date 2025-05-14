package com.example.unieats.frontend.dashboard.admin.TableManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unieats.databinding.FragmentMenuManagementBinding
import com.example.unieats.databinding.FragmentTableManagementBinding

class TableManagementFragment: Fragment() {
    private var _binding: FragmentTableManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTableManagementBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateSpanCount(): Int {
        val screenWidthDp = resources.configuration.screenWidthDp
        return when {
            screenWidthDp >= 840 -> 4 // tablets and large screens
            screenWidthDp >= 600 -> 3
            else -> 2 // phones
        }
    }

}
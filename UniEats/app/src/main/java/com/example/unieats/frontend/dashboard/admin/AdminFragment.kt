package com.example.unieats.frontend.dashboard.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.unieats.R
import com.example.unieats.databinding.FragmentAdminBinding

class AdminFragment : Fragment() {

    private lateinit var binding: FragmentAdminBinding
    private lateinit var adminViewModel: AdminViewModel
    private lateinit var navigationAdapter: AdminNavigationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        adminViewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        setupViewPager()
        return binding.root
    }

    private fun setupViewPager() {
        navigationAdapter = AdminNavigationAdapter(this)
        binding.viewPager.adapter = navigationAdapter
        binding.viewPager.isUserInputEnabled = false

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_menu -> binding.viewPager.currentItem = 0
                R.id.nav_orders -> binding.viewPager.currentItem = 1
                R.id.nav_reviews -> binding.viewPager.currentItem = 2
                R.id.nav_tables -> binding.viewPager.currentItem = 3
                R.id.nav_users -> binding.viewPager.currentItem = 4
            }
            true
        }
    }
}
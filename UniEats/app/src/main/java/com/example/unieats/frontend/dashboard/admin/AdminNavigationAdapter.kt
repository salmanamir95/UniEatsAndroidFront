package com.example.unieats.frontend.dashboard.admin

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.unieats.frontend.dashboard.admin.MenuManagement.MenuManagementFragment
import com.example.unieats.frontend.dashboard.admin.OrderManagement.OrderManagementFragment
import com.example.unieats.frontend.dashboard.admin.ReviewManagement.ReviewManagementFragment
import com.example.unieats.frontend.dashboard.admin.TableManagement.TableManagementFragment
import com.example.unieats.frontend.dashboard.admin.UserManagement.UserManagementFragment

class AdminNavigationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MenuManagementFragment()
            1 -> OrderManagementFragment()
            2 -> ReviewManagementFragment()
            3 -> TableManagementFragment()
            4 -> UserManagementFragment()
            else -> throw IndexOutOfBoundsException("Invalid fragment index")
        }
    }
}

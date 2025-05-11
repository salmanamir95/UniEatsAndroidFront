package com.example.unieats.frontend.dashboard.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.AppUser
import com.example.unieats.frontend.dashboard.student.HomePageStudent.HomePageStudentFragment
import com.example.unieats.frontend.dashboard.student.ListenerInterfaces.OnNavItemSelectedListener
import com.example.unieats.frontend.dashboard.student.navbar.NavbarFragment
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SharedStudentViewModel
import com.example.unieats.frontend.dashboard.student.SharedViewModels.MenuItemSharedViewModel
import com.example.unieats.frontend.dashboard.student.Menu.MenuViewModel



class StudentFragment: Fragment(), OnNavItemSelectedListener {
    private val menuItemShared: MenuItemSharedViewModel by viewModels()
    private val sharedStudentViewModel: SharedStudentViewModel by viewModels()
    private val menuViewModel: MenuViewModel by viewModels()
    private val currentStudent: SharedStudentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = savedInstanceState?.getSerializable("users") as? AppUser.Student
        currentStudent.user.value= user
        loadChildFragment(NavbarFragment(), R.id.fragment_student_navbar_container)
        loadChildFragment(HomePageStudentFragment(), R.id.fragment_student_content_container)
    }

    private fun loadChildFragment(fragment: Fragment, containerId: Int) {
        childFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onNavItemSelected(fragment: Fragment) {
        loadChildFragment(fragment, R.id.fragment_student_content_container)
    }


}
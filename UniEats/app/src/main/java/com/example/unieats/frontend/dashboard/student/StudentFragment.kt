package com.example.unieats.frontend.dashboard.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.AppUser
import com.example.unieats.frontend.dashboard.student.HomePageStudent.HomePageStudent
import com.example.unieats.frontend.dashboard.student.navbar.Navbar

class StudentFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = savedInstanceState?.getSerializable("users") as? AppUser.Student
        loadChildFragment(Navbar(), R.id.fragment_student_navbar_container)
        loadChildFragment(HomePageStudent(), R.id.fragment_student_content_container)
    }

    private fun loadChildFragment(fragment: Fragment, containerId: Int) {
        childFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }


}
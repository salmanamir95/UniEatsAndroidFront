package com.example.unieats.frontend.dashboard.student.navbar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.student.HomePageStudent.HomePageStudentFragment
import com.example.unieats.frontend.dashboard.student.Menu.MenuFragment
import com.example.unieats.frontend.dashboard.student.Orders.OrdersFragment
import com.example.unieats.frontend.dashboard.student.StudentFragment

class NavbarFragment : Fragment() {
    private var navController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentFragment = parentFragment as? StudentFragment
        navController = studentFragment?.navController

        view.findViewById<ImageView>(R.id.home_button).setOnClickListener {
            navController?.navigate(R.id.homePageStudentFragment)
        }

        view.findViewById<ImageView>(R.id.orders_button).setOnClickListener {
            navController?.navigate(R.id.menuFragment)
        }
    }
}
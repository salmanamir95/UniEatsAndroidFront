package com.example.unieats.frontend.dashboard.student.navbar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.student.HomePageStudent.HomePageStudent
import com.example.unieats.frontend.dashboard.student.OnNavItemSelectedListener

class Navbar: Fragment() {
    private var listener: OnNavItemSelectedListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnNavItemSelectedListener) {
            listener = parentFragment as OnNavItemSelectedListener
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_nav_bar, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageView>(R.id.home_button).setOnClickListener {
            listener?.onNavItemSelected(HomePageStudent())
        }

        view.findViewById<ImageView>(R.id.orders_button).setOnClickListener {
            listener?.onNavItemSelected(OrdersFragment())
        }


        view.findViewById<ImageView>(R.id.profile_button).setOnClickListener {
            listener?.onNavItemSelected(ProfileFragment())
        }
    }

}
package com.example.unieats.frontend.dashboard

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.fragment.app.FragmentManager
import com.example.unieats.R
import com.example.unieats.backend.models.User
import com.example.unieats.frontend.dashboard.admin.AdminFragment
import com.example.unieats.frontend.dashboard.staff.StaffFragment
import com.example.unieats.frontend.dashboard.student.StudentFragment
import com.example.unieats.frontend.register.RegisterFragment
import java.io.Serializable

sealed class AppUser : Serializable {
    abstract val id: String
    abstract val email: String
    abstract val name: String
    abstract val age: Int

    data class Student(
        override val id: String,
        override val name: String,
        override val email: String,
        override val age: Int
    ) : AppUser()

    data class Staff(
        override val id: String,
        override val name: String,
        override val email: String,
        override val age: Int
    ) : AppUser()

    data class Admin(
        override val id: String,
        override val name: String,
        override val email: String,
        override val age: Int
    ) : AppUser()

    companion object {
        fun from(user: User): AppUser = when (user.designation) {
            "Student" -> Student(user.id, user.name, user.email, user.age)
            "Staff"   -> Staff(user.id, user.name, user.email, user.age)
            "Admin"   -> Admin(user.id, user.name, user.email, user.age)
            else -> throw IllegalArgumentException("Unknown designation: ${user.designation}")
        }
    }
}
object UserNavigator {
    fun navigateToDashboard(user: AppUser, fm: FragmentManager) {
        val fragment = when (user) {
            is AppUser.Student -> StudentFragment()
            is AppUser.Staff   -> StaffFragment()
            is AppUser.Admin   -> AdminFragment()
        }

        val bundle = Bundle()
        bundle.putSerializable("user", user)
        fragment.arguments = bundle

        fm.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

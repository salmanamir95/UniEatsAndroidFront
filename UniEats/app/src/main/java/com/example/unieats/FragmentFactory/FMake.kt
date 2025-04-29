package com.example.unieats.FragmentFactory

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

object Fmake {

    /**
     * Dynamically loads a Fragment by class name.
     * @param fragmentManager - your activity's FragmentManager
     * @param containerId - the ID of the FrameLayout where the fragment goes
     * @param fragmentClassName - fully qualified class name of the fragment
     * @param data - optional Bundle (can be null)
     * @param addToBackStack - whether to add this transaction to the back stack
     */
    fun loadFragment(
        fragmentManager: FragmentManager,
        containerId: Int,
        fragmentClassName: String,
        data: Bundle? = null,
        addToBackStack: Boolean = true
    ) {
        try {
            val fragmentClass = Class.forName(fragmentClassName).asSubclass(Fragment::class.java)
            val fragment = fragmentClass.newInstance()

            if (data != null) {
                fragment.arguments = data
            }

            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(containerId, fragment)

            if (addToBackStack) {
                transaction.addToBackStack(fragmentClassName)
            }

            transaction.commit()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
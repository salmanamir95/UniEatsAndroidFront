package com.example.unieats.frontend.dashboard.admin.MenuItemDetailAdmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.unieats.databinding.FragmentAdminMenuItemDetailBinding
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class AdminMenuItemDetailFragment : Fragment() {

    private lateinit var menuItem: MenuItemModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdminMenuItemDetailBinding.inflate(inflater, container, false)

        // Get the MenuItemModel from the arguments
        arguments?.let {
            menuItem = it.getParcelable("menuItem")!!
        }

        // Display menu item details
        binding.menuItemName.text = menuItem.name
        binding.menuItemCategory.text = menuItem.category
        binding.menuItemPrice.text = "$${menuItem.price}"
        binding.menuItemQuantity.text = menuItem.quantity.toString()

        Glide.with(requireContext())
            .load(menuItem.imageBitmap)
            .into(binding.menuItemImage)

        return binding.root
    }

    companion object {
        fun newInstance(menuItem: MenuItemModel): AdminMenuItemDetailFragment {
            val fragment = AdminMenuItemDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable("menuItem", menuItem)
            fragment.arguments = bundle
            return fragment
        }
    }
}

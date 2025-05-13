package com.example.unieats.frontend.dashboard.admin.MenuItemDetailAdmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.unieats.databinding.FragmentAdminMenuItemDetailBinding
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class AdminMenuItemDetailFragment : DialogFragment() {

    private lateinit var menuItem: MenuItemModel
    private var _binding: FragmentAdminMenuItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_NoActionBar)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminMenuItemDetailBinding.inflate(inflater, container, false)

        arguments?.let {
            menuItem = it.getParcelable("menuItem")!!
        }

        binding.menuItemName.text = menuItem.name
        binding.menuItemCategory.text = menuItem.category
        binding.menuItemPrice.text = "Rs. ${menuItem.price}"
        binding.menuItemQuantity.text = menuItem.quantity.toString()

        Glide.with(requireContext())
            .load(menuItem.imageBitmap)
            .into(binding.menuItemImage)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

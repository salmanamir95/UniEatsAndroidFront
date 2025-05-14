package com.example.unieats.frontend.dashboard.admin.MenuItemDetailAdmin

import android.graphics.Bitmap.CompressFormat.*
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.unieats.backend.dbData.MenuItem
import com.example.unieats.databinding.FragmentAdminMenuItemDetailBinding
import com.example.unieats.frontend.dashboard.admin.SharedViewModels.MenuSharedViewModel
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel
import java.io.ByteArrayOutputStream

class AdminMenuItemDetailFragment : DialogFragment() {
    //Change Picture
    private lateinit var menuItem: MenuItemModel
    private var _binding: FragmentAdminMenuItemDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var item12 : AdminMenuItemViewModel

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
        item12 = ViewModelProvider(requireParentFragment())[AdminMenuItemViewModel::class.java]

        val sharedRepo = ViewModelProvider(requireActivity())[MenuSharedViewModel::class.java].data.value
        sharedRepo?.let {
            if (!item12.isInitialized()) item12.init(it)
        }
        // Set initial values
        binding.menuItemName.text = "Name: ${menuItem.name}"
        binding.menuItemNameEdit.setText(menuItem.name)

        binding.menuItemCategory.text = "Category: ${menuItem.category}"
        binding.menuItemCategoryEdit.setText(menuItem.category)

        binding.menuItemPrice.text = "Rs. ${menuItem.price}"
        binding.menuItemPriceEdit.setText(menuItem.price.toString())

        binding.menuItemQuantity.text = "Quantity: ${menuItem.quantity}"
        binding.menuItemQuantityEdit.setText(menuItem.quantity.toString())

        Glide.with(requireContext())
            .load(menuItem.imageBitmap)
            .into(binding.menuItemImage)

        // Handle Edit Button Click
        binding.btnEdit.setOnClickListener {
            toggleEditMode(true)
        }

        binding.btnDelete.setOnClickListener{
            item12.deleteMenuItem(menuItem.id){
                success ->
                if (success)
                    Log.d("Deleted", "Successfully")
                else
                    Log.d("Deleted", "Unsuccessfully")
                dismiss()
            }
        }

        binding.btnEditSave.setOnClickListener {
            toggleEditMode(false)
            val outputStream = ByteArrayOutputStream()
            menuItem.imageBitmap?.compress(JPEG, 70, outputStream)
            val byteArray = outputStream.toByteArray()
            val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)

// Now use base64Image (not byteArray.toString())
            val newModel = MenuItem(
                menuItem.id,
                binding.menuItemNameEdit.text.toString(),
                binding.menuItemCategoryEdit.text.toString(),
                binding.menuItemPriceEdit.text.toString().toDoubleOrNull() ?: 0.0,
                binding.menuItemQuantityEdit.text.toString().toIntOrNull() ?: 0,
                base64Image
            )

            item12.updateMenuItem(menuItem.id, newModel){
                dismiss()
            }
        }


        return binding.root
    }

    private fun toggleEditMode(isEditMode: Boolean) {
        // Name
        binding.menuItemName.visibility = if (isEditMode) View.GONE else View.VISIBLE
        binding.menuItemNameInputLayout.visibility = if (isEditMode) View.VISIBLE else View.GONE

        // Category
        binding.menuItemCategory.visibility = if (isEditMode) View.GONE else View.VISIBLE
        binding.menuItemCategoryInputLayout.visibility = if (isEditMode) View.VISIBLE else View.GONE

        // Price
        binding.menuItemPrice.visibility = if (isEditMode) View.GONE else View.VISIBLE
        binding.menuItemPriceInputLayout.visibility = if (isEditMode) View.VISIBLE else View.GONE

        // Quantity
        binding.menuItemQuantity.visibility = if (isEditMode) View.GONE else View.VISIBLE
        binding.menuItemQuantityInputLayout.visibility = if (isEditMode) View.VISIBLE else View.GONE

        // Edit/ EditSave
        binding.btnEdit.visibility = if(isEditMode) View.GONE else View.VISIBLE
        binding.btnEditSave.visibility = if (isEditMode) View.VISIBLE else View.GONE
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

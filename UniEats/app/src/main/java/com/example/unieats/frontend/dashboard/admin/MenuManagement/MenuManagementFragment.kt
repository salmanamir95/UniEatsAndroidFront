package com.example.unieats.frontend.dashboard.admin.MenuManagement

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.unieats.R
import com.example.unieats.databinding.FragmentMenuManagementBinding
import com.example.unieats.frontend.dashboard.admin.SharedViewModels.MenuSharedViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MenuManagementFragment : Fragment() {

    private var _binding: FragmentMenuManagementBinding? = null
    private val binding get() = _binding!!

    private val menuViewModel: MenuManageViewModel by viewModels()
    private lateinit var menuAdapter: MenuAdapter

    private lateinit var fullCameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    private val REQUEST_PERMISSIONS = 103
    private lateinit var currentPhotoUri: Uri

    private var selectedImageUri: Uri? = null
    private var imagePreview: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fullCameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && ::currentPhotoUri.isInitialized) {
                selectedImageUri = currentPhotoUri
                loadImageAsync(currentPhotoUri)
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                loadImageAsync(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuManagementBinding.inflate(inflater, container, false)

        val sharedRepo = ViewModelProvider(requireActivity())[MenuSharedViewModel::class.java].data.value
        sharedRepo?.let { menuViewModel.init(it) }

        menuAdapter = MenuAdapter()
        binding.rvMenuItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMenuItems.adapter = menuAdapter

        menuViewModel.menuItems.observe(viewLifecycleOwner) { items ->
            menuAdapter.submitList(items)
        }

        binding.btnAddMenuItem.setOnClickListener {
            if (checkAndRequestPermissions()) {
                if (menuViewModel.isInitialized()) {
                    showAddMenuItemDialog()
                } else {
                    Toast.makeText(requireContext(), "Repository not initialized yet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkAndRequestPermissions(): Boolean {
        val neededPermissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            neededPermissions.add(Manifest.permission.CAMERA)
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            neededPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        return if (neededPermissions.isNotEmpty()) {
            requestPermissions(neededPermissions.toTypedArray(), REQUEST_PERMISSIONS)
            false
        } else true
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
    }

    private fun showAddMenuItemDialog() {
        val context = requireContext()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_menu_item_admin, null)

        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etCategory = dialogView.findViewById<EditText>(R.id.etCategory)
        val etPrice = dialogView.findViewById<EditText>(R.id.etPrice)
        val etQuantity = dialogView.findViewById<EditText>(R.id.etQuantity)
        imagePreview = dialogView.findViewById(R.id.imagePreview)

        val btnCamera = dialogView.findViewById<Button>(R.id.btnCamera)
        val btnGallery = dialogView.findViewById<Button>(R.id.btnGallery)

        btnCamera.setOnClickListener {
            val imageFile = createImageFile()
            currentPhotoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            fullCameraLauncher.launch(currentPhotoUri)
        }

        btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        AlertDialog.Builder(context)
            .setTitle("Add Menu Item")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString()
                val category = etCategory.text.toString()
                val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0
                val quantity = etQuantity.text.toString().toIntOrNull() ?: 0

                if (selectedImageUri == null) {
                    Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (!menuViewModel.isInitialized()) {
                    Toast.makeText(context, "Repository not initialized", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val itemAdmin = MenuItemAdmin().apply {
                    this.name = name
                    this.category = category
                    this.price = price
                    this.quantity = quantity
                    this.imageUrl = selectedImageUri.toString()
                }

                menuViewModel.addMenuItem(itemAdmin, selectedImageUri!!) { success ->
                    if (success) {
                        Toast.makeText(context, "Item added successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to add item", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadImageAsync(uri: Uri) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                Glide.with(requireContext())
                    .load(uri)
                    .into(imagePreview ?: return@launch)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                Log.e("ImageLoad", "Error: ${e.message}")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                if (menuViewModel.isInitialized()) {
                    showAddMenuItemDialog()
                } else {
                    Toast.makeText(requireContext(), "Repository not initialized", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "Permissions denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

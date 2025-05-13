package com.example.unieats.frontend.dashboard.admin.MenuManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unieats.R
import com.example.unieats.databinding.FragmentMenuManagementBinding
import com.example.unieats.frontend.dashboard.admin.SharedViewModels.MenuSharedViewModel
import com.example.unieats.frontend.dashboard.student.Menu.MenuItemModel

class MenuManagementFragment : Fragment() {

    private var _binding: FragmentMenuManagementBinding? = null
    private val binding get() = _binding!!

    private val menuViewModel: MenuManageViewModel by viewModels()

    private lateinit var menuAdapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuManagementBinding.inflate(inflater, container, false)

        // Get the shared repository data from parent fragment
        val sharedRepo = ViewModelProvider(requireParentFragment())[MenuSharedViewModel::class.java].data.value!!

        // Initialize ViewModel with the repository
        menuViewModel.init(sharedRepo)

        // Setup RecyclerView
        menuAdapter = MenuAdapter()
        binding.rvMenuItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMenuItems.adapter = menuAdapter

        // Observe LiveData for menu items
        menuViewModel.menuItems.observe(viewLifecycleOwner) { items ->
            menuAdapter.submitList(items)
        }

        // Handle Add button click
        binding.btnAddMenuItem.setOnClickListener {
            // Handle the logic for adding a new menu item
            Toast.makeText(requireContext(), "Add Menu Item clicked", Toast.LENGTH_SHORT).show()

            // This can be replaced with a dialog to input a new menu item
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.unieats.frontend.dashboard.admin.OrderManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unieats.databinding.FragmentOrderManagementBinding
import com.example.unieats.frontend.dashboard.admin.SharedViewModels.OrderSharedViewModel


class OrderManagementFragment : Fragment() {
    private var _binding: FragmentOrderManagementBinding? = null
    private lateinit var orderAdapter: OrderManagementAdapter
    private val orderVM: OrderManagementViewModel by viewModels()
    private var repositoryInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderManagementBinding.inflate(inflater, container, false)
        val sharedViewModel = ViewModelProvider(requireActivity())[OrderSharedViewModel::class.java]

        orderAdapter = OrderManagementAdapter()

        _binding!!.recyclerViewOrdersAdmin.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }

        sharedViewModel.data.observe(viewLifecycleOwner) { repo ->
            repo?.let {
                if (!repositoryInitialized) {
                    orderVM.init(it)
                    repositoryInitialized = true
                    // Start observing orders only after initialization
                    orderVM.orders.observe(viewLifecycleOwner) { items ->
                        orderAdapter.submitList(items)
                    }
                }
            }
        }

        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
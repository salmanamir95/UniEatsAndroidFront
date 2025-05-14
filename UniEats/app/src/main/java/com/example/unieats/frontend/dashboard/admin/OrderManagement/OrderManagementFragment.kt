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
    private var _binding : FragmentOrderManagementBinding? = null
    private lateinit var orderAdapter: OrderManagementAdapter
    private  val orderVM : OrderManagementViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderManagementBinding.inflate(inflater, container,false)
        val SharedRepo = ViewModelProvider(requireActivity())[OrderSharedViewModel::class.java].data.value
        SharedRepo?.let{
            if (!orderVM.isInitialized())  orderVM.init(SharedRepo)
        }
        orderAdapter= OrderManagementAdapter()

        _binding!!.recyclerViewOrdersAdmin.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
        orderVM._orders.observe(viewLifecycleOwner){
            item -> orderAdapter.submitList(item)
        }

        return _binding!!.root
    }
}

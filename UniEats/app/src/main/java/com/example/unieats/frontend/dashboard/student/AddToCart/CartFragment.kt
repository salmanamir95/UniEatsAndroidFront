package com.example.unieats.frontend.dashboard.student.AddToCart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.student.Menu.MenuAdapter
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SelectedMenuItemsSharedtoCart

class CartFragment: Fragment() {
    private val selectedItemsViewModel: SelectedMenuItemsSharedtoCart by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter // reuse MenuAdapter for now


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvCartItems)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MenuAdapter(
            items = emptyList(),
            onItemClick = { /* Optional: navigate to details */ },
            onItemSelectionChanged = { changedItem ->
                if (changedItem.isSelected)
                    selectedItemsViewModel.insert(changedItem)
                else
                    selectedItemsViewModel.remove(changedItem)
            },
            selectedMenuItemsSharedtoCart = selectedItemsViewModel
        )

        recyclerView.adapter = adapter

        selectedItemsViewModel.studentSelection.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        val btnOrderNow: Button = view.findViewById(R.id.btn_order_now)
        btnOrderNow.setOnClickListener {
            val selectedItems = selectedItemsViewModel.studentSelection.value ?: emptyList()
            if (selectedItems.isNotEmpty()) {
                // Proceed to place order logic
            } else {
                Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
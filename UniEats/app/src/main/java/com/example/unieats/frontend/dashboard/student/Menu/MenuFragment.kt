package com.example.unieats.frontend.dashboard.student.Menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import androidx.fragment.app.Fragment

import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.R

import com.example.unieats.frontend.dashboard.student.SharedViewModels.MenuItemSharedViewModel
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SelectedMenuItemsSharedtoCart
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SharedStudentViewModel
import com.example.unieats.frontend.dashboard.student.StudentFragment

class MenuFragment : Fragment(){

    private val viewModel: MenuViewModel by viewModels({ requireParentFragment() })
    private val menuItemShared: MenuItemSharedViewModel by viewModels({ requireParentFragment() })
    private val _student : SharedStudentViewModel by viewModels({ requireParentFragment() })
    private val selectedMenuItemsViewModel: SelectedMenuItemsSharedtoCart by viewModels({requireParentFragment()})
    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_menu, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = (parentFragment?.parentFragment as? StudentFragment)?.navController ?: return
        recyclerView = view.findViewById(R.id.rvMenuItems)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        viewModel.menuItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        viewModel.fetchMenuItems()

        val btnAddToCart: Button = view.findViewById(R.id.btn_add_to_cart)
        val btnOrderNow: Button = view.findViewById(R.id.btn_order_now)

        adapter = MenuAdapter(emptyList(),
            onItemClick = { clickedItem ->
                menuItemShared.menuItem.value = clickedItem

            },
            onItemSelectionChanged = { changedItem ->
                viewModel.updateSelection(changedItem)
                if (changedItem.isSelected)
                    selectedMenuItemsViewModel.insert(changedItem)
                else
                    selectedMenuItemsViewModel.remove(changedItem)
            },
            selectedMenuItemsViewModel
        )

        recyclerView.adapter = adapter

        btnAddToCart.setOnClickListener {
            navController.navigate(R.id.cartFragment)
        }

        btnOrderNow.setOnClickListener {
            navController.navigate(R.id.ordersFragment)
        }
    }

}

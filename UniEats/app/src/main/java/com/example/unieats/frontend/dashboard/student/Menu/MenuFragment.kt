package com.example.unieats.frontend.dashboard.student.Menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.student.MenuItem.MenuItemPageFragment
import com.example.unieats.frontend.dashboard.student.SharedViewModels.MenuItemSharedViewModel
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SharedStudentViewModel

class MenuFragment : Fragment() {

    private val viewModel: MenuViewModel by viewModels({ requireParentFragment() })
    private val menuItemShared: MenuItemSharedViewModel by viewModels({ requireParentFragment() })
    private val _student : SharedStudentViewModel by viewModels({ requireParentFragment() })

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

        recyclerView = view.findViewById(R.id.rvMenuItems)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MenuAdapter(emptyList()) { clickedItem ->
            menuItemShared.menuItem.value = clickedItem
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MenuItemPageFragment())
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter

        viewModel.menuItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        viewModel.fetchMenuItems()
    }
}

package com.example.unieats.frontend.dashboard.student.Menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.R

class MenuFragment: Fragment() {
    private val viewModel: MenuViewModel by viewModels()
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

        adapter = MenuAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.menuItems.observe(viewLifecycleOwner, Observer { items ->
            adapter = MenuAdapter(items)
            recyclerView.adapter = adapter
        })

        viewModel.fetchMenuItems()
    }
}
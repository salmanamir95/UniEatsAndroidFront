package com.example.unieats.frontend.dashboard.student.Reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.R
import com.example.unieats.frontend.dashboard.student.SharedViewModels.SharedStudentViewModel

class TableFragment : Fragment() {

    private lateinit var viewModel: TableViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedStudentViewModel: SharedStudentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_table, container, false)

        recyclerView = view.findViewById(R.id.tableRecycler)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        sharedStudentViewModel = ViewModelProvider(requireActivity()).get(SharedStudentViewModel::class.java)
        val studentId = sharedStudentViewModel.getStudent()?.id ?: ""

        viewModel = TableViewModel(studentId)
        viewModel.tables.observe(viewLifecycleOwner) { tableList ->
            recyclerView.adapter = TableAdapter(tableList, studentId) { selectedTable ->
                viewModel.reserveOrUnreserve(selectedTable)
            }
        }

        return view
    }
}
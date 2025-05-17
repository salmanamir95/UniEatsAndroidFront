package com.example.unieats.frontend.dashboard.admin.TableManagement

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unieats.R
import com.example.unieats.databinding.FragmentTableManagementBinding
import com.example.unieats.frontend.dashboard.admin.SharedViewModels.TableSharedViewModel
import com.example.unieats.backend.dbData.Table
class TableManagementFragment : Fragment() {

    private var _binding: FragmentTableManagementBinding? = null
    private val binding get() = _binding!!

    private val tableVM: TableManagementViewModel by viewModels()
    private lateinit var tableAdapter: TableManagementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTableManagementBinding.inflate(inflater, container, false)

        // Get the repository from shared ViewModel
        val sharedViewModel = ViewModelProvider(requireActivity())[TableSharedViewModel::class.java]

        sharedViewModel.data.observe(viewLifecycleOwner) { repo ->
            repo?.let {
                if (!tableVM.isInitialized()) {
                    tableVM.init(it)

                    // Observe and bind table data
                    tableVM.tables.observe(viewLifecycleOwner) { tables ->
                        tableAdapter.submitList(tables)
                    }
                }
            }
        }

        setupRecyclerView()

        binding.btnAddTable.setOnClickListener {
            showAddTableDialog()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        tableAdapter = TableManagementAdapter { action ->
            when (action) {
                is TableAction.Delete -> {
                    // Show confirmation dialog before deleting
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete Table")
                        .setMessage("Are you sure you want to delete this table?")
                        .setPositiveButton("Yes") { _, _ ->
                            tableVM.deleteTable(action.tableId)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }

                is TableAction.Reserve -> {
                    tableVM.reserveTable(action.table.id, "student123") // Replace with actual ID
                }

                is TableAction.CancelReservation -> {
                    tableVM.cancelReservation(action.table.id)
                }

                is TableAction.Edit -> {
                    // You can implement editing here later if needed
                }
            }
        }

        binding.recyclerViewTables.apply {
            layoutManager = GridLayoutManager(requireContext(), calculateSpanCount())
            adapter = tableAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun calculateSpanCount(): Int {
        val screenWidthDp = resources.configuration.screenWidthDp
        return when {
            screenWidthDp >= 840 -> 3
            screenWidthDp >= 600 -> 2
            else -> 1
        }
    }

    private fun showAddTableDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_table, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Add Table")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val tableNumberInput = dialogView.findViewById<EditText>(R.id.inputTableNumber)
                val seatsInput = dialogView.findViewById<EditText>(R.id.inputSeats)

                val number = tableNumberInput.text.toString().toIntOrNull()
                val seats = seatsInput.text.toString().toIntOrNull()

                if (number != null && seats != null) {
                    val newTable = Table(number = number, seats = seats)
                    tableVM.addTable(newTable)
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        dialogBuilder.show()
    }

}

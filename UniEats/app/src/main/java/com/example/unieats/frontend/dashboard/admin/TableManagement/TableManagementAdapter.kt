package com.example.unieats.frontend.dashboard.admin.TableManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.backend.dbData.Table
import com.example.unieats.databinding.ItemTableBinding

class TableManagementAdapter
    ( private val onAction: (TableAction) -> Unit): ListAdapter<Table, TableManagementAdapter.TableViewHolder>(
    object : DiffUtil.ItemCallback<Table>() {
        override fun areItemsTheSame(oldItem: Table, newItem: Table) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Table, newItem: Table) = oldItem == newItem
    }
) {
    inner class TableViewHolder(private val binding: ItemTableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(table: Table) {
            binding.tableNumber.text = "Table #${table.number}"
            binding.seatsCount.text = "${table.seats} seats"
            binding.reservationStatus.text =
                if (table.isReserved) "Reserved by ${table.reservedBy}" else "Available"

            binding.btnEdit.setOnClickListener {
                onAction(TableAction.Edit(table))
            }

            binding.btnReserve.setOnClickListener {
                if (table.isReserved) {
                    onAction(TableAction.CancelReservation(table))
                } else {
                    onAction(TableAction.Reserve(table))
                }
            }

            binding.btnDelete.setOnClickListener {
                onAction(TableAction.Delete(table.id))
            }

            // Update reserve button label
            binding.btnReserve.text = if (table.isReserved) "Cancel" else "Reserve"
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val binding = ItemTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
sealed class TableAction {
    data class Edit(val table: Table) : TableAction()
    data class Reserve(val table: Table) : TableAction()
    data class CancelReservation(val table: Table) : TableAction()
    data class Delete(val tableId: String) : TableAction()
}

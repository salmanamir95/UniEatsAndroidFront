package com.example.unieats.frontend.dashboard.student.Reservation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.unieats.R
import com.example.unieats.backend.dbData.Table

class TableAdapter(
    private val tables: List<Table>,
    private val currentStudentId: String,
    private val onClick: (Table) -> Unit
) : RecyclerView.Adapter<TableAdapter.TableViewHolder>() {

    inner class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.tableCard)
        val number: TextView = view.findViewById(R.id.tableNumber)
        val seats: TextView = view.findViewById(R.id.seatCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table, parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val table = tables[position]
        holder.number.text = "Table ${table.number}"
        holder.seats.text = "Seats: ${table.seats}"

        val color = when {
            table.reservedBy == currentStudentId -> Color.YELLOW
            table.reservedBy != null -> Color.RED
            else -> Color.GREEN
        }

        holder.card.setCardBackgroundColor(color)

        holder.card.setOnClickListener {
            onClick(table)
        }
    }

    override fun getItemCount(): Int = tables.size
}
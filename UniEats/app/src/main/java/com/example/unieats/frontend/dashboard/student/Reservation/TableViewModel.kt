package com.example.unieats.frontend.dashboard.student.Reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.Table
import com.example.unieats.backend.repository.TableRepository
import com.google.firebase.database.ValueEventListener

class TableViewModel(private val studentId: String) : ViewModel() {

    private val repository = TableRepository()
    private val _tables = MutableLiveData<List<Table>>()
    val tables: LiveData<List<Table>> = _tables

    private var listener: ValueEventListener? = null

    init {
        fetchTables()
    }

    private fun fetchTables() {
        listener = repository.observeTables { updated ->
            _tables.postValue(updated)
        }
    }

    fun reserveOrUnreserve(table: Table) {
        if (table.reservedBy == null || table.reservedBy == studentId) {
            val newValue = if (table.reservedBy == studentId) null else studentId
            repository.reserveTable(table.id, newValue)
        }
    }

    override fun onCleared() {
        super.onCleared()
        listener?.let { repository.removeListener(it) }
    }
}
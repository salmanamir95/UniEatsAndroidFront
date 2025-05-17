package com.example.unieats.frontend.dashboard.admin.TableManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unieats.backend.dbData.Table
import com.example.unieats.backend.repository.TableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TableManagementViewModel : ViewModel() {

    private lateinit var repository: TableRepository
    val tables: LiveData<List<Table>> get() = repository.tablesLiveData

    private val _operationStatus = MutableLiveData<String>()
    val operationStatus: LiveData<String> get() = _operationStatus

    fun isInitialized(): Boolean = this::repository.isInitialized

    fun init(repo: TableRepository) {
        if (!isInitialized()) {
            repository = repo
            repository.observeTables() // Trigger real-time listener
        }
    }

    fun addTable(table: Table) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = repository.createTable(table)
            _operationStatus.postValue(
                if (success) "Table Added Successfully"
                else "Failed to Add Table"
            )
        }
    }

    fun updateTable(tableId: String, updatedTable: Table) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = repository.updateTable(tableId, mapOf(
                "number" to updatedTable.number,
                "seats" to updatedTable.seats,
                "reservedBy" to updatedTable.reservedBy
            ))
            _operationStatus.postValue(
                if (success) "Table Updated Successfully"
                else "Failed to Update Table"
            )
        }
    }


    fun reserveTable(tableId: String, studentId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = repository.reserveTable(tableId, studentId)
            _operationStatus.postValue(
                if (success) "Table Reserved Successfully"
                else "Failed to Reserve Table"
            )
        }
    }

    fun cancelReservation(tableId: String) {
        reserveTable(tableId, null)
    }

    fun deleteTable(tableId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = repository.deleteTable(tableId)
            _operationStatus.postValue(
                if (success) "Table Deleted Successfully"
                else "Failed to Delete Table"
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (isInitialized()) repository.removeListener()
    }
}

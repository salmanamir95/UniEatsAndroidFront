package com.example.unieats.frontend.dashboard.admin.TableManagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unieats.backend.dbData.Table
import com.example.unieats.backend.repository.TableRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TableManagementViewModel : ViewModel() {

    private lateinit var repository: TableRepository
    private val _tables = MutableLiveData<List<Table>>()
    val tables: LiveData<List<Table>> get() = _tables

    private val _operationStatus = MutableLiveData<String>()
    val operationStatus: LiveData<String> get() = _operationStatus

    // Ensure repository is initialized
    fun isInitialized(): Boolean = this::repository.isInitialized

    fun init(repo: TableRepository) {
        if (!this.isInitialized()) {
            repository = repo
            repo.tablesLiveData.observeForever {
                _tables.postValue(it)
            }
        }
    }

    // Add a new table
    fun addTable(table: Table) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.createTable(table)
                _operationStatus.postValue("Table Added Successfully")
            } catch (e: Exception) {
                _operationStatus.postValue("Failed to Add Table: ${e.message}")
            }
        }
    }

    // Update a table's details (e.g., reserve or change details)
    fun updateTable(tableId: String, updatedTable: Table) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.updateTable(tableId, updatedTable)
                _operationStatus.postValue("Table Updated Successfully")
            } catch (e: Exception) {
                _operationStatus.postValue("Failed to Update Table: ${e.message}")
            }
        }
    }

    // Reserve a table (assign a studentId)
    fun reserveTable(tableId: String, studentId: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.reserveTable(tableId, studentId)
                _operationStatus.postValue("Table Reserved Successfully")
            } catch (e: Exception) {
                _operationStatus.postValue("Failed to Reserve Table: ${e.message}")
            }
        }
    }

    // Cancel reservation (make the table empty again)
    fun cancelReservation(tableId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.reserveTable(tableId, null) // Setting reservedBy to null
                _operationStatus.postValue("Reservation Cancelled Successfully")
            } catch (e: Exception) {
                _operationStatus.postValue("Failed to Cancel Reservation: ${e.message}")
            }
        }
    }

    // Delete a table
    fun deleteTable(tableId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.deleteTable(tableId)
                _operationStatus.postValue("Table Deleted Successfully")
            } catch (e: Exception) {
                _operationStatus.postValue("Failed to Delete Table: ${e.message}")
            }
        }
    }

    // Remove table data and clear the LiveData if needed
    fun clearTables() {
        _tables.postValue(emptyList())
    }

    // Make sure to remove the listener when the ViewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        repository.removeListener()
    }
}

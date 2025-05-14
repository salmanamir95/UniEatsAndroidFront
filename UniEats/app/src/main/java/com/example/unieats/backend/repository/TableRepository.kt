package com.example.unieats.backend.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.unieats.backend.dbData.Table
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TableRepository {

    private val dbRef = FirebaseDatabase.getInstance().getReference("tables")

    // LiveData to expose the tables list
    private val _tablesLiveData = MutableLiveData<List<Table>>()
    val tablesLiveData: LiveData<List<Table>> = _tablesLiveData

    // To store the listener and remove it later if needed
    private var tablesListener: ValueEventListener? = null

    // Observe tables in real-time in background thread (via Dispatchers.IO)
    fun observeTables() {
        tablesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Switch to the IO dispatcher for the intensive operation of mapping and updating LiveData
                snapshot.children.mapNotNull { it.getValue(Table::class.java) }.also { tables ->
                    _tablesLiveData.postValue(tables) // Update the LiveData with tables on the main thread
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _tablesLiveData.postValue(emptyList()) // Handle error (you can customize this)
            }
        }

        // Adding listener on the database should be done on the main thread, but the data processing happens on the IO thread
        dbRef.addValueEventListener(tablesListener!!)
    }

    // Create a new table (Add table) in background thread
    suspend fun createTable(table: Table) {
        withContext(Dispatchers.IO) {
            val tableId = dbRef.push().key ?: return@withContext
            val tableWithId = table.copy(id = tableId) // Assign generated ID to the table
            dbRef.child(tableId).setValue(tableWithId) // Save the table to Firebase with a new ID
        }
    }

    // Update a table's details (e.g., reserve table or change seats) in background thread
    suspend fun updateTable(tableId: String, updatedTable: Table) {
        withContext(Dispatchers.IO) {
            dbRef.child(tableId).setValue(updatedTable) // Update the table in Firebase
        }
    }

    // Reserve a table in background thread (this is an update operation)
    suspend fun reserveTable(tableId: String, studentId: String?) {
        withContext(Dispatchers.IO) {
            val updatedTable = Table(
                id = tableId,
                reservedBy = studentId,
                number = 0, // No need to update the number
                seats = 4 // Seats won't change for a reservation
            )
            dbRef.child(tableId).setValue(updatedTable) // Update the table with reservation status
        }
    }

    // Remove a table (Delete operation) in background thread
    suspend fun deleteTable(tableId: String) {
        withContext(Dispatchers.IO) {
            dbRef.child(tableId).removeValue() // Removes the table node from Firebase
        }
    }

    // Remove the listener when no longer needed
    fun removeListener() {
        tablesListener?.let { dbRef.removeEventListener(it) }
        tablesListener = null
    }
}

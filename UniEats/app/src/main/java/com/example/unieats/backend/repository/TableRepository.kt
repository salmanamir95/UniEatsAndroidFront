package com.example.unieats.backend.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.unieats.backend.dbData.Table
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

class TableRepository {

    private val dbRef = FirebaseDatabase.getInstance().getReference("tables")
    private val _tablesLiveData = MutableLiveData<List<Table>>()
    val tablesLiveData: LiveData<List<Table>> = _tablesLiveData

    private val activeListener = AtomicBoolean(false)
    private var tablesListener: ValueEventListener? = null

    fun observeTables() {
        if (activeListener.getAndSet(true)) {
            removeListener() // Prevent multiple active listeners
        }

        tablesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                processSnapshot(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                handleDatabaseError(error)
            }
        }

        tablesListener?.let {
            dbRef.addValueEventListener(it)
        }
    }

    private fun processSnapshot(snapshot: DataSnapshot) {
        val tables = mutableListOf<Table>()
        for (child in snapshot.children) {
            child.getValue(Table::class.java)?.let { table ->
                tables.add(table)
            }
        }
        _tablesLiveData.postValue(tables)
    }

    private fun handleDatabaseError(error: DatabaseError) {
        _tablesLiveData.postValue(emptyList())
        // Consider adding proper error logging/handling here
    }

    suspend fun createTable(table: Table): Boolean = withContext(Dispatchers.IO) {
        try {
            val tableId = dbRef.push().key ?: return@withContext false
            dbRef.child(tableId).setValue(table.copy(id = tableId)).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateTable(tableId: String, updates: Map<String, Any?>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                dbRef.child(tableId).updateChildren(updates).await()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun reserveTable(tableId: String, studentId: String?): Boolean {
        return updateTable(tableId, mapOf("reservedBy" to studentId))
    }

    suspend fun deleteTable(tableId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            dbRef.child(tableId).removeValue().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun removeListener() {
        tablesListener?.let {
            dbRef.removeEventListener(it)
            activeListener.set(false)
            tablesListener = null
        }
    }
}
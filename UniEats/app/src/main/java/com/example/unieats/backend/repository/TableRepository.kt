package com.example.unieats.backend.repository

import com.example.unieats.backend.dbData.Table
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TableRepository {
    private val dbRef = FirebaseDatabase.getInstance().getReference("tables")

    fun observeTables(onUpdate: (List<Table>) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tables = snapshot.children.mapNotNull { it.getValue(Table::class.java) }
                onUpdate(tables)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        dbRef.addValueEventListener(listener)
        return listener
    }

    fun reserveTable(tableId: String, studentId: String?) {
        dbRef.child(tableId).child("reservedBy").setValue(studentId)
    }

    fun removeListener(listener: ValueEventListener) {
        dbRef.removeEventListener(listener)
    }
}
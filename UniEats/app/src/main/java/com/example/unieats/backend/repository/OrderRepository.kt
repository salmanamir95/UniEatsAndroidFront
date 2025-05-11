package com.example.unieats.backend.repository

import com.example.unieats.backend.dbData.Order
import com.example.unieats.backend.dbData.OrderStatus
import com.google.firebase.database.*

class OrderRepository {

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")

    // 1. Create Order
    fun createOrder(order: Order, onResult: (Boolean) -> Unit) {
        val key = dbRef.push().key ?: return onResult(false)
        val orderWithId = order.copy(orderId = key)
        dbRef.child(key).setValue(orderWithId)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // 2. Cancel Order
    fun cancelOrder(orderId: String, onResult: (Boolean) -> Unit) {
        dbRef.child(orderId).child("status").setValue(OrderStatus.CANCELLED)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // 3. Update Order (within 10 minutes)
    fun updateOrder(order: Order, onResult: (Boolean) -> Unit) {
        val tenMinInMillis = 10 * 60 * 1000
        val currentTime = System.currentTimeMillis()
        if ((currentTime - order.timestamp) <= tenMinInMillis) {
            dbRef.child(order.orderId).setValue(order)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } else {
            onResult(false)
        }
    }

    // 4. Observe User Order History (Real-Time)
    fun observeUserOrders(userId: String, onUpdate: (List<Order>) -> Unit): ValueEventListener {
        val query = dbRef.orderByChild("userId").equalTo(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = snapshot.children.mapNotNull { it.getValue(Order::class.java) }
                onUpdate(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                onUpdate(emptyList())
            }
        }
        query.addValueEventListener(listener)
        return listener
    }

    // 5. Observe All Orders (Real-Time)
    fun observeAllOrders(onUpdate: (List<Order>) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = snapshot.children.mapNotNull { it.getValue(Order::class.java) }
                onUpdate(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                onUpdate(emptyList())
            }
        }
        dbRef.addValueEventListener(listener)
        return listener
    }

    // 6. Observe Currently Processing Orders (Real-Time)
    fun observeProcessingOrders(onUpdate: (List<Order>) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = snapshot.children.mapNotNull {
                    it.getValue(Order::class.java)?.takeIf { o -> o.status == OrderStatus.PREPARING }
                }
                onUpdate(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                onUpdate(emptyList())
            }
        }
        dbRef.addValueEventListener(listener)
        return listener
    }

    // 7. Observe Served Orders (Real-Time)
    fun observeServedOrders(onUpdate: (List<Order>) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = snapshot.children.mapNotNull {
                    it.getValue(Order::class.java)?.takeIf { o -> o.status == OrderStatus.SERVED }
                }
                onUpdate(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                onUpdate(emptyList())
            }
        }
        dbRef.addValueEventListener(listener)
        return listener
    }

    // 8. Observe Today’s Orders (Real-Time)
    fun observeTodaysOrders(onUpdate: (List<Order>) -> Unit): ValueEventListener {
        val startOfDay = getStartOfDayTimestamp().toDouble()
        val query = dbRef.orderByChild("timestamp").startAt(startOfDay)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = snapshot.children.mapNotNull { it.getValue(Order::class.java) }
                onUpdate(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                onUpdate(emptyList())
            }
        }

        query.addValueEventListener(listener)
        return listener
    }

    // 9. Observe Monthly Orders (Real-Time)
    fun observeMonthlyOrders(onUpdate: (List<Order>) -> Unit): ValueEventListener {
        val startOfMonth = getStartOfMonthTimestamp().toDouble()
        val query = dbRef.orderByChild("timestamp").startAt(startOfMonth)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = snapshot.children.mapNotNull { it.getValue(Order::class.java) }
                onUpdate(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                onUpdate(emptyList())
            }
        }

        query.addValueEventListener(listener)
        return listener
    }

    // 🔁 Remove Listener When Not Needed
    fun removeListener(listener: ValueEventListener) {
        dbRef.removeEventListener(listener)
    }

    // Helpers
     fun getStartOfDayTimestamp(): Long {
        val now = java.util.Calendar.getInstance()
        now.set(java.util.Calendar.HOUR_OF_DAY, 0)
        now.set(java.util.Calendar.MINUTE, 0)
        now.set(java.util.Calendar.SECOND, 0)
        now.set(java.util.Calendar.MILLISECOND, 0)
        return now.timeInMillis
    }

     fun getStartOfMonthTimestamp(): Long {
        val now = java.util.Calendar.getInstance()
        now.set(java.util.Calendar.DAY_OF_MONTH, 1)
        now.set(java.util.Calendar.HOUR_OF_DAY, 0)
        now.set(java.util.Calendar.MINUTE, 0)
        now.set(java.util.Calendar.SECOND, 0)
        now.set(java.util.Calendar.MILLISECOND, 0)
        return now.timeInMillis
    }
}

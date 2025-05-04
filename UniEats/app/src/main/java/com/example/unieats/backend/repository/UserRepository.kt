package com.example.unieats.backend.repository

import com.example.unieats.backend.models.User
import com.example.unieats.frontend.register.Register
import com.google.firebase.database.*

class UserRepository {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    // CREATE
    fun registerUser(user: Register, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = System.currentTimeMillis().toString()
        val userWithId = User.fromRegistertoUser(user, userId.hashCode())

        usersRef.child(userId)
            .setValue(userWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // READ
    fun loginUser(email: String, password: String, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        usersRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.password == password) {
                            onSuccess(user)
                            return
                        }
                    }
                    onFailure("Invalid email or password")
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }
            })
    }

    // UPDATE
    fun updateUser(userId: String, updatedUser: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.child(userId)
            .setValue(updatedUser)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // DELETE
    fun deleteUser(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.child(userId)
            .removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}

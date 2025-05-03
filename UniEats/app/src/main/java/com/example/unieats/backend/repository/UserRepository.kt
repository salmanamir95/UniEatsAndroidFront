package com.example.unieats.backend.repository

import com.example.unieats.backend.models.User
import com.example.unieats.frontend.register.Register
import com.google.firebase.database.FirebaseDatabase

class UserRepository {

    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    fun registerUser(user: Register, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userId = usersRef.push().key ?: System.currentTimeMillis().toString()
        val userWithId = User.fromRegistertoUser(user,userId.hashCode())
        usersRef.child(userId).setValue(userWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

}
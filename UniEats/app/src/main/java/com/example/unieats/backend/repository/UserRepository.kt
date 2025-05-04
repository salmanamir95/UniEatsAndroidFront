package com.example.unieats.backend.repository

import com.example.unieats.backend.models.User
import com.example.unieats.frontend.register.Register
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepository {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(user: Register, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.pass)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid ?: run {
                    onFailure(Exception("User ID not found"))
                    return@addOnSuccessListener
                }

                val userWithId = User.fromRegistertoUser(user, userId)
                usersRef.child(userId)
                    .setValue(userWithId)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it) }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.uid?.let { userId ->
                    fetchUserFromDatabase(userId, onSuccess, onFailure)
                } ?: onFailure("User ID not found")
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Authentication failed")
            }
    }

    private fun fetchUserFromDatabase(
        userId: String,
        onSuccess: (User) -> Unit,
        onFailure: (String) -> Unit
    ) {
        usersRef.child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                    ?: return@addOnSuccessListener onFailure("User data not found")
                onSuccess(user)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Database error")
            }
    }

    fun updateUser(userId: String, updatedUser: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.child(userId)
            .setValue(updatedUser)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteUser(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersRef.child(userId)
            .removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
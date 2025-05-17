package com.example.unieats.backend.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.unieats.backend.dbData.Table
import com.example.unieats.backend.dbData.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")
    private val _usersLiveData = MutableLiveData<List<User>>()
    val usersLiveData: LiveData<List<User>> = _usersLiveData

    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }

    suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        age: Int,
        designation: String
    ): Result<User> = withContext(Dispatchers.IO) {
        try {
            // Create authentication entry
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return@withContext Result.Error("User creation failed")

            // Create database entry
            val user = User(
                id = userId,
                email = email,
                name = name,
                age = age,
                designation = designation
            )

            dbRef.child(userId).setValue(user).await()
            Result.Success(user)
        } catch (e: FirebaseAuthException) {
            Result.Error("Authentication error: ${e.message ?: "Unknown error"}")
        } catch (e: Exception) {
            Result.Error("Registration failed: ${e.message ?: "Unknown error"}")
        }
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return Result.Error("User not found")

            val user = getUserFromDatabase(userId)
                ?: return Result.Error("User data not found")

            Result.Success(user)
        } catch (e: FirebaseAuthException) {
            Result.Error("Authentication error: ${e.message ?: "Unknown error"}")
        } catch (e: Exception) {
            Result.Error("Login failed: ${e.message ?: "Unknown error"}")
        }
    }

    private suspend fun getUserFromDatabase(userId: String): User? {
        return try {
            dbRef.child(userId).get().await().getValue(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            dbRef.child(user.id).setValue(user).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Update failed: ${e.message ?: "Unknown error"}")
        }
    }

    suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            // Delete from authentication
            auth.currentUser?.delete()?.await()

            // Delete from database
            dbRef.child(userId).removeValue().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Deletion failed: ${e.message ?: "Unknown error"}")
        }
    }

    suspend fun getCurrentUser(): User? {
        return auth.currentUser?.uid?.let { userId ->
            try {
                dbRef.child(userId).get().await().getValue(User::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun observeUsers(): List<User>? = withContext(Dispatchers.IO) {
        return@withContext try {
            val snapshot = dbRef.get().await()
            val userList = snapshot.children.mapNotNull { it.getValue(User::class.java) }
            _usersLiveData.postValue(userList)
            userList
        } catch (e: Exception) {
            null
        }
    }
}
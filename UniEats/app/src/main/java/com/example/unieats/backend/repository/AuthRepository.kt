package com.example.unieats.backend.repository

import android.app.Activity
import android.content.Intent
import com.example.unieats.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository(
    private val activity: Activity,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Google Sign-In Client setup (lightweight operation, no need for coroutine)
    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    // Converted to suspend function for coroutine usage
    suspend fun handleGoogleSignInResult(data: Intent?): Result<FirebaseUser> {
        return try {
            val account = withContext(ioDispatcher) {
                getGoogleAccount(data)
            }

            val user = withContext(ioDispatcher) {
                firebaseAuthWithGoogle(account)
            }

            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun getGoogleAccount(data: Intent?): GoogleSignInAccount {
        return try {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.await()
        } catch (e: Exception) {
            throw when (e) {
                is ApiException -> GoogleSignInException("Google Sign-In failed: ${e.statusCode}", e)
                else -> GoogleSignInException("Google Sign-In failed", e)
            }
        }
    }

    private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val authResult = auth.signInWithCredential(credential).await()

        return authResult.user ?: throw FirebaseAuthException(
            "User authentication succeeded but user is null",
            "Null user object"
        )
    }

    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }

    class GoogleSignInException(message: String, cause: Exception) : Exception(message, cause)
}
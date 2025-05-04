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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepository(private val activity: Activity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Google Sign-In Client setup
    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }

    // Handle Google Sign-In result
    fun handleGoogleSignInResult(data: Intent?, onSuccess: (FirebaseUser) -> Unit, onFailure: (String) -> Unit) {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken ?: run {
                onFailure("Null ID token from Google")
                return
            }

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    result.user?.let { user ->
                        onSuccess(user)
                    } ?: run {
                        onFailure("User authentication succeeded but user is null")
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure("Google sign-in failed: ${exception.message}")
                }

        } catch (e: ApiException) {
            onFailure("Google Sign-In failed with error: ${e.statusCode}")
        }
    }
}
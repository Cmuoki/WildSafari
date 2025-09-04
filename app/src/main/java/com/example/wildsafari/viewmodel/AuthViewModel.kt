package com.example.wildsafari.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.wildsafari.ROUTE_LOGIN
import com.example.wildsafari.ROUTE_REGISTER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val navController: NavHostController,
    private val context: Context
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // 🔹 Signup with Firebase and callback for Compose UI
    fun signup(
        fullname: String,
        email: String,
        pass: String,
        confirmpass: String,
        callback: (Boolean, String) -> Unit
    ) {
        if (fullname.isBlank() || email.isBlank() || pass.isBlank() || confirmpass.isBlank()) {
            callback(false, "All fields are required")
            return
        }
        if (pass != confirmpass) {
            callback(false, "Passwords do not match")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, pass).await()
                val userId = result.user?.uid

                if (userId != null) {
                    // Save extra user data in Firestore
                    val userMap = mapOf(
                        "fullname" to fullname,
                        "email" to email,
                        "uid" to userId
                    )
                    db.collection("users").document(userId).set(userMap).await()
                }

                CoroutineScope(Dispatchers.Main).launch {
                    callback(true, "Registered Successfully")
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    callback(false, "Error: ${e.message}")
                }
            }
        }
    }

    // 🔹 Login with Firebase
    fun login(email: String, pass: String, callback: (Boolean, String) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            callback(false, "Email and password required")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithEmailAndPassword(email, pass).await()
                CoroutineScope(Dispatchers.Main).launch {
                    callback(true, "Successfully logged in")
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    callback(false, "Login failed: ${e.message}")
                }
            }
        }
    }

    // 🔹 Logout
    fun logout() {
        auth.signOut()
        navController.navigate(ROUTE_LOGIN) {
            popUpTo(0)
        }
    }

    // 🔹 Get current user fullname
    fun getUserFullname(onResult: (String?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    onResult(doc.getString("fullname"))
                }
                .addOnFailureListener {
                    onResult(null)
                }
        } else {
            onResult(null)
        }
    }

    // 🔹 Check if logged in
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}


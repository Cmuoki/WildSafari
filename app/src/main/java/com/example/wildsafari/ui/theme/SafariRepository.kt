package com.example.wildsafari.ui.theme

import com.example.wildsafari.Animal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SafariRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getAllAnimals(): List<Animal> {
        return try {
            val snapshot = db.collection("animals").get().await()
            snapshot.documents.map { document ->
                Animal(
                    //id = document.id,
                    name = document.getString("name") ?: "",
                    species = document.getString("species") ?: "",
                    habitat = document.getString("habitat") ?: "",
                    description = document.getString("description") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
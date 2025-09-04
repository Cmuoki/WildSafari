package com.example.wildsafari.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.example.wildsafari.Animal
import com.example.wildsafari.ROUTE_ANIMALS

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class AnimalViewModel(
    private val navController: NavHostController,
    private val context: Context
) {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Animals")

    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dmliltewv/image/upload" // change to your Cloudinary cloud
    private val uploadPreset = "WildSafari" // change to your upload preset

    private val _animals = mutableStateListOf<Animal>()
    val animals: List<Animal> = _animals

    // -------- Upload Animal --------
    fun uploadAnimal(
        imageUri: Uri?,
        name: String,
        species: String,
        description: String,
        habitat: String,
        price: Double
    ) {
        val ref = databaseReference.push()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageUrl = if (imageUri != null) {
                    uploadToCloudinary(context, imageUri)
                } else {
                    ""
                }

                val animalData = mapOf(
                    "id" to ref.key,
                    "name" to name,
                    "species" to species,
                    "description" to description,
                    "habitat" to habitat,
                    "price" to price,
                    "userId" to userId,
                    "imageUrl" to imageUrl
                )

                ref.setValue(animalData).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Animal saved successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(ROUTE_ANIMALS)
                    } else {
                        Toast.makeText(context, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // -------- Fetch Animals --------
    fun allAnimals(
        animal: MutableState<Animal>,
        animals: SnapshotStateList<Animal>
    ): SnapshotStateList<Animal> {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                animals.clear()
                for (snap in snapshot.children) {
                    val retrievedAnimal = snap.getValue(Animal::class.java)
                    if (retrievedAnimal != null) {
                        animal.value = retrievedAnimal
                        animals.add(retrievedAnimal)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database error", Toast.LENGTH_SHORT).show()
            }
        })
        return animals
    }

    // -------- Delete Animal --------
    fun deleteAnimal(animalId: String) {
        databaseReference.child(animalId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Animal deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
            }
    }

    // -------- Update Animal --------
    fun updateAnimal(
        animalId: String,
        name: String,
        species: String,
        description: String,
        habitat: String,
        price: Double,
        imageUri: Uri?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val newImageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = currentUser?.uid ?: ""

                val updates = mutableMapOf<String, Any>(
                    "id" to animalId,
                    "name" to name,
                    "species" to species,
                    "description" to description,
                    "habitat" to habitat,
                    "price" to price,
                    "userId" to userId
                )

                if (!newImageUrl.isNullOrEmpty()) {
                    updates["imageUrl"] = newImageUrl
                }

                val ref = databaseReference.child(animalId)
                ref.updateChildren(updates).await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Animal updated successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_ANIMALS)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // -------- Upload Image to Cloudinary --------
    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes() ?: throw Exception("Image read failed")

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes))
            .addFormDataPart("upload_preset", uploadPreset)
            .build()

        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = OkHttpClient().newCall(request).execute()

        if (!response.isSuccessful) throw Exception("Upload failed")

        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)

        return secureUrl ?: throw Exception("Failed to get image URL")
    }
}



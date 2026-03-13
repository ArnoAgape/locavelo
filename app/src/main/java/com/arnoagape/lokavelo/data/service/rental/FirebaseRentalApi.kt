package com.arnoagape.lokavelo.data.service.rental

import android.content.Context
import com.arnoagape.lokavelo.data.compression.ImageCompressor
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.domain.model.Rental
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseRentalApi @Inject constructor(
    private val compressor: ImageCompressor,
    @param:ApplicationContext private val context: Context
) : RentalApi {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val bikesCollection = firestore.collection("bikes")

    override fun observeOwnerRentals(): Flow<List<Rental>> {

        val userId = auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        return firestore
            .collection("rentals")
            .whereEqualTo("ownerId", userId)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Rental::class.java)?.copy(id = doc.id)
                }
            }
    }
}
package com.arnoagape.lokavelo.data.service.rental

import android.content.Context
import com.arnoagape.lokavelo.data.compression.ImageCompressor
import com.arnoagape.lokavelo.domain.model.Bike
import com.arnoagape.lokavelo.domain.model.Rental
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class FirebaseRentalApi @Inject constructor(
    private val compressor: ImageCompressor,
    @param:ApplicationContext private val context: Context
) : RentalApi {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val bikesCollection = firestore.collection("bikes")

    override fun observeOwnerRentals(): Flow<List<Rental>> {
        return TODO("Provide the return value")
    }
}
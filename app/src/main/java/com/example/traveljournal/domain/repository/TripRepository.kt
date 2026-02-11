package com.example.traveljournal.domain.repository

import com.example.traveljournal.data.local.TripDataStore
import com.example.traveljournal.domain.model.Trip
import com.example.traveljournal.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface TripRepository {
    fun getAllTrips(): Flow<List<Trip>>
    suspend fun addTrip(trip: Trip)
    suspend fun deleteTrip(id: Int)
    fun getWishlistTrips(): Flow<List<Trip>>
    suspend fun toggleWantToReturn(id: Int, wantToReturn: Boolean)
}
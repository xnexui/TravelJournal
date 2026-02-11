package com.example.traveljournal.data.repository

import com.example.traveljournal.data.local.TripDataStore
import com.example.traveljournal.domain.model.Trip
import com.example.traveljournal.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepositoryImpl @Inject constructor(
    private val tripDataStore: TripDataStore
) : TripRepository {

    override fun getAllTrips(): Flow<List<Trip>> = tripDataStore.getTrips()

    override suspend fun addTrip(trip: Trip) = tripDataStore.addTrip(trip)

    override suspend fun deleteTrip(id: Int) = tripDataStore.deleteTrip(id)

    override fun getWishlistTrips(): Flow<List<Trip>> =
        tripDataStore.getTrips().map { trips -> trips.filter { it.wantToReturn } }

    override suspend fun toggleWantToReturn(id: Int, wantToReturn: Boolean) {
        val currentTrips = tripDataStore.getTrips().firstOrNull() ?: emptyList()
        val updatedTrip = currentTrips.find { it.id == id }?.copy(wantToReturn = wantToReturn)
        updatedTrip?.let { tripDataStore.updateTrip(it) }
    }
}
package com.example.traveljournal.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.traveljournal.domain.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "travel_journal")

@Singleton
class TripDataStore @Inject constructor(
    private val context: Context
) {
    private val dataStore = context.dataStore
    private val TRIPS_KEY = stringPreferencesKey("trips_key")

    fun getTrips(): Flow<List<Trip>> = dataStore.data
        .map { preferences ->
            val json = preferences[TRIPS_KEY] ?: return@map defaultTrips()
            try {
                Json.decodeFromString<List<Trip>>(json)
            } catch (e: Exception) {
                defaultTrips()
            }
        }

    private fun defaultTrips(): List<Trip> {
        return listOf(
            Trip(1, "Италия", "Рим", 2023, "с семьей", "Колизей потрясающий!", 9.5f, true),
            Trip(2, "Франция", "Париж", 2022, "с друзьями", "Эйфелева башня ночью", 9.0f, false),
            Trip(3, "Япония", "Токио", 2024, "один", "Сакура и суши", 10.0f, true)
        )
    }

    suspend fun addTrip(trip: Trip) {
        val currentTrips = getTrips().firstOrNull() ?: emptyList()
        val newId = (currentTrips.maxOfOrNull { it.id } ?: 0) + 1
        val newTrip = trip.copy(id = newId)
        val newTrips = currentTrips + newTrip
        saveTrips(newTrips)
    }

    suspend fun deleteTrip(id: Int) {
        val currentTrips = getTrips().firstOrNull() ?: emptyList()
        val newTrips = currentTrips.filter { it.id != id }
        saveTrips(newTrips)
    }

    suspend fun updateTrip(updatedTrip: Trip) {
        val currentTrips = getTrips().firstOrNull() ?: emptyList()
        val newTrips = currentTrips.map {
            if (it.id == updatedTrip.id) updatedTrip else it
        }
        saveTrips(newTrips)
    }

    private suspend fun saveTrips(trips: List<Trip>) {
        val json = Json.encodeToString(trips)
        dataStore.edit { preferences ->
            preferences[TRIPS_KEY] = json
        }
    }
}
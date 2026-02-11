package com.example.traveljournal.presentation.screens.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveljournal.domain.model.Trip
import com.example.traveljournal.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripsViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripsUiState(isLoading = true))
    val uiState: StateFlow<TripsUiState> = _uiState.asStateFlow()

    init {
        loadTrips()
    }

    fun loadTrips() {
        viewModelScope.launch {
            _uiState.value = TripsUiState(isLoading = true)
            try {
                repository.getAllTrips().collectLatest { trips ->
                    _uiState.value = TripsUiState(
                        trips = trips,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = TripsUiState(
                    error = "Ошибка загрузки: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun toggleWantToReturn(trip: Trip) {
        viewModelScope.launch {
            repository.toggleWantToReturn(trip.id, !trip.wantToReturn)
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            repository.deleteTrip(trip.id)
        }
    }
}
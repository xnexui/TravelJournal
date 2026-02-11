package com.example.traveljournal.presentation.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveljournal.domain.model.Trip
import com.example.traveljournal.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTripUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

 @HiltViewModel
class AddTripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel()
{

    private val _uiState = MutableStateFlow(AddTripUiState())
    val uiState: StateFlow<AddTripUiState> = _uiState.asStateFlow()

    fun addTrip(
        country: String,
        city: String,
        year: String,
        companions: String,
        impression: String,
        rating: String
    ) {
        if (country.isBlank() || city.isBlank() || year.isBlank()) {
            _uiState.value = AddTripUiState(error = "Страна, город и год обязательны")
            return
        }

        viewModelScope.launch {
            _uiState.value = AddTripUiState(isLoading = true)
            try {
                val trip = Trip(
                    country = country,
                    city = city,
                    year = year.toIntOrNull() ?: 2024,
                    companions = companions.ifBlank { "один" },
                    impression = impression.ifBlank { "Без описания" },
                    rating = rating.toFloatOrNull() ?: 5.0f,
                    wantToReturn = false
                )
                repository.addTrip(trip)
                _uiState.value = AddTripUiState(isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = AddTripUiState(error = "Ошибка: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = AddTripUiState()
    }
}
package com.example.traveljournal.presentation.screens.trips

import com.example.traveljournal.domain.model.Trip

data class TripsUiState(
    val trips: List<Trip> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
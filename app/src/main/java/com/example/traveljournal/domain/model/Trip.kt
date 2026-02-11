package com.example.traveljournal.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Trip(
    val id: Int = 0,
    val country: String,
    val city: String,
    val year: Int,
    val companions: String,
    val impression: String,
    val rating: Float,
    val wantToReturn: Boolean = false
)
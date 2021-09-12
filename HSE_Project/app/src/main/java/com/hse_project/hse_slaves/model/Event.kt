package com.hse_project.hse_slaves.model

data class Event(
    val id: Int,
    val name: String,
    val description: String,
    val images: List<String>,
    val organizerId: Int,
    val participantsIDs: List<Int>,
    val rating: Double,
    val geoData: String,
    val specialization: String,
    val date: String,
    val likes: List<Int>
)
package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Score(val id: String, val playerId: String, val points: Int)
val scoreStorage = mutableListOf<Score>()
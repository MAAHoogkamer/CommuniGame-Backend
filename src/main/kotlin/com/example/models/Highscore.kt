package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Highscore(val id: String, val name: String, val points: Int)
val highscoreStorage = mutableListOf<Highscore>()
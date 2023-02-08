package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Player(val id: String, val name: String)


// val playerStorage = mutableListOf<Player>()
val playerStorage = mutableListOf(Player("1", "Rosa"))
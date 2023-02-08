package com.staffbase.communigame.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScoreDto(val id: String, val playerId: String, val points: Int)

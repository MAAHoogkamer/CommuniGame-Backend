package com.staffbase.communigame.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScoreCreationDto(val playerId: String, val points: Int)

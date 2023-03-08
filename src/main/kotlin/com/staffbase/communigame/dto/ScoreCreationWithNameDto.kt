package com.staffbase.communigame.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScoreCreationWithNameDto(val name: String, val points: Int)

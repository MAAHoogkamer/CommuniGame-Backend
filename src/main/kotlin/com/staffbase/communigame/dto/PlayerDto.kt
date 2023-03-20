package com.staffbase.communigame.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(val name: String, val topScore: Int)

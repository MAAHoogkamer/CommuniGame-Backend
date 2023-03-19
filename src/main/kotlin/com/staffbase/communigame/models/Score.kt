package com.staffbase.communigame.models

import com.staffbase.communigame.dto.ScoreDto
import kotlinx.serialization.Serializable

@Serializable
data class Score(val id: String, val playerId: String, val points: Int) {
    fun toDto(playerName: String): ScoreDto {
        // prüfe Rollen -> diese Funktion hier in einen Service / andere Klasse auszulagern
        return ScoreDto(id, playerName, playerId, points)
    }
}



val scoreStorage = mutableListOf<Score>()

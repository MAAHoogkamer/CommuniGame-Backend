package com.staffbase.communigame.service

import com.staffbase.communigame.dto.PlayerDto
import com.staffbase.communigame.dto.ScoreDto
import com.staffbase.communigame.models.Player
import com.staffbase.communigame.models.Score
import java.time.Instant

/*
class DataMapper {
    fun dboToDto(dbo: Score, playerName: String): ScoreDto {
        return dbo.toDto(playerName)
    }
}
*/

class DataMapper {
    fun scoreDboToDto(dbo: Score, playerName: String): ScoreDto {
        return ScoreDto(dbo.id, playerName, dbo.playerId, dbo.points)
    }

    fun scoreDtoToDbo(dto: ScoreDto): Score {
        val currentTime = Instant.now().toString() // get current time as a string
        return Score(dto.id, dto.playerId, dto.points, currentTime)
    }

    fun playerDboToDto(dbo: Player): PlayerDto {
        return PlayerDto(dbo.id, dbo.name, dbo.topScore)
    }

    fun playerDtoToDbo(dto: ScoreDto): Score {
        val currentTime = Instant.now().toString() // get current time as a string
        return Score(dto.id, dto.playerId, dto.points, currentTime)
    }
}

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
    private val currentTime = Instant.now().toString() // get current time as a string
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    fun scoreDboToDto(dbo: Score, playerName: String): ScoreDto {
        return ScoreDto(playerName, dbo.playerId, dbo.points)
    }

    fun scoreDtoToDbo(dto: ScoreDto): Score {
        val id = "sc" + List(15) { charPool.random() }.joinToString("")
        return Score(id, dto.playerId, dto.points, currentTime)
    }

    fun playerDboToDto(dbo: Player): PlayerDto {
        return PlayerDto(dbo.name, dbo.topScore)
    }

    fun playerDtoToDbo(dto: PlayerDto): Player {
        val id = "pl" + List(15) { charPool.random() }.joinToString("")
        val topScore = 666; // TODO Call and get top score
        return Player(id, dto.name, currentTime, topScore)
    }

}

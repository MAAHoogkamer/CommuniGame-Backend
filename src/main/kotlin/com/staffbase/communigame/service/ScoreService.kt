package com.staffbase.communigame.service

import com.staffbase.communigame.models.Score
import com.staffbase.communigame.persistence.ScoreDatabase

// Here be the database Interface functions
interface ScoreService {
    suspend fun createNewScore(playerId: String, points: Int): Score
    suspend fun getAllScores(): List<Score>
    suspend fun getScoreById(id: String): Score?
    suspend fun removeById(id: String): Boolean
    suspend fun getScoresByPlayerId(playerId: String): List<Score>
}

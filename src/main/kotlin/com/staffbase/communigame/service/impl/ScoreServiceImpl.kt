package com.staffbase.communigame.service.impl

import com.staffbase.communigame.models.Score
import com.staffbase.communigame.persistence.ScoreDatabase
import com.staffbase.communigame.service.ScoreService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

// Here be the Implementation Interface functions (using the database)
class ScoreServiceImpl(private val scoreDatabase: ScoreDatabase) : ScoreService {

    override suspend fun createNewScore(playerId: String, points: Int): Score {
        return scoreDatabase.createNewScore(playerId, points)
    }

    override suspend fun getAllScores(): List<Score> {
        return scoreDatabase.getAllScores()
    }

    override suspend fun getScoreById(id: String): Score? {
        return scoreDatabase.getScoreById(id)
    }

    override suspend fun removeById(id: String): Boolean {
        return scoreDatabase.removeById(id)
    }

    override suspend fun getScoresByPlayerId(playerId: String): List<Score> {
        return scoreDatabase.getScoresByPlayerId(playerId)
    }
}

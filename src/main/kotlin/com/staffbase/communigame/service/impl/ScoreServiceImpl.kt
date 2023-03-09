package com.staffbase.communigame.service.impl

import com.staffbase.communigame.models.Score
import com.staffbase.communigame.persistence.ScoreDatabase
import com.staffbase.communigame.service.ScoreService
import kotlinx.coroutines.coroutineScope

// Here be the Implementation Interface functions (using the database)
class ScoreServiceImpl : ScoreService {
    var scores = mutableListOf<Score>()

    var indexCounter: Int = 0

    private var ScoreDb = ScoreDatabase();

    override suspend fun createNewScore(playerId: String, points: Int): Score = coroutineScope {
        // not thread safe, ignore for now
        val score = Score((++indexCounter).toString(), playerId, points)
        scores.add(score)
        return@coroutineScope score
    }

    override suspend fun getAllScores(): List<Score> = coroutineScope {
        return@coroutineScope ScoreDb.getAllScores()
    }

    override suspend fun getScoreById(id: String): Score? {
        return scores.find { it.id == id }
    }

    override suspend fun removeById(id: String): Boolean {
        return scores.removeIf { it.id == id }
    }

    override suspend fun getScoresByPlayerId(playerId: String): List<Score> {
        return scores.filter { it.playerId == playerId }
    }
}

package com.staffbase.communigame.service.inmem

import com.staffbase.communigame.models.Score
import com.staffbase.communigame.service.ScoreService
// Here be the In Memory Interface functions (= without using database, testing)
class InMemoryScoreService : ScoreService {
    var scores = mutableListOf<Score>()

    var indexCounter: Int = 0

    override suspend fun createNewScore(playerId: String, points: Int): Score {
        // not thread safe, ignore for now
        val score = Score((++indexCounter).toString(), playerId, points)
        scores.add(score)
        return score
    }

    override suspend fun getAllScores(): List<Score> {
        return scores.toList()
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

package com.staffbase.communigame.service.inmem

import com.staffbase.communigame.models.Score
import com.staffbase.communigame.service.ScoreService
// Here be the In Memory Interface functions (= without using database, testing)
class InMemoryScoreService : ScoreService {
    var scores = mutableListOf<Score>()

    var indexCounter: Int = 0

    override fun createNewScore(name: String, points: Int): Score {
        // not thread safe, ignore for now
        val score = Score((++indexCounter).toString(), name)
        scores.add(score)
        return score
    }

    override fun getAllScores(): List<Score> {
        return scores.toList()
    }

    override fun getScoreById(id: String): Score? {
        return scores.find { it.id == id }
    }

    override fun removeById(id: String): Boolean {
        return scores.removeIf { it.id == id }
    }

    override fun getScoresByPlayerId(playerId: String): List<Score> {
        return scores.filter { it.playerId == playerId }
    }
}

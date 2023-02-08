package com.staffbase.communigame.service

import com.staffbase.communigame.models.Score

interface ScoreService {
    fun createNewScore(playerId: String, points: Int): Score
    fun getAllScores(): List<Score>
    fun getScoreById(id: String): Score?
    fun removeById(id: String): Boolean
}

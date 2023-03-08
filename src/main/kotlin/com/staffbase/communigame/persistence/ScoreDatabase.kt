package com.staffbase.communigame.persistence

import com.staffbase.communigame.models.Score
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class ScoreDatabase(dbName: String) {
    private val client = KMongo.createClient().coroutine // create client connection
    private val database = client.getDatabase(dbName) // reference to the database
    private val collection = database.getCollection<Score>("scores") // reference to the scores collection

    suspend fun getAllScores(): List<Score> {
        return collection.find().toList()
    }

    suspend fun getScoreById(id: String): Score? {
        return collection.findOneById(id)
    }

    suspend fun addScore(score: Score) {
        collection.insertOne(score)
    }

    suspend fun updateScore(score: Score) {
        collection.replaceOneById(score.id, score)
    }

    suspend fun deleteScoreById(id: String) {
        collection.deleteOneById(id)
    }
}


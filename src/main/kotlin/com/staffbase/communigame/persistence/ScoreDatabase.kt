package com.staffbase.communigame.persistence

import com.staffbase.communigame.models.Score
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

class ScoreDatabase {

    private val client = KMongo.createClient().coroutine // create client connection
    private val database = client.getDatabase("communigame") // reference to the database
    private val collection = database.getCollection<Score>("scores") // reference to the scores collection

    suspend fun createNewScore(playerId: String, points: Int): Score {
        val score = Score(ObjectId().toString(), playerId, points)
        collection.insertOne(score)
        return score
    }

    suspend fun getAllScores(): List<Score> {
        return collection.find().toList()
    }

    suspend fun getScoreById(id: String): Score? {
        return collection.findOneById(id)
    }

    suspend fun removeById(id: String): Boolean {
        val result = collection.deleteOneById(id)
        return result.deletedCount == 1L
    }

    suspend fun getScoresByPlayerId(playerId: String): List<Score> {
        return collection.find(Score::playerId eq playerId).toList()
    }
}
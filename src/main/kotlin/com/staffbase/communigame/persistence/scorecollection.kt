package com.staffbase.communigame.persistence

import com.staffbase.communigame.models.Player
import com.staffbase.communigame.models.Score
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.reactivestreams.KMongo

class Database(collectionName: String) {
    private val client = KMongo.createClient().coroutine // create a client connection
    private val database = client.getDatabase("my_database") // get a reference to the database
    private val collection = when(collectionName) {
        "players" -> database.getCollection<Player>("players") // get a reference to the players collection
        "scores" -> database.getCollection<Score>("scores") // get a reference to the scores collection
        else -> throw IllegalArgumentException("Invalid collection name: $collectionName")
    }

    suspend fun insertOne(score: Score) {
        collection.insertOne(score)
    }

    suspend fun updateOne(score: Score) {
        val filter = Score::id eq score.id
        collection.replaceOne(filter, score)
    }

    suspend fun deleteOne(score: Score) {
        val filter = Score::id eq score.id
        collection.deleteOne(filter)
    }

    suspend fun findByPlayer(playerName: String): List<Score> {
        val filter = Score::playerName eq playerName
        return collection.find(filter).toList()
    }

    // Add more methods as needed...
}

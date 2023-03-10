package com.staffbase.communigame.persistence

import com.staffbase.communigame.models.Player
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class PlayerDatabase() {
    private val client = KMongo.createClient().coroutine // create a client connection
    private val database = client.getDatabase("communigame") // get a reference to the database
    private val collection = database.getCollection<Player>("players") // get a reference to the players collection

    suspend fun createNewPlayer(name: String): Player?
    suspend fun getAllPlayers(): List<Player> {
        return collection.find().toList()
    }
    suspend fun getPlayerById(id: String): Player? {
        return collection.findOneById(id)
    }
    suspend fun removeById(id: String): Boolean
    suspend fun getPlayerIdByName(name: String): String?
    suspend fun getPlayerNameById(playerId: String): String?
    suspend fun createNewPlayerReturnId(name: String): String?


    suspend fun addPlayer(player: Player) {
        collection.insertOne(player)
    }

    suspend fun updatePlayer(player: Player) {
        collection.replaceOneById(player.id, player)
    }

    suspend fun deletePlayerById(id: String) {
        collection.deleteOneById(id)
    }
}
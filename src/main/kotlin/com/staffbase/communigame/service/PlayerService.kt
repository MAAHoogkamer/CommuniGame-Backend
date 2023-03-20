package com.staffbase.communigame.service

import com.staffbase.communigame.models.Player
// Here be the database Interface functions
interface PlayerService {
    suspend fun createNewPlayer(name: String): Player?
    suspend fun getAllPlayers(): List<Player>
    suspend fun getPlayerById(id: String): Player?
    suspend fun removeById(id: String): Boolean
    suspend fun getPlayerIdByName(name: String): String?
    // Get single name using an id:
    suspend fun getPlayerNameById(playerId: String): String?
    // Get a list of names using ids:
    suspend fun getPlayerNamesListById(playerId: List<String>): String?
    suspend fun createNewPlayerReturnId(name: String): String?
}
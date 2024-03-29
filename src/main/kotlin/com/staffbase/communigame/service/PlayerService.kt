package com.staffbase.communigame.service

import com.staffbase.communigame.models.Player
// Here be the database Interface functions
interface PlayerService {
    fun createNewPlayer(name: String): Player?
    fun getAllPlayers(): List<Player>
    fun getPlayerById(id: String): Player?
    fun removeById(id: String): Boolean
    fun getPlayerIdByName(name: String): String?
    fun getPlayerNameById(playerId: String): String?
    fun createNewPlayerReturnId(name: String): String?
}
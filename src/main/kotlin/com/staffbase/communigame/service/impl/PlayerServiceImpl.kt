package com.staffbase.communigame.service.impl

import com.staffbase.communigame.models.Player
import com.staffbase.communigame.models.Score
import com.staffbase.communigame.persistence.PlayerDatabase
import com.staffbase.communigame.persistence.ScoreDatabase
import com.staffbase.communigame.service.PlayerService
import com.staffbase.communigame.service.ScoreService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

// Here be the Implementation Interface functions (using the database)
class PlayerServiceImpl(private val playerDatabase: PlayerDatabase) : PlayerService {

    override suspend fun createNewPlayer(name: String): Player? {
        return playerDatabase.createNewPlayer(name)
    }

    override suspend fun getAllPlayers(): List<Player> {
        return playerDatabase.getAllPlayers()
    }

    override suspend fun getPlayerById(id: String): Player? {
        return playerDatabase.getPlayerById(id)
    }

    override suspend fun removeById(id: String): Boolean {
        return playerDatabase.removeById(id)
    }

    override suspend fun getPlayerIdByName(name: String): String? {
        return playerDatabase.getPlayerIdByName(name)
    }

    override suspend fun getPlayerNameById(playerId: String): String? {
        return playerDatabase.getPlayerNameById(playerId)
    }

    override suspend fun createNewPlayerReturnId(name: String): String? {
        return playerDatabase.createNewPlayerReturnId(name)
    }
}

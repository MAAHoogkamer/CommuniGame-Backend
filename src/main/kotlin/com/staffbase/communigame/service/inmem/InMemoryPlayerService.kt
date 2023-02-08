package com.staffbase.communigame.service.inmem

import com.staffbase.communigame.models.Player
import com.staffbase.communigame.service.PlayerService

class InMemoryPlayerService : PlayerService {
    var players = mutableListOf<Player>()

    var indexCounter: Int = 0

    override fun createNewPlayer(name: String): Player {
        // not thread safe, ignore for now
        val player = Player((++indexCounter).toString(), name)
        players.add(player)
        return player
    }

    override fun getAllPlayers(): List<Player> {
        return players.toList()
    }

    override fun getPlayerById(id: String): Player? {
        return players.find { it.id == id }
    }

    override fun removeById(id: String): Boolean {
        return players.removeIf { it.id == id }
    }
}

package com.staffbase.communigame.service.inmem

import com.staffbase.communigame.models.Player
import com.staffbase.communigame.service.PlayerService
// Here be the In Memory Interface functions (= without using database, testing)
class InMemoryPlayerService : PlayerService {
    var players = mutableListOf<Player>()

    var indexCounter: Int = 0

    override fun createNewPlayer(name: String): Player? {
        // not thread safe, ignore for now
        if (players.any { it.name == name }) {
            // If player with that name already exists:
            return null
        }
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

    override fun getPlayerIdByName(name: String): String? {
        // String? is the return type. It can return a String or a null value.
        // If the nme is found, the id is returned. Otherwise null is returned.
        return players.find { it.name == name }?.id
        /* ?.id: If find returns a non-null Player object, the safe call operator (?.)
        is used to access its id property. If the Player object is null, the entire expression evaluates to null
         */
    }
    override fun getPlayerNameById(playerId: String): String? {
        return players.find { it.id == playerId }?.name
    }
}

package com.staffbase.communigame.persistence

import com.staffbase.communigame.models.Player
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

/**
 * DBO = Data Base Object (DB => MongoDB oder Postgres....)
 * DTO = Data Transfer Object (API)
 *
 * [API Layer] - [Kotlin] - [DBO] - [Database]
 * DTO                      DBO
 *
 * Warum?
 * - Use Case 1 (Zensur): User Klasse: FirstName, LastName (nicht sensitiv - DBO, DTO). Kostenstelle/Gehalt (DBO, ggf. nicht DTO)
 *   - Bedingung 1: Abfrage kommt von einem nicht priviligierten Nutzer (normaler User) -> der darf Gehalt nicht einsehen
 *      - aber, ein Admin/SuperUser darf ALLE Attribute abfragen, inkl. Kostenstelle/Gehalt
 *   - Request 1: von User - User darf Kostenstelle NICHT sehen -> Feld wir leer gelassen, weggelassen oder "" ... (HTTP GET Request -> { "name", ...  }
 *   - Request 2: von Admin - darf alles sehen -> Feld ist mit drin (HTTP GET Request -> { "name", ... , "kostenstelle" }
 *   - Hintergrund: Authorization - Rollen, Rechte
 *
 * - Use Case 2: Data Proccessing (Transformation)
 *  - Score(id, playerId, value) -> DBO
 *  - ein API-Konsument (HTTP Request) will die Information in einer UI darstellen / oder einfach den Namen kennen
 *  - ScoreDto(id, playerId, playerName*, value) -> DTO
 *  - Transformation: playerId -> playerName umwandeln und an DTO anfügen
 *  - Hintergrund: Convinience, Enrichement of Data (statt 2 API calls, 1) - (statt 10, 1 - top10)
 *
 * Wie?
 * - Klasse Score. funktion dbo.toDto(), dto.toDbo()
 *  - data class Score() - die ist allgemein
 * - versch. DTOs - aufwändig
 */

class PlayerDatabase() {
    private val client = KMongo.createClient().coroutine // create a client connection
    private val database = client.getDatabase("communigame") // get a reference to the database
    private val collection = database.getCollection<Player>("players") // get a reference to the players collection

    suspend fun createNewPlayer(name: String): Player? = null // TODO:
    suspend fun getAllPlayers(): List<Player> {
        return collection.find().toList()
    }
    suspend fun getPlayerById(id: String): Player? {
        return collection.findOneById(id)
    }
    suspend fun removeById(id: String): Boolean = false
    suspend fun getPlayerIdByName(name: String): String = ""
    suspend fun getPlayerNameById(playerId: String): String = "null"
    suspend fun createNewPlayerReturnId(name: String): String = "null"


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
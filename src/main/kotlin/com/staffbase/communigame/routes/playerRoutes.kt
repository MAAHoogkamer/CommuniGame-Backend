package com.staffbase.communigame.routes

import com.staffbase.communigame.dto.PlayerCreationDto
import com.staffbase.communigame.dto.PlayerDto
import com.staffbase.communigame.dto.ScoreDto
import com.staffbase.communigame.service.PlayerService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

// API
fun Route.playerRouting(playerService: PlayerService) {
    route("/players") {
        get {
            call.respond(playerService.getAllPlayers().map {
                PlayerDto(it.id, it.name)
            })
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            /*
            In this context the "?:" operator is used as a shorthand for an "if-else" statement,
            where the expression to the left of "?:" is evaluated, and if it is not null, it is returned;
            otherwise, the expression to the right of ":" is executed
            (in this case, "return@get call.respondText(...)"). In Kotlin, it is called the Elvis operator.
             */
            val player =
                playerService.getPlayerById(id) ?: return@get call.respondText(
                    "No player with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(PlayerDto(player.id, player.name, 42))
        }
        post {
            val player = call.receive<PlayerCreationDto>()
            val created = playerService.createNewPlayer(player.name)
            call.respond(status = HttpStatusCode.Created, message = PlayerDto(created.id, created.name))
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (playerService.removeById(id)) {
                call.respondText("Player removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
        // players/byplayer/{name}
        // For getting all scores tied to a player id
        get("/byplayer/{name}") {
            val name = call.parameters["name"] ?: return@get call.respondText(
                "Missing player name",
                status = HttpStatusCode.BadRequest
            )
            val playerId = playerService.getPlayerById()
                /*
            call.respond(scoreService.getScoresByPlayerId(playerId).map {
                ScoreDto(it.id, it.playerId, it.points)
                 */
            })
        }
    }
}

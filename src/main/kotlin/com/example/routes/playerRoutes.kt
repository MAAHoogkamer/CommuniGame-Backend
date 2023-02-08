package com.example.routes

import com.example.models.* // Imports everything in models, doesn't work in Staffbase backend. Use .player
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.playerRouting() {
    route("/players") {
        get {
            if (playerStorage.isNotEmpty()) {
                call.respond(playerStorage)
            } else {
                call.respondText("No players found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
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
                playerStorage.find { it.id == id } ?: return@get call.respondText(
                    "No player with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(player)
        }
        post {
            val player = call.receive<Player>()
            if(player.id != null) {
                call.respondText("Can't transfer ID", status = HttpStatusCode.BadRequest)
            }
            playerStorage.add(player)
            call.respondText("Player stored correctly", status = HttpStatusCode.Created)
            // call.respondText can probably be removed
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (playerStorage.removeIf { it.id == id }) {
                call.respondText("Player removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
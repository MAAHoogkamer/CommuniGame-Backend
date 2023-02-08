package com.staffbase.communigame.routes

import com.staffbase.communigame.models.Score
import com.staffbase.communigame.models.scoreStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.scoreRouting() {
    route("/scores") {
        get {
            if (scoreStorage.isNotEmpty()) {
                call.respond(scoreStorage)
            } else {
                call.respondText("No scores found", status = HttpStatusCode.OK)
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
            val score =
                scoreStorage.find { it.id == id } ?: return@get call.respondText(
                    "No score with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(score)
        }
        post {
            val score = call.receive<Score>()
            scoreStorage.add(score)
            call.respondText("Highscore stored correctly", status = HttpStatusCode.Created)
            // call.respondText can probably be removed
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (scoreStorage.removeIf { it.id == id }) {
                call.respondText("Highscore removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}

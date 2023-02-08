package com.staffbase.communigame.routes

import com.staffbase.communigame.dto.ScoreCreationDto
import com.staffbase.communigame.dto.ScoreDto
import com.staffbase.communigame.models.Score
import com.staffbase.communigame.models.scoreStorage
import com.staffbase.communigame.service.ScoreService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.scoreRouting(scoreService: ScoreService) {
    route("/scores") {
        get {
            call.respond(scoreService.getAllScores().map {
                ScoreDto(it.id, it.playerId, it.points)
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
            val score =
                scoreService.getScoreById(id) ?: return@get call.respondText(
                    "No score with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(ScoreDto(score.id, score.playerId, score.points))
        }
        post {
            val score = call.receive<ScoreCreationDto>()
            val created = scoreService.createNewScore(score.playerId, score.points)
            call.respond(status = HttpStatusCode.Created,
                message = ScoreDto(created.id, created.playerId, created.points))
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (scoreService.removeById(id)) {
                call.respondText("Score removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}

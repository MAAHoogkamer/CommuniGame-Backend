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
        // scores/byplayer/{id}
        // For getting all scores tied to a player id
        get("/byplayer/{id}") {
            val playerId = call.parameters["id"] ?: return@get call.respondText(
                "Missing player id",
                status = HttpStatusCode.BadRequest
            )
            call.respond(scoreService.getScoresByPlayerId(playerId).map {
                ScoreDto(it.id, it.playerId, it.points)
            })
        }
        // Get the 20 best scores
        get("/top20") {
            call.respond(scoreService.getAllScores().sortedByDescending { it.points }.take(20).map {
                ScoreDto(it.id, it.playerId, it.points)
            })
        }
        /*
        'scoreService.getAllScores()' uses the existing function to retrieve all the scores from the scoreService.
        'sortedByDescending { it.points }' sorts the list in descending order, based on points.
        'it.points' refers to points property of each individual Score in the list.
        'take(20)' takes the first 20 items from the list.
        'map { ScoreDto(it.id, it.playerId, it.points) }' transforms(/maps) each Score in the list into a ScoreDto.
        'it.id' 'it.playerId' 'it.points' refer to id playerId & points properties of each Score.
        'call.respond' is called with the list of ScoreDtos as an argument.
            This function sends this list of ScoreDtos.
         */
    }
}

package com.staffbase.communigame.routes

import com.staffbase.communigame.dto.ScoreCreationDto
import com.staffbase.communigame.dto.ScoreCreationWithNameDto
import com.staffbase.communigame.dto.ScoreDto
import com.staffbase.communigame.service.DataMapper
import com.staffbase.communigame.service.PlayerService
import com.staffbase.communigame.service.ScoreService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.MongoOperator

fun Route.scoreRouting(scoreService: ScoreService, playerService: PlayerService) {
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
        /*
        'get("/byplayer/{id}")' = Endpoint definition.
        'val playerId = call.parameters["id"]' variable value = gets id value from the URL path.
        'call.parameters' map contains all of the URL parameters and query parameters in the incoming HTTP request.
        '?: return@get call.respondText(...)' handles the case where the id parameter is missing.
        'call.respond(scoreService.getScoresByPlayerId(playerId).map { ScoreDto(it.id, it.playerId, it.points) })'
        calls the getScoresByPlayerId of scoreService, retrieves all scores associated with the playerid.
        The returned list of Score objects is then transformed(/mapped) into a list of ScoreDto objects,
         and using call.respond serializes the data (in JSON) and sends it back as response to the HTTP request.
         */
        // Get the 10 best scores
        get("/top10") {
            call.respond(scoreService.getAllScores().sortedByDescending { it.points }.take(10).map {
                ScoreDto(it.id, playerService.getPlayerNameById(it.playerId) ?: "Unknown", it.points)
            })
        }

        /*
        'sortedByDescending { it.points }' sorts the list in descending order, based on points.
        'it.points' refers to points property of each individual Score in the list.
        'take(20)' takes the first 20 items from the list.
        'map { ScoreDto(it.id, it.playerId, it.points) }' transforms(/maps) each Score in the list into a ScoreDto.
        'it.id' 'it.points' refer to id & points properties of each Score.
        'playerService.getPlayerNameById(it.playerId) ?: "Unknown"' uses this method to get the name instead of the
        playerId
        'call.respond' is called with the list of ScoreDtos as an argument.
            This function sends this list of ScoreDtos.
         */
        post("/nameandscore/") {
            val (name, points) = call.receive<ScoreCreationWithNameDto>()
            val playerId = playerService.getPlayerIdByName(name) ?: playerService.createNewPlayerReturnId(name) ?: ""
            val created = scoreService.createNewScore(playerId, points)
            call.respond(status = HttpStatusCode.Created, message =
            ScoreDto(created.id, created.playerId, created.points))
        }
        /*
        First val contains name and points received from the call
        Second val looks up playerId corresponding to the name
        Third val creates a new score using the playerId and points
        The call.respond gives the created score back
        ...
        newplayer is basically the same, except the second variable creates a new player with the given name
        if it isn't already taken, and the id given  to the name is extracted and used for the score creation
         */
    }
}

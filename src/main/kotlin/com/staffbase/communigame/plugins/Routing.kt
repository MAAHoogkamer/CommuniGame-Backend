package com.staffbase.communigame.plugins

import com.staffbase.communigame.routes.playerRouting
import com.staffbase.communigame.routes.scoreRouting
import com.staffbase.communigame.service.inmem.InMemoryPlayerService
import com.staffbase.communigame.service.inmem.InMemoryScoreService
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(playerService: InMemoryPlayerService, scoreService: InMemoryScoreService) {
    routing {
        scoreRouting(scoreService, playerService)
        playerRouting(playerService, scoreService)
    }
}

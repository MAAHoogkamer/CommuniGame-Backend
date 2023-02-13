package com.staffbase.communigame

import io.ktor.server.application.*
import com.staffbase.communigame.plugins.configureRouting
import com.staffbase.communigame.plugins.configureSerialization
import com.staffbase.communigame.service.inmem.InMemoryPlayerService
import com.staffbase.communigame.service.inmem.InMemoryScoreService

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val playerService = InMemoryPlayerService()
    val scoreService = InMemoryScoreService()
    // add test data
    playerService.createNewPlayer("Rosa")
    scoreService.createNewScore("1", 512)
    scoreService.createNewScore("1", 336)
    scoreService.createNewScore("1", 451)
    playerService.createNewPlayer("Karl")
    scoreService.createNewScore("2", 1043)
    scoreService.createNewScore("2", 732)
    configureSerialization()

    configureRouting(playerService, scoreService)
}

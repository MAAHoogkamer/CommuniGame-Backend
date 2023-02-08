package com.staffbase.communigame

import io.ktor.server.application.*
import com.staffbase.communigame.plugins.configureRouting
import com.staffbase.communigame.plugins.configureSerialization
import com.staffbase.communigame.service.inmem.InMemoryPlayerService

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val playerService = InMemoryPlayerService()
    // add test data
    playerService.createNewPlayer("Rosa")
    configureSerialization()

    configureRouting(playerService)
}

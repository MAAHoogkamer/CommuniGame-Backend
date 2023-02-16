package com.staffbase.communigame

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import com.staffbase.communigame.plugins.*
import com.staffbase.communigame.plugins.configureRouting
import com.staffbase.communigame.service.inmem.InMemoryPlayerService
import com.staffbase.communigame.service.inmem.InMemoryScoreService

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            val playerService = InMemoryPlayerService()
            val scoreService = InMemoryScoreService()
            configureRouting(playerService, scoreService)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}

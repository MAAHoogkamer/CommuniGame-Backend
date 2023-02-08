package com.staffbase.communigame

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import com.example.plugins.*
import com.staffbase.communigame.plugins.configureRouting

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting(playerService)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}

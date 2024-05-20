package gr.optima

import gr.optima.data.UserSession
import gr.optima.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.sessions.*
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Sessions){
        header<UserSession>("X-SESSION", directorySessionStorage(File("build/.session")))
    }

    configureMonitoring()
    configureSerialization()
    configureRouting()
}

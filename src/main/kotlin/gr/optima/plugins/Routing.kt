package gr.optima.plugins

import gr.optima.data.UserSession
import gr.optima.json.HelloPostBodyRequest
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureRouting() {
    install(Authentication) {
        bearer("auth-bearer") {
            realm = "DB for Auth"
            authenticate { bearerTokenCredential ->
                return@authenticate if (bearerTokenCredential.token == "123456789") {
                    UserIdPrincipal("valid")
                } else {
                    null
                }
            }
        }
        session<UserSession>("auth-session") {
            validate { session ->
                if (session.id.startsWith("J")) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/")
            }
        }
    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/") {
            val data = call.receive<HelloPostBodyRequest>()
            call.respondText("Hello World from a post request!!!".plus("\n\n").plus(data))
        }
        get("/dev/vassilis") {
            call.respondText("Hello World!")
        }
        route("/api") {
            get("/") {
                call.respond(mapOf("hello" to "from get"))
            }
            post("/") {
                call.respond(mapOf("hello" to "from post", "name" to "server"))
            }
            put("/") {
                call.respond(mapOf("hello" to "from put", "name" to "server"))
            }
            delete("/") {
                call.respond(mapOf("hello" to "from delete"))
            }
            route("/custom") {
                authenticate("auth-bearer") {
                    get("/{customerId}") {
                        val name = call.parameters["customerId"]
                        val queryParameters = call.request.queryParameters["search"]
                        println(queryParameters)
                        call.respond(mapOf("hello" to name))
                    }
                }
                authenticate("auth-session") {
                    get("/") {
                        val user = call.principal<UserSession>()
                        call.sessions.set(user)
                        call.respond(mapOf("hello" to "from get"))
                    }
                }

                post("/") {
                    call.respond(mapOf("hello" to "from post", "name" to "server"))
                }
                put("/") {
                    call.respond(mapOf("hello" to "from put", "name" to "server"))
                }
                delete("/") {
                    call.respond(mapOf("hello" to "from delete"))
                }
            }
        }
    }
}

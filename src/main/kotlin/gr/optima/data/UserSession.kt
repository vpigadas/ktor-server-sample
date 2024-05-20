package gr.optima.data

import io.ktor.server.auth.*

data class UserSession(
    val id: String
) : Principal
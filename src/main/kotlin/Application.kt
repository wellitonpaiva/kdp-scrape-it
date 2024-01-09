package scrapeit

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.flywaydb.core.Flyway
import scrapeit.plugins.configureRouting

val books = listOf(Book("A Paixao do Visconde", "https://a.co/d/25b4nSh"))

val positions = books.map { BookPositions.getRanking(it) }

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }
    val url = "jdbc:postgresql://localhost:5432/postgres"
    val user = "postgres"
    val password = ""

    val flyway = Flyway.configure().dataSource(url, user, password).load()
    flyway.migrate()

    configureRouting(positions)
}

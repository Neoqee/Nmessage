import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import java.time.Duration
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

fun main(args: Array<String>) {

    val sessionMap = HashMap<String, WebSocketSession>()

    embeddedServer(Netty, 8080) {
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(5)
        }
        routing {
            webSocket("/ws") {
                val host = call.request.local.remoteHost
                if (sessionMap[host] == null) {
                    sessionMap[host] = this
                }
                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val msg = frame.readText()
                            println("${Date()}-$host-$msg")
                            send("${Date()}-$host-$msg")
                            sessionMap.forEach { (h, session) ->
                                if (h != host) {
                                    GlobalScope.launch(Dispatchers.IO) { sendMsg(session, msg) }
//                                    if (session.isActive) {
//
//                                    } else {
//                                        sessionMap.remove(h)
//                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    sessionMap.remove(host)
                }
            }
        }
    }.start()

}

suspend fun sendMsg(session: WebSocketSession, msg: String) {
    println("send -> $msg")
    session.send(msg)
}
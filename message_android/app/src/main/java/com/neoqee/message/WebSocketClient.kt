package com.neoqee.message

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object WebSocketClient {

    private val ktorClient = HttpClient(OkHttp) {
        install(WebSockets) {
            pingInterval = 5L
        }
    }

    private var session: WebSocketSession? = null

    fun start(onRecv: (text: String) -> Unit) {
        Thread{
            runBlocking(Dispatchers.IO) {
                ktorClient.ws("ws://192.168.1.3:8080/ws") {
                    session = this
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val msg = frame.readText()
                            onRecv(msg)
                        }
                    }
                }
            }
        }.start()
    }

    fun send(msg: String) {
        GlobalScope.launch(Dispatchers.IO) {
            session?.send(msg)
        }
    }

}
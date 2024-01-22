package com.gravitycode.solitaryfitness.util.net

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

/**
 * TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
 *
 * @return `true` if this device is connected to the internet, otherwise `false`
 *
 * See: [https://stackoverflow.com/questions/1560788/]
 * */
suspend fun isOnline(timeout: Int): Boolean {
    return try {
        Socket().use { sock ->
            withContext(Dispatchers.IO) {
                val socketAddress = InetSocketAddress("8.8.8.8", 53)
                sock.connect(socketAddress, timeout)
            }
            true
        }
    } catch (t: Throwable) {
        false
    }
}
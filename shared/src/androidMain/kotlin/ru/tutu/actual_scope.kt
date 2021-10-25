package ru.tutu

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.*

actual internal inline fun getAppScope():CoroutineScope = MainScope()
actual val ktorClient: HttpClient = HttpClient(OkHttp)

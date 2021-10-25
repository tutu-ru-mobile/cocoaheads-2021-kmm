package ru.tutu

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.MainScope

actual internal inline fun getAppScope() = MainScope()
actual val ktorClient: HttpClient = HttpClient(CIO)

package ru.tutu

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.tutu.serialization.ClientSideEffect
import kotlin.random.Random
import kotlin.random.nextUInt

@Composable
fun ServerDrivenView(
    userId: String,
    networkReducerUrl: String,
    autoUpdate: Boolean,
    sideEffectHandler: (ClientSideEffect) -> Unit
) {
    val store = remember { createServerDrivenViewStore(
        userId,
        networkReducerUrl = networkReducerUrl,
        autoUpdate = autoUpdate
    ) {
        sideEffectHandler(it)
    } }
    val globalState = store.stateFlow.collectAsState()
    val screen = globalState.value.screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        when(screen) {
            is ServerDrivenViewState.ServerDrivenViewScreen.Loading -> {
                CircularProgressIndicator(strokeWidth = 8.dp)
            }
            is ServerDrivenViewState.ServerDrivenViewScreen.Normal -> {
                val clientStorage = globalState.value.clientStorage
                RenderNode(clientStorage, screen.node) {
                    store.send(it)
                }
            }
            is ServerDrivenViewState.ServerDrivenViewScreen.NetworkError -> {
                Text("Сетевая ошибка:")
                Text(screen.exception)
            }
        }
    }
}

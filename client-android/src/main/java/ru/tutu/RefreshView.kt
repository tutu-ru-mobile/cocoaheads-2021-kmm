package ru.tutu

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import kotlin.random.nextUInt

@Composable
fun RefreshView(userId:String, networkReducerUrl:String, autoUpdate:Boolean) {
    val store = remember { createRefreshViewStore(
        userId,
        networkReducerUrl = networkReducerUrl,
        autoUpdate = autoUpdate
    ) {
        //todo handle side effects
    } }
    val globalState = store.stateFlow.collectAsState()
    val screen = globalState.value.screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        when(screen) {
            is RefreshViewState.RefreshViewScreen.Loading -> {
                CircularProgressIndicator(strokeWidth = 8.dp)
            }
            is RefreshViewState.RefreshViewScreen.Normal -> {
                val clientStorage = globalState.value.clientStorage
                RenderNode(clientStorage, screen.node) {
                    store.send(it)
                }
            }
            is RefreshViewState.RefreshViewScreen.NetworkError -> {
                Text("Сетевая ошибка:")
                Text(screen.exception)
            }
        }
    }
}

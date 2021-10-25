package ru.tutu

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RefreshView() {
    val store = remember { createRefreshViewStore() }
    val globalState = store.stateFlow.collectAsState()
    val serverData = globalState.value.serverData
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        when(serverData) {
            is RefreshViewState.ServerData.Loading -> {
                CircularProgressIndicator(strokeWidth = 8.dp)
            }
            is RefreshViewState.ServerData.Loaded -> {
                val clientStorage = globalState.value.clientStorage
                RenderNode(clientStorage, serverData.node) {
                    store.send(it)
                }
            }
        }
    }
}
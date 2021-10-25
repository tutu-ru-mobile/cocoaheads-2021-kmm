package ru.tutu

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import example.imageviewer.view.RefreshView

suspend fun main() {
    val serverClosable = startServer()
    application {
        Window(
            onCloseRequest = {
                serverClosable.close()
                exitApplication()
            },
            title = "Refresh view",
            state = WindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                size = getPreferredWindowSize(800, 1000)
            ),
        ) {
            MaterialTheme {
                RefreshView()
            }
        }
    }
}

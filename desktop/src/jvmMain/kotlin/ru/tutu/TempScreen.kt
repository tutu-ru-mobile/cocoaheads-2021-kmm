/*
 * Copyright 2020-2021 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */

package example.imageviewer.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import example.imageviewer.*
import kotlinx.coroutines.launch
import ru.tutu.*

//fun createRefreshViewStore(): Store<RefreshViewState, ClientIntent> {
//    val result = createStore(
//        RefreshViewState(
//            clientStorage = emptyMap()
//        )
//    ) {s, a: ClientIntent ->
//        val serverData = s.serverData
//        when (serverData) {
//            is RefreshViewState.ServerData.Loaded -> {
//                when (a) {
//                    is ClientIntent.UpdateClientStorage -> {
//                        s.copy(
//                            clientStorage = s.clientStorage.toMutableMap().also {
//                                it[a.key] = a.value
//                            }
//                        )
//                        //todo изменения отправлять на сервер
//                    }
//                    is ClientIntent.SendToServer -> {
//                        val reducedNode: Node = networkReducer(serverData.sessionId, s.clientStorage, a.intent)
//                        s.copy(
//                            serverData = serverData.copy(node = reducedNode)
//                        )
//                    }
//                    else -> throw Error("unpredictable state")
//                }
//            }
//            is RefreshViewState.ServerData.Loading -> {
//                when (a) {
//                    is ClientIntent.FirstServerResponse -> {
//                        s.copy(
//                            serverData = RefreshViewState.ServerData.Loaded(
//                                sessionId = a.sessionId,
//                                node = a.node
//                            )
//                        )
//                    }
//                    else -> throw Error("unpredictable state")
//                }
//            }
//        }
//    }
//    APP_SCOPE.launch {
//        val firstResponse = getFirstState("my UID", result.state.clientStorage)
//        result.send(ClientIntent.FirstServerResponse(firstResponse.reducerResult.state, firstResponse.sessionId))
//    }
//    return result
//}

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

@Composable
fun NetworkImage(imageUrl: String, width: Int, height: Int) {
    var mutableImage by remember { mutableStateOf<ImageBitmap?>(null) }
    remember {
        APP_SCOPE.launch {
            mutableImage = downloadImageBitmap(imageUrl)
        }
    }
    val image = mutableImage
    if(image != null) {
        Image(
            BitmapPainter(image),
            contentDescription = null,
            modifier = Modifier.size(width.dp, height.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(modifier = Modifier.size(width.dp, height.dp).background(color = Color.LightGray),
            contentAlignment = Alignment.Center) {
            Text("Loading image...")
        }
    }
}

@Composable
fun RenderNode(clientStorage:Map<String, ClientValue>, node: Node, sendIntent: (ClientIntent) -> Unit) {
    when (node) {
        is Node.Container.H -> {
            Row {
                for (child in node.children) {
                    RenderNode(clientStorage, child, sendIntent)
                }
            }
        }
        is Node.Container.V -> {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                for (child in node.children) {
                    RenderNode(clientStorage, child, sendIntent)
                }
            }
        }
        is Node.Leaf.Rectangle -> {
            Box(modifier = Modifier.size(node.width.dp, node.height.dp).background(color = Color(node.color.toInt())))
        }
        is Node.Leaf.Label -> {
            Text(text = node.text)
        }
        is Node.Leaf.Button -> {
            Button(onClick = {
                sendIntent(ClientIntent.SendToServer(Intent.ButtonPressed(node.id)))
            }) {
                Text(text = node.text)
            }
        }
        is Node.Leaf.Input -> {
            val text = clientStorage[node.storageKey]?.stringValue ?: ""
            TextField(text, onValueChange = {
                sendIntent(ClientIntent.UpdateClientStorage(node.storageKey, ClientValue(it)))
            })
        }
        is Node.Leaf.Image -> {
            NetworkImage(node.imgUrl, node.width, node.height)
        }
    }.also { }
}

//val ktorClient:HttpClient = HttpClient(CIO)
//val SERVER_URL = "http://localhost:8081"
//
//suspend fun getFirstState(userId:String, clientStorage: Map<String, ClientValue>): FirstResponse {
//    return ktorClient.post<String>("$SERVER_URL/${SERVER_PATH_FIRST_REQUEST}"){
//        body = TextContent(FirstRequestBody(userId, clientStorage).toJson(), ContentType.Application.Json)
//    }.parseToFirstResponse()
//}
//
//suspend fun networkReducer(sessionId: String, clientStorage: Map<String, ClientValue>, intent: Intent): Node =
//    ktorClient.post<String>("$SERVER_URL/$SERVER_PATH_NETWORK_REDUCER"){
//        body = TextContent(NetworkReducerRequestBody(sessionId, clientStorage, intent).toJson(), ContentType.Application.Json)
//    }.parseToReducerResult().state
//
//
//data class RefreshViewState(
//    val clientStorage: Map<String, ClientValue>,
//    val serverData: ServerData = ServerData.Loading
//) {
//    sealed class ServerData {
//        object Loading : ServerData()
//        data class Loaded(val sessionId: String, val node: Node) : ServerData()
//    }
//}
//
//sealed class ClientIntent() {
//    class SendToServer(val intent: Intent):ClientIntent()
//    data class UpdateClientStorage(val key: String, val value: ClientValue) : ClientIntent()
//    class FirstServerResponse(val node:Node, val sessionId: String):ClientIntent()
//}

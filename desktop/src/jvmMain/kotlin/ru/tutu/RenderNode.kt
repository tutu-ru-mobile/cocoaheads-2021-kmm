package ru.tutu

import ru.tutu.serialization.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RenderNode(clientStorage:Map<String, ClientValue>, node: ViewTreeNode, sendIntent: (ClientIntent) -> Unit) {
    when (node) {
        is ViewTreeNode.Container.Horizontal -> {
            Row {
                for (child in node.children) {
                    RenderNode(clientStorage, child, sendIntent)
                }
            }
        }
        is ViewTreeNode.Container.Vertical -> {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                for (child in node.children) {
                    RenderNode(clientStorage, child, sendIntent)
                }
            }
        }
        is ViewTreeNode.Leaf.Rectangle -> {
            Box(modifier = Modifier.size(node.width.dp, node.height.dp).background(color = Color(node.color.hexValue.toInt())))
        }
        is ViewTreeNode.Leaf.Label -> {
            Text(text = node.text)
        }
        is ViewTreeNode.Leaf.Button -> {
            Button(onClick = {
                sendIntent(ClientIntent.SendToServer(Intent.ButtonPressed(node.id)))
            }) {
                Text(text = node.text)
            }
        }
        is ViewTreeNode.Leaf.Input -> {
            val text = clientStorage[node.storageKey]?.stringValue ?: ""
            TextField(text, onValueChange = {
                sendIntent(ClientIntent.UpdateClientStorage(node.storageKey, ClientValue(it)))
            })
        }
        is ViewTreeNode.Leaf.Image -> {
            NetworkImage(node.imgUrl, node.width, node.height)
        }
    }.also { }
}

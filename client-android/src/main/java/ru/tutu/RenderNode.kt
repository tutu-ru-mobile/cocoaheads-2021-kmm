package ru.tutu

import ru.tutu.serialization.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RenderNode(
    clientStorage: ClientStorage,
    node: ViewTreeNode,
    sendIntent: (ClientIntent) -> Unit
) {
    when (node) {
        is ViewTreeNode.Container.Horizontal -> {
            Row(
                modifier = Modifier.padding(3.dp)
                    .background(color = Color(node.backgroundColor.hexValue.toInt())),
                verticalAlignment = node.verticalAlignment.toAndroid(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (child in node.children) {
                    RenderNode(clientStorage, child, sendIntent)
                }
            }
        }
        is ViewTreeNode.Container.Vertical -> {
            Column(
                modifier = Modifier.background(color = Color(node.backgroundColor.hexValue.toInt())),
                horizontalAlignment = node.horizontalAlignment.toAndroid(),
                verticalArrangement = Arrangement.Center
            ) {
                for (child in node.children) {
                    RenderNode(clientStorage, child, sendIntent)
                }
            }
        }
        is ViewTreeNode.Leaf.Rectangle -> {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(node.width.dp, node.height.dp)
                    .background(color = Color(node.color.hexValue.toInt()))
            )
        }
        is ViewTreeNode.Leaf.Text -> {
            Text(text = node.text, textAlign = TextAlign.Center, modifier = Modifier.padding(4.dp))
        }
        is ViewTreeNode.Leaf.Button -> {
            Text(
                text = node.text,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    sendIntent(ClientIntent.SendToServer(Intent.ButtonPressed(node.id)))
                },
                color = Color.Blue
            )
        }
        is ViewTreeNode.Leaf.Input -> {
            val text = clientStorage.map[node.storageKey]?.stringValue ?: ""
            TextField(text, onValueChange = {
                sendIntent(ClientIntent.UpdateClientStorage(node.storageKey, ClientValue(it)))
            })
        }
        is ViewTreeNode.Leaf.Image -> {
            NetworkImage(node.imgUrl, node.width * 8 / 10, node.height * 8 / 10)
        }
        is ViewTreeNode.Leaf.Space -> {
            Spacer(Modifier.size(node.size.dp))
        }
    }.also { }
}

fun VAlign.toAndroid(): Alignment.Vertical =
    when(this) {
        is VAlign.Top -> Alignment.Top
        is VAlign.Center -> Alignment.CenterVertically
        is VAlign.Bottom -> Alignment.Bottom
    }

fun HAlign.toAndroid(): Alignment.Horizontal =
    when(this) {
        is HAlign.Left -> Alignment.Start
        is HAlign.Center -> Alignment.CenterHorizontally
        is HAlign.Right -> Alignment.End
    }

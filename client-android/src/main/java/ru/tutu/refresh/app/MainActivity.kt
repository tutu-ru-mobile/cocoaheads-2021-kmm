package ru.tutu.refresh.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tutu.RefreshView
import ru.tutu.refresh.app.theme.AppAndroidTheme
import ru.tutu.serialization.ClientSideEffect

enum class Screen(val tab: String, val title: String, val icon: ImageVector) {
    MAIN("Главная", "Главный экран", Icons.Default.Home),
    ORDERS("Заказы", "Мои билеты", Icons.Default.List),
    SUPPORT("Помощь", "Справка и помощь", Icons.Default.Phone),
    IMPORTANT("Важное", "Важная информация", Icons.Default.Star)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppAndroidTheme {
                MainContainer()
            }
        }
    }
}

@Composable
fun MainContainer() {
    val ordersAdditionalInfo = remember { mutableStateOf<String?>(null) }
    val selectedTab = remember { mutableStateOf(Screen.MAIN) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = selectedTab.value.title,
                            fontSize = 22.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomNavigation(elevation = 10.dp) {
                Screen.values().forEach {
                    BottomNavigationItem(icon = {
                        Icon(imageVector = it.icon, "")
                    },
                        label = { Text(text = it.tab) },
                        selected = (selectedTab.value == it),
                        onClick = {
                            selectedTab.value = it
                        })
                }
            }
        }) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            when (selectedTab.value) {
                Screen.MAIN -> {
                    Text("Здесь можно купить билет")
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Посмотрите важную информацию!",
                            modifier = Modifier.clickable {
                                selectedTab.value = Screen.IMPORTANT
                            },
                            color = Color.Blue
                        )
                    }
                }
                Screen.ORDERS -> {
                    Text("Билет №1")
                    Text("Билет №2")
                    Text("Билет №3")
                    ordersAdditionalInfo.value?.let {
                        Text(it)
                    }

                }
                Screen.SUPPORT -> {
                    Text("Напишите в чат,")
                    Text("или позвоните +5(555)555-55-55")
                    Box(
                        Modifier
                            .padding(5.dp)
                            .border(border = BorderStroke(1.dp, color = Color.Black))
                            .padding(5.dp)
                    ) {
                        RefreshView(
                            userId = "my_user_id",
                            networkReducerUrl = "http://10.0.2.2:8081/playground_reducer",
                            autoUpdate = true
                        ) {}
                    }

                }
                Screen.IMPORTANT -> RefreshView(
                    "my_user_id_android",
                    "http://10.0.2.2:8081/important_reducer",
                    true
                ) {
                    when (it) {
                        is ClientSideEffect.OpenOrder -> {
                            ordersAdditionalInfo.value = it.additionInfo
                            selectedTab.value = Screen.ORDERS
                        }
                        is ClientSideEffect.OpenSupportScreen -> {
                            selectedTab.value = Screen.SUPPORT
                        }
                    }
                }
            }
        }
    }
}


import kotlinx.coroutines.*

actual internal inline fun getAppScope():CoroutineScope = MainScope()
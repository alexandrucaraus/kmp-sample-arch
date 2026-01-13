package eu.caraus.kmp.test.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

//suspend fun <T> TestScope.waitOrFail(
//    timeMillis: Duration = 10.seconds,
//    block: suspend CoroutineScope.() -> T
//): T? = withContext(Dispatchers.Default.limitedParallelism(1)) {
//    var r = block()
//    while (b)
//}

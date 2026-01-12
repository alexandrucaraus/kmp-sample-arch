package eu.caraus.kmp.notes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.classgraph.ClassGraph
import io.github.classgraph.MethodInfo
import java.lang.reflect.Method

// todo move this to a test support module
class ComposePreviewsAggregator {


    fun scan(vararg packagesName: String): List<Method> {
        return ClassGraph()
            .enableMethodInfo()
            .enableAnnotationInfo()
            .acceptPackages(*packagesName)
            .scan().use { scanResult ->
                scanResult.getClassesWithMethodAnnotation(Preview::class.java.name)
                    .flatMap { clazz -> clazz.methodInfo }
                    //.filter { method -> method.hasAnnotation(Preview::class.java.name) }
                    .map(::extractMethod)
            }
    }

    private fun extractMethod(methodInfo: MethodInfo): Method {
        val clazz = Class.forName(methodInfo.className)
        val method = clazz.declaredMethods.find { it.name == methodInfo.name }
            ?: throw NoSuchMethodException("${methodInfo.className} in ${methodInfo.className} not found")
        method.isAccessible = true
        return method
    }

}

@Composable
fun SnapshotDarkBackground(
    modifier: Modifier = Modifier,
    toSnapshot: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        toSnapshot()
    }
}
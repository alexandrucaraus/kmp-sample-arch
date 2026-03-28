package eu.caraus.kmp.test.common.previews

import androidx.compose.ui.tooling.preview.Preview
import io.github.classgraph.ClassGraph
import io.github.classgraph.MethodInfo
import java.lang.reflect.Method

class ComposePreviewsAggregator {
    fun scan(vararg packagesName: String): List<Method> =
        ClassGraph()
            .enableMethodInfo()
            .enableAnnotationInfo()
            .acceptPackages(*packagesName)
            .scan()
            .use { scanResult ->
                scanResult
                    .getClassesWithMethodAnnotation(Preview::class.java.name)
                    .flatMap { clazz -> clazz.methodInfo }
                    .map(::extractMethod)
            }

    private fun extractMethod(methodInfo: MethodInfo): Method {
        val clazz = Class.forName(methodInfo.className)
        val method =
            clazz.declaredMethods.find { it.name == methodInfo.name }
                ?: throw NoSuchMethodException("${methodInfo.className} in ${methodInfo.className} not found")
        method.isAccessible = true
        return method
    }
}

fun Method.name(): String =
    name
        .replace(Regex("([A-Z])")) { "_${it.value.lowercase()}" }
        .trimStart('_')

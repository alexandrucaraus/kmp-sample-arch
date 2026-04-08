package eu.caraus.kmp.coverage

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.io.File

private const val PREVIEW_ANNOTATION = "Landroidx/compose/ui/tooling/preview/Preview;"
private const val COMPOSABLE_ANNOTATION = "Landroidx/compose/runtime/Composable;"

/**
 * Returns true if the class file contains at least one method annotated with
 * both @Composable and @Preview. Such classes are excluded from coverage reports.
 */
fun File.hasComposablePreview(): Boolean {
    if (!name.endsWith(".class")) return false
    return try {
        val reader = ClassReader(readBytes())
        var found = false
        reader.accept(object : ClassVisitor(Opcodes.ASM9) {
            override fun visitMethod(
                access: Int,
                name: String,
                descriptor: String,
                signature: String?,
                exceptions: Array<out String>?,
            ): MethodVisitor = object : MethodVisitor(Opcodes.ASM9) {
                private var hasPreview = false
                private var hasComposable = false

                override fun visitAnnotation(descriptor: String, visible: Boolean) =
                    super.visitAnnotation(descriptor, visible).also {
                        if (descriptor == PREVIEW_ANNOTATION) hasPreview = true
                        if (descriptor == COMPOSABLE_ANNOTATION) hasComposable = true
                    }

                override fun visitEnd() {
                    if (hasPreview && hasComposable) found = true
                }
            }
        }, ClassReader.SKIP_CODE or ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES)
        found
    } catch (_: Exception) {
        false
    }
}

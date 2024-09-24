plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.modulegraph) apply true
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

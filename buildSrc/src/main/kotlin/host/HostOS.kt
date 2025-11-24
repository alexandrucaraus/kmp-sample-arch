package eu.caraus.kmp.build.host

import org.gradle.api.Project

enum class HostOS {
    LINUX,
    WINDOWS,
    MAC;

    companion object {
        fun current(): HostOS {
            val osName = System.getProperty("os.name").lowercase()
            return when {
                osName.contains("linux") -> LINUX
                osName.contains("win") -> WINDOWS
                osName.contains("mac") || osName.contains("darwin") -> MAC
                else -> throw IllegalStateException("Unsupported OS: $osName")
            }
        }

        fun isMac() = current() == MAC
        fun isLinux() = current() == LINUX
        fun isWindows() = current() == WINDOWS
    }
}

enum class KmpTarget {
    ANDROID,
    JVM,
    IOS_ARM64,
    IOS_SIMULATOR_ARM64,
    IOS_X64,
    JS,
    WINDOWS,
    WASM;

    val requiresMac: Boolean
        get() = this in listOf(IOS_ARM64, IOS_SIMULATOR_ARM64, IOS_X64)
}


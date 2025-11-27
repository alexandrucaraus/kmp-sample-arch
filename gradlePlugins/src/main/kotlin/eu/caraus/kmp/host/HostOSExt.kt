package eu.caraus.kmp.host

import org.gradle.api.Project

/**
 * Check if running on Mac (for conditional logic in build scripts)
 */
fun Project.isMac(): Boolean = HostOS.isMac()

/**
 * Check if running on Linux
 */
fun Project.isLinux(): Boolean = HostOS.isLinux()

/**
 * Check if running on Windows
 */
fun Project.isWindows(): Boolean = HostOS.isWindows()

/**
 * Get current host OS
 */
fun Project.hostOS(): HostOS = HostOS.current()

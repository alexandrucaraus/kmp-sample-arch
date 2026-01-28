package eu.caraus.kmp.coverage
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

abstract class AndroidEmulatorTask : DefaultTask() {

    @get:Input
    abstract val avdName: Property<String>

    @get:Input
    @get:Optional
    abstract val systemImage: Property<String>

    @get:Input
    @get:Optional
    abstract val deviceType: Property<String>

    @get:Input
    @get:Optional
    abstract val bootTimeout: Property<Int>

    @get:Input
    @get:Optional
    abstract val emulatorAction: Property<String> // start stop


    init {
        group = "android"
        description = "Manages Android emulator creation and execution"

        // Set defaults
        avdName.convention("test_avd")
        systemImage.convention("system-images;android-33;google_apis;x86_64")
        deviceType.convention("pixel_5")
        bootTimeout.convention(300) // 5 minutes
    }

    @TaskAction
    fun execute() {
        logger.lifecycle("Starting Android Emulator Task for AVD: ${avdName.get()}")

        if (!checkEmulatorExists()) {
            logger.lifecycle("Emulator does not exist. Creating...")
            createEmulator()
        } else {
            logger.lifecycle("Emulator already exists")
        }

        when (emulatorAction.get()) {
            "start" -> {
                startEmulator()
                waitForBoot()
            }
            "stop" -> {
                stopEmulator()
            }
        }

        logger.lifecycle("Emulator is ready for use")
    }

    private fun createEmulator() {
        logger.lifecycle("Creating emulator with AVD name: ${avdName.get()}")

        // First, download system image if needed
        val downloadCmd = listOf(
            getAndroidSdkPath() + "/cmdline-tools/latest/bin/sdkmanager",
            systemImage.get()
        )

        logger.lifecycle("Ensuring system image is installed...")
        executeCommand(downloadCmd, "Failed to download system image")

        // Create AVD
        val createCmd = listOf(
            getAndroidSdkPath() + "/cmdline-tools/latest/bin/avdmanager",
            "create", "avd",
            "-n", avdName.get(),
            "-k", systemImage.get(),
            "-d", deviceType.get(),
            "--force"
        )

        executeCommand(createCmd, "Failed to create emulator", input = "no\n")
        logger.lifecycle("Emulator created successfully")
    }


    private fun startEmulator() {
        logger.lifecycle("Starting emulator: ${avdName.get()}")

        val emulatorPath = getAndroidSdkPath() + "/emulator/emulator"
        val startCmd = listOf(
            emulatorPath,
            "-avd", avdName.get(),
            "-no-snapshot",
            "-no-audio",
            "-no-window",
            "-gpu", "swiftshader_indirect",
            "-no-boot-anim",
            "-accel", "off",
            "-memory", "4096",
            "-cores", "4"
        )

        // Start emulator in background
        val processBuilder = ProcessBuilder(startCmd)
        processBuilder.redirectErrorStream(true)
        processBuilder.start()

        logger.lifecycle("Emulator process started")

        // Give it a moment to initialize
        Thread.sleep(3000)
    }

    private fun checkEmulatorExists(): Boolean {
        logger.lifecycle("Checking if emulator exists: ${avdName.get()}")

        logger.lifecycle("Avd manager path: ${findAvdManagerPath()}")

        val listCmd = listOf(
            findAvdManagerPath(),
            "list", "avd"
        )

        return try {
            val output = executeCommandWithOutput(listCmd)
            val exists = output.contains("Name: ${avdName.get()}")
            logger.lifecycle("Emulator exists: $exists")
            exists
        } catch (e: Exception) {
            logger.warn("Error checking emulator existence: ${e.message}")
            false
        }
    }

    private fun findAvdManagerPath(): String {
        val sdkRoot = getAndroidSdkPath()
        val cmdlineToolsDir = File("$sdkRoot/cmdline-tools")

        val direct = File(cmdlineToolsDir, "latest/bin/avdmanager")
        if (direct.exists()) return direct.absolutePath

        return cmdlineToolsDir
                .walkTopDown()
                .firstOrNull { it.name == "avdmanager" && it.parentFile.name == "bin" }
                ?.absolutePath ?: throw RuntimeException("avdmanager not found")
    }

    private fun waitForBoot() {
        logger.lifecycle("Waiting for emulator to boot (timeout: ${bootTimeout.get()}s)...")

        val adbPath = getAndroidSdkPath() + "/platform-tools/adb"
        val startTime = System.currentTimeMillis()
        val timeoutMillis = bootTimeout.get() * 1000L

        // Wait for device to be detected
        logger.lifecycle("Detecting emulators/devices")
        var count = 0
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            logger.lifecycle("Try ${count++}")
            try {
                val devices = executeCommandWithOutput(listOf(adbPath, "devices"))
                logger.lifecycle("Devices: $devices")
                val hasOnlineEmulator = devices.lines().any {
                    logger.lifecycle("Emulators $it")
                    it.startsWith("emulator") && it.contains("\tdevice")
                }
                if (hasOnlineEmulator) {
                    logger.lifecycle("Online emulator detected")
                    break
                } else
                if (count == 3) {
                    break
                }
            } catch (e: Exception) {
                logger.lifecycle("Waiting for device detection... ${e.message}")
            }
            logger.lifecycle("Retry in 2s")
            Thread.sleep(2000)
        }

        // Wait for boot to complete
        logger.lifecycle("Start wait on boot")
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            try {

                val bootComplete = executeCommandWithOutput(
                    listOf(
                        adbPath,
                        //"-s", serial ,
                        "shell", "getprop", "sys.boot_completed")
                ).trim()

                if (bootComplete == "1") {
                    logger.lifecycle("Emulator boot completed successfully")

                    // Additional wait for stability
                    Thread.sleep(5000)
                    return
                }
            } catch (e: Exception) {
                logger.lifecycle("Boot not complete yet: ${e.message}")
            }

            logger.lifecycle("Still booting...")
            Thread.sleep(5000)
        }

        throw RuntimeException("Emulator failed to boot within ${bootTimeout.get()} seconds")
    }

    private fun stopEmulator() {
        logger.lifecycle("Stopping emulator")

        val adbPath = getAndroidSdkPath() + "/platform-tools/adb"
        val stopCmd = listOf(adbPath, "emu", "kill")

        try {
            executeCommand(stopCmd, "Failed to stop emulator gracefully")
            Thread.sleep(2000)
            logger.lifecycle("Emulator stopped successfully")
        } catch (e: Exception) {
            logger.warn("Error stopping emulator: ${e.message}")
        }
    }

    private fun getAndroidSdkPath(): String {
        return System.getenv("ANDROID_SDK_ROOT")
            ?: System.getenv("ANDROID_HOME")
            ?: "/home/alex/Android/Sdk" // todo find it dynamically
            ?: throw RuntimeException("ANDROID_SDK_ROOT or ANDROID_HOME environment variable must be set")
    }

    private fun executeCommand(
        command: List<String>,
        errorMessage: String,
        input: String? = null
    ) {
        val processBuilder = ProcessBuilder(command)
        processBuilder.redirectErrorStream(true)

        val process = processBuilder.start()

        // Provide input if needed (e.g., for "no" response)
        input?.let {
            process.outputStream.write(it.toByteArray())
            process.outputStream.flush()
            process.outputStream.close()
        }

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        reader.forEachLine { logger.debug(it) }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("$errorMessage (exit code: $exitCode)")
        }
    }

    @Suppress("NewApi")
    private fun executeCommandWithOutput(command: List<String>): String {
        val processBuilder = ProcessBuilder(command)
        processBuilder.redirectErrorStream(true)

        val process = processBuilder.start()
        val output = process.inputStream.bufferedReader().readText()

        process.waitFor(10, TimeUnit.SECONDS)

        return output
    }
}

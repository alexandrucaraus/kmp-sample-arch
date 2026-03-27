package eu.caraus.kmp.notes.ui

import eu.caraus.kmp.notes.domain.Di
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(
    includes = [
        Di::class,
    ],
)
@ComponentScan("eu.caraus.kmp.notes.ui")
@Configuration
object Di

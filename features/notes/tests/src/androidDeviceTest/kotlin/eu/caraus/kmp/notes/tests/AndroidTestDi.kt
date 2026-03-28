package eu.caraus.kmp.notes.tests

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.Factory
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import eu.caraus.kmp.database.Di as DatabaseDi
import eu.caraus.kmp.notes.data.Di as NoteDataDi
import eu.caraus.kmp.notes.domain.Di as NoteDomainDi
import eu.caraus.kmp.notes.ui.Di as NoteUiDi

@Module
class AndroidTestSubstitutes {
    @Factory
    fun createViewModelCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    @Factory
    fun appContext(): Context = InstrumentationRegistry.getInstrumentation().targetContext
}

@KoinApplication(
    configurations = [""],
    modules = [
        DatabaseDi::class,
        NoteDataDi::class,
        NoteDomainDi::class,
        NoteUiDi::class,
        AndroidTestSubstitutes::class,
    ],
)
object IntegrationTestDi

package eu.caraus.kmp.notes.ui

import eu.caraus.kmp.notes.domain.NoteDiModule
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module(
    includes = [
        NoteDiModule::class,
        NoteUiDiModule::class
    ]
)
class NoteListTestModule
// This is probably a bug. The module is only generated in androidHostTest module
// when those source sets are added to the file tree. It will then appear in
// generated/ksp/androidHostTest as a koin module.
// TODO needs clarification

// meanwhile
fun noteUiTestKoinModule() = module {
    includes(NoteDiModule().module, NoteUiDiModule().module)
    //includes(NoteListTestModule().module)
}

import SwiftUI
import composeApp

@main
struct iOSApp: App {

    init () {
        Koin_IosKt.startKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

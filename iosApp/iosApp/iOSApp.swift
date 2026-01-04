import SwiftUI
import composeApp

@main
struct iOSApp: App {

    init () {
        Koin_iosKt.startKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

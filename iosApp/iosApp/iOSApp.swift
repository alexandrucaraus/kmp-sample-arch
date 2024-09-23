import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
       SetupDiKt.setupDi()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

import SwiftUI
import Shared

struct ContentView: View {

    @State private var selectedTab = 0

    var body: some View {
        TabView(selection: $selectedTab) {
            RenderRefreshView().tabItem {
                Label("Главная", systemImage: "house.fill")
            }
            Text("Todo2").tabItem {
                Label("Билеты", systemImage: "list.dash")
            }
            Text("Todo").tabItem {
                Label("Помощь", systemImage: "phone.fill")
            }
        }

    }
}

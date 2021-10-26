import SwiftUI
import Shared

struct ContentView: View {

    @State private var selectedTab = 0

    var body: some View {
        TabView(selection: $selectedTab) {
            NavigationView {
                RenderRefreshView().navigationBarTitle("Важная информация", displayMode: .inline)
            }.tabItem {
                Label("Главная", systemImage: "house.fill")
            }
            // Приобретенные билеты
            NavigationView {
                List {
                    HStack {
                        Text("Билет №1")
                        Button(action: {}) {
                            Text("отменить поездку")
                        }
                    }
                    HStack {
                        Text("Билет №2")
                        Button(action: {}) {
                            Text("отменить поездку")
                        }
                    }
                    HStack {
                        Text("Билет №3")
                        Button(action: {}) {
                            Text("отменить поездку")
                        }
                    }
                }.navigationBarTitle("Мои билеты", displayMode: .inline)
            }.tabItem {
                Label("Билеты", systemImage: "list.dash")
            }
            // Экран помощи контактов
            NavigationView {
                VStack {
                    Text("Вы можете связать с нами в чате,")
                    Text("или по телефону +5(555)555-55-55")

                }.navigationBarTitle("Справка и помощь", displayMode: .inline)
            }.tabItem {
                Label("Помощь", systemImage: "phone.fill")
            }
        }


    }
}

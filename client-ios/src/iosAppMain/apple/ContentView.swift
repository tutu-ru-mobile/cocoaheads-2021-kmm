import SwiftUI
import Shared

struct ContentView: View {

    @State private var selectedTab = 0
    @State private var orderAdditionalInfo: String? = nil

    var body: some View {
        TabView(selection: $selectedTab) {
            // Главный экран
            NavigationView {
                VStack {
                    Text("Главный экран")
                    Text("Здесь можно купить билет")
                    Spacer()
                    Button(action: {
                        selectedTab = 3
                    }) {
                        Text("Посмотрите важную информацию!")
                    }
                    Spacer()
                }.navigationBarTitle("Главный экран", displayMode: .inline)
            }.tabItem {
                Label("Главная", systemImage: "house.fill")
            }.tag(0)

            // Приобретенные билеты
            NavigationView {
                VStack {
                    List {
                        Text("Билет №1")
                        Text("Билет №2")
                        Text("Билет №3")
                        orderAdditionalInfo.flatMap { _ in
                            Spacer(minLength: 20)
                        }
                        orderAdditionalInfo.flatMap {
                            Text($0)
                        }
                    }
                }.navigationBarTitle("Мои билеты", displayMode: .inline)
            }.tabItem {
                Label("Билеты", systemImage: "list.dash")
            }.tag(1)

            // Экран помощи контактов
            NavigationView {
                VStack {
                    Text("Напишите в чат,")
                    Text("или позвоните +5(555)555-55-55")

                }.navigationBarTitle("Справка и помощь", displayMode: .inline)
            }.tabItem {
                Label("Помощь", systemImage: "phone.fill")
            }.tag(2)

            // Важная информация
            NavigationView {
                RenderRefreshView(userId: "my_user_id"){ sideEffect in
                    switch sideEffect {
                    case let openOrder as ClientSideEffect.OpenOrder:
                        selectedTab = 1
                        orderAdditionalInfo = openOrder.additionInfo
                    case let openSupport as ClientSideEffect.OpenSupportScreen:
                        selectedTab = 2
                    case let openBrowser as ClientSideEffect.OpenBrowser:
                        print("open browser link \(openBrowser.url)")
                    default:
                        print("do nothing")
                    }
                }.navigationBarTitle("Важная информация", displayMode: .inline)
            }.tabItem {
                Label("Важно", systemImage: "star")
            }.tag(3)
        }

    }
}

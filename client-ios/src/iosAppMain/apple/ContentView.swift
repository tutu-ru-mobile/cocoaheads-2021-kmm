import SwiftUI
import Shared

struct ContentView: View {

    enum Tab {
        case main
        case orders
        case support
        case important
    }

    @State private var selectedTab = Tab.main
    @State private var orderAdditionalInfo: String? = nil

    var body: some View {
        TabView(selection: $selectedTab) {
            //-------------------------------------------------------------
            // Главный экран
            NavigationView {
                VStack {
                    Text("Главный экран")
                    Text("Здесь можно купить билет")
                    Spacer()
                    Button(action: {
                        selectedTab = Tab.important
                    }) {
                        Text("Посмотрите важную информацию!")
                    }
                    Spacer()
                }.navigationBarTitle("Главный экран", displayMode: .inline)
            }.tabItem {
                Label("Главная", systemImage: "house.fill")
            }.tag(Tab.main)

            //-------------------------------------------------------------
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
                Label("БилетыБилеты", systemImage: "list.dash")
            }.tag(Tab.orders)

            //-------------------------------------------------------------
            // Экран помощи и контактов
            NavigationView {
                VStack {
                    Text("Напишите в чат,")
                    Text("или позвоните +5(555)555-55-55")

                    RenderRefreshView(
                            userId: "my_user_id",
                            networkReducerUrl: "http://localhost:8081/playground_reducer",
                            autoUpdate: true
                    ) { sideEffect in
                    }.padding()
                            .border(Color.black)

                }.navigationBarTitle("Справка и помощь", displayMode: .inline)
            }.tabItem {
                Label("Помощь", systemImage: "phone.fill")
            }.tag(Tab.support)

            //-------------------------------------------------------------
            // Важная информация
            NavigationView {
                RenderRefreshView(
                        userId: "my_user_id",
                        networkReducerUrl: "http://localhost:8081/important_reducer",
                        autoUpdate: false
                ) { sideEffect in
                    switch sideEffect {
                    case let openOrder as ClientSideEffect.OpenOrder:
                        selectedTab = Tab.orders
                        orderAdditionalInfo = openOrder.additionInfo
                    case let openSupport as ClientSideEffect.OpenSupportScreen:
                        selectedTab = Tab.support
                    case let openBrowser as ClientSideEffect.OpenBrowser:
                        print("open browser link \(openBrowser.url)")
                    default:
                        print("do nothing")
                    }
                }.navigationBarTitle("Важная информация", displayMode: .inline)
            }.tabItem {
                Label("Важно", systemImage: "star")
            }.tag(Tab.important)
            //-------------------------------------------------------------
        }

    }
}

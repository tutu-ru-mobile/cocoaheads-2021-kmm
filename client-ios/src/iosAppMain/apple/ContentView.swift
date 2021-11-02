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
                    AsyncImage(url: URL(string: "http://localhost:8081/static/logo.png"), content: { image in
                        image.resizable()
                                .aspectRatio(contentMode: .fit)
                                .frame(maxWidth: 200, maxHeight: 200)
                    }, placeholder: {
                        ProgressView()
                    })

                    Text("Здесь можно купить билет").font(.system(size: 24))
                    Spacer()
                    Button(action: {
                        selectedTab = Tab.important
                    }) {
                        Text("Посмотрите важную информацию!").font(.system(size: 20))
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
                        Text("Билет №1").font(.system(size: 20))
                        Text("Билет №2").font(.system(size: 20))
                        Text("Билет №3").font(.system(size: 20))
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
            }.tag(Tab.orders)

            //-------------------------------------------------------------
            // Экран помощи и контактов
            NavigationView {
                VStack {
                    Text("Напишите в чат,").font(.system(size: 20))
                    Text("или позвоните +5(555)555-55-55").font(.system(size: 20))

                    ServerDrivenView(
                            userId: "my_user_id",
                            networkReducerUrl: "http://localhost:8081/playground_reducer",
                            autoUpdate: true
                    ) { sideEffect in}
                    .padding(3)
                    .border(Color.black)
                    .padding(5)
                }.navigationBarTitle("Справка и помощь", displayMode: .inline)
            }.tabItem {
                Label("Помощь", systemImage: "phone.fill")
            }.tag(Tab.support)

            //-------------------------------------------------------------
            // Важная информация
            NavigationView {
                ServerDrivenView(
                        userId: "my_user_id",
                        networkReducerUrl: "http://localhost:8081/important_reducer",
                        autoUpdate: true
                ) { (sideEffect: ClientSideEffect) in
                    switch sideEffect {
                    case let openOrder as ClientSideEffect.OpenOrder:
                        selectedTab = Tab.orders
                        orderAdditionalInfo = openOrder.additionInfo
                    case let openSupport as ClientSideEffect.OpenSupportScreen:
                        selectedTab = Tab.support
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

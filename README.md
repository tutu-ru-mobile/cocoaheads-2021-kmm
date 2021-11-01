# Доклад CocoaHeads "Server driven view + Kotlin"

### Вопросы и обратная связь в GitHub discussions:
### https://github.com/tutu-ru-mobile/cocoaheads-2021-kmm/discussions/1

## Для запуска iOS приложения:
 - Установить JetBrains Toolbox. (https://www.jetbrains.com/toolbox-app/)
 - Через него установить AppCode 2021.3
 - Запустить AppCode и установить плагин Kotlin Multiplatform Mobile. (https://plugins.jetbrains.com/plugin/17098-kotlin-multiplatform-mobile-for-appcode)
 - Открыть директорию проекта через AppCode. (File -> Open)
 - Первый раз проект будет синхронизироваться и качать Gradle зависимости.
 - Сверху появится меню запуска на iOS симуляторе.

## Запуск сервера без автообновления
 - В AppCode нажать Ctrl+Ctrl `gradle server:run`

## Для запуска сервера с авто обновлением:
 - Нужна Java 1.8 или старше (проверить можно командой `java -version`)
 - Запуск сервера с автоматическим обновлением `./start_server_watch.sh`

## Для установки Android приложения:
 - Нужно поставить AndroidStudio (или Android-SDK) и принять условия лицензии.
 - Запустить эмулятор, или соединить Android телефон по USB.
 - Запустить `./gradlew client-android:installDebug`
 - Приложение установится, и его можно запустить

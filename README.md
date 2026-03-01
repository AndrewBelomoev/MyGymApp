# My Gym App (Дневник тренировок) 🏋️‍♂️

## 🛠 Технологический стек (Tech Stack)
Приложение написано на языке **Kotlin** и использует современный стек технологий Jetpack:
* **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Декларативный UI, Material 3, Navigation Compose)
* **Архитектура**: MVVM (Model-View-ViewModel) в связке с Unidirectional Data Flow (StateFlow).
* **Локальная БД**: [Room Database](https://developer.android.com/training/data-storage/room) (SQLite ORM) с поддержкой предзаполнения базы через `RoomDatabase.Callback`.
* **DI (Внедрение зависимостей)**: [Koin for Android](https://insert-koin.io/) (Легковесный DI-фреймворк, обеспечивающий внедрение ViewModel и Repository).
* **Асинхронность**: Kotlin Coroutines & Flow (Асинхронные запросы к БД, реактивный UI-стейт).
* **Хранение настроек**: `androidx.datastore:datastore-preferences` (DataStore для реактивного хранения выбранной темы интерфейса).

## 🗂 Структура проекта
Исходный код разделен на логические слои:
* `data.local` — Room DAO, Entities (модели таблиц) и инициализация базы `AppDatabase`.
* `data.repository` — паттерн Repository, реализация взаимодействия с DAO `ExerciseRepositoryImpl` и `WorkoutRepositoryImpl`.
* `domain.repository` — интерфейсы репозиториев для обеспечения разорванности зависимостей.
* `domain.preferences` — логика работы с DataStore (тема оформления).
* `di` — Koin-модули (`AppModule`), объявляющие графы зависимостей приложения.
* `ui` — Jetpack Compose экраны (группировка по фичам: `home`, `workout`, `archive`, `settings`, `exercise`).
* `ui.theme` — Базовые системные токены: Typography, ColorScheme, Shapes.

## ⚙️ Установка и запуск
1. Склонируйте репозиторий.
2. Откройте проект в **Android Studio** (рекомендуется версия *Iguana* или новее).
3. Дождитесь завершения синхронизации Gradle.
4. Выберите эмулятор или подключенное физическое Android-устройство.
5. Нажмите **Run** (`Shift + F10`), чтобы скомпилировать и установить приложение `app-debug.apk`.

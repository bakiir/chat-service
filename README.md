📦 Chat Service for BePro-v2 
Chat Service — это серверная часть чата, построенная на Spring Boot с использованием WebSocket для обмена сообщениями в режиме реального времени. Проект реализует функциональность групповых чатов, обработку сообщений, управление сессиями пользователей и хранение истории сообщений в базе данных.

🚀 Основные технологии
Java 17+

Spring Boot

WebSocket

Gradle

JPA (Hibernate)

H2 / PostgreSQL (по умолчанию H2)

REST API

🧩 Структура проекта
bash
Копировать
Редактировать
src
└── main
    ├── java/com/example/be_pro_chat_service
    │   ├── config/                 # WebSocket конфигурация
    │   ├── controllers/            # REST-контроллеры
    │   ├── dto/                    # DTO классы
    │   ├── handlers/               # Обработчики WebSocket-сообщений
    │   ├── model/                  # Модели JPA
    │   ├── repo/                   # Репозитории Spring Data JPA
    │   ├── service/                # Сервисы бизнес-логики
    │   └── session/                # Управление сессиями пользователей
    └── resources/
        └── application.properties  # Настройки приложения
🔧 Как запустить
✅ Предварительные требования
Java 17+

Gradle (или использовать ./gradlew)

IDE: IntelliJ IDEA / VS Code

🛠 Запуск
bash
Копировать
Редактировать
git clone https://github.com/bakiir/chat-service.git
cd chat-service
./gradlew bootRun
Сервер будет доступен на: http://localhost:8080

📡 WebSocket Endpoint
ws://localhost:8080/chat — основной WebSocket endpoint

Пример handshake: ws://localhost:8080/chat?username=Bakr

Сообщения отправляются в формате JSON, обрабатываются через ChatMessageHandler и GroupMessageHandler.

🧪 Тестирование
bash
Копировать
Редактировать
./gradlew test
💾 Пример сообщения
json
Копировать
Редактировать
{
  "type": "CHAT",
  "sender": "Bakr",
  "content": "Привет всем!",
  "groupId": 1
}
📂 TODO / Возможности для расширения
Аутентификация через JWT

Интеграция с фронтендом (React, Angular и др.)

История чатов с пагинацией

Хранение в PostgreSQL

Docker-файл и docker-compose для быстрого деплоя

🧑‍💻 Автор
Разработано Bakr Bkr
GitHub: @bakiir


# 💬 Chat Service

**Chat Service for BePro-v2** — это серверная часть чата на Spring Boot с использованием WebSocket, предназначенная для обмена сообщениями в режиме реального времени. Проект поддерживает групповые чаты, хранение истории сообщений и управление пользовательскими сессиями.

---

## 🚀 Технологии

- Java 17+
- Spring Boot
- Redis
- WebSocket (STOMP)
- Spring Data JPA (Hibernate)
- Gradle
- H2 (по умолчанию) / PostgreSQL

---

## 📁 Структура проекта

```

src/
├── main/
│   ├── java/com/example/be\_pro\_chat\_service/
│   │   ├── config/               # WebSocket конфигурация
│   │   ├── controllers/          # REST API контроллеры
│   │   ├── dto/                  # DTO классы
│   │   ├── handlers/             # Обработчики WebSocket-сообщений
│   │   ├── model/                # JPA сущности
│   │   ├── repo/                 # Репозитории Spring Data
│   │   ├── service/              # Сервисный слой
│   │   └── session/              # Управление сессиями
│   └── resources/
│       └── application.properties

````

---

## ⚙️ Установка и запуск

### 1. Клонируйте репозиторий

```bash
git clone https://github.com/bakiir/chat-service.git
cd chat-service
````

### 2. Запустите приложение

```bash
./gradlew bootRun
```

Приложение будет доступно по адресу: `http://localhost:8080`

---

## 🌐 WebSocket

**Endpoint:**

```
ws://localhost:8080/chat?username=ВАШЕ_ИМЯ
```

### Пример сообщения:

```json
{
  "type": "CHAT",
  "sender": "Bakr",
  "content": "Привет!",
  "groupId": 1
}
```

Сообщения обрабатываются через `ChatMessageHandler` и `GroupMessageHandler`.

---

## 🧪 Тестирование

```bash
./gradlew test
```

---

## ✅ Планируемые улучшения

* JWT авторизация
* Docker поддержка
* PostgreSQL миграция
* UI (на React или Angular)
* История сообщений с пагинацией

---

## 👤 Автор

Разработано [Bakr Bkr](https://github.com/bakiir)

---

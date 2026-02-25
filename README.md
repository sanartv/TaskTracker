# Task Tracker API

---
Это RESTful Api для управления задачами, написанное на **JAVA** с использованием **SPRING BOOT**.
Проект включает в себя полноценную систему аутентификации (JWT), ролевую модель доступа и документацию.
---

# Технологии

* **Java 21**
* **Spring Boot 3.2** (Web, Data JPA, Security, Validation)
* **PostgreSQL** (Основная база данных)
* **H2 Database** (Для интеграционных тестов)
* **JWT (JSON Web Tokens)** (Безопасность)
* **Docker & Docker Compose** (Контейнеризация)
* **Maven** (Сборка)

---

## Установка и запуск

### Запуск через Docker

Без установки Java или PostgreSQL. Достаточно иметь Docker.

1. **Клонируйте репозиторий:**
   ```bash
   git clone [https://github.com/ТВОЙ_НИК/TaskTracker.git](https://github.com/ТВОЙ_НИК/TaskTracker.git)
   cd TaskTracker
2. **Соберите JAR файл:**
   ```bash
   ./mvnw clean package -DskipTests
3. **Запустите проект:**
   ```bash
   docker-compose up --build
Приложение будет доступно по адресу: http://localhost:8080.   

---
## API Endpoints
### Аутентификация
| Метод | URL              | Описание                | Тело запроса(JSON)                                     |
|-------|------------------|-------------------------|--------------------------------------------------------|
| POST  | /api/auth/signup | 	Регистрация	           | {"username": "...", "password": "...", "email": "..."} |
| POST  | /api/auth/signin | Вход (Получение токена) | {"username": "...", "password": "..."}                 |

Для доступа к задачам нужно добавить заголовок 
>Authorization: Bearer <ВАШ_ТОКЕН>

### Задачи
| Метод  | URL                         | Описание                    |
|--------|-----------------------------|-----------------------------|
| GET    | 	/api/tasks                 | 	Получить список всех задач |
| POST   | 	/api/tasks                 | 	Создать новую задачу       |
| GET    | /api/tasks/search?query=... | 	Поиск задач по названию    |
| DELETE | /api/tasks/{id}             | 	Удалить задачу             |

## Тестирование
Проект покрыт интеграционными тестами с использованием базы данных H2.

**Для запуска тестов выполните:**
   ```bash
   ./mvnw clean test   

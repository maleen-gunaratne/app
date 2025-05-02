# 🌤️ Melbourne Weather HTTP Service

A resilient, cache-enabled Spring Boot service that fetches Melbourne weather data from external APIs with failover, retry, and circuit breaker mechanisms. Designed for high availability and developer friendliness.

---

## 🧩 Features

- ✅ Unified REST API for Melbourne weather (`temperature` and `wind speed`)
- 🧠 **@ControllerAdvice** for centralized exception handling
- 🚀 **Resilience4J Circuit Breaker & Retry** to ensure robust external API communication
- ⚡ **Caffeine caching (3 seconds TTL)** to reduce provider load
- 🔁 Automatic **failover** between WeatherStack and OpenWeatherMap
- 🧪 Unit tests with JUnit & Mockito
- 📦 Docker-ready for containerized deployment
- 🧱 Clean, modular code structure
- 💬 Configurable via `application.yml`

---

## 📦 Tech Stack

- Java 21+
- Spring Boot 3.x
- Resilience4j (Retry & CircuitBreaker)
- Caffeine (for caching)
- Docker
- JUnit 5, Mockito

---

## 📥 Installation & Running

### 🔧 Prerequisites

- Java 21+
- Maven or Gradle
- Internet access to hit external weather APIs

---

### 🛠️ Setup

1. **Clone or unzip the project**
2. **Update your API keys** in `src/main/resources/application.yml`:
   ```yaml
   weather:
     api:
       weatherstack:
         url: http://api.weatherstack.com/current
         key: YOUR_ACCESS_KEY
       openweathermap:
         url: http://api.openweathermap.org/data/2.5/weather
         key: YOUR_ACCESS_KEY
   ```

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the project:**
   ```bash
   ./mvnw spring-boot:run
   ```

---

### 🐳 Docker Support

1. **Build Docker Image:**
   ```bash
   docker build -t weather-service .
   ```

2. **Run Container:**
   ```bash
   docker run -p 8080:8080 weather-service
   ```

---

## 🌐 API Endpoint

```http
GET /v1/weather?city=melbourne
```

### ✅ Sample Response

```json
{
  "temperatureDegrees": 22.5,
  "windSpeed": 12.3
}
```

---

## 🧠 Design Decisions & Trade-offs

- Used **WeatherStack as primary**, OpenWeatherMap as fallback.
- Cached responses for 3 seconds using Caffeine to limit API hits.
- Used **Resilience4j** to handle failure with circuit breakers and retries.
- Made the app **extendable** to support other cities or weather providers easily.
- For simplicity, used **Spring’s `RestTemplate`**, though `WebClient` can be used for reactive support.

---

## 📈 Future Enhancements

- Use `WebClient` for async non-blocking HTTP calls.
- Add rate-limiting to prevent abuse.
- Add more unit/integration tests for edge cases.

---

## 👨‍💻 Author

Crafted by a Spring Boot enthusiast with 6+ years of backend experience.

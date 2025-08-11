# Spring Session with Redis

Proyecto base de Spring Boot 3.4.5 con Java 17 implementando arquitectura hexagonal.

## Características

- **Spring Boot 3.4.5**
- **Java 17**
- **Arquitectura Hexagonal (Ports & Adapters)**
- **Maven y Gradle** como gestores de dependencias
- **Endpoint de health check** en `/api/health`
- **Endpoint de conversión de moneda** en `/api/currency/convert`
- **Tests unitarios** incluidos

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/example/
│   │       ├── Application.java                    # Clase principal de Spring Boot
│   │       ├── application/                        # Capa de aplicación (casos de uso)
│   │       │   ├── port/
│   │       │   │   ├── in/                        # Puertos de entrada
│   │       │   │   │   ├── HealthCheckUseCase.java
│   │       │   │   │   └── CurrencyConversionUseCase.java
│   │       │   │   └── out/                       # Puertos de salida
│   │       │   │       ├── HealthCheckRepository.java
│   │       │   │       └── ExchangeRateRepository.java
│   │       │   └── service/                       # Servicios de aplicación
│   │       │       ├── HealthCheckService.java
│   │       │       └── CurrencyConversionService.java
│   │       └── infrastructure/                     # Capa de infraestructura
│   │           └── adapter/
│   │               ├── in/                         # Adaptadores de entrada
│   │               │   └── web/
│   │               │       ├── HealthCheckController.java
│   │               │       └── CurrencyConversionController.java
│   │               └── out/                        # Adaptadores de salida
│   │                   └── persistence/
│   │                       ├── HealthCheckRepositoryImpl.java
│   │                       └── ExchangeRateRepositoryImpl.java
│   └── resources/
│       └── application.yml                         # Configuración de la aplicación
└── test/                                           # Tests unitarios
    └── java/
        └── com/example/
            ├── ApplicationTests.java
            ├── application/service/
            │   └── CurrencyConversionServiceTest.java
            └── infrastructure/adapter/in/web/
                ├── HealthCheckControllerTest.java
                └── CurrencyConversionControllerTest.java
```

## Arquitectura Hexagonal

Este proyecto implementa la arquitectura hexagonal (también conocida como Ports & Adapters):

- **Dominio**: Lógica de negocio central
- **Puertos de Entrada**: Interfaces que definen qué puede hacer la aplicación
- **Puertos de Salida**: Interfaces que definen qué necesita la aplicación del exterior
- **Adaptadores de Entrada**: Implementaciones de los puertos de entrada (ej: REST controllers)
- **Adaptadores de Salida**: Implementaciones de los puertos de salida (ej: repositories, external APIs)

## Requisitos

- Java 17 o superior
- Maven 3.6+ o Gradle 8.0+

## Ejecución

### Con Maven

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar la aplicación
mvn spring-boot:run
```

### Con Gradle

```bash
# Compilar el proyecto
./gradlew clean compileJava

# Ejecutar tests
./gradlew test

# Ejecutar la aplicación
./gradlew bootRun
```

## Endpoints

### Health Check
- **URL**: `GET /api/health`
- **Respuesta**: `OK`
- **Descripción**: Endpoint para verificar el estado de salud de la aplicación

### Conversión de Moneda
- **URL**: `GET /api/currency/convert?amount={valor}`
- **Parámetros**: 
  - `amount`: Cantidad en Soles Peruanos (PEN) a convertir
- **Respuesta**: JSON con detalles de la conversión
- **Descripción**: Convierte Soles Peruanos a Dólares Estadounidenses usando tasa fija de 3.8 PEN/USD

#### Ejemplo de Uso:
```bash
curl "http://localhost:8080/api/currency/convert?amount=38"
```

#### Respuesta:
```json
{
  "originalAmount": 38.0,
  "originalCurrency": "PEN",
  "convertedAmount": 10.0,
  "targetCurrency": "USD",
  "exchangeRate": 3.8,
  "message": "Conversion successful"
}
```

### Actuator
- **Health**: `GET /actuator/health`
- **Info**: `GET /actuator/info`

## Configuración

La aplicación se ejecuta por defecto en el puerto 8080. Puedes modificar la configuración en `src/main/resources/application.yml`.

## Desarrollo

### Agregar un nuevo caso de uso

1. Crear el puerto de entrada en `application/port/in/`
2. Crear el puerto de salida en `application/port/out/` (si es necesario)
3. Implementar el servicio en `application/service/`
4. Crear el adaptador de entrada en `infrastructure/adapter/in/`
5. Crear el adaptador de salida en `infrastructure/adapter/out/` (si es necesario)

### Ejecutar tests

```bash
# Con Maven
mvn test

# Con Gradle
./gradlew test
```

## Licencia

Este proyecto está bajo la licencia MIT. 
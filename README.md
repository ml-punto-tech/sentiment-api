# API REST para Análisis de Sentimiento 🧠

API Backend desarrollada con Spring Boot que proporciona análisis de sentimiento de textos mediante integración con modelo de Machine Learning en Python.

## 📋 Descripción

Sistema que recibe textos, los procesa y clasifica en tres categorías de sentimiento (Positivo, Negativo, Neutral) utilizando un modelo de Machine Learning. La API actúa como intermediario entre el cliente y el servicio de Data Science.

## 🏗️ Arquitectura

```
Cliente → Spring Boot API → Python ML Service → Respuesta
```

## 🛠️ Tecnologías

- **Java 17+**
- **Spring Boot 3.x**
- **Maven**
- **Lombok**
- **RestTemplate/WebClient**

## 📦 Dependencias Maven

```xml
<dependencies>
    <!-- Spring Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- DevTools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

## 📁 Estructura del Proyecto

```
src/main/java/
├── controller/
│   └── SentimentController.java
├── service/
│   ├── SentimentService.java
│   └── DataScienceClient.java
├── model/
│   ├── SentimentRequest.java
│   └── SentimentResponse.java
└── Application.java
```

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone [URL_DEL_REPOSITORIO]
cd sentiment-api
```

### 2. Configurar application.properties

```properties
# Puerto del servidor
server.port=8080

# URL del servicio de ML Python
ml.service.url=http://localhost:5000/api_sentimiento

# Logging
logging.level.root=INFO
logging.level.com.tuempresa=DEBUG
```

### 3. Compilar el proyecto

```bash
mvn clean install
```

### 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

## 📝 Endpoints

### Análisis de Sentimiento

**POST** `/api/sentiment`

Analiza el sentimiento de un texto proporcionado.

#### Request Body

```json
{
  "text": "El servicio es excelente, muy recomendado"
}
```

#### Validaciones

- `text`: No puede estar vacío (`@NotBlank`)
- `text`: Longitud mínima de 5 caracteres (`@Size(min=5)`)

#### Response (200 OK)

```json
{
  "prevision": "Positivo",
  "probabilidad": 0.87
}
```

#### Posibles Respuestas

- **200 OK**: Análisis exitoso
- **400 Bad Request**: Validación fallida
- **500 Internal Server Error**: Error en el servicio de ML

## 🔄 Flujo de Procesamiento

### 1. Configuración del Proyecto
- Proyecto Spring Boot con estructura modular
- Dependencias Maven configuradas
- Estructura de paquetes organizada

### 2. Recepción y Validación
- Endpoint REST recibe petición POST
- Controller valida datos con anotaciones Lombok
- Verificación de texto no vacío y longitud mínima

### 3. Integración Data Science
- DataScienceClient realiza llamada HTTP
- Request enviado al servicio Python (localhost:5000)
- Modelo ML procesa y clasifica el texto
- Response con predicción y probabilidad

### 4. Respuesta al Cliente
- Formateo de respuesta JSON
- Logging de operación
- Retorno HTTP 200 con resultado

## 📊 Categorías de Sentimiento

| Categoría | Emoji | Descripción |
|-----------|-------|-------------|
| Positivo  | 😊    | Sentimiento favorable o optimista |
| Negativo  | 😞    | Sentimiento desfavorable o pesimista |
| Neutral   | 😐    | Sin carga emocional clara |

## 🎯 Métricas del Sistema

- **Requests procesados**: 762
- **Categorías disponibles**: 3 (Positivo, Negativo, Neutral)
- **Accuracy del modelo**: 99%

### Distribución de Análisis

- Positivos: 280 (36.7%)
- Negativos: 152 (20.0%)
- Neutrales: 330 (43.3%)

## 🔧 Configuración Avanzada

### Timeout de Conexión

```java
@Bean
public RestTemplate restTemplate() {
    HttpComponentsClientHttpRequestFactory factory = 
        new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(3000);
    factory.setReadTimeout(3000);
    return new RestTemplate(factory);
}
```

### Manejo de Errores

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleException(Exception e) {
    // Logging del error
    log.error("Error procesando sentimiento", e);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(e.getMessage()));
}
```

## 📋 Logging

El sistema registra:
- Timestamp de cada request
- Request ID único
- Texto analizado
- Predicción obtenida
- Probabilidad del resultado
- Tiempo de procesamiento

## 🧪 Testing

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests de integración
mvn verify
```

## 🐍 Servicio Python (Requisito)

La API requiere un servicio Python ejecutándose en:

```
POST http://localhost:5000/api_sentimiento
```

Este servicio debe aceptar:

```json
{
  "text": "texto a analizar"
}
```

Y retornar:

```json
{
  "prediccion": "Positivo|Negativo|Neutral",
  "probabilidad": 0.87
}
```

## 🤝 Contribuir

1. Fork del proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add: AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.

## 👥 Autores

- Tu Nombre - [@tu_usuario](https://github.com/tu_usuario)

## 🙏 Agradecimientos

- Spring Boot Community
- Equipo de Data Science por el modelo ML
- Contribuidores del proyecto

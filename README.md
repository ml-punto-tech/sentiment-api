# 🎯 Sistema de Análisis de Sentimiento con Machine Learning

## 📋 Descripción del Proyecto

Sistema completo de análisis de sentimiento que integra **Spring Boot** para el backend API REST y **Python/Machine Learning** para el procesamiento y clasificación de texto. El sistema procesa texto en lenguaje natural y determina si el sentimiento expresado es positivo, negativo o neutral.

## 🏗️ Arquitectura del Sistema

```
┌─────────────────┐      ┌─────────────────┐      ┌─────────────────┐
│   Cliente Web   │ ──▶  │  Spring Boot    │ ──▶  │  Python ML API  │
│                 │      │   (Backend)     │      │   (Port 5000)   │
└─────────────────┘      └─────────────────┘      └─────────────────┘
                                │
                                ▼
                         ┌─────────────┐
                         │  Response   │
                         │  JSON       │
                         └─────────────┘
```

## 🚀 Características Principales

- ✅ **API REST** robusta con Spring Boot
- 🤖 **Modelo de Machine Learning** para clasificación de sentimientos
- 🔄 **Pipeline completo** de procesamiento de datos
- 📊 **Clasificación en 3 categorías**: Positivo, Negativo, Neutral
- ✨ **Validación de entrada** con Spring Validation
- 📝 **Logging completo** de requests y predicciones
- 🎨 **Respuestas JSON** estandarizadas

## 🛠️ Tecnologías Utilizadas

### Backend (Spring Boot)
- **Spring Boot** - Framework principal
- **Spring Web** - API REST
- **Spring Validation** - Validación de datos
- **Lombok** - Reducción de código boilerplate
- **RestTemplate/WebClient** - Cliente HTTP
- **Spring Boot Dev Tools** - Herramientas de desarrollo

### Data Science & ML
- **Python** - Lenguaje de programación
- **Machine Learning** - Modelos de clasificación
- **Procesamiento de Lenguaje Natural (NLP)**
- **Pandas/NumPy** - Manipulación de datos

## 📁 Estructura del Proyecto

```
src/main/java/
├── controller/
│   └── SentimentController.java
├── service/
│   └── SentimentService.java
│   └── DataScienceClient.java
├── model/
│   ├── SentimentRequest.java
│   └── SentimentResponse.java
└── config/
    └── RestTemplateConfig.java
```

## 🔄 Proceso Backend - API REST

### 1. ⚙️ Configuración del Proyecto
- Proyecto Spring Boot con Maven
- Dependencias necesarias configuradas
- Estructura de paquetes organizada

### 2. 📨 Recepción y Validación
**Endpoint**: `POST /api/sentiment`

**Request Body**:
```json
{
  "text": "El servicio es excelente y muy confiable"
}
```

**Validaciones**:
- `@NotBlank`: El texto no puede estar vacío
- `@Size(min=5)`: Longitud mínima de 5 caracteres

### 3. 🔗 Integración con Data Science
- Llamada HTTP al modelo de Python (puerto 5000)
- Endpoint: `POST http://localhost:5000/api_sentimiento`
- Comunicación mediante RestTemplate/WebClient

**Response del Modelo ML**:
```json
{
  "prediccion": "Positivo",
  "probabilidad": 0.87
}
```

### 4. ✅ Respuesta al Cliente
**HTTP 200 OK**:
```json
{
  "prevision": "Positivo",
  "probabilidad": 0.87
}
```

**Features adicionales**:
- Logging con timestamp y request ID
- Métricas de rendimiento
- Content-Type: application/json

## 📊 Pipeline de Preparación de Datos

### 1. 📥 Carga y Selección de Datos
- Extracción del dataset en formato texto crudo
- Filtrado de columnas relevantes
- Output: Archivo con columnas Texto y Sentimiento

### 2. 🧹 Limpieza y Normalización
Técnicas aplicadas:
- Conversión a minúsculas
- Eliminación de tildes, URLs, hashtags y menciones (@)
- Remoción de emojis y símbolos Unicode
- Eliminación de "ruido" digital
- Scripts automatizados de estandarización

### 3. 🎨 Categorización de Sentimientos
Clasificación en tres categorías:
- 😊 **Positivo**: 280 registros
- 😞 **Negativo**: 152 registros
- 😐 **Neutral**: 330 registros

**Total**: 762 registros procesados

### 4. 💾 Exportación del Dataset Final
- Formato de salida: CSV estructurado
- Dataset listo para entrenamiento de modelos ML
- 100% de datos limpios y categorizados

## 📈 Métricas del Sistema

| Métrica | Valor |
|---------|-------|
| Total Requests | 762 |
| Categorías | 3 |
| Accuracy | 99% |
| Registros Procesados | 762 |

## 🚦 Guía de Inicio Rápido

### Prerrequisitos
```bash
- Java 11 o superior
- Maven 3.6+
- Python 3.8+
- Dependencias Python (requirements.txt)
```

### Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/sentiment-analysis-api.git
cd sentiment-analysis-api
```

2. **Compilar el proyecto Backend**
```bash
mvn clean install
```

3. **Iniciar el servicio de Python**
```bash
cd python-ml-service
pip install -r requirements.txt
python app.py
```

4. **Iniciar Spring Boot**
```bash
mvn spring-boot:run
```

### Uso de la API

**Ejemplo con cURL**:
```bash
curl -X POST http://localhost:8080/api/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "Este producto es excelente"}'
```

**Respuesta esperada**:
```json
{
  "prevision": "Positivo",
  "probabilidad": 0.92
}
```

## 🧪 Ejemplos de Uso

### Request Positivo
```json
POST /api/sentiment
{
  "text": "Me encanta este servicio, es increíble"
}
```

### Request Negativo
```json
POST /api/sentiment
{
  "text": "Muy decepcionado con la calidad del producto"
}
```

### Request Neutral
```json
POST /api/sentiment
{
  "text": "El producto llegó en la fecha indicada"
}
```

## 🔐 Validaciones y Manejo de Errores

### Error 400 - Bad Request
```json
{
  "error": "Validation failed",
  "message": "El texto debe tener al menos 5 caracteres"
}
```

### Error 500 - Internal Server Error
```json
{
  "error": "Service unavailable",
  "message": "No se pudo conectar con el servicio de ML"
}
```

## 📝 Logging

El sistema registra:
- Timestamp de cada request
- Request ID único
- Texto analizado (parcial por privacidad)
- Predicción obtenida
- Probabilidad del resultado
- Tiempo de respuesta

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👥 Equipo

- **Backend Team** - Spring Boot API Development
- **Data Science Team** - ML Model & Data Processing

## 📞 Contacto

Para preguntas o sugerencias, por favor abre un issue en el repositorio.

---

⭐ **Si este proyecto te resultó útil, considera darle una estrella en GitHub**

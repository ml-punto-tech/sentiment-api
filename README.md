# 🎯 Sentiment Analysis API

> Sistema completo de análisis de sentimiento en tiempo real que clasifica textos en español como Positivo, Negativo o Neutral usando Machine Learning.

## 📋 Descripción

Sistema de análisis de sentimiento que integra **Spring Boot** como API Gateway y **FastAPI/Python** como motor de Machine Learning. Procesa texto en lenguaje natural y determina el sentimiento expresado con alta precisión.

### ✨ Características Principales

- 🤖 **Modelo de ML** entrenado con 762 registros
- 🎯 **Clasificación en 3 categorías** con probabilidades
- 🌐 **API REST** robusta y escalable
- ⚡ **Procesamiento NLP** avanzado en español
- 🔄 **CORS configurado** para integración web
- 📊 **Interfaz web interactiva** con feedback visual
- ✅ **Validación de entrada** en múltiples capas
- 📝 **Logging completo** de requests y predicciones

## 🏗️ Arquitectura del Sistema

```
┌──────────────────────────────────────────────────────────────┐
│                        CLIENTE WEB                            │
│                     (JavaScript/HTML)                         │
│          https://sentimient-walo.vercel.app                   │
└────────────────────────┬─────────────────────────────────────┘
                         │ HTTP POST
                         │ /api/v1/sentiment
                         ▼
┌──────────────────────────────────────────────────────────────┐
│                    SPRING BOOT API                            │
│                  (Gateway - Puerto 8080)                      │
│  ┌──────────────┐  ┌─────────────┐  ┌──────────────┐        │
│  │   CORS       │  │ Validation  │  │   Logging    │        │
│  │   Config     │  │   Layer     │  │   Layer      │        │
│  └──────────────┘  └─────────────┘  └──────────────┘        │
└────────────────────────┬─────────────────────────────────────┘
                         │ HTTP POST
                         │ /predict
                         ▼
┌──────────────────────────────────────────────────────────────┐
│                    FASTAPI ML ENGINE                          │
│                   (Python - Puerto 8000)                      │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Pipeline de Procesamiento NLP                       │   │
│  │  1. Limpieza de texto                                │   │
│  │  2. Normalización Unicode                            │   │
│  │  3. Eliminación de stopwords                         │   │
│  │  4. Vectorización                                    │   │
│  │  5. Predicción con modelo entrenado                  │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                               │
│  📦 Modelo: modelo_entrenado.joblib                          │
│  📊 Accuracy: 99%                                            │
└───────────────────────────────────────────────────────────────┘
```

## 🚀 Inicio Rápido

### Prerrequisitos

```bash
- Java 21 o superior
- Maven 3.6+
- Python 3.8+
- Git
```

### Instalación

**1. Clonar el repositorio**
```bash
git clone https://github.com/ml-punto-tech/sentiment-api.git
cd sentiment-api
```

**2. Configurar variables de entorno**
```bash
# Crear archivo .env
cp .env.example .env

# Editar las variables según tu entorno
MODEL_API_URL=http://localhost:8000
SERVER_PORT=8080
API_VERSION=v1
```

**3. Iniciar el servicio Python (FastAPI)**
```bash
# Instalar dependencias
pip install -r requirements.txt

# Descargar recursos NLTK (automático al iniciar)
# Iniciar servidor
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

**4. Iniciar el servicio Spring Boot**
```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

**5. Acceder a la aplicación**
```
- API Gateway: http://localhost:8080/api/v1/
- ML Engine: http://localhost:8000/docs (Swagger UI)
- Frontend: Abrir index.html en el navegador
```

## 📡 API Reference

### Analizar Sentimiento

**Endpoint:** `POST /api/v1/sentiment`

**Request:**
```json
{
  "text": "Este producto es excelente y superó mis expectativas"
}
```

**Response:**
```json
{
  "prevision": "Positivo",
  "probabilidad": 0.9234
}
```

**Códigos de Estado:**
- `200 OK` - Predicción exitosa
- `400 Bad Request` - Texto inválido o muy corto
- `500 Internal Server Error` - Error en el modelo ML

### Health Check

**Endpoint:** `GET /`

**Response:**
```json
{
  "status": "online",
  "model_loaded": true
}
```

## 🧪 Ejemplos de Uso

### cURL

```bash
# Sentimiento Positivo
curl -X POST http://localhost:8080/api/v1/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "Me encanta este servicio, es increíble y muy eficiente"}'

# Sentimiento Negativo
curl -X POST http://localhost:8080/api/v1/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "Muy decepcionado con la calidad, no lo recomiendo"}'

# Sentimiento Neutral
curl -X POST http://localhost:8080/api/v1/sentiment \
  -H "Content-Type: application/json" \
  -d '{"text": "El producto llegó en la fecha indicada según lo esperado"}'
```

### JavaScript (Frontend)

```javascript
const response = await fetch('http://localhost:8080/api/v1/sentiment', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ 
        text: 'Este servicio es excelente' 
    })
});

const data = await response.json();
console.log(`Sentimiento: ${data.prevision} (${data.probabilidad * 100}%)`);
```

### Python

```python
import requests

response = requests.post(
    'http://localhost:8080/api/v1/sentiment',
    json={'text': 'La atención al cliente fue excepcional'}
)

result = response.json()
print(f"Sentimiento: {result['prevision']}")
print(f"Confianza: {result['probabilidad']:.2%}")
```

## 🛠️ Tecnologías Utilizadas

### Backend (Spring Boot)
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Spring Boot | 4.0.0 | Framework principal |
| Java | 21 | Lenguaje de programación |
| Maven | 3.6+ | Gestión de dependencias |
| Lombok | Latest | Reducción de boilerplate |
| Spring Validation | Latest | Validación de datos |

### ML Engine (Python)
| Tecnología | Propósito |
|------------|-----------|
| FastAPI | Framework web asíncrono |
| scikit-learn | Modelo de clasificación ML |
| NLTK | Procesamiento de lenguaje natural |
| Pandas | Manipulación de datos |
| NumPy | Operaciones numéricas |
| Joblib | Serialización del modelo |

### Frontend
| Tecnología | Propósito |
|------------|-----------|
| Vanilla JavaScript | Lógica de la aplicación |
| HTML5/CSS3 | Interfaz de usuario |
| Fetch API | Comunicación con backend |

## 📊 Pipeline de Procesamiento NLP

### 1. Limpieza de Texto
```python
# Entrada: "¡Hola! Me ENCANTA este producto 😊 https://example.com #feliz"

# Salida después del pipeline:
# "encanta producto"
```

**Pasos aplicados:**
1. ✅ Conversión a minúsculas
2. ✅ Normalización Unicode (eliminación de tildes)
3. ✅ Remoción de URLs, hashtags, menciones
4. ✅ Eliminación de emojis y símbolos especiales
5. ✅ Eliminación de puntuación y números
6. ✅ Filtrado de stopwords en español

### 2. Vectorización y Predicción
- TF-IDF o Count Vectorizer (según el modelo)
- Clasificador entrenado (Naive Bayes, SVM, o similar)
- Cálculo de probabilidades para cada clase

## 📈 Métricas del Modelo

| Métrica | Valor |
|---------|-------|
| Dataset Total | 762 registros |
| Registros Positivos | 280 (36.7%) |
| Registros Negativos | 152 (19.9%) |
| Registros Neutrales | 330 (43.3%) |
| Accuracy | 99% |
| Idioma | Español |

## 🔐 Configuración CORS

El sistema está configurado para aceptar requests desde:
- `http://localhost:3000` (desarrollo local)
- `https://sentimient-walo.vercel.app` (producción)

Para agregar más orígenes, edita `CorsConfig.java`:
```java
.allowedOriginPatterns(
    "http://localhost:3000",
    "https://sentimient-walo.vercel.app",
    "https://tu-nuevo-dominio.com"  // Agregar aquí
)
```

## 📂 Estructura del Proyecto

```
sentiment-api/
├── src/main/java/com/one8/sentiment_tech_api/
│   ├── config/
│   │   ├── ClientConfig.java          # Configuración RestClient
│   │   └── CorsConfig.java            # Configuración CORS
│   ├── controller/
│   │   └── SentimentController.java   # Endpoints REST
│   ├── service/
│   │   └── SentimentService.java      # Lógica de negocio
│   └── model/
│       ├── SentimentRequest.java      # DTO Request
│       └── SentimentResponse.java     # DTO Response
├── src/main/resources/
│   └── application.yaml               # Configuración Spring
├── main.py                            # FastAPI ML Engine
├── modelo_entrenado.joblib            # Modelo ML serializado
├── requirements.txt                   # Dependencias Python
├── pom.xml                            # Configuración Maven
├── index.js                           # Frontend JavaScript
└── README.md                          # Documentación
```

## 🔧 Configuración Avanzada

### Variables de Entorno (application.yaml)

```yaml
spring:
  application:
    name: sentiment-tech-api
  profiles:
    active: dev

api:
  version: v1
  base-path: /api/${api.version}

server:
  port: 8080

model:
  api:
    url: http://localhost:8000
```

### Personalizar el Modelo ML

Para reentrenar o actualizar el modelo:

1. Preparar dataset en formato CSV con columnas `text` y `sentiment`
2. Ejecutar script de entrenamiento (no incluido en el repo)
3. Guardar nuevo modelo como `modelo_entrenado.joblib`
4. Reiniciar el servicio FastAPI

## 🐛 Troubleshooting

### Error: "Modelo no encontrado"
```bash
# Verificar que modelo_entrenado.joblib existe
ls -la modelo_entrenado.joblib

# Si no existe, descargar o entrenar el modelo
```

### Error: "Connection refused" al ML Engine
```bash
# Verificar que FastAPI está corriendo
curl http://localhost:8000/

# Revisar logs de Python
tail -f fastapi.log
```

### Error CORS en el frontend
```javascript
// Verificar que el origen está en la lista de permitidos
// Revisar CorsConfig.java y actualizar allowedOriginPatterns
```

## 🧪 Testing

### Backend (Spring Boot)
```bash
mvn test
```

### ML Engine (Python)
```bash
# Instalar pytest
pip install pytest

# Ejecutar tests (crear tests primero)
pytest tests/
```

### Frontend
```bash
# Abrir en navegador y usar DevTools Console
# Revisar Network tab para debugging de requests
```

## 🚀 Deployment

### Opción 1: Docker (Recomendado)

```dockerfile
# Dockerfile para Spring Boot
FROM openjdk:21-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```dockerfile
# Dockerfile para FastAPI
FROM python:3.9-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY . .
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
```

### Opción 2: Cloud Services
- **Spring Boot:** Railway, Render, Heroku
- **FastAPI:** Render, Railway, AWS Lambda
- **Frontend:** Vercel, Netlify, GitHub Pages

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

### Guías de Contribución
- Mantener el estilo de código consistente
- Agregar tests para nuevas funcionalidades
- Actualizar documentación según sea necesario
- Revisar que el código pase los linters

## 📝 Roadmap

- [ ] Agregar soporte para más idiomas (inglés, portugués)
- [ ] Implementar caché de predicciones frecuentes
- [ ] Dashboard de métricas en tiempo real
- [ ] API de batch processing para múltiples textos
- [ ] Integración con bases de datos (PostgreSQL/MongoDB)
- [ ] Sistema de feedback para mejorar el modelo
- [ ] Autenticación y rate limiting
- [ ] Containerización completa con Docker Compose

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## 👥 Equipo

- **Backend Team** - Desarrollo Spring Boot & API Gateway
- **Data Science Team** - Modelo ML & Pipeline NLP
- **Frontend Team** - Interfaz Web Interactiva

## 📞 Contacto & Soporte

- **GitHub Issues:** [Reportar un problema](https://github.com/ml-punto-tech/sentiment-api/issues)
- **Documentación API:** http://localhost:8000/docs (Swagger UI)
- **Email:** soporte@sentiment-api.com

---


**⭐ Si este proyecto te resultó útil, considera darle una estrella en GitHub ⭐**

Hecho con ❤️ por el equipo de ml-punto-tech

[🌟 Star](https://github.com/ml-punto-tech/sentiment-api) • [🐛 Report Bug](https://github.com/ml-punto-tech/sentiment-api/issues) • [💡 Request Feature](https://github.com/ml-punto-tech/sentiment-api/issues)


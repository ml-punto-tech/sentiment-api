# 🧠 SentimentAPI — Data Science MVP
> **Hackathon ONE | Equipo Data Science**

![Status](https://img.shields.io/badge/Status-MVP_Finalizado-success)
![Python](https://img.shields.io/badge/Python-3.10%2B-blue?logo=python&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-0.95%2B-009688?logo=fastapi&logoColor=white)
![Scikit-Learn](https://img.shields.io/badge/ML-Scikit--Learn-orange?logo=scikit-learn&logoColor=white)

**SentimentAPI** es un microservicio inteligente que clasifica el feedback de usuarios (reseñas, comentarios, encuestas) y devuelve una predicción de sentimiento consumible vía API REST.

---

## 📋 Tabla de Contenidos
- [Equipo y Roles](#-equipo-y-roles)
- [Descripción General](#-descripción-general)
- [Arquitectura y Flujo](#-arquitectura-y-flujo)
- [Datasets y Diccionario de Datos](#-datasets-y-diccionario-de-datos)
- [Pipeline de Procesamiento](#-pipeline-de-procesamiento)
- [QA y Testing (Resultados)](#-qa-y-testing-calidad-y-reproducibilidad)
- [Uso de la API](#-uso-de-la-api)
- [Instalación y Ejecución](#-instalación-y-ejecución)

---

## 👥 Equipo y Roles

| Rol | Miembro |
| :--- | :--- |
| **Líder de Integración (Java/DS)** | Eduardo |
| **Especialista NLP** | Marely |
| **Científico/a de ML** | Alex |
| **Data QA & Documentation** | Agustin |

---

## 📖 Descripción General

Este proyecto implementa un pipeline de **Natural Language Processing (NLP)** supervisado. El objetivo es recibir texto crudo desde un Back-end (Java) y retornar:
1.  **Predicción:** `Positivo`, `Neutral` o `Negativo`.
2.  **Probabilidad:** Score de confianza del modelo.

### Objetivos de Data Science
* ✅ **Dataset:** Limpieza y etiquetado para entrenamiento supervisado.
* ✅ **Pipeline:** Normalización de texto y vectorización reproducible.
* ✅ **Modelo:** Entrenamiento de modelo base (TF-IDF + Logistic Regression).
* ✅ **QA:** Evidencia de calidad y pruebas de estrés de datos.

---

## 🏗 Arquitectura y Flujo

El Back-end envía un JSON con el campo `text`. El microservicio en Python procesa, clasifica y responde.

![Flujo y Arquitectura](ruta/a/tu/imagen_arquitectura.png)
*Figura 1. Microservicio de sentimientos (Python API) — Flujo y arquitectura.*

**Tech Stack:**
* **Entrada:** JSON.
* **Motor:** `scikit-learn`, `joblib`, `pandas`.
* **API:** `FastAPI`, `uvicorn`.

---

## 💾 Datasets y Diccionario de Datos

Se utilizan dos datasets principales en el flujo de trabajo:

### 1. Dataset Final (`dataset_listo_para_ML.csv`)
*Dataset limpio utilizado para el entrenamiento del modelo.*

| Variable | Tipo | Descripción |
| :--- | :--- | :--- |
| `Texto_Limpio` | String | Texto normalizado (minúsculas, sin tildes/ASCII, sin ruido). |
| `Sentimiento_Final` | String | Target: `Positivo`, `Neutral`, `Negativo`. |

### 2. Dataset Crudo (`sentimentdataset_es.csv`)
*Contiene 15 columnas originales incluyendo `Timestamp`, `User`, `Platform`, `Hashtags`, etc.*

---

## ⚙️ Pipeline de Procesamiento

El notebook `Procesamiento_y_Clasificacion_de_Datos_SentimentAPI.ipynb` ejecuta las siguientes transformaciones:

1.  **Carga y Selección:** Extracción de columnas `Text` y `Sentiment`.
2.  **Limpieza:**
    * Conversión a minúsculas.
    * Eliminación de URLs, Hashtags, Menciones y Emojis.
    * Normalización ASCII (eliminación de tildes).
3.  **Categorización:** Mapeo de emociones complejas a las 3 clases base.
    * *Nota:* Sentimientos ambiguos no mapeados se asignan a `Neutral` (Regla de negocio MVP).

---

## 🧪 QA y Testing (Calidad y Reproducibilidad)

### 6.A Testing de Datos (ETL)
Validamos que el dataset final sea íntegro y consistente antes del entrenamiento.

* **Integridad:** 0 Nulos, 0 filas perdidas.
* **Duplicados:** Se conservaron 26 duplicados (3.55%) intencionalmente para reforzar patrones de feedback comunes.
* **Distribución de Clases:**

![Distribución de Clases](class_distribution.png)
*Figura 2. Distribución de clases en el dataset final: Neutral (45.1%), Positivo (34.2%), Negativo (20.8%).*

### 6.B Testing de Machine Learning
**Modelo:** Pipeline `TF-IDF Vectorizer` + `Logistic Regression`.

| Métrica | Valor (Holdout 20%) |
| :--- | :--- |
| **Accuracy** | **0.7075** |
| **F1 Macro** | **0.6751** |
| **F1 Weighted** | **0.6978** |

**Matriz de Confusión:**
![Matriz de Confusión](confusion_matrix.png)
*Figura 3. Matriz de confusión. Se observa que la clase 'Negativo' es la más difícil de clasificar (menor Recall).*

**Validación Cruzada (5-Fold):**
El modelo demuestra estabilidad con un F1 Macro promedio de **0.6061 ± 0.0241**.

---

## 🔌 Uso de la API

### Endpoint: `/sentiment`

**Request (JSON):**
JSON
{
  "text": "El servicio fue excelente y muy rápido"
}

**Response OK (200):**
JSON
{
  "prevision": "Positivo",
  "probabilidad": 0.87
}

**Response Error:**
JSON
{
  "error": "El campo 'text' es obligatorio y debe tener al menos 3 caracteres."
}

¡Claro que sí! Aquí tienes el contenido transformado en un archivo README.md optimizado para GitHub.

He aplicado las siguientes mejoras estéticas y funcionales:

Badges (Escudos): Para resaltar el stack tecnológico y el estado del proyecto.

Highlighting de Sintaxis: Bloques de código con colores para JSON y Bash.

Tablas Markdown: Formateo limpio para los diccionarios de datos y métricas.

Jerarquía Visual: Uso de emojis y negritas para mejorar la legibilidad.

Placeholders de Imágenes: He dejado el código listo para que las imágenes se vean (solo asegúrate de subir los archivos .png a tu repo).

Copia el siguiente bloque de código y guárdalo como README.md en la raíz de tu repositorio:

Markdown

# 🧠 SentimentAPI — Data Science MVP
> **Hackathon ONE | Equipo Data Science**

![Status](https://img.shields.io/badge/Status-MVP_Finalizado-success)
![Python](https://img.shields.io/badge/Python-3.10%2B-blue?logo=python&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-0.95%2B-009688?logo=fastapi&logoColor=white)
![Scikit-Learn](https://img.shields.io/badge/ML-Scikit--Learn-orange?logo=scikit-learn&logoColor=white)

**SentimentAPI** es un microservicio inteligente que clasifica el feedback de usuarios (reseñas, comentarios, encuestas) y devuelve una predicción de sentimiento consumible vía API REST.

---

## 📋 Tabla de Contenidos
- [Equipo y Roles](#-equipo-y-roles)
- [Descripción General](#-descripción-general)
- [Arquitectura y Flujo](#-arquitectura-y-flujo)
- [Datasets y Diccionario de Datos](#-datasets-y-diccionario-de-datos)
- [Pipeline de Procesamiento](#-pipeline-de-procesamiento)
- [QA y Testing (Resultados)](#-qa-y-testing-calidad-y-reproducibilidad)
- [Uso de la API](#-uso-de-la-api)
- [Instalación y Ejecución](#-instalación-y-ejecución)

---

## 👥 Equipo y Roles

| Rol | Miembro |
| :--- | :--- |
| **Líder de Integración (Java/DS)** | Eduardo |
| **Especialista NLP** | Marely |
| **Científico/a de ML** | Alex |
| **Data QA & Documentation** | Agustin |

---

## 📖 Descripción General

Este proyecto implementa un pipeline de **Natural Language Processing (NLP)** supervisado. El objetivo es recibir texto crudo desde un Back-end (Java) y retornar:
1.  **Predicción:** `Positivo`, `Neutral` o `Negativo`.
2.  **Probabilidad:** Score de confianza del modelo.

### Objetivos de Data Science
* ✅ **Dataset:** Limpieza y etiquetado para entrenamiento supervisado.
* ✅ **Pipeline:** Normalización de texto y vectorización reproducible.
* ✅ **Modelo:** Entrenamiento de modelo base (TF-IDF + Logistic Regression).
* ✅ **QA:** Evidencia de calidad y pruebas de estrés de datos.

---

## 🏗 Arquitectura y Flujo

El Back-end envía un JSON con el campo `text`. El microservicio en Python procesa, clasifica y responde.

![Flujo y Arquitectura](ruta/a/tu/imagen_arquitectura.png)
*Figura 1. Microservicio de sentimientos (Python API) — Flujo y arquitectura.*

**Tech Stack:**
* **Entrada:** JSON.
* **Motor:** `scikit-learn`, `joblib`, `pandas`.
* **API:** `FastAPI`, `uvicorn`.

---

## 💾 Datasets y Diccionario de Datos

Se utilizan dos datasets principales en el flujo de trabajo:

### 1. Dataset Final (`dataset_listo_para_ML.csv`)
*Dataset limpio utilizado para el entrenamiento del modelo.*

| Variable | Tipo | Descripción |
| :--- | :--- | :--- |
| `Texto_Limpio` | String | Texto normalizado (minúsculas, sin tildes/ASCII, sin ruido). |
| `Sentimiento_Final` | String | Target: `Positivo`, `Neutral`, `Negativo`. |

### 2. Dataset Crudo (`sentimentdataset_es.csv`)
*Contiene 15 columnas originales incluyendo `Timestamp`, `User`, `Platform`, `Hashtags`, etc.*

---

## ⚙️ Pipeline de Procesamiento

El notebook `Procesamiento_y_Clasificacion_de_Datos_SentimentAPI.ipynb` ejecuta las siguientes transformaciones:

1.  **Carga y Selección:** Extracción de columnas `Text` y `Sentiment`.
2.  **Limpieza:**
    * Conversión a minúsculas.
    * Eliminación de URLs, Hashtags, Menciones y Emojis.
    * Normalización ASCII (eliminación de tildes).
3.  **Categorización:** Mapeo de emociones complejas a las 3 clases base.
    * *Nota:* Sentimientos ambiguos no mapeados se asignan a `Neutral` (Regla de negocio MVP).

---

## 🧪 QA y Testing (Calidad y Reproducibilidad)

### 6.A Testing de Datos (ETL)
Validamos que el dataset final sea íntegro y consistente antes del entrenamiento.

* **Integridad:** 0 Nulos, 0 filas perdidas.
* **Duplicados:** Se conservaron 26 duplicados (3.55%) intencionalmente para reforzar patrones de feedback comunes.
* **Distribución de Clases:**

![Distribución de Clases](class_distribution.png)
*Figura 2. Distribución de clases en el dataset final: Neutral (45.1%), Positivo (34.2%), Negativo (20.8%).*

### 6.B Testing de Machine Learning
**Modelo:** Pipeline `TF-IDF Vectorizer` + `Logistic Regression`.

| Métrica | Valor (Holdout 20%) |
| :--- | :--- |
| **Accuracy** | **0.7075** |
| **F1 Macro** | **0.6751** |
| **F1 Weighted** | **0.6978** |

**Matriz de Confusión:**
![Matriz de Confusión](confusion_matrix.png)
*Figura 3. Matriz de confusión. Se observa que la clase 'Negativo' es la más difícil de clasificar (menor Recall).*

**Validación Cruzada (5-Fold):**
El modelo demuestra estabilidad con un F1 Macro promedio de **0.6061 ± 0.0241**.

---

## 🔌 Uso de la API

### Endpoint: `/sentiment`

**Request (JSON):**
JSON
{
  "text": "El servicio fue excelente y muy rápido"
}
Response OK (200):

JSON

{
  "prevision": "Positivo",
  "probabilidad": 0.87
}
Response Error:

JSON

{
  "error": "El campo 'text' es obligatorio y debe tener al menos 3 caracteres."
}


🚀 Instalación y Ejecución
Requisitos:
- Python 3.10+
- Librerías: pandas, scikit-learn, joblib, fastapi, uvicorn, python-multipart.

Pasos
Clonar el repositorio:
git clone [https://github.com/tu-usuario/sentiment-api.git](https://github.com/tu-usuario/sentiment-api.git)
cd sentiment-api

Instalar dependencias:
pip install -r requirements.txt

Entrenar el modelo (Opcional si ya tienes el .joblib): Ejecutar el notebook ModeloSentimentAPI.ipynb para generar modelo_entrenado.joblib.

Levantar la API:
uvicorn main:app --reload
Fecha de actualización: 2025-12-29



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
* ✅ **Dataset:** Curación y documentación del dataset oficial (`dataset_listo_para_ML_esp.csv`) para entrenamiento supervisado.
* ✅ **Pipeline:** Normalización de texto y vectorización reproducible (TF-IDF).
* ✅ **Modelo (final):** Entrenamiento con **SVM (LinearSVC)** y **calibración de probabilidades** (`CalibratedClassifierCV`) para devolver `probabilidad` en la API.
* ✅ **QA:** Evidencia de calidad, decisiones de limpieza, replicabilidad (semillas/paths) y resultados de evaluación.
---

## 🏗 Arquitectura y Flujo

El Back-end envía un JSON con el campo `text`. El microservicio en Python procesa, clasifica y responde.

![Flujo y Arquitectura](images/architecture_microservice.png)
*Figura 1. Microservicio de sentimientos (Python API) — Flujo y arquitectura.*

**Tech Stack:**
* **Entrada:** JSON.
* **Motor:** `scikit-learn`, `joblib`, `pandas`.
* **API:** `FastAPI`, `uvicorn`.

---

### Dataset Oficial (único) — `dataset_listo_para_ML_esp.csv`
Dataset final en español, listo para entrenamiento/evaluación del modelo.

**Origen del dataset:** Dataset propio construido por el equipo (Hackathon ONE).  

**Columnas**
| Variable | Tipo | Descripción |
| :--- | :--- | :--- |
| `texto` | String | Texto crudo en español (input del modelo). |
| `sentimiento` | String | Target con 3 clases: `positivo`, `neutral`, `negativo`. |

**QA rápido (dataset oficial)**
- Nulos en `texto`: 0
- Nulos en `sentimiento`: 0
- Etiquetas inválidas: 0 (solo `positivo/neutral/negativo`)
- Recomendación: mantener etiquetas en minúscula para consistencia del pipeline.




### Procesamiento aplicado (notebook final)
El notebook **`Modelo_SentimentAPI.ipynb`** entrena el modelo a partir del dataset oficial y aplica limpieza antes de vectorizar.

**Limpieza (resumen)**
- Minúsculas.
- Eliminación de puntuación/caracteres especiales.
- Eliminación de stopwords (lista manual).
- **Preservación de negaciones** (`no`, `ni`, `nunca`, etc.) para no perder polaridad.

**Feature Engineering**
- Vectorización: `TfidfVectorizer(max_features=5000, ngram_range=(1,3))`

---

## 🧪 QA y Testing (Calidad y Reproducibilidad)

### 6.A Testing de Datos (ETL)
Validamos que el dataset final sea íntegro y consistente antes del entrenamiento.

* **Integridad:** 0 nulos en columnas críticas, sin pérdida de registros.
* **Duplicados:** Se detectaron **424 duplicados (12.96%)** por texto.
  *Decisión sugerida:* conservarlos (refuerzan frases comunes) o deduplicar (reduce sesgo). Queda explicitado como criterio de QA.
* **Distribución de Clases:**

![Distribución de Clases](images/class_distribution.png)
*Figura 2. Distribución de clases (dataset v2): Negativo (39.73%), Positivo (37.62%), Neutral (22.65%).*

**Problemas y resoluciones (Dataset):**
- **Incidente de encoding (Excel → CSV):** se detectó “mojibake”/caracteres corruptos al importar el dataset desde Excel.
  **Resolución:** exportación a CSV y normalización del encoding antes de integrar al pipeline.

### 6.B Testing de Machine Learning (modelo final)
**Modelo final:** `TF-IDF` + **SVM (LinearSVC)** + **Calibración de probabilidades** (`CalibratedClassifierCV`).

**Configuración de evaluación**
- Split estratificado **80/20** (`random_state=42`)
- Optimización: `GridSearchCV` sobre `C` con **5-fold** (scoring = accuracy)
- Output: predicción (`positivo/neutral/negativo`) + **probabilidad** (post-calibración)

**Resultados (holdout 20%)**
- **Accuracy:** ~**82.78%**
- (Recomendado reportar también F1 por clase por desbalance residual / confusiones entre neutral y extremos)

**Clasificación (resumen)**
- `negativo`: precision ~0.84 | recall ~0.75 | f1 ~0.79  
- `neutral`: precision ~0.80 | recall ~0.80 | f1 ~0.80  
- `positivo`: precision ~0.85 | recall ~0.93 | f1 ~0.89  

**Matriz de Confusión**
![Matriz de Confusión](images/confusion_matrix_v2.png)

---

## 📸 Evidencia visual (corrida final)

> Capturas y gráficas generadas por el equipo Data Science durante la corrida final del pipeline.

### Proceso general (del caos al modelo)
![del_caos_al_modelo](https://github.com/user-attachments/assets/54f0ba26-2472-400a-850c-05e762b83c9f)

### Limpieza del dataframe (registros eliminados vs conservados)
![analisis_limpieza_dataframe](https://github.com/user-attachments/assets/22f34c24-5a07-4eb2-b85a-ee64b88f441c)

### Distribución final de sentimientos
![distribucion_sentimientos_final](https://github.com/user-attachments/assets/24e3b47c-9ba2-4142-bce8-c83330d8eda7)

---

## 🔌 Uso de la API

### Endpoint: `/sentiment`

**Request (JSON):**
json
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

🚀 Instalación y Ejecución
Requisitos:
- Python 3.10+
- Librerías: pandas, scikit-learn, joblib, fastapi, uvicorn, python-multipart.

Pasos
1) Clonar el repositorio
git clone https://github.com/AgusLopez50/sentiment-api.git
cd sentiment-api

2) Instalar dependencias
pip install -r requirements.txt

3) Entrenar el modelo (Opcional si ya tienes el .joblib)
- Ejecutar el notebook ModeloSentimentAPI.ipynb para generar modelo_entrenado.joblib.

4) Levantar la API
uvicorn main:app --reload

Fecha de actualización: 2026-01-05

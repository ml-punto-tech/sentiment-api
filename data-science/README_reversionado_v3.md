# SentimentAPI — Data Science  
**MVP Hackathon ONE | Equipo Data Science — Procesamiento, Clasificación y Modelo ML**  

> ✅ README listo para **GitHub**. Guardalo como `README.md` para renderizado automático.

---

## 📌 Notebooks
- `Procesamiento_y_Clasificacion_de_Datos_SentimentAPI.ipynb`
- `Modelo_SentimentAPI.ipynb`

---

## 👥 Equipo y roles

| Rol | Integrante |
|---|---|
| Líder de Integración (Java/DS) | Eduardo |
| Especialista NLP | Marely |
| Científico/a de ML | Alex |
| Data QA & Documentation | Agustin |

---

## 1. Descripción general
**SentimentAPI** es un MVP que clasifica el **sentimiento** de textos de feedback (reseñas, comentarios, encuestas o publicaciones) y devuelve una predicción consumible por aplicaciones (Back-end Java u otros clientes) en formato **JSON**, incluyendo una **probabilidad** asociada.

---

## 2. Objetivos del equipo de Data Science
- Preparar y documentar un dataset limpio y etiquetado (**positivo / neutral / negativo**) para entrenamiento supervisado.
- Implementar un pipeline reproducible de **procesamiento de texto** y **categorización**.
- Entrenar un modelo base de NLP + ML (**TF‑IDF + SVM calibrado**) y exportarlo como artefacto (**joblib**).
- Proveer evidencia de calidad (**QA**), resultados de pruebas y ejemplos reales para la demo.

---

## 3. Arquitectura y flujo (integración con Back-end)
1. El Back-end (Java) envía un JSON con el campo `text` al microservicio de sentimientos en Python (FastAPI).
2. El servicio aplica vectorización **TF‑IDF** y un modelo entrenado (**SVM calibrado**), retornando `prevision` y `probabilidad`.

### 3.1 Diagrama de arquitectura (en el repo)
Ubicación sugerida:
- `docs/images/architecture_microservice.png`

Inserción en Markdown:
```md
![Microservicio de Sentimientos — Flujo & Arquitectura](docs/images/architecture_microservice.png)
```

---

## 4. Dataset oficial (único en uso)
✅ **Dataset en uso:** `dataset_listo_para_ML_esp.csv`  
**Columnas:** `texto`, `sentimiento` (clases: `positivo`, `neutral`, `negativo`)

**Resumen rápido:**
- **Registros:** **3453**
- **Valores nulos:** **0** en `texto` | **0** en `sentimiento`
- **Textos vacíos:** **0**
- **Duplicados por texto:** **0 (0.00%)**

**Distribución de clases:**
| Clase | Cantidad | % |
|---|---:|---:|
| `positivo` | 1198 | 34.7% |
| `neutral` | 1142 | 33.1% |
| `negativo` | 1113 | 32.2% |

> Nota: las etiquetas vienen en **minúscula**. Si la API espera capitalización, aplicar `.title()` al construir el response.

---

## 5. Procesamiento y clasificación de datos (ETL)
El notebook de procesamiento transforma fuentes crudas en el dataset final.

**Pipeline (alto nivel):**
- **Carga y selección** de columnas relevantes.
- **Normalización/Limpieza** (URLs, menciones, emojis, símbolos, espacios múltiples, etc.).
- **Categorización** a 3 clases (`positivo / neutral / negativo`).
- **Exportación** del dataset final.

### 5.1 Evidencia visual del proceso (capturas del equipo)
```md
![Del caos al modelo — proceso](docs/images/process_overview_team.png)
![Análisis de limpieza — eliminaciones](docs/images/cleaning_analysis_team.png)
![Distribución de sentimientos — dataset final](docs/images/sentiment_distribution_team.png)
```

---

## 6. QA y Testing (Calidad y Reproducibilidad)

### 6.1 Checks automáticos sobre dataset oficial
| Chequeo | Resultado |
|---|---|
| Registros totales | **3453** |
| Nulos en `texto` | **0** |
| Vacíos en `texto` | **0** |
| Nulos en `sentimiento` | **0** |
| Duplicados por texto | **0 (0.00%)** |
| Espacios dobles (`\s{2,}`) | **0 (0.00%)** |
| Filas con mayúsculas | **3115 (90.21%)** |
| Filas con caracteres no-ASCII | **992 (28.73%)** |
| Filas con URLs | **71 (2.06%)** |
| Filas con `#` o `@` | **404 (11.70%)** |

### 6.2 Longitud de textos (en palabras)
- min: **1**
- p25: **11**
- mediana: **17**
- media: **21.41**
- p75: **29**
- max: **585**

---

## 7. Decisiones de QA (para reproducibilidad)
- **Dataset único y trazable:** se define `dataset_listo_para_ML_esp.csv` como **única fuente oficial** para entrenamiento e inferencia.
- **Consistencia de etiquetas:** se valida que `sentimiento` ∈ {positivo, neutral, negativo} (sin clases fuera de dominio).
- **Integridad:** se valida ausencia de nulos/vacíos en campos críticos (`texto`, `sentimiento`).
- **Trazabilidad de cambios:** ante actualizaciones de dataset, se requiere re‑entrenar el modelo y regenerar evidencias (métricas + gráficos).
- **Riesgos conocidos:** presencia de URLs/hashtags/menciones (dominio redes) y outliers de longitud; se documenta para futuras mejoras.

---

## 8. Testing — Machine Learning (Modelo)
**Modelo productivo (según notebook):**
- **Vectorización:** `TfidfVectorizer(max_features=5000, ngram_range=(1,3))`
- **Clasificador:** `LinearSVC(C=1.0)` + `CalibratedClassifierCV` (para obtener probabilidades)
- **Balanceo (experimento):** SMOTE aplicado al set de entrenamiento (comparado contra versión “Vieja Confiable” sin balanceo)

**Métricas reportadas por el equipo (capturas de ejecución):**
- **Accuracy final:** **82.8%**  
- Tabla por clase (promedio): precisión y F1 (ver imagen “Del caos al modelo”).

> Nota: si cambia el dataset o el pipeline, se debe re‑ejecutar el notebook y actualizar estas métricas.

---

## 9. Ejemplos para demostración (inferencia)
| Input (`text`) | Predicción | Probabilidad |
|---|---|---|
| "la euforia del lanzamiento exitoso de un producto" | positivo | 0.53 |
| "decepcion con el servicio en un restaurante local" | neutral | 0.71 |
| "amarga experiencia en el departamento de atencion..." | negativo | 0.39 |

---

## 10. Contrato de integración (API)

### Request
```json
{ "text": "..." }
```

### Response OK
```json
{ "prevision": "Positivo", "probabilidad": 0.87 }
```

### Response Error
```json
{ "error": "El campo \"text\" es obligatorio..." }
```

---

## 11. Requisitos y ejecución
- **Python 3.10+**
- **Librerías principales:** `pandas`, `numpy`, `scikit-learn`, `joblib`, `fastapi`, `uvicorn`
- **Si se usa SMOTE:** `imbalanced-learn`

**Pasos recomendados**
1. Ejecutar `Procesamiento_y_Clasificacion_de_Datos_SentimentAPI.ipynb` para generar/validar el dataset.
2. Ejecutar `Modelo_SentimentAPI.ipynb` para entrenar y exportar el modelo (`modelo_entrenado.joblib`).
3. Levantar la API con FastAPI y consultar el modelo serializado.

---

## 12. Evidencia (gráficos)
Estructura sugerida:
```text
docs/images/
  architecture_microservice.png
  process_overview_team.png
  cleaning_analysis_team.png
  sentiment_distribution_team.png
  class_distribution_v2.png
  duplicates_summary_v2.png
  text_length_words_hist_v2.png
  top_words_pos_v2.png
  top_words_neu_v2.png
  top_words_neg_v2.png
  confusion_matrix_v2.png
```

Inserción (ejemplo):
```md
![Distribución de clases](docs/images/class_distribution_v2.png)
![Duplicados](docs/images/duplicates_summary_v2.png)
![Longitud de textos](docs/images/text_length_words_hist_v2.png)
![Top positivo](docs/images/top_words_pos_v2.png)
![Top neutral](docs/images/top_words_neu_v2.png)
![Top negativo](docs/images/top_words_neg_v2.png)
![Matriz de confusión](docs/images/confusion_matrix_v2.png)
```

---

## 13. Troubleshooting (errores típicos)
| Problema | Causa común | Solución rápida |
|---|---|---|
| `UnicodeDecodeError` / caracteres raros | Encoding inconsistente | Re‑exportar a CSV y forzar UTF‑8/Latin‑1. |
| `FileNotFoundError` en notebooks | Rutas relativas fuera del root | Ejecutar desde el root del repo o usar carpeta `data/`. |
| Columnas no coinciden (`KeyError`) | Cambió el nombre de columnas | Verificar columnas del CSV y actualizar variables. |
| Métricas no coinciden | Modelo viejo con dataset nuevo | Re‑entrenar y regenerar evidencia. |

---

### ✅ Estado del MVP
- ETL documentado ✅  
- Dataset oficial integrado ✅  
- QA base documentado ✅  
- Contrato de API definido ✅  
- Evidencia visual y métricas ✅  

_Última actualización: 2026-01-21_

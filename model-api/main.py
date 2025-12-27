from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import joblib
import numpy as np
import os

# --- CONFIGURACIÓN ---
MODEL_PATH = "modelo_entrenado.joblib"

# 1. Definir el contrato de entrada
class SentimentRequest(BaseModel):
    text: str

# 2. Inicializar App
app = FastAPI(
    title="API Sentimiento",
    version="1.0.0"
)

# 3. Cargar el Modelo (Pipeline) al inicio
model = None

@app.on_event("startup")
def load_model():
    global model
    if os.path.exists(MODEL_PATH):
        try:
            model = joblib.load(MODEL_PATH)
            print(f"✅ Modelo cargado exitosamente: {MODEL_PATH}")
        except Exception as e:
            print(f"❌ Error crítico cargando el modelo: {e}")
    else:
        print(f"⚠️ ADVERTENCIA: No se encontró el archivo {MODEL_PATH}")

# 4. Endpoint para el Backend Java
@app.post("/api_sentimiento") # Ajusté la ruta para que coincida con tu carpeta si quieres
def predict_sentiment(request: SentimentRequest):
    global model
    if not model:
        raise HTTPException(status_code=500, detail="Modelo no disponible en el servidor.")

    try:
        # A. Predecir
        # Como usamos un Pipeline, podemos pasar el texto directo.
        # El pipeline se encarga de vectorizar internamente.
        input_data = [request.text] 
        prediction = model.predict(input_data)[0]
        
        # B. Probabilidad
        probs = model.predict_proba(input_data)[0]
        max_prob = float(np.max(probs))

        # C. Respuesta JSON estricta para Java
        # Java espera: String prevision, double probabilidad
        return {
            "prevision": str(prediction),
            "probabilidad": round(max_prob, 4) # 4 decimales para mayor precisión
        }

    except Exception as e:
        print(f"Error procesando solicitud: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/")
def health_check():
    return {"status": "online", "model_loaded": model is not None}
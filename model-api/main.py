from fastapi import FastAPI, HTTPException, Request
from pydantic import BaseModel, Field
from contextlib import asynccontextmanager
from pathlib import Path
import joblib
import numpy as np
import logging
import re
import unicodedata
from starlette.datastructures import State

# -------------------------------------------------------------------
# LOGGING
# -------------------------------------------------------------------
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# -------------------------------------------------------------------
# CONFIGURACIÓN
# -------------------------------------------------------------------
BASE_DIR = Path(__file__).resolve().parent
MODEL_PATH = BASE_DIR / "modelo_entrenado.joblib"

# Stopwords manuales del Colab para evitar descargas en el servidor
STOP_WORDS_MANUAL = {
    'de', 'la', 'que', 'el', 'en', 'y', 'a', 'los', 'del', 'se', 'las', 'por', 'un', 'para',
    'con', 'una', 'su', 'al', 'lo', 'como', 'mas', 'pero', 'sus', 'le', 'ya', 'o', 'este',
    'si', 'porque', 'esta', 'entre', 'cuando', 'muy', 'sin', 'sobre', 'tambien', 'me', 'hasta',
    'hay', 'donde', 'quien', 'desde', 'todo', 'nos', 'durante', 'todos', 'uno', 'les',
    'contra', 'otros', 'ese', 'eso', 'ante', 'ellos', 'e', 'esto', 'mi', 'antes', 'algunos',
    'unos', 'yo', 'otro', 'otras', 'otra', 'cual', 'poco', 'ella', 'estar',
    'estas', 'algunas', 'algo', 'nosotros', 'mis', 'tu', 'te', 'ti', 'tus',
    'ellas', 'nosotras', 'vosotros', 'vosotras', 'os', 'mio', 'mia', 'mios', 'mias', 'tuyo',
    'tuya', 'tuyos', 'tuyas', 'suyo', 'suya', 'suyos', 'suyas', 'nuestro', 'nuestra',
    'nuestros', 'nuestras', 'vuestro', 'vuestra', 'vuestros', 'vuestras', 'es', 'son', 'fue',
    'era', 'eramos', 'fui', 'fuiste', 'fueron'
}
NEGACIONES_A_PRESERVAR = {'no', 'ni', 'nunca', 'jamas', 'tampoco', 'nada', 'sin'}
STOP_WORDS_FINAL = STOP_WORDS_MANUAL - NEGACIONES_A_PRESERVAR

# -------------------------------------------------------------------
# FUNCIONES DE LIMPIEZA (Adaptadas exactamente del Colab)
# -------------------------------------------------------------------

def limpiar_texto_para_modelo(texto: str) -> str:
    """Aplica la limpieza exacta que usó el modelo en entrenamiento"""
    if not isinstance(texto, str):
        return ""
    
    # 1. Normalización y minúsculas
    texto = texto.lower()
    
    # 2. Eliminar caracteres especiales (manteniendo letras para el vectorizador)
    texto = re.sub(r'[^\w\s]', '', texto)
    
    # 3. Filtrar stopwords preservando negaciones
    palabras = [word for word in texto.split() if word not in STOP_WORDS_FINAL]
    
    return " ".join(palabras).strip()

# -------------------------------------------------------------------
# MODELOS Pydantic (Sin cambios)
# -------------------------------------------------------------------
class SentimentRequest(BaseModel):
    text: str = Field(..., min_length=1, description="Texto a analizar")

class SentimentResponse(BaseModel):
    prevision: str
    probabilidad: float

# -------------------------------------------------------------------
# LIFESPAN (Sin cambios en lógica)
# -------------------------------------------------------------------
@asynccontextmanager
async def lifespan(api: FastAPI):
    api.state = State()
    logger.info("Iniciando API...")
    
    if not MODEL_PATH.exists():
        logger.error("Modelo no encontrado en %s", MODEL_PATH)
        raise RuntimeError(f"No se encontró el archivo {MODEL_PATH}")

    try:
        # Cargamos el pipeline completo (Tfidf + Modelo)
        api.state.model = joblib.load(MODEL_PATH)
        logger.info("✅ Modelo cargado exitosamente")
    except Exception as e:
        logger.exception("Error crítico cargando el modelo")
        raise RuntimeError("Fallo al cargar el modelo") from e
    yield
    logger.info("Apagando API")

# -------------------------------------------------------------------
# APP
# -------------------------------------------------------------------
app = FastAPI(
    title="API de Análisis de Sentimiento",
    version="1.1.0",
    lifespan=lifespan
)

# -------------------------------------------------------------------
# ENDPOINTS (Misma estructura de retorno)
# -------------------------------------------------------------------
@app.post("/predict", response_model=SentimentResponse)
def predict_sentiment(request: SentimentRequest, req: Request):
    model = req.app.state.model

    try:
        # A. Limpieza con lógica del Colab
        texto_procesado = limpiar_texto_para_modelo(request.text)
        
        if not texto_procesado:
            return SentimentResponse(prevision="neutral", probabilidad=0.0)

        # B. Predicción (Usando el pipeline cargado directamente)
        input_data = [texto_procesado]
        prediction = model.predict(input_data)[0]
        probs = model.predict_proba(input_data)[0]
        
        # Obtenemos la probabilidad de la clase predicha
        class_labels = list(model.classes_)
        predicted_idx = class_labels.index(prediction)
        max_prob = float(probs[predicted_idx])

        return SentimentResponse(
            prevision=str(prediction),
            probabilidad=round(max_prob, 4)
        )

    except Exception as e:
        logger.exception("Error procesando la solicitud")
        raise HTTPException(status_code=500, detail="Error interno")

@app.get("/")
def health_check(request: Request):
    return {
        "status": "online",
        "model_loaded": hasattr(request.app.state, "model")
    }
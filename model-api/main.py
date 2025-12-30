from fastapi import FastAPI, HTTPException, Request
from pydantic import BaseModel, Field
from contextlib import asynccontextmanager
from pathlib import Path
import joblib
import numpy as np
import logging
import re
import unicodedata
import nltk
from nltk.corpus import stopwords
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

# -------------------------------------------------------------------
# FUNCIONES DE LIMPIEZA (Traídas del Colab)
# -------------------------------------------------------------------
def descargar_recursos_nltk():
    """Descarga stopwords si no existen"""
    try:
        nltk.data.find('corpora/stopwords')
    except LookupError:
        nltk.download('stopwords')
        logger.info("Stopwords descargadas.")

def limpiar_texto_para_modelo(texto: str) -> str:
    # 1. Minúsculas
    texto = texto.lower()
    
    # 2. Normalización Unicode (quitar tildes)
    texto = unicodedata.normalize('NFD', texto)
    texto = texto.encode('ascii', 'ignore').decode("utf-8")
    
    # 3. Eliminar URLs, Hashtags, Menciones
    texto = re.sub(r'https?://\S+|www\.\S+', '', texto)
    texto = re.sub(r'#\w+', '', texto)
    texto = re.sub(r'@\w+', '', texto)
    
    # 4. Eliminar puntuación y caracteres especiales
    texto = re.sub(r'[^\w\s]', '', texto)
    
    # 5. Eliminar números
    texto = re.sub(r'\d+', '', texto)

    # 6. Stopwords (Usando NLTK)
    stop_words = set(stopwords.words('spanish'))
    palabras = [word for word in texto.split() if word not in stop_words]
    return " ".join(palabras).strip()

# -------------------------------------------------------------------
# MODELOS Pydantic
# -------------------------------------------------------------------
class SentimentRequest(BaseModel):
    # Ajusté min_length a 1 para permitir frases cortas como "Hola"
    text: str = Field(..., min_length=1, description="Texto a analizar")

class SentimentResponse(BaseModel):
    prevision: str
    probabilidad: float

# -------------------------------------------------------------------
# LIFESPAN (startup / shutdown)
# -------------------------------------------------------------------
@asynccontextmanager
async def lifespan(api: FastAPI):
    api.state = State()

    # 🔼 STARTUP
    logger.info("Iniciando API...")
    
    # 1. Descargar recursos NLTK
    descargar_recursos_nltk()

    # 2. Cargar Modelo
    if not MODEL_PATH.exists():
        logger.error("Modelo no encontrado en %s", MODEL_PATH)
        raise RuntimeError(f"No se encontró el archivo {MODEL_PATH}")

    try:
        api.state.model = joblib.load(MODEL_PATH)
        logger.info("✅ Modelo cargado exitosamente: %s", MODEL_PATH)
    except Exception as e:
        logger.exception("Error crítico cargando el modelo")
        raise RuntimeError("Fallo al cargar el modelo") from e

    yield

    # 🔽 SHUTDOWN
    logger.info("Apagando API de Sentimiento")

# -------------------------------------------------------------------
# APP
# -------------------------------------------------------------------
app = FastAPI(
    title="API de Análisis de Sentimiento",
    version="1.1.0",
    lifespan=lifespan
)

# -------------------------------------------------------------------
# ENDPOINTS
# -------------------------------------------------------------------
@app.post(
    "/predict",
    response_model=SentimentResponse,
    summary="Analiza el sentimiento de un texto",
)
def predict_sentiment(request: SentimentRequest, req: Request):
    model = req.app.state.model

    try:
        # A. Limpieza (CRÍTICO: Igualar condiciones del entrenamiento)
        texto_procesado = limpiar_texto_para_modelo(request.text)
        
        # Validación: Si la limpieza deja el texto vacío (ej: solo mandaron emojis o stopwords)
        if not texto_procesado:
            return SentimentResponse(
                prevision="Neutral",
                probabilidad=0.0
            )

        # B. Predicción
        input_data = [texto_procesado]
        prediction = model.predict(input_data)[0]
        probs = model.predict_proba(input_data)[0]
        max_prob = float(np.max(probs))

        return SentimentResponse(
            prevision=str(prediction),
            probabilidad=round(max_prob, 4)
        )

    except Exception as e:
        logger.exception("Error procesando la solicitud")
        raise HTTPException(
            status_code=500,
            detail="Error interno procesando la predicción"
        )

@app.get("/", summary="Health check")
def health_check(request: Request):
    return {
        "status": "online",
        "model_loaded": hasattr(request.app.state, "model")
    }
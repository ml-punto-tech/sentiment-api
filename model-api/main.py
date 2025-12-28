from fastapi import FastAPI, HTTPException, Request
from pydantic import BaseModel, Field
from contextlib import asynccontextmanager
from pathlib import Path
import joblib
import numpy as np
import logging
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
# MODELOS Pydantic
# -------------------------------------------------------------------
class SentimentRequest(BaseModel):
    text: str = Field(..., min_length=10, description="Texto a analizar")

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
    if not MODEL_PATH.exists():
        logger.error("Modelo no encontrado en %s", MODEL_PATH)
        raise RuntimeError("Modelo requerido no disponible")

    try:
        api.state.model = joblib.load(MODEL_PATH)
        logger.info("Modelo cargado exitosamente: %s", MODEL_PATH)
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
        input_data = [request.text]

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

@app.get(
    "/",
    summary="Health check"
)
def health_check(request: Request):
    return {
        "status": "online",
        "model_loaded": hasattr(request.app.state, "model")
    }

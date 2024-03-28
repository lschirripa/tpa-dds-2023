import uvicorn
from fastapi import FastAPI, Depends
from starlette.middleware.cors import CORSMiddleware
from starlette.responses import RedirectResponse

from services import CalculatorService
from models.models import Comunidad, ComunidadOutput
from fastapi import APIRouter

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

router = APIRouter(prefix="/calculador")


@app.get("/", include_in_schema=False)
async def docs_redirect():
    return RedirectResponse(url='/docs')


@app.post("/comunidad/usuarios/",
          response_model=ComunidadOutput,
          status_code=200,
          tags=["Comunidad"],
          summary="Calcula el nivel de confianza de una comunidad de usuarios",
          description="Los IDs son necesarios para poder diferenciar a los usuarios y a los distintos incidentes para poder hacer los calculos. \n  \n  \n IMPORTANTE: Se entiende como incidente similar a aquellos que tienen el mismo establecimiento y servicio afectado (hashing entre id_establecimiento + id_servicio_afectado).",
          response_description="Nivel de confianza de la comunidad de usuarios",
          )
def calcular_confianza(
        comunidad: Comunidad,
        calculator_service=Depends(CalculatorService),
):
    return calculator_service.calcular_confianza(comunidad)


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8008, reload="True")

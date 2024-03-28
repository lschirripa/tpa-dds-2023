from pydantic import BaseModel, ConfigDict


# fechas (YYYY-MM-DD). ISO 8601
class Usuario(BaseModel):
    id: int
    puntaje_inicial: float


class Incidente(BaseModel):
    id: int
    id_establecimiento: str
    id_servicio_afectado : str
    fecha_de_apertura: str
    fecha_de_cierre: str
    id_usuario_de_apertura: int
    id_usuario_de_cierre: int


class Comunidad(BaseModel):
    usuarios: list[Usuario]
    incidentes: list[Incidente]


class UsuarioOutput(BaseModel):
    id: int
    puntaje_inicial: float
    puntaje_final: float
    nivel_de_confianza: str
    recomendacion: str


class ComunidadOutput(BaseModel):
    nivel_de_confianza: float
    usuarios_output: list[UsuarioOutput]
import pytest
from externalApi.models.models import Incidente, Usuario


@pytest.fixture()
def incEstablecimiento1Servicio1():
    incidente = Incidente(
        id=1,
        id_establecimiento="1",
        id_servicio_afectado="1",
        fecha_de_apertura="2018-03-14T13:00",
        fecha_de_cierre="2018-03-14T13:59",
        id_usuario_de_apertura=1,
        id_usuario_de_cierre=2
    )
    return incidente

@pytest.fixture()
def incEstablecimiento1Servicio2():
    incidente = Incidente(
        id=2,
        id_establecimiento="1",
        id_servicio_afectado="2",
        fecha_de_apertura="2018-03-14T13:00",
        fecha_de_cierre="2018-03-14T13:59",
        id_usuario_de_apertura=1,
        id_usuario_de_cierre=2
    )
    return incidente


@pytest.fixture()
def incEstablecimiento2Servicio1():
    incidente = Incidente(
        id=3,
        id_establecimiento="2",
        id_servicio_afectado="1",
        fecha_de_apertura="2018-03-14T13:00",
        fecha_de_cierre="2018-03-14T13:59",
        id_usuario_de_apertura=1,
        id_usuario_de_cierre=2
    )
    return incidente


@pytest.fixture()
def incEstablecimiento2Servicio2():
    incidente = Incidente(
        id=4,
        id_establecimiento="2",
        id_servicio_afectado="2",
        fecha_de_apertura="2018-03-14T13:00",
        fecha_de_cierre="2018-03-14T13:59",
        id_usuario_de_apertura=1,
        id_usuario_de_cierre=2
    )
    return incidente

#incidentes con apertura fraudulenta

@pytest.fixture()
def incidenteConAperturaFraudulenta():
    incidente = Incidente(
        id=5,
        id_establecimiento="1",
        id_servicio_afectado="1",
        fecha_de_apertura="2018-03-14T13:00",
        fecha_de_cierre="2018-03-14T13:01",
        id_usuario_de_apertura=1,
        id_usuario_de_cierre=2
    )
    return incidente

# incidente similar a incEstablecimiento1Servicio1 y con cierre fraudulento
@pytest.fixture()
def incidenteSimilarConCierreFraudulento():
    incidente = Incidente(
        id=6,
        id_establecimiento="1",
        id_servicio_afectado="1",
        fecha_de_apertura="2018-03-14T14:01",
        fecha_de_cierre="2018-03-14T14:50",
        id_usuario_de_apertura=3,
        id_usuario_de_cierre=1
    )
    return incidente




@pytest.fixture()
def usuario1():
    usuario = Usuario(
        id=1,
        puntaje_inicial=0
    )
    return usuario

@pytest.fixture()
def usuario2():
    usuario = Usuario(
        id=2,
        puntaje_inicial=0
    )
    return usuario
@pytest.fixture()
def usuario3():
    usuario = Usuario(
        id=3,
        puntaje_inicial=2
    )
    return usuario


import sys

from externalApi.models.models import Comunidad
from externalApi.services import CalculatorService

calculator_service = CalculatorService()


def test_calcular_confianza_comunidad(usuario1, usuario2, incEstablecimiento1Servicio1, incEstablecimiento1Servicio2, incEstablecimiento2Servicio1, incEstablecimiento2Servicio2):
    comunidad = Comunidad(
        usuarios=[usuario1, usuario2],
        incidentes=[incEstablecimiento1Servicio1, incEstablecimiento1Servicio2, incEstablecimiento2Servicio1, incEstablecimiento2Servicio2]
    )
    response = calculator_service.calcular_confianza(comunidad)
    assert response["puntaje_comunidad"] == 1.6

def test_sin_incidentes(usuario1, usuario2):
    comunidad = Comunidad(
            usuarios=[usuario1, usuario2],
            incidentes=[]
        )
    response = calculator_service.calcular_confianza(comunidad)
    assert response["puntaje_comunidad"] == 0

def test_con_una_apertura_fraudulenta(usuario1,usuario2, incidenteConAperturaFraudulenta):
    comunidad = Comunidad(
        usuarios=[usuario1, usuario2],
        incidentes=[incidenteConAperturaFraudulenta]
    )
    response = calculator_service.calcular_confianza(comunidad)
    assert response["puntaje_comunidad"] == 0.15

def test_con_un_cierre_fraudulento(usuario1, usuario2, usuario3, incidenteSimilarConCierreFraudulento, incEstablecimiento1Servicio1):
    comunidad = Comunidad(
        usuarios=[usuario1, usuario2, usuario3],
        incidentes=[incEstablecimiento1Servicio1, incidenteSimilarConCierreFraudulento]
    )
    response = calculator_service.calcular_confianza(comunidad)
    assert response["puntaje_comunidad"] == 0.9



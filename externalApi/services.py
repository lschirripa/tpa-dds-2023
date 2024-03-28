from datetime import datetime
from models.models import Comunidad, UsuarioOutput, Incidente, Usuario


class CalculatorService:

    def __init__(self):
        ...

    def calcular_confianza(self, comunidad: Comunidad):
        usuarios = comunidad.usuarios
        incidentes = comunidad.incidentes
        output_usuarios = self.preparar_output_usuarios(usuarios, incidentes)
        confianza_comunidad = self.calcular_confianza_comunidad(output_usuarios)
        armado_output = {"usuarios": output_usuarios,
                         "puntaje_comunidad": round(confianza_comunidad, 2)}
        return armado_output

    def preparar_output_usuarios(self, usuarios: list[Usuario], incidentes: list[Incidente]) -> \
            list[UsuarioOutput]:
        lista_usuarios_output = []
        for usuario in usuarios:
            puntaje_final = self.calcular_puntos_usuario(usuario, incidentes)
            nivel_de_confianza = self.calcular_nivel_de_confianza_y_recomendacion(puntaje_final)[0]
            recomendacion = self.calcular_nivel_de_confianza_y_recomendacion(puntaje_final)[1]
            usuario_output = UsuarioOutput(
                id=usuario.id,
                puntaje_inicial=usuario.puntaje_inicial,
                puntaje_final=round(puntaje_final, 2),
                nivel_de_confianza=nivel_de_confianza,
                recomendacion=recomendacion
            )
            lista_usuarios_output.append(usuario_output)
        return lista_usuarios_output

    def calcular_nivel_de_confianza_y_recomendacion(self, puntajeFinal):
        if puntajeFinal < 2:
            return "No confiable", "inactivar el usuario"
        elif puntajeFinal >= 2 and puntajeFinal <= 3:
            return "Con reservas", "ojo con este..."
        elif puntajeFinal > 3 and puntajeFinal < 5:
            return "Confiable nivel 1", "confiable"
        else:
            return "Confiable nivel 2", "ta ok el pana"

    def calcular_puntos_usuario(self, usuario: Usuario, incidentes: list[Incidente]) -> float:
        puntaje_final = usuario.puntaje_inicial
        incidentes_similares = self.detectar_incidentes_similares(incidentes)
        for incidente in incidentes:
            puntaje_a_sumar = 0
            puntaje_a_sumar += self.apertura_fraudulenta(usuario, incidente)
            puntaje_a_sumar += self.cierre_fraudulento(usuario, incidente, incidentes_similares)
            if puntaje_a_sumar < 0:
                puntaje_final += puntaje_a_sumar
                continue
            puntaje_a_sumar += self.apertura_no_fraudulenta(usuario, incidente)
            puntaje_a_sumar += self.cierre_no_fraudulento(usuario, incidente)
            puntaje_final += puntaje_a_sumar
        return puntaje_final

    # ● Si entre la apertura de un incidente por cualquier usuario y su cierre por el usuario analizado
    # existen menos de 3 minutos se descontarán 0,2 puntos por cada incidente, estimando que se
    # realizó una apertura fraudulenta.
    def apertura_fraudulenta(self, usuario: Usuario, incidente: Incidente) -> float:
        if incidente.id_usuario_de_cierre == usuario.id:
            fecha_apertura = datetime.strptime(incidente.fecha_de_apertura, "%Y-%m-%dT%H:%M")
            fecha_cierre = datetime.strptime(incidente.fecha_de_cierre, "%Y-%m-%dT%H:%M")
            diferencia = fecha_cierre - fecha_apertura
            if diferencia.total_seconds() / 60 < 3:
                return -0.2
        return 0

    # ● Si entre el cierre de un incidente realizado por el usuario y la apertura de uno similar realizado por
    # cualquier usuario existen menos de 3 minutos se descontarán 0,2 puntos por cada incidente,
    # estimando que se realizó un cierre fraudulento.
    def cierre_fraudulento(self, usuario, incidente: Incidente,
                           incidentes_repetidos: list[Incidente]) -> float:
        if incidente.id_usuario_de_cierre == usuario.id:
            for incidente_repetido in incidentes_repetidos:
                if incidente.id != incidente_repetido.id:
                    if incidente.id_establecimiento + incidente.id_servicio_afectado == incidente_repetido.id_establecimiento + incidente_repetido.id_servicio_afectado:
                        fecha_cierre_incidente = datetime.strptime(incidente.fecha_de_cierre,
                                                                   "%Y-%m-%dT%H:%M")
                        fecha_apertura_incidente_repetido = datetime.strptime(
                            incidente_repetido.fecha_de_apertura, "%Y-%m-%dT%H:%M")
                        diferencia = fecha_apertura_incidente_repetido - fecha_cierre_incidente
                        if diferencia.total_seconds() / 60 < 3:
                            return -0.2
        return 0

    def apertura_no_fraudulenta(self, usuario, incidente) -> float:
        if incidente.id_usuario_de_apertura == usuario.id:
            return 0.5
        else:
            return 0

    def cierre_no_fraudulento(self, usuario, incidente) -> float:
        if incidente.id_usuario_de_cierre == usuario.id:
            return 0.5
        else:
            return 0

    def detectar_incidentes_similares(self, incidentes: list[Incidente]):
        incidente_visto = []
        incidentes_repetidos = []
        for incidente in incidentes:
            if incidente.id_establecimiento + incidente.id_servicio_afectado in incidente_visto:
                incidentes_repetidos.append(incidente)
            else:
                incidente_visto.append(
                    incidente.id_establecimiento + incidente.id_servicio_afectado)
        return incidentes_repetidos

    def calcular_confianza_comunidad(self, confianza_usuarios: list[UsuarioOutput]) -> float:
        contador_a_restar = 0
        puntaje_total = 0
        for usuario in confianza_usuarios:
            puntaje_total += usuario.puntaje_final
            if usuario.nivel_de_confianza == "Con reservas":
                contador_a_restar += 1
        puntaje_promedio = puntaje_total / len(confianza_usuarios)
        puntaje_promedio = puntaje_promedio - contador_a_restar * 0.2
        return puntaje_promedio

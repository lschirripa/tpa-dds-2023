package utils;

import externalservices.Notificador;
import externalservices.TipoMensaje;
import java.util.List;
import java.util.stream.Collectors;
import layers.models.domain.Comunidad;
import layers.models.domain.Incidente;
import layers.models.domain.Usuario;
import layers.services.IncidenteService;
import layers.services.UsuarioService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SugerenciaDeRevision {

  private static final int DISTANCIA_MAXIMA = 500;
  private UsuarioService usuarioService;
  private IncidenteService incidenteService;
  private Notificador notificador;
  private CalculadorDistancia calculadorDistancia;

  public SugerenciaDeRevision(UsuarioService usuarioService, IncidenteService incidenteService) {
    this.usuarioService = usuarioService;
    this.incidenteService = incidenteService;
    this.notificador = new Notificador(usuarioService);
    this.calculadorDistancia = new CalculadorDistancia();
  }

  public String sugerirRevisionDe(Usuario usuario) {
    List<Incidente> incidentesCercanos = obtenerIncidentesCercanos(usuario);

    incidentesCercanos.forEach(incidente -> notificador
        .notificarUsuario(usuario, incidente, TipoMensaje.REVISION));

    if (incidentesCercanos.size() > 0) {
      incidentesCercanos.forEach(incidente -> this.notificador
          .notificarUsuario(usuario, incidente, TipoMensaje.REVISION));
      return "Se ha sugerido una revision. Revisar en su mensajeria seleccionada";
    } else {
      return "No se ha sugerido una revision";
    }

  }

  public List<Incidente> obtenerIncidentesCercanos(Usuario usuario) {
    List<Comunidad> comunidadesDeUsuario = usuario.getComunidades();

    return incidenteService.incidenteRepository.getAll(Incidente.class).stream()
        .filter(incidente -> incidente.getComunidades().stream()
            .anyMatch(comunidadesDeUsuario::contains))
        .toList().stream()
        .filter(incidente -> incidente.getFechaResolucion() == null)
        .filter(incidente -> this.calculadorDistancia.calcularDistancia(
            incidenteService.getUbicacionDeEstablecimiento(incidente),
            usuario.getUbicacionActual()
        ) < DISTANCIA_MAXIMA)
        .toList();
  }

}

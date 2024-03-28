package layers.services;

import excepciones.IncidenteInexistenteException;
import externalservices.Notificador;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import layers.models.domain.Comunidad;
import layers.models.domain.Incidente;
import layers.models.domain.UbicacionGeografica;
import layers.models.domain.Usuario;
import layers.models.repositories.IncidenteRepository;

public class IncidenteService {
  public IncidenteRepository incidenteRepository;
  public UsuarioService usuarioService;

  private Notificador notificador;

  public IncidenteService(IncidenteRepository incidenteRepository, UsuarioService usuarioService) {
    this.incidenteRepository = incidenteRepository;
    this.usuarioService = usuarioService;
    this.notificador = new Notificador(usuarioService);
  }

  public List<Incidente> listarIncidentes() {
    return this.getAll();
  }

  public Incidente buscarYValidarIncidente(int incidenteId) {
    Incidente incidente = incidenteRepository.getById(incidenteId, Incidente.class);
    if (incidente == null) {
      throw new IncidenteInexistenteException("Incidente no encontrado");
    }
    return incidente;
  }

  public void save(Incidente incidente) {
    incidenteRepository.save(incidente);
  }

  public void agregarComunidades(Incidente incidente, List<Comunidad> comunidades) {
    incidente.getComunidades().addAll(comunidades);
  }

  public void notificarComunidades(Incidente incidente) {

    // Crear un Set de Usuarios, asi no se le notifica 2 veces y luego crear un ARrayList sobre este:
    Set<Usuario> usuarios = new HashSet<>();
    for (Comunidad comunidad : incidente.getComunidades()) {
      usuarios.addAll(comunidad.getMiembros());
    }
    List<Usuario> usuariosAnotificar = new ArrayList<>(usuarios);

    System.out.println("Todos los usuarios a notificar:");
    for (Usuario usuario : usuariosAnotificar) {
      System.out.println(usuario.getUserName() + " - "
          + usuario.getDatosPersonales().getCorreo() + " - "
          + usuario.getDatosPersonales().getTelefono());
    }


    // Notificar a los usuarios
    for (Usuario usuario : usuariosAnotificar) {
      notificador.notificarUsuarioSiCorresponde(usuario, incidente);
    }
  }

  public void resolverIncidente(Incidente incidente) {
    ZoneId zonaHorariaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
    incidente.setFechaResolucion(LocalDateTime.now(zonaHorariaArgentina));
  }

  public UbicacionGeografica getUbicacionDeEstablecimiento(Incidente incidente) {
    return incidente.getServicioIncidentado().getEstablecimiento().getUbicacionGeografica();
  }

  public List<Incidente> getAll() {
    return incidenteRepository.getAll(Incidente.class);
  }

  public void update(Incidente incidente) {
    incidenteRepository.update(incidente);
  }

  public void delete(Incidente incidente) {
    incidenteRepository.delete(incidente);
  }

  public List<Incidente> filtrarIncidentes(String descripcion, String servicioIncidentadoId,
                                           Boolean resuelto, Integer usuarioId) {
    // Inicializa una lista para almacenar los incidentes filtrados
    List<Incidente> incidentesFiltrados = new ArrayList<>();

    //me quedo con los incidentes de las comunidades que coinciden con el usuario
    List<Incidente> incidentesDeComunidad = incidenteRepository.getIncidentesByUsuarioId(usuarioId);
    System.out.println("Cantidad de incidentes totales filtrados: " + getAll().size());
    System.out.println("Cantidad de incidentes totales filtrados: " + incidentesDeComunidad.size());

    // Recupera todos los incidentes de la base de datos (o desde donde los obtengas)
    //List<Incidente> todosLosIncidentes = this.incidenteRepository.getAll(Incidente.class); // Implementa este método según tu lógica

    // Realiza el filtrado basado en los parámetros proporcionados
    for (Incidente incidente : incidentesDeComunidad) {
      boolean cumpleFiltro = true;

      if (descripcion != null && !descripcion.isEmpty()) {
        if (!incidente.getDescripcion().toLowerCase().contains(descripcion.toLowerCase())) {
          cumpleFiltro = false;
        }
      }

      if (servicioIncidentadoId != null && !servicioIncidentadoId.isEmpty()) {
        int idServicioIncidentado = Integer.parseInt(servicioIncidentadoId);
        if (incidente.getServicioIncidentado().getId() != idServicioIncidentado) {
          cumpleFiltro = false;
        }
      }

      if (resuelto != null) {
        if (resuelto && incidente.getFechaResolucion() == null) {
          cumpleFiltro = false;
        } else if (!resuelto && incidente.getFechaResolucion() != null) {
          cumpleFiltro = false;
        }
      }

      // Si el incidente cumple con los criterios de filtrado, agrégalo a la lista de incidentes filtrados
      if (cumpleFiltro) {
        incidentesFiltrados.add(incidente);
      }
    }

    return incidentesFiltrados;
  }

}

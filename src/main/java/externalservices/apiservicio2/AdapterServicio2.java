package externalservices.apiservicio2;

import externalservices.apiservicio2.entities.BodyRequest;
import externalservices.apiservicio2.entities.ConfianzaApiService;
import externalservices.apiservicio2.entities.IncidenteAdaptado;
import externalservices.apiservicio2.entities.ResponseData;
import externalservices.apiservicio2.entities.UsuarioAdaptado;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import layers.models.domain.Establecimiento;
import layers.models.domain.Incidente;
import layers.models.domain.ServicioAsociado;
import layers.models.domain.Usuario;
import retrofit2.Response;

public class AdapterServicio2 {
  private static AdapterServicio2 instance = null;

  private ConfianzaApiService confianzaApiService;

  public AdapterServicio2() {
    this.confianzaApiService = ConfianzaApiService.getInstance();
  }

  public static AdapterServicio2 getInstance() {
    if (instance == null) {
      instance = new AdapterServicio2();
    }
    return instance;
  }

  public Response<ResponseData> postConfianza(List<Usuario> usuarios, List<Incidente> incidentes) throws IOException {

    BodyRequest bodyRequest = this.build_request(usuarios, incidentes);
    Response<ResponseData> responseData = this.confianzaApiService.postConfianza(bodyRequest);

    return responseData;

  }

  public UsuarioAdaptado adaptar_usuarios(Usuario usuario) {

    UsuarioAdaptado usuarioAdaptado = new UsuarioAdaptado(usuario.getId(), usuario.getPuntosDeConfianza());
    return usuarioAdaptado;

  }

  public IncidenteAdaptado adaptar_incidente(Incidente incidente) {
    Establecimiento establecimiento = incidente.getServicioIncidentado().getEstablecimiento();
    ServicioAsociado servicioIncidentado = incidente.getServicioIncidentado();

    LocalDateTime fechaDeApertura = incidente.getFechaCreacion();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    String formattedFechaDeApertura = fechaDeApertura.format(formatter);

    LocalDateTime fechaDeResolucion = incidente.getFechaResolucion();
    String formattedFechaDeResolucion = fechaDeResolucion.format(formatter);


    IncidenteAdaptado incidenteAdaptado = new IncidenteAdaptado(incidente.getId(), establecimiento.getId(), servicioIncidentado.getId(), formattedFechaDeApertura, formattedFechaDeResolucion, incidente.getUsuarioDeApertura().getId(), incidente.getUsuarioDeResolucion().getId() );
    return incidenteAdaptado;
  }

  public BodyRequest build_request(List<Usuario> usuarios, List<Incidente> incidentes) {
    List<UsuarioAdaptado> usuariosAdaptados = new ArrayList<>();
    List<IncidenteAdaptado> incidentesAdaptados = new ArrayList<>();

    for (Usuario usuario : usuarios) {
      usuariosAdaptados.add(adaptar_usuarios(usuario));
    }

    for (Incidente incidente : incidentes) {
      incidentesAdaptados.add(adaptar_incidente(incidente));
    }


    BodyRequest bodyRequest = new BodyRequest(usuariosAdaptados, incidentesAdaptados);
    return bodyRequest;
  }

}

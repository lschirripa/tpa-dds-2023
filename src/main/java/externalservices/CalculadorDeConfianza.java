package externalservices;

import externalservices.apiservicio2.AdapterServicio2;
import externalservices.apiservicio2.entities.ResponseData;
import java.io.IOException;
import layers.models.domain.Usuario;
import layers.models.repositories.IncidenteRepository;
import layers.models.repositories.UsuarioRepository;
import retrofit2.Response;

public class CalculadorDeConfianza {

  private AdapterServicio2 adapterServicio2 = AdapterServicio2.getInstance();
  private IncidenteRepository incidenteRepository = new IncidenteRepository();
  private UsuarioRepository usuarioRepository = new UsuarioRepository();

  public Response<ResponseData> calcularConfianza() throws IOException {

    Response<ResponseData> Response = this.adapterServicio2
        .postConfianza(this.usuarioRepository.getAll(Usuario.class), this.incidenteRepository.getResolvedIncidentes());

    return Response;

  }
}

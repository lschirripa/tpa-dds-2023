package incidentes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import layers.models.domain.*;
import layers.models.repositories.*;
import layers.services.IncidenteService;
import layers.services.ServicioAsociadoService;
import layers.services.ServicioService;
import layers.services.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.SugerenciaDeRevision;

public class SugerenciaDeRevisionTest {

  private static final UsuarioRepository usuarioRepository = new UsuarioRepository();
  private static final UbicacionGeograficaRepository ubicacionGeograficaRepository = new UbicacionGeograficaRepository();

  private static final ServicioRepository servicioRepository = new ServicioRepository();
  private static final EntidadRepository entidadRepository = new EntidadRepository();
  private static final EstablecimientoRepository establecimientoRepository = new EstablecimientoRepository();
  private static final ServicioAsociadoRepository servicioAsociadoRepository = new ServicioAsociadoRepository();
  private static final IncidenteRepository incidenteRepository = new IncidenteRepository();
  private static final ComunidadRepository comunidadRepository = new ComunidadRepository();
  private static final LocalizacionRepository localizacionRepository = new LocalizacionRepository();
  private final SugerenciaDeRevision sugerenciaDeRevision = new SugerenciaDeRevision(new UsuarioService(usuarioRepository, new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(servicioRepository, new ServicioUsuarioRepository()))), new IncidenteService(incidenteRepository, new UsuarioService(usuarioRepository, new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(servicioRepository, new ServicioUsuarioRepository())))));

  @BeforeAll
  public static void setUp() throws IOException {
    generarComunidades();
    generarUsuario();
    generarServicioAscociado();
  }

  private static void generarComunidades() {
    Comunidad comunidad = new Comunidad("Comunidad 1");
    Comunidad comunidad2 = new Comunidad("Comunidad 2");
    comunidadRepository.save(comunidad);
    comunidadRepository.save(comunidad2);

  }

  private static void generarUsuario() {
    DatosPersonalesUsuario datosPersonalesUsuario = new DatosPersonalesUsuario("fran", "maver", "franciscomaver.fm@gmail.com", "1138718498");
    UbicacionGeografica ubicacionGeografica = new UbicacionGeografica(-26.8753965086829, -54.6516966230371);
    ubicacionGeograficaRepository.save(ubicacionGeografica);

    Usuario usuario = new Usuario("fmaver", "passdifficult1", datosPersonalesUsuario, ubicacionGeografica, null);
    usuario.addComunidad(comunidadRepository.getById(1, Comunidad.class));
    usuarioRepository.save(usuario);
  }

  private static void generarServicioAscociado() throws IOException {
    Servicio servicio = new Servicio("Servicio de agua", "servicio de agua potable");
    servicioRepository.save(servicio);
    UbicacionGeografica ubicacionGeografica = ubicacionGeograficaRepository.getById(1, UbicacionGeografica.class);

    Sucursal sucursal = new Sucursal("Sucursal 1", List.of(servicio), ubicacionGeografica);
    localizacionRepository.getOrCreate(sucursal.getLocalizacion());
    establecimientoRepository.save(sucursal);

    ArrayList<Sucursal> sucursals = new ArrayList<>();
    sucursals.add(sucursal);

    Organizacion organizacion = new Organizacion("Servicio de agua", ubicacionGeografica, sucursals);
    localizacionRepository.getOrCreate(organizacion.getLocalizacion());
    entidadRepository.save(organizacion);

    ServicioAsociado servicioAsociado = new ServicioAsociado(organizacion, sucursal, servicio);
    servicioAsociadoRepository.save(servicioAsociado);
  }

  private void generarIncidente() {
    ServicioAsociado servicioAsociado = servicioAsociadoRepository.getById(1, ServicioAsociado.class);
    Incidente incidente = new Incidente(servicioAsociado, "no hay agua", LocalDateTime.now());
    incidente.addComunidad(comunidadRepository.getById(1, Comunidad.class));
    incidenteRepository.save(incidente);

  }

  private void generarOtroIncidente() {
    ServicioAsociado servicioAsociado = servicioAsociadoRepository.getById(1, ServicioAsociado.class);
    Incidente incidente = new Incidente(servicioAsociado, "no hay agua", LocalDateTime.now());
    incidente.addComunidad(comunidadRepository.getById(2, Comunidad.class));
    incidenteRepository.save(incidente);
  }

  @Test
  public void dados2incidentesSoloTomaEnCuentaElqueCoincideConElUsuario() {

    generarIncidente();
    generarOtroIncidente();

    Assertions.assertEquals(2, incidenteRepository.getAll(Incidente.class).size());

    List<Incidente> incidentesCercanos = sugerenciaDeRevision.obtenerIncidentesCercanos(usuarioRepository.getById(1, Usuario.class));

    Assertions.assertEquals(1, incidentesCercanos.size());
  }
}

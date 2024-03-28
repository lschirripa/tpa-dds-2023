package apiServices.servicio1;


import externalservices.FusionadorDeComunidades;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import layers.models.domain.*;
import layers.models.repositories.*;
import org.junit.jupiter.api.*;

public class FusionarComunidadesTest {
  private static ComunidadRepository comunidadRepository;
  private static UsuarioRepository usuarioRepository;
  private static ServicioAsociadoRepository servicioAsociadoRepository;
  private static EstablecimientoRepository establecimientoRepository;
  private static LocalizacionRepository localizacionRepository;

  private FusionadorDeComunidades fusionDeComunidades;

  @BeforeAll
  public static void setUpClass() throws IOException {
    System.out.println("#######################");
    System.out.println("Inicializando test");
    System.out.println("#######################");
  }

  @AfterAll
  public static void tearDownClass() {
    //por cada comunidad del repositorio, eliminarla
    comunidadRepository.getAll(Comunidad.class).forEach(comunidad -> {
      comunidadRepository.delete(comunidad);
    });
    //por cada usuario del repositorio, eliminarlo
    usuarioRepository.getAll(Usuario.class).forEach(usuario -> {
      usuarioRepository.delete(usuario);
    });
    //por cada servicio del repositorio, eliminarlo
    servicioAsociadoRepository.getAll(ServicioAsociado.class).forEach(servicio -> {
      servicioAsociadoRepository.delete(servicio);
    });
    //por cada establecimiento del repositorio, eliminarlo
    establecimientoRepository.getAll(Establecimiento.class).forEach(establecimiento -> {
      establecimientoRepository.delete(establecimiento);
    });
    //por cada localizacion del repositorio, eliminarlo
    localizacionRepository.getAll(Localizacion.class).forEach(localizacion -> {
      localizacionRepository.delete(localizacion);
    });

  }

  // Resto de tus tests aquí

  @BeforeEach
  public void setUp() throws IOException {
    // El código de inicialización individual de cada test permanece aquí
    comunidadRepository = new ComunidadRepository();
    usuarioRepository = new UsuarioRepository();
    servicioAsociadoRepository = new ServicioAsociadoRepository();
    establecimientoRepository = new EstablecimientoRepository();
    localizacionRepository = new LocalizacionRepository();

    generarServiciosAsociados();
    generarUsuarios();
    generarComunidades();
  }

  @Test
  public void laCantidadDeMiembrosEnAmbasComunidadesEsLaQueCorresponde() {
    Assertions.assertEquals(5, comunidadRepository.getById(1, Comunidad.class).getMiembros().size());
    Assertions.assertEquals(4, comunidadRepository.getById(2, Comunidad.class).getMiembros().size());
  }

  @Test
  public void dadoDosComunidadesLasUne() throws Exception {
    fusionDeComunidades = new FusionadorDeComunidades(comunidadRepository, usuarioRepository);

    Comunidad comunidad1 = comunidadRepository.getById(1, Comunidad.class);
    Comunidad comunidad2 = comunidadRepository.getById(2, Comunidad.class);

    Comunidad comunidadFusionada = fusionDeComunidades.fusionDeComunidades(comunidad1, comunidad2);

    Assertions.assertEquals(5, comunidad1.getMiembros().size());
    Assertions.assertEquals(5, comunidadRepository.getById(1, Comunidad.class).getMiembros().size());
    Assertions.assertEquals("Comunidad fusionada", comunidadFusionada.getDescripcion());
    Assertions.assertEquals("Merged Community", comunidadFusionada.getNombre());
    Assertions.assertEquals(5, comunidadFusionada.getMiembros().size());
  }


  private void generarServiciosAsociados() throws IOException {

    System.out.println("#######################");
    System.out.println("Generando servAsociados");
    System.out.println("#######################");

    List<Servicio> servicios = new ArrayList<>();
    Servicio servicio1 = new Servicio("Banio", "de hombres");
    Servicio servicio2 = new Servicio("Banio", "de mujeres");
    servicios.add(servicio1);
    servicios.add(servicio2);

    Sucursal sucursal1 = new Sucursal("Santander", servicios, new UbicacionGeografica(-26.8753965086829, -54.6516966230371));

    localizacionRepository.getOrCreate(sucursal1.getLocalizacion());
    establecimientoRepository.save(sucursal1);

    ServicioAsociado servicioAsociado1 = new ServicioAsociado(null, sucursal1, servicio1);
    ServicioAsociado servicioAsociado2 = new ServicioAsociado(null, sucursal1, servicio2);

    servicioAsociadoRepository.save(servicioAsociado1);
    servicioAsociadoRepository.save(servicioAsociado2);
  }

  private void generarUsuarios() {
    System.out.println("##################");
    System.out.println("Generando usuarios");
    System.out.println("##################");

    Usuario usuario1 = new Usuario("Juan", "contraDif111", null, null, null);
    Usuario usuario2 = new Usuario("Pedro", "contraDif222", null, null, null);
    Usuario usuario3 = new Usuario("Carlos", "contraDif111", null, null, null);
    Usuario usuario4 = new Usuario("Luciano", "contraDif222", null, null, null);
    Usuario usuario5 = new Usuario("Juan", "contraDif222", null, null, null);

    usuario1.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario1.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuario2.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario2.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuario3.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario3.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuario4.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario4.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuario5.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario5.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));


    usuarioRepository.save(usuario1);
    usuarioRepository.save(usuario2);
    usuarioRepository.save(usuario3);
    usuarioRepository.save(usuario4);
    usuarioRepository.save(usuario5);
  }


  private void generarComunidades() {

    System.out.println("#####################");
    System.out.println("Generando comunidades");
    System.out.println("#####################");

    List<Usuario> miembrosComu1 = new ArrayList<>();
    miembrosComu1.add(usuarioRepository.getById(1, Usuario.class));
    miembrosComu1.add(usuarioRepository.getById(2, Usuario.class));
    miembrosComu1.add(usuarioRepository.getById(3, Usuario.class));
    miembrosComu1.add(usuarioRepository.getById(4, Usuario.class));
    miembrosComu1.add(usuarioRepository.getById(5, Usuario.class));

    List<Usuario> miembrosComu2 = new ArrayList<>();
    miembrosComu2.add(usuarioRepository.getById(1, Usuario.class));
    miembrosComu2.add(usuarioRepository.getById(2, Usuario.class));
    miembrosComu2.add(usuarioRepository.getById(3, Usuario.class));
    miembrosComu2.add(usuarioRepository.getById(4, Usuario.class));


    Comunidad comunidad1 = new Comunidad("Discapacidad Auditiva");
    comunidad1.setPuntosDeConfianza(5.0);
    comunidad1.setMiembros(miembrosComu1);
    comunidad1.setUltimaFechaDeFusion(LocalDateTime.now().minusYears(1));
    comunidadRepository.save(comunidad1);

    Comunidad comunidad2 = new Comunidad("Discapacidad Visual");
    comunidad2.setPuntosDeConfianza(5.0);
    comunidad2.setMiembros(miembrosComu2);
    comunidad2.setUltimaFechaDeFusion(LocalDateTime.now().minusYears(1));
    comunidadRepository.save(comunidad2);
  }
}

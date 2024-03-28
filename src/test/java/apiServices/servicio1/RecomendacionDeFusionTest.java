package apiServices.servicio1;


import externalservices.FusionadorDeComunidades;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import layers.models.domain.*;
import layers.models.repositories.*;
import org.junit.jupiter.api.*;

public class RecomendacionDeFusionTest {
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

  @BeforeEach
  public void setUp() throws IOException {
    comunidadRepository = new ComunidadRepository();
    usuarioRepository = new UsuarioRepository();
    servicioAsociadoRepository = new ServicioAsociadoRepository();
    establecimientoRepository = new EstablecimientoRepository();
    localizacionRepository = new LocalizacionRepository();

    generarServiciosAsociados();
    generarUsuarios();
    generarComunidades();
    fusionDeComunidades = new FusionadorDeComunidades(comunidadRepository, usuarioRepository);
  }

  @Test
  public void laCantidadDeMiembrosEnAmbasComunidadesEsDeDos() {
    Assertions.assertEquals(2, comunidadRepository.getById(1, Comunidad.class).getMiembros().size());
    Assertions.assertEquals(2, comunidadRepository.getById(2, Comunidad.class).getMiembros().size());
  }

  @Test
  public void dadoDosComunidadesMuySimilaresLasDevuelveComoSugerencia() throws Exception {
    List<Comunidad> comunidades = fusionDeComunidades.recomendacionDeFusion();
    Assertions.assertEquals(2, comunidades.size());

    Comunidad comunidad1 = comunidades.get(0);
    Comunidad comunidad2 = comunidades.get(1);

    Assertions.assertEquals(1, comunidad1.getId());
    Assertions.assertEquals(2, comunidad2.getId());
    Assertions.assertEquals(2, comunidad1.getMiembros().size());
    Assertions.assertEquals(2, comunidad2.getMiembros().size());
  }

  @Test
  public void dadoUnGrupoMuyDistintoDeComunidadesNoDevuelveNinguna() throws Exception {
    tearDownClass();
    generoComunidadDistinta();
    generotraComunidadDistinta();


    List<Comunidad> comunidades = fusionDeComunidades.recomendacionDeFusion();
    Assertions.assertEquals(0, comunidades.size());


  }

  private void generotraComunidadDistinta() throws IOException {
    List<Servicio> servicios = new ArrayList<>();
    Servicio servicio = new Servicio("Banio", "de hombres");
    servicios.add(servicio);

    Sucursal sucursal = new Sucursal("Santander", servicios, new UbicacionGeografica(-26.8753965086829, -54.6516966230371));
    localizacionRepository.getOrCreate(sucursal.getLocalizacion());
    establecimientoRepository.save(sucursal);

    ServicioAsociado servicioAsociado = new ServicioAsociado(null, sucursal, servicio);
    servicioAsociadoRepository.save(servicioAsociado);

    Usuario usuario = new Usuario("Franchu", "maverga912", null, null, null);
    usuario.addServicioAsociado(servicioAsociado);
    usuarioRepository.save(usuario);

    Comunidad comunidad = new Comunidad("Embarazadas");
    comunidad.setPuntosDeConfianza(8.0);
    List<Usuario> miembros = new ArrayList<>();
    miembros.add(usuario);
    comunidad.setMiembros(miembros);
    comunidad.setUltimaFechaDeFusion(LocalDateTime.now());
    comunidadRepository.save(comunidad);
  }

  private void generoComunidadDistinta() throws IOException {
    List<Servicio> servicios = new ArrayList<>();
    Servicio servicio = new Servicio("Escaleras", "mecanicas");
    servicios.add(servicio);

    Sucursal sucursal = new Sucursal("Santander", servicios, new UbicacionGeografica(-26.8753965086829, -54.6516966230371));
    localizacionRepository.getOrCreate(sucursal.getLocalizacion());
    establecimientoRepository.save(sucursal);

    ServicioAsociado servicioAsociado = new ServicioAsociado(null, sucursal, servicio);
    servicioAsociadoRepository.save(servicioAsociado);

    Usuario usuario = new Usuario("Fran", "maverga912", null, null, null);
    usuario.addServicioAsociado(servicioAsociado);
    usuarioRepository.save(usuario);

    Comunidad comunidad = new Comunidad("Discapacidad Motriz");
    comunidad.setPuntosDeConfianza(6.0);
    List<Usuario> miembros = new ArrayList<>();
    miembros.add(usuario);
    comunidad.setMiembros(miembros);
    comunidad.setUltimaFechaDeFusion(LocalDateTime.now());
    comunidadRepository.save(comunidad);


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

    usuario1.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario1.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuario2.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario2.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuarioRepository.save(usuario1);
    usuarioRepository.save(usuario2);
  }


  private void generarComunidades() {

    System.out.println("#####################");
    System.out.println("Generando comunidades");
    System.out.println("#####################");

    Comunidad comunidad1 = new Comunidad("Discapacidad Auditiva");
    comunidad1.setPuntosDeConfianza(5.0);
    comunidad1.setMiembros(usuarioRepository.getAll(Usuario.class));
    comunidad1.setUltimaFechaDeFusion(LocalDateTime.now().minusYears(1));
    comunidadRepository.save(comunidad1);

    Comunidad comunidad2 = new Comunidad("Discapacidad Visual");
    comunidad2.setPuntosDeConfianza(5.0);
    comunidad2.setMiembros(usuarioRepository.getAll(Usuario.class));
    comunidad2.setUltimaFechaDeFusion(LocalDateTime.now().minusYears(1));
    comunidadRepository.save(comunidad2);
  }
}

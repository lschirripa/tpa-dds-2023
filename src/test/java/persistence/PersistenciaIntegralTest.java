package persistence;

import externalservices.CalculadorDeConfianza;
import externalservices.apiservicio2.entities.ResponseData;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import layers.models.domain.*;
import layers.models.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

public class PersistenciaIntegralTest {

  private static UsuarioRepository usuarioRepository = new UsuarioRepository();
  private static UbicacionGeograficaRepository ubica = new UbicacionGeograficaRepository();
  private static ServicioAsociadoRepository servAso = new ServicioAsociadoRepository();
  private static ComunidadRepository comuRepo = new ComunidadRepository();
  private static IncidenteRepository inciRepo = new IncidenteRepository();
  private static ServicioRepository servRepo = new ServicioRepository();
  private static EntidadRepository entidadRepository = new EntidadRepository();
  private static PrestadoraDeServicioRepository prestadoraRepository = new PrestadoraDeServicioRepository();
  private static OrganismoDeControlRepository orgaDeControlRepository = new OrganismoDeControlRepository();
  private static EstablecimientoRepository estableRepo = new EstablecimientoRepository();
  private static LocalizacionRepository localizacionRepository = new LocalizacionRepository();
  CalculadorDeConfianza calculadorDeConfianza = new CalculadorDeConfianza();
  Response<ResponseData> response;

  private static void crearYGuardarEstablecimientos() throws IOException {
    List<Servicio> servicios = servRepo.getAll(Servicio.class);

    Estacion estacion1 = new Estacion();
    estacion1.setNombre("Estacion 1");
    estacion1.setUbicacionGeografica(ubica.getById(1, UbicacionGeografica.class));
    estacion1.setServicios(servicios);

    Estacion estacion2 = new Estacion("Estacion 2", servicios, ubica.getById(1, UbicacionGeografica.class));

    localizacionRepository.getOrCreate(estacion1.getLocalizacion());
    localizacionRepository.getOrCreate(estacion2.getLocalizacion());


    estableRepo.save(estacion1);
    estableRepo.save(estacion2);

    Sucursal sucursal2 = new Sucursal();
    sucursal2.setNombre("Sucursal 2");
    sucursal2.setUbicacionGeografica(ubica.getById(1, UbicacionGeografica.class));
    sucursal2.setServicios(servicios);

    Sucursal sucursal1 = new Sucursal("Sucursal 1", servicios, ubica.getById(1, UbicacionGeografica.class));

    localizacionRepository.getOrCreate(sucursal2.getLocalizacion());
    localizacionRepository.getOrCreate(sucursal1.getLocalizacion());

    estableRepo.save(sucursal1);
    estableRepo.save(sucursal2);

    Linea linea1 = entidadRepository.getLineaById(3, Linea.class);
    linea1.agregarEstacion(estacion1);
    linea1.setEstacionOrigen(estableRepo.getEstacionById(1, Estacion.class));
    linea1.setEstacionDestino(estableRepo.getEstacionById(2, Estacion.class));

    Organizacion organizacion1 = entidadRepository.getOrganizacionById(1, Organizacion.class);

    organizacion1.agregarSucursal(sucursal1);
    organizacion1.agregarSucursal(sucursal2);

    entidadRepository.update(linea1);
    entidadRepository.update(organizacion1);

    ServicioAsociado servicioAsociado1 = servAso.getById(1, ServicioAsociado.class);
    servicioAsociado1.setEstablecimiento(estableRepo.getEstacionById(1, Estacion.class));

    ServicioAsociado servicioAsociado2 = servAso.getById(2, ServicioAsociado.class);
    servicioAsociado2.setEstablecimiento(estableRepo.getEstacionById(2, Estacion.class));

  }

  private static void crearYGuardarOrganismos() {
    OrganismoDeControl organismoDeControl = new OrganismoDeControl(new Organismo("Organismo 1", "Descripcion 1"));
    organismoDeControl.setUsuarioDesignado(usuarioRepository.getById(1, Usuario.class));
    organismoDeControl.addEntidad(entidadRepository.getById(2, Entidad.class));

    OrganismoDeControl organismoDeControl2 = new OrganismoDeControl(new Organismo("Organismo 2", "Descripcion 2"));
    organismoDeControl2.setUsuarioDesignado(usuarioRepository.getById(2, Usuario.class));
    organismoDeControl2.addEntidad(entidadRepository.getById(3, Entidad.class));
    organismoDeControl2.addEntidad(entidadRepository.getById(2, Entidad.class));

    orgaDeControlRepository.save(organismoDeControl);
    orgaDeControlRepository.save(organismoDeControl2);
  }

  private static void crearYGuardarPrestadoras() {
    PrestadoraDeServicio presta = new PrestadoraDeServicio(new Prestadora("Prestadora 1", "Descripcion 1"));
    presta.setUsuarioDesignado(usuarioRepository.getById(1, Usuario.class));
    presta.addEntidad(entidadRepository.getById(1, Entidad.class));
    presta.addEntidad(entidadRepository.getById(2, Entidad.class));

    PrestadoraDeServicio presta2 = new PrestadoraDeServicio(new Prestadora("Prestadora 2", "Descripcion 2"));
    presta2.setUsuarioDesignado(usuarioRepository.getById(2, Usuario.class));
    presta2.addEntidad(entidadRepository.getById(2, Entidad.class));

    prestadoraRepository.save(presta);
    prestadoraRepository.save(presta2);
  }

  private static void crearYGuardarEntidades() throws IOException {
    Organizacion organizacion = new Organizacion();
    organizacion.setNombre("Organizacion 1");
    organizacion.setUbicacionActual(ubica.getById(1, UbicacionGeografica.class));

    Organizacion organizacion2 = new Organizacion();
    organizacion2.setNombre("Organizacion 2");
    organizacion2.setUbicacionActual(ubica.getById(1, UbicacionGeografica.class));

    Linea linea = new Linea();
    linea.setNombre("Linea 1");
    linea.setUbicacionActual(ubica.getById(1, UbicacionGeografica.class));

    localizacionRepository.getOrCreate(organizacion.getLocalizacion());
    localizacionRepository.getOrCreate(organizacion2.getLocalizacion());
    localizacionRepository.getOrCreate(linea.getLocalizacion());


    entidadRepository.save(organizacion);
    entidadRepository.save(organizacion2);
    entidadRepository.save(linea);

    ServicioAsociado servicioAsociado1 = servAso.getById(1, ServicioAsociado.class);
    ServicioAsociado servicioAsociado2 = servAso.getById(2, ServicioAsociado.class);

    servicioAsociado1.setEntidad(organizacion);
    servicioAsociado2.setEntidad(linea);

    servAso.update(servicioAsociado1);
    servAso.update(servicioAsociado2);
  }

  private static void crearYGuardarServicios() {
    Servicio servicio = new Servicio();
    servicio.setNombre("Servicio 1");
    servicio.setDescripcion("Descripcion 1");

    Servicio servicio2 = new Servicio();
    servicio2.setNombre("Servicio 2");
    servicio2.setDescripcion("Descripcion 2");

    crearYGuardarServicioAsociado();
    ServicioAsociado servicioAsociado = servAso.getById(1, ServicioAsociado.class);
    ServicioAsociado servicioAsociado2 = servAso.getById(2, ServicioAsociado.class);

    List<ServicioUsuario> servicioUsuarios = new ArrayList<>();
    servicioUsuarios.add(new ServicioUsuario(servicioAsociado, usuarioRepository.getById(1, Usuario.class), true, false));
    servicioUsuarios.add(new ServicioUsuario(servicioAsociado, usuarioRepository.getById(2, Usuario.class), false, true));

    servicioAsociado.setServicioUsuarios(servicioUsuarios);

    List<ServicioUsuario> servicioUsuarios2 = new ArrayList<>();
    servicioUsuarios2.add(new ServicioUsuario(servicioAsociado2, usuarioRepository.getById(1, Usuario.class), false, true));

    servicioAsociado2.setServicioUsuarios(servicioUsuarios2);

    servRepo.save(servicio);
    servRepo.save(servicio2);
  }

  private static void crearYGuardarIncidentes() {
    Incidente incidente = new Incidente();
    incidente.setDescripcion("Incidente 1");
    incidente.setFechaCreacion(LocalDateTime.now());
    incidente.setUsuarioDeApertura(usuarioRepository.getById(1, Usuario.class));
    incidente.setUsuarioDeResolucion(usuarioRepository.getById(2, Usuario.class));

    Incidente incidente2 = new Incidente();
    incidente2.setDescripcion("Incidente 2");
    incidente2.setFechaCreacion(LocalDateTime.now().minusHours(1));
    incidente2.setUsuarioDeApertura(usuarioRepository.getById(2, Usuario.class));
    incidente2.setUsuarioDeResolucion(usuarioRepository.getById(1, Usuario.class));

    Incidente incidente3 = new Incidente();
    incidente3.setDescripcion("Incidente 3");
    incidente3.setFechaCreacion(LocalDateTime.now().minusHours(5));
    incidente3.setFechaResolucion(LocalDateTime.now().minusHours(5));
    incidente3.setUsuarioDeApertura(usuarioRepository.getById(2, Usuario.class));
    incidente3.setUsuarioDeResolucion(usuarioRepository.getById(1, Usuario.class));

    Incidente incidente4 = new Incidente();
    incidente4.setDescripcion("Incidente 4");
    incidente4.setFechaCreacion(LocalDateTime.now().minusHours(15));
    incidente4.setFechaResolucion(LocalDateTime.now().minusHours(10));
    incidente4.setUsuarioDeApertura(usuarioRepository.getById(2, Usuario.class));
    incidente4.setUsuarioDeResolucion(usuarioRepository.getById(1, Usuario.class));


    ServicioAsociado servicioAsociado = servAso.getById(1, ServicioAsociado.class);

    incidente.setServicioIncidentado(servicioAsociado);
    incidente2.setServicioIncidentado(servicioAsociado);
    incidente3.setServicioIncidentado(servicioAsociado);
    incidente4.setServicioIncidentado(servicioAsociado);
    incidente2.setFechaResolucion(LocalDateTime.now().minusMinutes(30));

    incidente.addComunidad(comuRepo.getById(1, Comunidad.class));
    incidente2.addComunidad(comuRepo.getById(1, Comunidad.class));
    incidente3.addComunidad(comuRepo.getById(1, Comunidad.class));
    incidente4.addComunidad(comuRepo.getById(1, Comunidad.class));

    inciRepo.save(incidente);
    inciRepo.save(incidente2);
    inciRepo.save(incidente3);
    inciRepo.save(incidente4);

    servicioAsociado.addHistoricoIncidente(incidente);
    servicioAsociado.addHistoricoIncidente(incidente2);
    servicioAsociado.addHistoricoIncidente(incidente3);
    servicioAsociado.addHistoricoIncidente(incidente4);

    servAso.update(servicioAsociado);

    Usuario usuario = usuarioRepository.getById(1, Usuario.class);
    usuario.addNotificacionIncidentesPendientes(incidente);
    usuario.addNotificacionIncidentesPendientes(incidente2);
    usuario.addNotificacionIncidentesPendientes(incidente3);
    usuario.addNotificacionIncidentesPendientes(incidente4);

    usuarioRepository.update(usuario);

  }

  private static void crearYGuardarComuniades() {
    Comunidad comunidad = new Comunidad("comunidad1");
    comunidad.setUltimaFechaDeFusion(LocalDateTime.now());
    comunidad.addMiembro(usuarioRepository.getById(1, Usuario.class));
    comunidad.addMiembro(usuarioRepository.getById(2, Usuario.class));
    //como mapeo manyTomany, no hace falta asignarle al usuario la comunidad, ya que se guarda en la tabla intermedia

    comuRepo.save(comunidad);

    Comunidad comunidad1 = new Comunidad("Discapacidad Auditiva");
    comunidad1.setPuntosDeConfianza(5.0);
    List<Usuario> usuarios = new ArrayList<>();
    usuarios.add(usuarioRepository.getById(1, Usuario.class));
    usuarios.add(usuarioRepository.getById(2, Usuario.class));
    comunidad1.setMiembros(usuarios);
    comunidad1.setUltimaFechaDeFusion(LocalDateTime.now().minusYears(1));
    comuRepo.save(comunidad1);

    Comunidad comunidad2 = new Comunidad("Discapacidad Visual");
    comunidad2.setPuntosDeConfianza(5.0);
    usuarios.add(usuarioRepository.getById(1, Usuario.class));
    usuarios.add(usuarioRepository.getById(2, Usuario.class));
    comunidad2.setMiembros(usuarios);
    comunidad2.setUltimaFechaDeFusion(LocalDateTime.now().minusYears(1));
    comuRepo.save(comunidad2);

  }

  private static void crearYGuardarServicioAsociado() {
    ServicioAsociado servicioAsociado = new ServicioAsociado();
    servAso.save(servicioAsociado);

    Usuario usuario = usuarioRepository.getById(1, Usuario.class);
    usuario.addServicioAsociado(servicioAsociado);
    usuarioRepository.update(usuario);

    ServicioAsociado servicioAsociado2 = new ServicioAsociado();
    servAso.save(servicioAsociado2);

  }

  private static void crearYGuardarUsuarios() {
    UbicacionGeografica ubicacion = new UbicacionGeografica(-26.8753965086829, -54.6516966230371);
    //saveUbicacion(ubicacion);
    DatosPersonalesUsuario datos = new DatosPersonalesUsuario("fran", "mav", "fmaver@carp.com", "123456789");
    List<LocalTime> horariosNoti = new ArrayList<>();
    horariosNoti.add(LocalTime.of(12, 0));
    horariosNoti.add(LocalTime.of(13, 0));

    saveUsuario("franmav", "contradificil22", datos, ubicacion, horariosNoti);
    saveUsuario("franmav2", "contradificil22", datos, ubicacion, horariosNoti);

    System.out.println("cantidad de usuarios: " + usuarioRepository.getAll(Usuario.class).size());
    usuarioRepository.getAll(Usuario.class).forEach(usuario -> System.out.println(usuario.getId()));

    Usuario usuario = usuarioRepository.getById(1, Usuario.class);
    System.out.println("nombre del usuario buscado: " + usuario.getUserName());
    usuario.setUserName("franmav3");
    usuarioRepository.update(usuario);

    System.out.println("nombre del usuario: " + usuarioRepository.getById(1, Usuario.class).getUserName());
  }

  private static void saveUsuario(String nombre, String pass,
                                  DatosPersonalesUsuario datos,
                                  UbicacionGeografica ubicacion,
                                  List<LocalTime> horariosNotificacion) {
    Usuario usuario = new Usuario();
    usuario.setUserName(nombre);
    usuario.setPassword(pass);
    usuario.setDatosPersonales(datos);
    usuario.setUbicacionActual(ubicacion);
    usuario.setHorariosNotificacion(horariosNotificacion);
    System.out.println("usuario creado: " + usuario.getUserName());
    usuarioRepository.save(usuario);

    System.out.println("cantidad de usuarios: " + usuarioRepository.getAll(Usuario.class).size());
    System.out.println("nombre del usuario: " + usuarioRepository.getById(usuario.getId(), Usuario.class).getUserName());
  }

  public void calcularConfianza() throws IOException {
    response = calculadorDeConfianza.calcularConfianza();
    Assertions.assertNotNull(200, String.valueOf(response.code()));
  }

  @Test
  public void context() throws IOException {
    crearYGuardarUsuarios();
    crearYGuardarServicios();
    crearYGuardarServicioAsociado();
    crearYGuardarComuniades();
    crearYGuardarIncidentes();
    crearYGuardarEntidades();
    crearYGuardarEstablecimientos();
    crearYGuardarPrestadoras();
    crearYGuardarOrganismos();
    //calcularConfianza();
  }
}

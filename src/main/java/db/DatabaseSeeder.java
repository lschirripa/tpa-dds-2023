package db;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import layers.models.domain.*;
import layers.models.repositories.*;
import utils.PasswordHasher;
import utils.enums.FormaNotificacion;
import utils.enums.ModoNotificacion;
import utils.enums.Rol;
import utils.enums.UserStatus;

public class DatabaseSeeder {
  private static UsuarioRepository usuarioRepository = new UsuarioRepository();
  private static UbicacionGeograficaRepository ubica = new UbicacionGeograficaRepository();
  private static ServicioAsociadoRepository servAso = new ServicioAsociadoRepository();
  private static ComunidadRepository comuRepo = new ComunidadRepository();
  private static IncidenteRepository inciRepo = new IncidenteRepository();
  private static ServicioRepository servRepo = new ServicioRepository();
  private static RoleRepository roleRepository = new RoleRepository();

  private static EntidadRepository entidadRepository = new EntidadRepository();

  private static PrestadoraDeServicioRepository prestadoraRepository = new PrestadoraDeServicioRepository();
  private static OrganismoDeControlRepository orgaDeControlRepository = new OrganismoDeControlRepository();

  private static EstablecimientoRepository estableRepo = new EstablecimientoRepository();

  private static LocalizacionRepository localizacionRepository = new LocalizacionRepository();

  private static void agregarServiciosAsociadosAUsuarios() {
    Usuario usuario1 = usuarioRepository.getById(1, Usuario.class);
    Usuario usuario2 = usuarioRepository.getById(2, Usuario.class);

    List<ServicioAsociado> serviciosAsociados = servAso.getAll(ServicioAsociado.class);

    usuario1.setServiciosAsociados(serviciosAsociados);
    usuario2.setServiciosAsociados(serviciosAsociados);

    usuarioRepository.update(usuario1);
    usuarioRepository.update(usuario2);
  }

  private static void crearYGuardarEstablecimientos() throws IOException {

    UbicacionGeografica ubicacion4 = new UbicacionGeografica(-26.8753965086829, -54.9856541651323);
    UbicacionGeografica ubicacion5 = new UbicacionGeografica(-28.8753965086829, -56.9856541651323);
    UbicacionGeografica ubicacion6 = new UbicacionGeografica(-29.8753965086829, -57.9856541651323);


    ubica.save(ubicacion4);
    ubica.save(ubicacion5);
    ubica.save(ubicacion6);

    List<Servicio> servicios = servRepo.getAll(Servicio.class);

    Estacion estacion1 = new Estacion();
    estacion1.setNombre("Plaza Constitucion");
    estacion1.setUbicacionGeografica(ubica.getById(1, UbicacionGeografica.class));
    estacion1.addServicio(servicios.get(0));
    estacion1.addServicio(servicios.get(1));
    estacion1.addServicio(servicios.get(2));
    //estacion1.setServicios(servicios);

    Estacion estacion2 = new Estacion();
    estacion2.setNombre("Hipolito Yrigoyen");
    estacion2.setUbicacionGeografica(ubica.getById(2, UbicacionGeografica.class));
    estacion2.addServicio(servicios.get(0));
    estacion2.addServicio(servicios.get(1));
    estacion2.addServicio(servicios.get(2));

    Estacion estacion3 = new Estacion();
    estacion3.setNombre("Ezeiza");
    estacion3.setUbicacionGeografica(ubica.getById(3, UbicacionGeografica.class));
    estacion3.addServicio(servicios.get(0));
    estacion3.addServicio(servicios.get(1));

    localizacionRepository.getOrCreate(estacion1.getLocalizacion());
    localizacionRepository.getOrCreate(estacion2.getLocalizacion());
    localizacionRepository.getOrCreate(estacion3.getLocalizacion());

    estableRepo.save(estacion1);
    estableRepo.save(estacion2);
    estableRepo.save(estacion3);


    Sucursal sucursal2 = new Sucursal();
    sucursal2.setNombre("Alto Avellaneda");
    sucursal2.setUbicacionGeografica(ubicacion4);
    sucursal2.setServicios(servicios);

    Sucursal sucursal1 = new Sucursal();
    sucursal1.setNombre("Alto Palermo");
    sucursal1.setUbicacionGeografica(ubicacion5);
    estacion3.getServicios().add(servicios.get(0));
    estacion3.getServicios().add(servicios.get(1));

    Sucursal sucursal3 = new Sucursal();
    sucursal3.setNombre("Dot Baires");
    sucursal3.setUbicacionGeografica(ubicacion6);
    estacion3.getServicios().add(servicios.get(2));
    estacion3.getServicios().add(servicios.get(3));

    Sucursal sucursal4 = new Sucursal();
    sucursal4.setNombre("Dot Rosario");
    sucursal4.setUbicacionGeografica(ubica.getById(3, UbicacionGeografica.class));
    estacion3.getServicios().add(servicios.get(0));
    estacion3.getServicios().add(servicios.get(2));
    estacion3.getServicios().add(servicios.get(5));

    localizacionRepository.getOrCreate(sucursal2.getLocalizacion());
    localizacionRepository.getOrCreate(sucursal1.getLocalizacion());
    localizacionRepository.getOrCreate(sucursal3.getLocalizacion());
    localizacionRepository.getOrCreate(sucursal4.getLocalizacion());

    estableRepo.save(sucursal1);
    estableRepo.save(sucursal2);
    estableRepo.save(sucursal3);
    estableRepo.save(sucursal4);

    //estacion y sucursal

    Linea linea1 = entidadRepository.getLineaById(3, Linea.class);
    linea1.agregarEstacion(estacion1);
    linea1.agregarEstacion(estacion2);
    linea1.agregarEstacion(estacion3);
    linea1.setEstacionOrigen(estableRepo.getEstacionById(1, Estacion.class));
    linea1.setEstacionDestino(estableRepo.getEstacionById(3, Estacion.class));

    Organizacion organizacion1 = entidadRepository.getOrganizacionById(1, Organizacion.class);
    organizacion1.agregarSucursal(sucursal1);
    organizacion1.agregarSucursal(sucursal2);

    Organizacion organizacion2 = entidadRepository.getOrganizacionById(2, Organizacion.class);
    organizacion2.agregarSucursal(sucursal3);
    organizacion2.agregarSucursal(sucursal4);

    entidadRepository.update(linea1);
    entidadRepository.update(organizacion1);
    entidadRepository.update(organizacion2);

    ServicioAsociado servicioAsociado1 = servAso.getById(1, ServicioAsociado.class);
    servicioAsociado1.setEstablecimiento(estacion1);
    servAso.update(servicioAsociado1);

    ServicioAsociado servicioAsociado2 = servAso.getById(2, ServicioAsociado.class);
    servicioAsociado2.setEstablecimiento(estacion2);
    servAso.update(servicioAsociado2);

    ServicioAsociado servicioAsociado3 = servAso.getById(3, ServicioAsociado.class);
    servicioAsociado3.setEstablecimiento(estacion1);
    servAso.update(servicioAsociado3);

    ServicioAsociado servicioAsociado4 = servAso.getById(4, ServicioAsociado.class);
    servicioAsociado4.setEstablecimiento(sucursal2);
    servAso.update(servicioAsociado4);

    ServicioAsociado servicioAsociado5 = servAso.getById(5, ServicioAsociado.class);
    servicioAsociado5.setEstablecimiento(estacion2);
    servAso.update(servicioAsociado5);

    ServicioAsociado servicioAsociado6 = servAso.getById(6, ServicioAsociado.class);
    servicioAsociado6.setEstablecimiento(sucursal3);
    servAso.update(servicioAsociado6);

    ServicioAsociado servicioAsociado7 = servAso.getById(7, ServicioAsociado.class);
    servicioAsociado7.setEstablecimiento(sucursal4);
    servAso.update(servicioAsociado7);

    ServicioAsociado servicioAsociado8 = servAso.getById(8, ServicioAsociado.class);
    servicioAsociado8.setEstablecimiento(estacion3);
    servAso.update(servicioAsociado8);

    ServicioAsociado servicioAsociado9 = servAso.getById(9, ServicioAsociado.class);
    servicioAsociado9.setEstablecimiento(estacion2);
    servAso.update(servicioAsociado9);

    ServicioAsociado servicioAsociado10 = servAso.getById(10, ServicioAsociado.class);
    servicioAsociado10.setEstablecimiento(sucursal4);
    servAso.update(servicioAsociado10);

    ServicioAsociado servicioAsociado11 = servAso.getById(11, ServicioAsociado.class);
    servicioAsociado11.setEstablecimiento(sucursal3);
    servAso.update(servicioAsociado11);

    ServicioAsociado servicioAsociado12 = servAso.getById(12, ServicioAsociado.class);
    servicioAsociado12.setEstablecimiento(estacion1);
    servAso.update(servicioAsociado12);

  }

  private static void crearYGuardarOrganismos() {
    OrganismoDeControl organismoDeControl = new OrganismoDeControl(new Organismo("FASE", "Ferrocarriles Argentinos Sociedad del Estado"));
    organismoDeControl.setUsuarioDesignado(usuarioRepository.getById(1, Usuario.class));
    organismoDeControl.addEntidad(entidadRepository.getById(2, Entidad.class));

    orgaDeControlRepository.save(organismoDeControl);
  }

  private static void crearYGuardarPrestadoras() {

    PrestadoraDeServicio presta = new PrestadoraDeServicio(new Prestadora("Agencia Gubernamental de Control", "Habilita y fiscaliza los locales comerciales de la Ciudad"));
    presta.setUsuarioDesignado(usuarioRepository.getById(2, Usuario.class));
    presta.addEntidad(entidadRepository.getById(2, Entidad.class));

    prestadoraRepository.save(presta);
  }

  private static void crearYGuardarEntidades() throws IOException {

    UbicacionGeografica ubicacion1 = new UbicacionGeografica(-26.7566846542456, -54.6621654586545);
    UbicacionGeografica ubicacion2 = new UbicacionGeografica(-26.7752278257855, -54.8946513352586);
    UbicacionGeografica ubicacion3 = new UbicacionGeografica(-26.8016515384538, -54.9176246216515);

    ubica.save(ubicacion1);
    ubica.save(ubicacion2);
    ubica.save(ubicacion3);

    Organizacion organizacion = new Organizacion();
    organizacion.setNombre("Centro Comercial Alto");
    organizacion.setUbicacionActual(ubicacion2);

    Organizacion organizacion2 = new Organizacion();
    organizacion2.setNombre("Centro Comercial Dot");
    organizacion2.setUbicacionActual(ubicacion3);

    Linea linea = new Linea();
    linea.setNombre("Tren Roca ramal Ezeiza");
    linea.setUbicacionActual(ubicacion1);

    localizacionRepository.getOrCreate(organizacion.getLocalizacion());
    localizacionRepository.getOrCreate(organizacion2.getLocalizacion());
    localizacionRepository.getOrCreate(linea.getLocalizacion());


    entidadRepository.save(organizacion);
    entidadRepository.save(organizacion2);
    entidadRepository.save(linea);

    ServicioAsociado servicioAsociado1 = servAso.getById(1, ServicioAsociado.class);
    servicioAsociado1.setEntidad(linea);
    servAso.update(servicioAsociado1);

    ServicioAsociado servicioAsociado2 = servAso.getById(2, ServicioAsociado.class);
    servicioAsociado2.setEntidad(linea);
    servAso.update(servicioAsociado2);

    ServicioAsociado servicioAsociado3 = servAso.getById(3, ServicioAsociado.class);
    servicioAsociado3.setEntidad(linea);
    servAso.update(servicioAsociado3);

    ServicioAsociado servicioAsociado4 = servAso.getById(4, ServicioAsociado.class);
    servicioAsociado4.setEntidad(organizacion);
    servAso.update(servicioAsociado4);

    ServicioAsociado servicioAsociado5 = servAso.getById(5, ServicioAsociado.class);
    servicioAsociado5.setEntidad(linea);
    servAso.update(servicioAsociado5);

    ServicioAsociado servicioAsociado6 = servAso.getById(6, ServicioAsociado.class);
    servicioAsociado6.setEntidad(organizacion2);
    servAso.update(servicioAsociado6);

    ServicioAsociado servicioAsociado7 = servAso.getById(7, ServicioAsociado.class);
    servicioAsociado7.setEntidad(organizacion2);
    servAso.update(servicioAsociado7);

    ServicioAsociado servicioAsociado8 = servAso.getById(8, ServicioAsociado.class);
    servicioAsociado8.setEntidad(linea);
    servAso.update(servicioAsociado8);

    ServicioAsociado servicioAsociado9 = servAso.getById(9, ServicioAsociado.class);
    servicioAsociado9.setEntidad(linea);
    servAso.update(servicioAsociado9);

    ServicioAsociado servicioAsociado10 = servAso.getById(10, ServicioAsociado.class);
    servicioAsociado10.setEntidad(organizacion2);
    servAso.update(servicioAsociado10);

    ServicioAsociado servicioAsociado11 = servAso.getById(11, ServicioAsociado.class);
    servicioAsociado11.setEntidad(organizacion2);
    servAso.update(servicioAsociado11);

    ServicioAsociado servicioAsociado12 = servAso.getById(12, ServicioAsociado.class);
    servicioAsociado12.setEntidad(linea);
    servAso.update(servicioAsociado12);

  }

  private static void crearYGuardarServicios() {
    Servicio servicio = new Servicio();
    servicio.setNombre("Banio sin genero");
    servicio.setDescripcion("Sin Genero");

    Servicio servicio2 = new Servicio();
    servicio2.setNombre("Escaleras Mecanicas");
    servicio2.setDescripcion("De ambos Sentidos");

    Servicio servicio3 = new Servicio();
    servicio3.setNombre("Ascensor");
    servicio3.setDescripcion("Con sensor infrarojo");

    Servicio servicio4 = new Servicio();
    servicio4.setNombre("Rampa");
    servicio4.setDescripcion("Para sillas de ruedas");

    Servicio servicio5 = new Servicio();
    servicio5.setNombre("Banio para Discapacitados");
    servicio5.setDescripcion("Para Discapacitados");

    Servicio servicio6 = new Servicio();
    servicio6.setNombre("Banio Quimico");
    servicio6.setDescripcion("Quimico");

    Servicio servicio7 = new Servicio();
    servicio7.setNombre("Pantalla Braille");
    servicio7.setDescripcion("Para no videntes");


    servRepo.save(servicio);
    servRepo.save(servicio2);
    servRepo.save(servicio3);
    servRepo.save(servicio4);
    servRepo.save(servicio5);
    servRepo.save(servicio6);
    servRepo.save(servicio7);

    crearYGuardarServicioAsociado();

    ServicioAsociado servicioAsociado1 = servAso.getById(1, ServicioAsociado.class);
    ServicioAsociado servicioAsociado2 = servAso.getById(2, ServicioAsociado.class);

    List<ServicioUsuario> servicioUsuarios = new ArrayList<>();
    servicioUsuarios.add(new ServicioUsuario(servicioAsociado1, usuarioRepository.getById(1, Usuario.class), true, false));
    servicioUsuarios.add(new ServicioUsuario(servicioAsociado1, usuarioRepository.getById(2, Usuario.class), false, true));

    servicioAsociado1.setServicioUsuarios(servicioUsuarios);

    List<ServicioUsuario> servicioUsuarios2 = new ArrayList<>();
    servicioUsuarios2.add(new ServicioUsuario(servicioAsociado2, usuarioRepository.getById(1, Usuario.class), false, true));

    servicioAsociado2.setServicioUsuarios(servicioUsuarios2);

    servAso.update(servicioAsociado1);
    servAso.update(servicioAsociado2);

  }

  private static void crearYGuardarIncidentes() {
    Incidente incidente = new Incidente();
    incidente.setDescripcion("Se corto el agua");
    incidente.setFechaCreacion(LocalDateTime.now());
    incidente.setUsuarioDeApertura(usuarioRepository.getById(1, Usuario.class));
    incidente.setUsuarioDeResolucion(usuarioRepository.getById(2, Usuario.class));

    Incidente incidente2 = new Incidente();
    incidente2.setDescripcion("Las puertas no traban");
    incidente2.setFechaCreacion(LocalDateTime.now().minusHours(1));
    incidente2.setUsuarioDeApertura(usuarioRepository.getById(2, Usuario.class));
    incidente2.setUsuarioDeResolucion(usuarioRepository.getById(1, Usuario.class));

    Incidente incidente3 = new Incidente();
    incidente3.setDescripcion("Se cayo el techo");
    incidente3.setFechaCreacion(LocalDateTime.now().minusDays(2).minusHours(1).minusMinutes(10));
    incidente3.setUsuarioDeApertura(usuarioRepository.getById(2, Usuario.class));
    incidente3.setUsuarioDeResolucion(usuarioRepository.getById(1, Usuario.class));

    ServicioAsociado servicioAsociado = servAso.getById(1, ServicioAsociado.class);
    ServicioAsociado servicioAsociado2 = servAso.getById(2, ServicioAsociado.class);

    incidente.setServicioIncidentado(servicioAsociado);
    incidente2.setServicioIncidentado(servicioAsociado2);
    incidente2.setFechaResolucion(LocalDateTime.now().minusMinutes(30));

    incidente3.setServicioIncidentado(servicioAsociado2);
    incidente3.setFechaResolucion(LocalDateTime.now().minusDays(2).minusMinutes(30));



    incidente.addComunidad(comuRepo.getById(1, Comunidad.class));
    incidente2.addComunidad(comuRepo.getById(1, Comunidad.class));
    incidente3.addComunidad(comuRepo.getById(1, Comunidad.class));


    System.out.println("CANTIDAD DE COMUNIDADES EN INCIDENTE: ");
    System.out.println(incidente.getComunidades().size());

    inciRepo.save(incidente);
    inciRepo.save(incidente2);
    inciRepo.save(incidente3);

    servicioAsociado.addHistoricoIncidente(incidente);
    servicioAsociado.addHistoricoIncidente(incidente2);
    servicioAsociado.addHistoricoIncidente(incidente3);

    servAso.update(servicioAsociado);
    servAso.update(servicioAsociado2);


  }

  private static void crearYGuardarComunidades() {
    Comunidad comunidad = new Comunidad("Dificultad Auditiva");
    comunidad.addMiembro(usuarioRepository.getById(1, Usuario.class));
    comunidad.addMiembro(usuarioRepository.getById(2, Usuario.class));
    //como mapeo manyTomany, no hace falta asignarle al usuario la comunidad, ya que se guarda en la tabla intermedia

    comuRepo.save(comunidad);

    Comunidad comunidad2 = new Comunidad("Dificultad Visual");
    comunidad2.addMiembro(usuarioRepository.getById(1, Usuario.class));
    comunidad2.addMiembro(usuarioRepository.getById(2, Usuario.class));

    comuRepo.save(comunidad2);
  }

  private static void crearYGuardarServicioAsociado() {
    Servicio servicio1 = servRepo.getById(1, Servicio.class); //baño
    Servicio servicio2 = servRepo.getById(2, Servicio.class); //escaleras
    Servicio servicio3 = servRepo.getById(3, Servicio.class); //ascensor
    Servicio servicio4 = servRepo.getById(4, Servicio.class); //rampa
    Servicio servicio5 = servRepo.getById(5, Servicio.class); //baño discapacitados
    Servicio servicio6 = servRepo.getById(6, Servicio.class); //baño quimico
    Servicio servicio7 = servRepo.getById(7, Servicio.class); //pantalla braille

    ServicioAsociado servicioAsociado = new ServicioAsociado();
    servicioAsociado.setServicio(servicio1);
    servAso.save(servicioAsociado);

    Usuario usuario = usuarioRepository.getById(1, Usuario.class);
    usuario.addServicioAsociado(servicioAsociado);
    usuarioRepository.update(usuario);

    ServicioAsociado servicioAsociado2 = new ServicioAsociado();
    servicioAsociado2.setServicio(servicio2);
    servAso.save(servicioAsociado2);
    usuario.addServicioAsociado(servicioAsociado2);
    usuarioRepository.update(usuario);

    ServicioAsociado servicioAsociado3 = new ServicioAsociado();
    servicioAsociado3.setServicio(servicio2);
    servAso.save(servicioAsociado3);

    ServicioAsociado servicioAsociado4 = new ServicioAsociado();
    servicioAsociado4.setServicio(servicio2);
    servAso.save(servicioAsociado4);

    ServicioAsociado servicioAsociado5 = new ServicioAsociado();
    servicioAsociado5.setServicio(servicio3);
    servAso.save(servicioAsociado5);

    ServicioAsociado servicioAsociado6 = new ServicioAsociado();
    servicioAsociado6.setServicio(servicio4);
    servAso.save(servicioAsociado6);

    ServicioAsociado servicioAsociado7 = new ServicioAsociado();
    servicioAsociado7.setServicio(servicio5);
    servAso.save(servicioAsociado7);

    ServicioAsociado servicioAsociado8 = new ServicioAsociado();
    servicioAsociado8.setServicio(servicio6);
    servAso.save(servicioAsociado8);

    ServicioAsociado servicioAsociado9 = new ServicioAsociado();
    servicioAsociado9.setServicio(servicio7);
    servAso.save(servicioAsociado9);

    ServicioAsociado servicioAsociado10 = new ServicioAsociado();
    servicioAsociado10.setServicio(servicio4);
    servAso.save(servicioAsociado10);

    ServicioAsociado servicioAsociado11 = new ServicioAsociado();
    servicioAsociado11.setServicio(servicio5);
    servAso.save(servicioAsociado11);

    ServicioAsociado servicioAsociado12 = new ServicioAsociado();
    servicioAsociado12.setServicio(servicio7);
    servAso.save(servicioAsociado12);

  }

  private static void crearYGuardarUsuarios() {
    UbicacionGeografica ubicacion = new UbicacionGeografica(-26.8753965086829, -54.6516966230371);
    //saveUbicacion(ubicacion);
    DatosPersonalesUsuario datosFran = new DatosPersonalesUsuario("fran", "mav", "franciscomaver.fm@gmail.com", "1138718498");
    DatosPersonalesUsuario datosJuan = new DatosPersonalesUsuario("juan", "cobas", "cobasjuanignacio@gmail.com", "1136430993");
    DatosPersonalesUsuario datosLucho = new DatosPersonalesUsuario("Lucho", "Schirri", "lucho.schirripa@gmail.com", "1155239730");
    DatosPersonalesUsuario datosJuli = new DatosPersonalesUsuario("Juli", "Ramos", "julieta.ramos.jr@gmail.com", "1153867878");

    List<LocalTime> horariosNoti = new ArrayList<>();
    horariosNoti.add(LocalTime.now().plusMinutes(1));
    horariosNoti.add(LocalTime.of(12, 0));
    horariosNoti.add(LocalTime.of(13, 0));


    saveUsuario("franmav", "contradificil22", datosFran, ubicacion, horariosNoti);
    saveUsuario("juancai", "contradificil22", datosJuan, ubicacion, horariosNoti);
    saveUsuario("luchokpo", "contradificil123", datosLucho, ubicacion, horariosNoti);
    saveUsuarioNormal("juli", "contradificil22", datosJuli, ubicacion, horariosNoti);

    Usuario fran = usuarioRepository.getById(1, Usuario.class);
    fran.setFormaNotificacion(FormaNotificacion.NOW);
    usuarioRepository.update(fran);

    System.out.println("cantidad de usuarios: " + usuarioRepository.getAll(Usuario.class).size());
    usuarioRepository.getAll(Usuario.class).forEach(usuario -> System.out.println(usuario.getId()));

    Usuario usuario = usuarioRepository.getById(1, Usuario.class);
    System.out.println("nombre del usuario buscado: " + usuario.getUserName());
    usuario.setUserName("franmav3");
    usuarioRepository.update(usuario);

    System.out.println("nombre del usuario: " + usuarioRepository.getById(1, Usuario.class).getUserName());
  }

  private static void saveUsuarioNormal(String nombre, String pass,
                                        DatosPersonalesUsuario datos,
                                        UbicacionGeografica ubicacion,
                                        List<LocalTime> horariosNotificacion) {
    Usuario usuario = new Usuario();
    usuario.setUserName(nombre);
    usuario.setPasswordSinValidacion(pass);
    usuario.setDatosPersonales(datos);
    usuario.setUbicacionActual(ubicacion);
    usuario.setHorariosNotificacion(horariosNotificacion);
    usuario.setModoNotificacion(ModoNotificacion.WHATSAPP);
    usuario.setCreatedAt(LocalDateTime.now());
    usuario.setUserStatus(UserStatus.ACTIVE);
    usuario.setRole(Rol.USUARIO);

    System.out.println("usuario creado: " + usuario.getUserName());
    usuarioRepository.save(usuario);

    System.out.println("cantidad de usuarios: " + usuarioRepository.getAll(Usuario.class).size());
    System.out.println("nombre del usuario: " + usuarioRepository.getById(usuario.getId(), Usuario.class).getUserName());
  }

  private static void saveUsuario(String nombre, String pass,
                                  DatosPersonalesUsuario datos,
                                  UbicacionGeografica ubicacion,
                                  List<LocalTime> horariosNotificacion) {
    Usuario usuario = new Usuario();
    usuario.setUserName(nombre);
    usuario.setPasswordSinValidacion(pass);
    usuario.setDatosPersonales(datos);
    usuario.setUbicacionActual(ubicacion);
    usuario.setHorariosNotificacion(horariosNotificacion);
    usuario.setModoNotificacion(ModoNotificacion.WHATSAPP);
    usuario.setCreatedAt(LocalDateTime.now());
    usuario.setUserStatus(UserStatus.ACTIVE);
    usuario.setRole(Rol.ADMINISTRADOR);

    System.out.println("usuario creado: " + usuario.getUserName());
    usuarioRepository.save(usuario);

    System.out.println("cantidad de usuarios: " + usuarioRepository.getAll(Usuario.class).size());
    System.out.println("nombre del usuario: " + usuarioRepository.getById(usuario.getId(), Usuario.class).getUserName());
  }

  public void seed() throws IOException {
    crearYGuardarUsuarios();
    // SE ESTAN CREANDO LOS SERVICIOS ASOCIADOS DENTRO DE crearYGuardarServicios
    crearYGuardarServicios();
    crearYGuardarComunidades();
    crearYGuardarIncidentes();
    crearYGuardarEntidades();
    crearYGuardarEstablecimientos();
    crearYGuardarPrestadoras();
    crearYGuardarOrganismos();

    //agregarServiciosAsociadosAUsuarios();
  }

}

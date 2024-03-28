package externalservices.cron;

import externalservices.Notificador;
import externalservices.UsuarioScheduler;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import layers.controllers.IncidenteController;
import layers.controllers.UsuarioController;
import layers.models.domain.*;
import layers.models.repositories.*;
import layers.services.*;
import org.quartz.SchedulerException;
import utils.enums.FormaNotificacion;
import utils.enums.ModoNotificacion;
import utils.enums.Rol;

public class CronQuartzApp {
  public static void main(String[] args) throws SchedulerException, IOException {

    UsuarioRepository usuarioRepository = new UsuarioRepository();
    ServicioAsociadoRepository servicioAsociadoRepository = new ServicioAsociadoRepository();
    ServicioRepository servicioRepository = new ServicioRepository();
    ServicioUsuarioRepository servicioUsuarioRepository = new ServicioUsuarioRepository();
    EntidadRepository entidadRepository = new EntidadRepository();
    EstablecimientoRepository establecimientoRepository = new EstablecimientoRepository();
    IncidenteRepository incidenteRepository = new IncidenteRepository();
    LocalizacionRepository localizacionRepository = new LocalizacionRepository();
    ComunidadRepository comunidadRepository = new ComunidadRepository();
    RoleRepository roleRepository = new RoleRepository();

    ServicioService servicioService = new ServicioService(servicioRepository, servicioUsuarioRepository);
    ServicioAsociadoService servicioAsociadoService = new ServicioAsociadoService(servicioAsociadoRepository, servicioService);
    UsuarioService usuarioService = new UsuarioService(usuarioRepository, servicioAsociadoService);
    IncidenteService incidenteService = new IncidenteService(incidenteRepository, usuarioService);
    EntidadService entidadService = new EntidadService(entidadRepository, localizacionRepository);
    EstablecimientoService establecimientoService = new EstablecimientoService(establecimientoRepository, localizacionRepository);
    ComunidadService comunidadService = new ComunidadService(comunidadRepository, usuarioService);

    UsuarioController usuarioController = new UsuarioController(usuarioService, entidadService, establecimientoService, servicioAsociadoService);
    IncidenteController incidenteController = new IncidenteController(incidenteService, servicioAsociadoService, usuarioService);

    Notificador notificador = new Notificador(usuarioService);
    UsuarioScheduler usuarioScheduler = new UsuarioScheduler();

    //UsuarioController usuarioController = new UsuarioController(usuarioRepository, entidadService, establecimientoService, usuarioScheduler);

    Comunidad comunidad = new Comunidad("comunidad1");
    comunidadService.save(comunidad);

    UbicacionGeografica ubicacionGeografica =
        new UbicacionGeografica(-26.8753965086829, -54.6516966230371);

    DatosPersonalesUsuario datosPersonalesUsuario
        = new DatosPersonalesUsuario("Guadi", "Mav", "franciscomaver.fm@gmail.com", "1138718498");

    Usuario usuario = new Usuario("franmav", "vamosRiver912!",
        datosPersonalesUsuario, ubicacionGeografica, Rol.USUARIO);

    usuario.setFormaNotificacion(FormaNotificacion.LATER);
    usuario.setModoNotificacion(ModoNotificacion.WHATSAPP);
    usuario.addComunidad(comunidad);
    //TODO --> chequear en todos los casos y hacerlo bidireccional
    comunidad.addMiembro(usuario);
    usuarioRepository.save(usuario);

    List<LocalTime> horarios = new ArrayList<>();
    horarios.add(LocalTime.of(12, 57));
    horarios.add(LocalTime.of(12, 54));

    Organizacion organizacion = new Organizacion();
    organizacion.setNombre("Santander Rio");
    organizacion.setUbicacionActual(ubicacionGeografica);
    entidadService.save(organizacion);

    Sucursal sucursal = new Sucursal();
    sucursal.setNombre("Deutsch Bank");
    sucursal.setUbicacionGeografica(ubicacionGeografica);
    establecimientoService.save(sucursal);

    Servicio servicio = new Servicio();
    servicio.setNombre("Banio de Hombres");
    servicio.setDescripcion("descripcionServicio1");
    servicioService.save(servicio);

    ServicioAsociado servicioAsociado = new ServicioAsociado(organizacion, sucursal, servicio);
    servicioAsociadoService.save(servicioAsociado);

    usuario.addServicioAsociado(servicioAsociado);
    usuarioRepository.update(usuario);


    UbicacionGeografica ubicacionGeografica2 =
        new UbicacionGeografica(-34.5118758903445, -58.7776710941743);

    DatosPersonalesUsuario datosPersonalesUsuario2
        = new DatosPersonalesUsuario("Fran", "Mav", "fmaver@frba.utn.edu.ar", "1138718498");

    Usuario usuario2 = new Usuario("franmav2", "vamosRiver912!", datosPersonalesUsuario2, ubicacionGeografica2, Rol.USUARIO);
    usuario2.setFormaNotificacion(FormaNotificacion.NOW);
    usuario2.setModoNotificacion(ModoNotificacion.WHATSAPP);
    usuario2.addComunidad(comunidad);
    //TODO --> chequear en todos los casos y hacerlo bidireccional
    comunidad.addMiembro(usuario2);
    usuario2.addServicioAsociado(servicioAsociado);
    usuarioRepository.save(usuario2);


    //////////////////////////
    // CREAMOS UN INCIDENTE //
    //////////////////////////
    System.out.println("Creacion de un Incidente..");
    Incidente incidente = new Incidente(
        servicioAsociadoRepository.find(ServicioAsociado.class, 1),
        "Se corto el agua debido a un fallo en las canierias.", LocalDateTime.now());
    incidente.addComunidad(comunidad);
    incidenteRepository.save(incidente);
    ServicioAsociado servicioAsociado1 = servicioAsociadoService.buscarYValidarServicioAsociado(1);
    servicioAsociado1.setIncidentado(true);
    servicioAsociadoRepository.update(servicioAsociado1);
    incidenteService.notificarComunidades(incidente);


    //////////////////////////
    //    CREAMOS UN CRON   //
    //////////////////////////
    System.out.println("Creacion de un Cron..");
    usuarioController.addHorarioNotificacionUsuario(1, horarios.get(0));
    usuarioController.addHorarioNotificacionUsuario(1, horarios.get(1));
  }

}

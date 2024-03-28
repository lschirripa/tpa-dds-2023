package layers.controllers;

import externalservices.FusionadorDeComunidades;
import layers.controllers.AuthHandlers.AdminMiddleware;
import layers.controllers.AuthHandlers.AuthenticationHandler;
import layers.controllers.AuthHandlers.LoginHandler;
import layers.controllers.AuthHandlers.RegisterHandler;
import layers.models.repositories.*;
import layers.services.*;
import utils.SugerenciaDeRevision;

public class FactoryController {

  public static Object controller(String nombre) {
    Object controller = null;
    switch (nombre) {
      case "Init":
        controller = new InitController();
        break;
      case "Servicios":
        controller = new ServicioController(new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()));
        break;
      case "Usuario":
        controller = new UsuarioController(new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()))), new EntidadService(new EntidadRepository(), new LocalizacionRepository()), new EstablecimientoService(new EstablecimientoRepository(), new LocalizacionRepository()), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository())));
        break;
      case "Incidentes":
        controller = new IncidenteController(new IncidenteService(new IncidenteRepository(), new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository())))), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository())), new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()))));
        break;
      case "Fusionador":
        controller = new FusionadorController(new FusionadorDeComunidades(new ComunidadRepository(), new UsuarioRepository()));
        break;
      case "Recomendador":
        controller = new RecomendadorController(new FusionadorDeComunidades(new ComunidadRepository(), new UsuarioRepository()));
        break;
      case "Comunidades":
        controller = new ComunidadController(new ComunidadService(new ComunidadRepository(), new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository())))), new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()))), new IncidenteService(new IncidenteRepository(), new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository())))));
        break;
      case "ServiciosAsociados":
        controller = new ServicioAsociadoController(new EstablecimientoService(new EstablecimientoRepository(), new LocalizacionRepository()), new EntidadService(new EntidadRepository(), new LocalizacionRepository()), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository())));
        break;
      case "Organismo":
        controller = new OrganismoController(new OrganismoDeControlRepository());
        break;
      case "Prestadora":
        controller = new PrestadoraController(new PrestadoraDeServicioRepository());
        break;
      case "Reguladora":
        controller = new ReguladorController();
        break;
      case "Sugerencia":
        controller = new SugerenciaDeRevisionController(new SugerenciaDeRevision(new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()))), new IncidenteService(new IncidenteRepository(), new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()))))));
        break;
      case "Establecimientos":
        controller = new EstablecimientoController(new UbicacionGeograficaRepository(), new EstablecimientoRepository(), new LocalizacionRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()));
        break;
      case "Linea":
        controller = new LineaController(new EstablecimientoService(new EstablecimientoRepository(), new LocalizacionRepository()), new UbicacionGeograficaRepository(), new LocalizacionRepository(), new EntidadRepository());
        break;
      case "Organizacion":
        controller = new OrganizacionController(new EstablecimientoService(new EstablecimientoRepository(), new LocalizacionRepository()), new UbicacionGeograficaRepository(), new LocalizacionRepository(), new EntidadRepository());
        break;
      case "Login":
        controller = new LoginHandler(new UsuarioRepository());
        break;
      case "Authentication":
        controller = new AuthenticationHandler();
        break;
      case "Register":
        controller = new RegisterHandler(new UsuarioRepository(), new RoleRepository());
        break;
      case "Perfil":
        controller = new PerfilController(new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository()))), new ComunidadService(new ComunidadRepository(), new UsuarioService(new UsuarioRepository(), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository())))), new ServicioAsociadoService(new ServicioAsociadoRepository(), new ServicioService(new ServicioRepository(), new ServicioUsuarioRepository())), new ServicioUsuarioRepository(), new EntidadRepository(), new EstablecimientoRepository());
        break;
      case "AdminMiddleware":
        controller = new AdminMiddleware();
        break;
      default:
        break;
    }
    return controller;
  }
}

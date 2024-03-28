package server;

import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.Map;
import layers.controllers.*;
import layers.controllers.AuthHandlers.AdminMiddleware;
import layers.controllers.AuthHandlers.AuthenticationHandler;
import layers.controllers.AuthHandlers.LoginHandler;
import layers.controllers.AuthHandlers.RegisterHandler;
import server.Auth.AuthorizationHandler;

public class Router {

  public static void init() {
    Server.app().get("/", ctx -> {
      ctx.render("homepage.hbs");
    });

    /*
    Server.app().before(ctx -> {
      new AuthenticationHandler().handle(ctx);
    });

    Server.app().before(ctx -> {
      new AuthorizationHandler().handle(ctx);
    });
    */
    Server.app().routes(() -> {

      get("init", ((InitController) FactoryController.controller("Init"))::init);

      get("incidentes", ((IncidenteController) FactoryController.controller("Incidentes"))::index);
      get("incidentes/crear", ((IncidenteController) FactoryController.controller("Incidentes"))::create);
      get("incidentes/{id}", ((IncidenteController) FactoryController.controller("Incidentes"))::show);
      get("incidentes/{id}/editar", ((IncidenteController) FactoryController.controller("Incidentes"))::edit);
      post("incidentes/nuevo", ((IncidenteController) FactoryController.controller("Incidentes"))::save);
      post("incidentes/{id}/marcar-resuelto", ((IncidenteController) FactoryController.controller("Incidentes"))::update);
      post("incidentes/{id}/eliminar", ((IncidenteController) FactoryController.controller("Incidentes"))::delete);
      delete("incidentes/{id}", ((IncidenteController) FactoryController.controller("Incidentes"))::delete);

      get("servicios", ((ServicioController) FactoryController.controller("Servicios"))::index);
      get("servicios/crear", ((ServicioController) FactoryController.controller("Servicios"))::create);
      get("servicios/{id}", ((ServicioController) FactoryController.controller("Servicios"))::show);
      get("servicios/{id}/editar", ((ServicioController) FactoryController.controller("Servicios"))::edit);
      post("servicios/nuevo", ((ServicioController) FactoryController.controller("Servicios"))::save);
      post("servicios/{id}/", ((ServicioController) FactoryController.controller("Servicios"))::update);
      post("servicios/{id}/eliminar", ((ServicioController) FactoryController.controller("Servicios"))::delete);
      delete("servicios/{id}/eliminar", ((ServicioController) FactoryController.controller("Servicios"))::delete);

      get("fusionador", ((FusionadorController) FactoryController.controller("Fusionador"))::index);
      post("fusionador", ((FusionadorController) FactoryController.controller("Fusionador"))::create);
      get("fusionador/recomendar", ((FusionadorController) FactoryController.controller("Fusionador"))::create);
      get("recomendador", ((RecomendadorController) FactoryController.controller("Recomendador"))::index);
      get("recomendador/recomendar", ((RecomendadorController) FactoryController.controller("Recomendador"))::create);

      get("comunidades", ((ComunidadController) FactoryController.controller("Comunidades"))::index);
      get("comunidades/crear", ((ComunidadController) FactoryController.controller("Comunidades"))::create);
      get("comunidades/{id}", ((ComunidadController) FactoryController.controller("Comunidades"))::show);
      get("comunidades/{id}/editar", ((ComunidadController) FactoryController.controller("Comunidades"))::edit);
      post("comunidades/nuevo", ((ComunidadController) FactoryController.controller("Comunidades"))::save);
      post("comunidades/{id}/", ((ComunidadController) FactoryController.controller("Comunidades"))::update);
      post("comunidades/{id}/eliminar", ((ComunidadController) FactoryController.controller("Comunidades"))::delete);
      delete("comunidades/{id}", ((ComunidadController) FactoryController.controller("Comunidades"))::delete);

      get("serviciosasociados", ((ServicioAsociadoController) FactoryController.controller("ServiciosAsociados"))::index);
      get("serviciosasociados/crear", ((ServicioAsociadoController) FactoryController.controller("ServiciosAsociados"))::create);
      get("serviciosasociados/{id}", ((ServicioAsociadoController) FactoryController.controller("ServiciosAsociados"))::show);
      get("serviciosasociados/{id}/editar", ((ServicioAsociadoController) FactoryController.controller("ServiciosAsociados"))::edit);
      post("serviciosasociados/nuevo", ((ServicioAsociadoController) FactoryController.controller("ServiciosAsociados"))::save);
      post("serviciosasociados/{id}/", ((ServicioAsociadoController) FactoryController.controller("ServiciosAsociados"))::update);
      post("serviciosasociados/{id}/eliminar", ((ServicioAsociadoController) FactoryController.controller("ServiciosAsociados"))::delete);
      delete("serviciosasociados/{id}", ((ServicioAsociadoController) FactoryController.controller("ServiciosAsociados"))::delete);

      get("reguladoras/carga-masiva", ((ReguladorController) FactoryController.controller("Reguladora"))::create);
      post("reguladoras/carga-masiva", ((ReguladorController) FactoryController.controller("Reguladora"))::save);


      get("rankings", ctx -> {
        String[] pdfs = {"pdf1.pdf", "pdf2.pdf", "pdf3.pdf"};
        ctx.render("rankings/rankings.hbs", Map.of("pdfs", pdfs));
      });

      get("establecimientos", ((EstablecimientoController) FactoryController.controller("Establecimientos"))::index);
      get("establecimientos/crear", ((EstablecimientoController) FactoryController.controller("Establecimientos"))::create);
      post("establecimientos/establecimientos/nuevo", ((EstablecimientoController) FactoryController.controller("Establecimientos"))::save);

      get("lineas", ((LineaController) FactoryController.controller("Linea"))::index);
      get("linea/crear", ((LineaController) FactoryController.controller("Linea"))::create);
      post("linea/linea/nueva", ((LineaController) FactoryController.controller("Linea"))::save);

      get("organizaciones", ((OrganizacionController) FactoryController.controller("Organizacion"))::index);
      get("organizacion/crear", ((OrganizacionController) FactoryController.controller("Organizacion"))::create);
      post("organizacion/organizacion/nueva", ((OrganizacionController) FactoryController.controller("Organizacion"))::save);
      get("sugerenciarevision", ((SugerenciaDeRevisionController) FactoryController.controller("Sugerencia"))::index);
      get("sugerenciarevision/sugerir", ((SugerenciaDeRevisionController) FactoryController.controller("Sugerencia"))::create);

      get("perfil", ((PerfilController) FactoryController.controller("Perfil"))::index);
      get("perfil/editar", ((PerfilController) FactoryController.controller("Perfil"))::edit);
      get("perfil/agregar-comunidades", ((PerfilController) FactoryController.controller("Perfil"))::addComunidad);
      get("perfil/agregar-servicios", ((PerfilController) FactoryController.controller("Perfil"))::addServicio);
      get("perfil/agregar-horario", ((PerfilController) FactoryController.controller("Perfil"))::addHorarioNotificacion);
      get("perfil/agregar-servicios-multiples", ((PerfilController) FactoryController.controller("Perfil"))::addServicioMultiple);
      post("perfil/unirse-comunidades", ((PerfilController) FactoryController.controller("Perfil"))::saveComunidad);
      post("perfil/agregar-servicios", ((PerfilController) FactoryController.controller("Perfil"))::saveServicio);
      post("perfil/agregar-horario", ((PerfilController) FactoryController.controller("Perfil"))::saveHorarioNotificacion);
      post("perfil/editar", ((PerfilController) FactoryController.controller("Perfil"))::update);
      post("perfil/{id}/eliminar-comunidad", ((PerfilController) FactoryController.controller("Perfil"))::deleteComunidad);
      post("perfil/{id}/eliminar-servicio", ((PerfilController) FactoryController.controller("Perfil"))::deleteServicio);
      post("perfil/eliminar-horario", ((PerfilController) FactoryController.controller("Perfil"))::deleteHorario);
      post("perfil/agregar-servicios-multiples", ((PerfilController) FactoryController.controller("Perfil"))::saveServicioMultiple);
      delete("perfil/{id}/eliminar-comunidad", ((PerfilController) FactoryController.controller("Perfil"))::deleteComunidad);
      delete("perfil/{id}/eliminar-servicio", ((PerfilController) FactoryController.controller("Perfil"))::deleteServicio);
      delete("perfil/eliminar-horario", ((PerfilController) FactoryController.controller("Perfil"))::deleteHorario);


      get("login", ((LoginHandler) FactoryController.controller("Login"))::handleView);

      post("login", ((LoginHandler) FactoryController.controller("Login"))::handle);

      get("logout", ctx -> {
        ctx.req().getSession().invalidate();
        System.out.println("Sesión cerrada");
        ctx.redirect("/login");
      });

      get("register", ((RegisterHandler) FactoryController.controller("Register"))::handleView);

      post("register", ((RegisterHandler) FactoryController.controller("Register"))::handle);

      get("admin/config", ((AdminMiddleware) FactoryController.controller("AdminMiddleware"))::handle);

      Server.app().before(ctx -> {
        // Verificar si la ruta actual es la ruta raíz ("/")
        new AuthenticationHandler().handle(ctx);
      });


    });

  }

}
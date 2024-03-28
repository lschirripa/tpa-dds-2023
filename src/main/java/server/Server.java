package server;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.JavalinRenderer;
import java.io.IOException;
import java.util.function.Consumer;
import server.Auth.Auth;

public class Server {
  private static Javalin app = null;

  public static Javalin app() {
    if (app == null) {
      throw new RuntimeException("Aplicación no inicializada");
    }
    return app;
  }

  public static void init() {
    if (app == null) {
      Integer port = Integer.parseInt(System.getProperty("port", "8080"));
      app = Javalin.create(config()).start(port);
      initTemplateEngine();
      Router.init();
    }
  }


  public static Consumer<JavalinConfig> config() {
    return config -> {
      config.staticFiles.add(staticFiles -> {
        staticFiles.hostedPath = "/";
        staticFiles.directory = "/public";
      });
//      config.accessManager(new Auth()::manage);
    };
  }

  private static void initTemplateEngine() {
    JavalinRenderer.register(
            (path, model, context) -> { // Función que renderiza el template
              Handlebars handlebars = new Handlebars();
              Template template = null;
              try {
                template = handlebars.compile(
                        "templates/" + path.replace(".hbs", ""));
                return template.apply(model);
              } catch (IOException e) {
                e.printStackTrace();
                context.status(HttpStatus.NOT_FOUND);
                return "No se encuentra la página indicada...";
              }
            }, ".hbs" // Extensión del archivo de template
    );
  }
}
package layers.controllers;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.UploadedFile;
import java.nio.file.NoSuchFileException;
import utils.ICrudViewsHandler;
import utils.ReadCsv;

public class ReguladorController implements ICrudViewsHandler {

  @Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
    context.render("reguladoras/bulk-reguladoras.hbs");
  }

  @Override
  public void save(Context context) {
    String result = "";
    try {
      UploadedFile file = context.uploadedFile("file");
      ReadCsv readCsv = new ReadCsv(file);
      result = readCsv.leerCsv();
      context.result(result);
      context.status(HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      result = "el TIPO ingresado no es valido. Debe ser 'prestadora' u 'organismo'";
      context.status(HttpStatus.UNPROCESSABLE_CONTENT);
    } catch (NoSuchFileException e) {
      result = e.getMessage();
      context.status(HttpStatus.UNPROCESSABLE_CONTENT);
    } catch (Exception e) {
      result = e.toString();
      context.status(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    context.result(result);
//      ctx.redirect("/servicios");
  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }
}

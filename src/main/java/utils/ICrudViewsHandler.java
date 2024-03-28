package utils;

import io.javalin.http.Context;
import java.io.IOException;

public interface ICrudViewsHandler {
  void index(Context context);

  void show(Context context);

  void create(Context context);

  void save(Context context) throws IOException;

  void edit(Context context);

  void update(Context context);

  void delete(Context context);
}

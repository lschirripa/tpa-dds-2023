package server;

import server.config.Setup;

public class  WebApp {

  public static void main(String[] args) {
    Server.init();

    Setup setup = new Setup();
    setup.init_setup();

  }
}

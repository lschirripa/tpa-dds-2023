package server.config;

import db.DatabaseSeeder;
import externalservices.RankingScheduler;
import externalservices.ServicioConfianzaScheduler;
import layers.models.domain.OrganismoDeControl;
import layers.models.exportadapter.PDFAdapter.AdapterApachePDFBox;
import layers.models.repositories.OrganismoDeControlRepository;
import org.quartz.SchedulerException;
import utils.rankingincidente.MayorCantidadDeIncidentes;
import utils.rankingincidente.MayorPromedioDeCierre;

public class Setup {
  OrganismoDeControlRepository organismoDeControlRepository = new OrganismoDeControlRepository();

  public void init_setup() {
    init_db();
    //TODO -> ver si qeuda comentado o no
    create_pdfs();
    init_schedulers();
  }

  public void init_db() {
    System.out.println("inicializando base de datos...");
    try {
      DatabaseSeeder databaseSeeder = new DatabaseSeeder();
      databaseSeeder.seed();
      System.out.println("base de datos inicializada");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error al cargar datos de prueba en la db. Verifique la conexion");
    }
  }

  public void init_schedulers() {
    System.out.println("inicializando schedulers...");
    try {
      RankingScheduler rankingScheduler = new RankingScheduler();
      rankingScheduler.scheduleNotificacionSemanal();

      ServicioConfianzaScheduler servicioConfianzaScheduler = new ServicioConfianzaScheduler();
      servicioConfianzaScheduler.scheduleConfianzaSemanal();

    } catch (SchedulerException e) {
      e.printStackTrace();
      System.out.println("Error al inicializar schedulers");
    }
    System.out.println("schedulers inicializados");
  }

  public void create_pdfs() {
    System.out.println("generando pdf 1...");
    MayorCantidadDeIncidentes mayorCantidadDeIncidentes = new MayorCantidadDeIncidentes("mayorCantidadDeIncidentes");

    mayorCantidadDeIncidentes.generarRankingDe(organismoDeControlRepository.getById(1, OrganismoDeControl.class));
    AdapterApachePDFBox adapterApachePDFBox = new AdapterApachePDFBox();
    adapterApachePDFBox.exportar(mayorCantidadDeIncidentes, "pdf1");
    System.out.println("pdf 1 generado");

    System.out.println("generando pdf 2...");
    MayorPromedioDeCierre mayorPromedioDeCierre = new MayorPromedioDeCierre("mayorPromedioDeCierre");
    mayorPromedioDeCierre.generarRankingDe(organismoDeControlRepository.getById(1, OrganismoDeControl.class));
    adapterApachePDFBox.exportar(mayorPromedioDeCierre, "pdf2");
    System.out.println("pdf 2 generado");
  }

}

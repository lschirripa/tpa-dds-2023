package externalservices.apiservicio1.servicio1.entities;

import externalservices.FusionadorDeComunidades;
import externalservices.apiservicio1.servicio1.FusionApiService;
import externalservices.apiservicio1.servicio1.entities.entidadesadaptadas.ComunidadAdaptada;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import layers.models.domain.*;
import layers.models.repositories.*;
import utils.enums.Rol;

public class MainMerge {

  private static ComunidadRepository comunidadRepository = new ComunidadRepository();
  private static UsuarioRepository usuarioRepository = new UsuarioRepository();
  private static EstablecimientoRepository establecimientoRepository = new EstablecimientoRepository();
  private static ServicioAsociadoRepository servicioAsociadoRepository = new ServicioAsociadoRepository();
  private static LocalizacionRepository localizacionRepository = new LocalizacionRepository();

  private static RoleRepository roleRepository = new RoleRepository();

  public static void main(String[] args) throws Exception {
    generarServiciosAsociados();
    generarUsuarios();
    generarComunidades();
    FusionadorDeComunidades fusionDeComunidades = new FusionadorDeComunidades(comunidadRepository, usuarioRepository);

    Comunidad comunidad1 = comunidadRepository.getById(1, Comunidad.class);
    Comunidad comunidad2 = comunidadRepository.getById(2, Comunidad.class);

    Comunidad comunidadFusionada = fusionDeComunidades.fusionDeComunidades(comunidad1, comunidad2);

    System.out.println("##################");
    System.out.println("Comunidad fusionada");
    System.out.println("##################");

    System.out.println("Nombre: " + comunidadFusionada.getNombre());
    System.out.println("Descripcion: " + comunidadFusionada.getDescripcion());
    System.out.println("Miembros: " + comunidadFusionada.getMiembros().size());


  }

  private static void generarServiciosAsociados() throws IOException {

    System.out.println("#######################");
    System.out.println("Generando servAsociados");
    System.out.println("#######################");

    List<Servicio> servicios = new ArrayList<>();
    Servicio servicio1 = new Servicio("Banio", "de hombres");
    Servicio servicio2 = new Servicio("Banio", "de mujeres");
    servicios.add(servicio1);
    servicios.add(servicio2);

    Sucursal sucursal1 = new Sucursal("Santander", servicios, new UbicacionGeografica(-26.8753965086829, -54.6516966230371));

    localizacionRepository.getOrCreate(sucursal1.getLocalizacion());
    establecimientoRepository.save(sucursal1);

    ServicioAsociado servicioAsociado1 = new ServicioAsociado(null, sucursal1, servicio1);
    ServicioAsociado servicioAsociado2 = new ServicioAsociado(null, sucursal1, servicio2);

    servicioAsociadoRepository.save(servicioAsociado1);
    servicioAsociadoRepository.save(servicioAsociado2);
  }

  private static void generarUsuarios() {
    System.out.println("##################");
    System.out.println("Generando usuarios");
    System.out.println("##################");

    Usuario usuario1 = new Usuario("Juan", "contraDif111", null, null, Rol.ADMINISTRADOR);
    Usuario usuario2 = new Usuario("Pedro", "contraDif222", null, null, Rol.ADMINISTRADOR);
    Usuario usuario3 = new Usuario("Carlos", "contraDif111", null, null, Rol.USUARIO);
    Usuario usuario4 = new Usuario("Luciano", "contraDif222", null, null, Rol.USUARIO);
    Usuario usuario5 = new Usuario("Juan", "contraDif222", null, null, Rol.USUARIO);

    usuario1.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario1.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuario2.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario2.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuario3.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario3.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuario4.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario4.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));

    usuario5.addServicioAsociado(servicioAsociadoRepository.getById(1, ServicioAsociado.class));
    usuario5.addServicioAsociado(servicioAsociadoRepository.getById(2, ServicioAsociado.class));


    usuarioRepository.save(usuario1);
    usuarioRepository.save(usuario2);
    usuarioRepository.save(usuario3);
    usuarioRepository.save(usuario4);
    usuarioRepository.save(usuario5);
  }


  private static void generarComunidades() {

    System.out.println("#####################");
    System.out.println("Generando comunidades");
    System.out.println("#####################");

    List<Usuario> miembrosComu1 = new ArrayList<>();
    miembrosComu1.add(usuarioRepository.getById(1, Usuario.class));
    miembrosComu1.add(usuarioRepository.getById(2, Usuario.class));
    miembrosComu1.add(usuarioRepository.getById(3, Usuario.class));
    miembrosComu1.add(usuarioRepository.getById(4, Usuario.class));
    miembrosComu1.add(usuarioRepository.getById(5, Usuario.class));

    List<Usuario> miembrosComu2 = new ArrayList<>();
    miembrosComu2.add(usuarioRepository.getById(1, Usuario.class));
    miembrosComu2.add(usuarioRepository.getById(2, Usuario.class));
    miembrosComu2.add(usuarioRepository.getById(3, Usuario.class));
    miembrosComu2.add(usuarioRepository.getById(4, Usuario.class));


    Comunidad comunidad1 = new Comunidad("Discapacidad Auditiva");
    comunidad1.setPuntosDeConfianza(5.0);
    comunidad1.setMiembros(miembrosComu1);
    comunidad1.setUltimaFechaDeFusion(LocalDateTime.now().minusYears(1));
    comunidadRepository.save(comunidad1);

    Comunidad comunidad2 = new Comunidad("Discapacidad Visual");
    comunidad2.setPuntosDeConfianza(5.0);
    comunidad2.setMiembros(miembrosComu2);
    comunidad2.setUltimaFechaDeFusion(LocalDateTime.now().minusYears(1));
    comunidadRepository.save(comunidad2);
  }
}

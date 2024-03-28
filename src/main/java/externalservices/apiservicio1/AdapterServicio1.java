package externalservices.apiservicio1;

import externalservices.apiservicio1.servicio1.FusionApiService;
import externalservices.apiservicio1.servicio1.entities.MergedCommunitySchema;
import externalservices.apiservicio1.servicio1.entities.PosibleCombinacion;
import externalservices.apiservicio1.servicio1.entities.PossibleMergesSchema;
import externalservices.apiservicio1.servicio1.entities.entidadesadaptadas.*;
import java.util.*;
import layers.models.domain.Comunidad;
import layers.models.domain.ServicioAsociado;
import layers.models.domain.Usuario;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdapterServicio1 {
  private static AdapterServicio1 instance = null;
  private FusionApiService fusionApiService;

  public AdapterServicio1() {
    this.fusionApiService = FusionApiService.getInstance();
  }

  public static AdapterServicio1 getInstance() {
    if (instance == null) {
      instance = new AdapterServicio1();
    }
    return instance;
  }


  public PossibleMergesSchema postRecomendacionDeFusion(List<Comunidad> comunidades)
      throws Exception {
    List<ComunidadAdaptada> comunidadesAdaptadas = adaptarComunidades(comunidades);

    return this.fusionApiService.postRecomendacionDeFusion(comunidadesAdaptadas);
  }

  public MergedCommunitySchema postComunidadesFusionadas(Comunidad comunidad1,
                                                         Comunidad comunidad2) throws Exception {
    PosibleCombinacion comunidades = new PosibleCombinacion();
    comunidades.setCommunity1(adaptarComunidad(comunidad1));
    comunidades.setCommunity2(adaptarComunidad(comunidad2));

    return this.fusionApiService.postComunidadesFusionadas(comunidades);
  }

  private List<ComunidadAdaptada> adaptarComunidades(List<Comunidad> comunidades) {
    List<ComunidadAdaptada> comunidadesAdaptadas = new ArrayList<>();

    for (Comunidad comunidad : comunidades) {
      comunidadesAdaptadas.add(adaptarComunidad(comunidad));
    }

    return comunidadesAdaptadas;
  }

  private ComunidadAdaptada adaptarComunidad(Comunidad comunidad) {

    ComunidadAdaptada comunidadAdaptada = new ComunidadAdaptada();

    comunidadAdaptada.setId(Integer.toString(comunidad.getId()));
    comunidadAdaptada.setName(comunidad.getNombre());
    comunidadAdaptada.setLastTimeMerged(comunidad.getUltimaFechaDeFusion().toString());
    comunidadAdaptada.setDegreeOfConfidence(comunidad.getPuntosDeConfianza().intValue());

    comunidadAdaptada.setMembers(adaptarMiembros(comunidad.getMiembros()));
    comunidadAdaptada.setInterestingServices(adaptarServicios(comunidad.getMiembros()));
    comunidadAdaptada
        .setInterestingEstablishments(adaptarEstablecimientos(comunidad.getMiembros()));

    return comunidadAdaptada;
  }

  private List<UsuarioAdaptado> adaptarMiembros(List<Usuario> miembros) {
    List<UsuarioAdaptado> miembrosAdaptados = new ArrayList<>();

    for (Usuario miembro : miembros) {
      miembrosAdaptados.add(adaptarMiembro(miembro));
    }
    return miembrosAdaptados;
  }

  private UsuarioAdaptado adaptarMiembro(Usuario miembro) {
    return new UsuarioAdaptado(Integer.toString(miembro.getId()), miembro.getUserName());
  }

  private List<ServicioAdaptado> adaptarServicios(List<Usuario> miembros) {
    List<ServicioAdaptado> serviciosAdaptados = new ArrayList<>();

    for (Usuario miembro : miembros) {
      for (ServicioAsociado servicioAsociado : miembro.getServiciosAsociados()) {
        if (!noEstaEnLaListaServicio(servicioAsociado, serviciosAdaptados)) {
          serviciosAdaptados.add(adaptarServicio(servicioAsociado));
        }
      }
    }
    return serviciosAdaptados;
  }


  private ServicioAdaptado adaptarServicio(ServicioAsociado servicioAsociado) {
    return new
        ServicioAdaptado(Integer.toString(servicioAsociado.getServicio().getId()),
        servicioAsociado.getServicio().getNombre());
  }

  private List<EstablecimientoAdaptado> adaptarEstablecimientos(List<Usuario> miembros) {
    List<EstablecimientoAdaptado> establecimientosAdaptadosSet = new ArrayList<>();

    for (Usuario miembro : miembros) {
      for (ServicioAsociado servicioAsociado : miembro.getServiciosAsociados()) {
        if (!noEstaEnLaListaEstablecimiento(servicioAsociado, establecimientosAdaptadosSet)) {
          establecimientosAdaptadosSet.add(adaptarEstablecimiento(servicioAsociado));
        }
      }
    }
    return establecimientosAdaptadosSet;
  }

  private EstablecimientoAdaptado adaptarEstablecimiento(ServicioAsociado servicioAsociado) {
    return new EstablecimientoAdaptado(
        Integer.toString(servicioAsociado.getEstablecimiento().getId()),
        servicioAsociado.getEstablecimiento().getNombre());
  }

  private boolean noEstaEnLaListaEstablecimiento(ServicioAsociado servicioAsociado,
                                                 List<EstablecimientoAdaptado> establAdaptados) {
    int idEstablecimiento = servicioAsociado.getEstablecimiento().getId();

    return establAdaptados.stream()
        .anyMatch(adaptado -> adaptado.getId().equals(Integer.toString(idEstablecimiento)));
  }

  private boolean noEstaEnLaListaServicio(ServicioAsociado servicioAsociado,
                                          List<ServicioAdaptado> serviciosAdaptados) {
    int idServicio = servicioAsociado.getServicio().getId();

    return serviciosAdaptados.stream()
        .anyMatch(adaptado -> adaptado.getId().equals(Integer.toString(idServicio)));
  }

}

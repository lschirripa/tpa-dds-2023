package externalservices;

import externalservices.apiservicio1.AdapterServicio1;
import externalservices.apiservicio1.servicio1.entities.MergedCommunitySchema;
import externalservices.apiservicio1.servicio1.entities.PosibleCombinacion;
import externalservices.apiservicio1.servicio1.entities.PossibleMergesSchema;
import externalservices.apiservicio1.servicio1.entities.entidadesadaptadas.ComunidadAdaptada;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import layers.models.domain.Comunidad;
import layers.models.domain.Usuario;
import layers.models.repositories.ComunidadRepository;
import layers.models.repositories.UsuarioRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FusionadorDeComunidades {
  protected AdapterServicio1 fusionador = AdapterServicio1.getInstance();

  //todo -> chequear despues si debe conocer el repositorio, o el servicio o el controlador
  private ComunidadRepository comunidadRepository;
  private UsuarioRepository usuarioRepository;

  public FusionadorDeComunidades(ComunidadRepository comunidadRepository,
                                 UsuarioRepository usuarioRepository) {
    this.comunidadRepository = comunidadRepository;
    this.usuarioRepository = usuarioRepository;
  }

  public List<Comunidad> recomendacionDeFusion() throws Exception {
    List<Comunidad> comunidadesRecomendadas = new ArrayList<>();

    PossibleMergesSchema recomendacionDeFusion = this.fusionador
        .postRecomendacionDeFusion(this.comunidadRepository.getAll(Comunidad.class));

    List<PosibleCombinacion> posibleCombinaciones = recomendacionDeFusion.getPossibleMerges();

    if (posibleCombinaciones.size() == 0) {
      System.out.println("No hay recomendaciones de fusion");
      return comunidadesRecomendadas;
    }

    posibleCombinaciones.forEach(posibleCombinacion -> {
      comunidadesRecomendadas.add(getNuestraComunidad(posibleCombinacion.getCommunity1()));
      comunidadesRecomendadas.add(getNuestraComunidad(posibleCombinacion.getCommunity2()));
    });

    return comunidadesRecomendadas;
  }

  public Comunidad fusionDeComunidades(Comunidad comunidad1, Comunidad comunidad2)
      throws Exception {

    MergedCommunitySchema mergedCommunitySchema = this.fusionador
        .postComunidadesFusionadas(comunidad1, comunidad2);

    if (Objects.isNull(mergedCommunitySchema)) {
      throw new Exception("No se pudo fusionar las comunidades");
    }

    Comunidad comunidadFusionada = crearNuestraComunidad(
        mergedCommunitySchema.getMergedCommunity().get(2));

    List<Usuario> miembrosComu1 = new ArrayList<>(comunidad1.getMiembros());
    for (Usuario usuario : miembrosComu1) {
      usuario.removeComunidad(comunidad1);
      comunidad1.removeMiembro(usuario);
      usuarioRepository.update(usuario);

    }

    List<Usuario> miembrosComu2 = new ArrayList<>(comunidad2.getMiembros());
    for (Usuario usuario : miembrosComu2) {
      usuario.removeComunidad(comunidad2);
      comunidad2.removeMiembro(usuario);
      usuarioRepository.update(usuario);
    }

    // Desactiva las comunidades originales
    comunidad1.setActiva(false);
    comunidad2.setActiva(false);
    comunidadRepository.update(comunidad1);
    comunidadRepository.update(comunidad2);

    return comunidadFusionada;
  }

  private Comunidad crearNuestraComunidad(ComunidadAdaptada comunidadFusionada) {
    List<Usuario> usuarios = new ArrayList<>();

    comunidadFusionada.getMembers().forEach(user -> {
      usuarios.add(this.usuarioRepository.getById(Integer.parseInt(user.getId()), Usuario.class));
    });

    Comunidad comunidad = new Comunidad();
    comunidad.setNombre(comunidadFusionada.getName());
    comunidad.setMiembros(usuarios);
    comunidad.setPuntosDeConfianza((double) comunidadFusionada.getDegreeOfConfidence());

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    try {
      LocalDateTime localDateTime = LocalDateTime
          .parse(comunidadFusionada.getLastTimeMerged(), formatter);

      comunidad.setUltimaFechaDeFusion(localDateTime);

    } catch (DateTimeParseException e) {
      System.out.println("Error parsing the input string: " + e.getMessage());
    }

    if (Objects.equals(comunidadFusionada.getId(), "merged")) {
      comunidad.setDescripcion("Comunidad fusionada");
    }

    comunidadRepository.save(comunidad);
    return comunidad;
  }

  private Comunidad getNuestraComunidad(ComunidadAdaptada comunidad) {
    return this.comunidadRepository.getById(Integer.parseInt(comunidad.getId()), Comunidad.class);
  }
}

package externalservices.apiservicio2.entities;

import com.google.gson.annotations.SerializedName;

public class IncidenteAdaptado {
  @SerializedName("id")
  private int id;

  @SerializedName("id_establecimiento")
  private String idEstablecimiento;

  @SerializedName("id_servicio_afectado")
  private String idServicioAfectado;

  @SerializedName("fecha_de_apertura")
  private String fechaDeApertura;

  @SerializedName("fecha_de_cierre")
  private String fechaDeCierre;

  @SerializedName("id_usuario_de_apertura")
  private String idUsuarioDeApertura;

  @SerializedName("id_usuario_de_cierre")
  private String idUsuarioDeCierre;

  public IncidenteAdaptado (int id, int idEstablecimiento, int idServicioAfectado, String fechaDeApertura, String fechaDeCierre, int idUsuarioDeApertura, int idUsuarioDeCierre) {
    this.id = id;
    this.idEstablecimiento = String.valueOf(idEstablecimiento);
    this.idServicioAfectado = String.valueOf(idServicioAfectado);
    this.fechaDeApertura = fechaDeApertura;
    this.fechaDeCierre = fechaDeCierre;
    this.idUsuarioDeApertura = String.valueOf(idUsuarioDeApertura);
    this.idUsuarioDeCierre = String.valueOf(idUsuarioDeCierre);
  }

}

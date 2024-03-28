package utils.enums;

public enum Rol {
  ADMINISTRADOR("Administrador"),
  OPERADOR("Operador"),
  USUARIO("Usuario");

  private final String nombre;

  Rol(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }
}

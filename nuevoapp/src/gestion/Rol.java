package gestion;

import java.io.Serializable;

/**
 * ENUM: Rol
 * <p>
 * Define los distintos roles de usuario dentro del sistema de gestión deportiva.
 * Al implementar {@link Serializable}, los roles se pueden persistir correctamente
 * al guardar los datos de la federación en archivos o bases de datos.
 * </p>
 * <p>
 * Cada rol tiene un nombre legible para mostrar en la interfaz de usuario.
 * </p>
 */
public enum Rol implements Serializable {

    /** Solo puede ver equipos y jugadores. No ve el botón "Gestionar". */
    INVITADO("Invitado"),

    /** Puede modificar jornadas y partidos únicamente. */
    ARBITRO("Árbitro"),

    /** Puede modificar equipos, jugadores, escudos y logos. */
    MANAGER("Manager"),

    /** Tiene control total: gestión de usuarios, temporadas y todo lo anterior. */
    ADMINISTRADOR("Administrador");

    /** Nombre legible del rol para mostrar en la interfaz */
    private final String nombreLegible;

    /**
     * Constructor del enum Rol.
     *
     * @param nombreLegible nombre del rol para mostrar al usuario
     */
    Rol(String nombreLegible) {
        this.nombreLegible = nombreLegible;
    }

    /**
     * Obtiene el nombre legible del rol.
     *
     * @return nombre del rol como cadena
     */
    public String getNombreLegible() {
        return nombreLegible;
    }
}

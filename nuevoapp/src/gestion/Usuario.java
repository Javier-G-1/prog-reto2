package gestion;

import java.io.Serializable;

/**
 * CLASE: Usuario
 * <p>
 * Representa un usuario del sistema de gestión deportiva.
 * Contiene información personal, credenciales y el rol asignado.
 * </p>
 * <p>
 * Implementa {@link Serializable} para permitir la persistencia de datos.
 * </p>
 */
public class Usuario implements Serializable {

    /** Identificador de versión para la serialización */
    private static final long serialVersionUID = 1L;

    /** Nombre completo del usuario */
    private String nombreReal;

    /** Nombre de usuario para login */
    private String nombreUsuario;

    /** Contraseña del usuario */
    private String contrasena;

    /** Rol asignado al usuario */
    private Rol rol;

    /**
     * Constructor principal de la clase Usuario.
     *
     * @param nombreReal nombre completo del usuario
     * @param nombreUsuario nombre de usuario para login
     * @param contrasena contraseña del usuario
     * @param rol rol asignado
     * @throws IllegalArgumentException si alguno de los parámetros es nulo
     */
    public Usuario(String nombreReal, String nombreUsuario, String contrasena, Rol rol) {
        if (nombreReal == null || nombreUsuario == null || contrasena == null || rol == null)
            throw new IllegalArgumentException("Ningún dato de usuario puede ser nulo.");
        this.nombreReal = nombreReal;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // --- GETTERS Y SETTERS ---

    /**
     * Obtiene el nombre completo del usuario.
     *
     * @return nombre real del usuario
     */
    public String getNombreReal() { return nombreReal; }

    /**
     * Establece el nombre completo del usuario.
     *
     * @param nombreReal nombre real a asignar
     */
    public void setNombreReal(String nombreReal) { this.nombreReal = nombreReal; }

    /**
     * Obtiene el nombre de usuario para login.
     *
     * @return nombre de usuario
     */
    public String getNombreUsuario() { return nombreUsuario; }

    /**
     * Establece el nombre de usuario para login.
     *
     * @param nombreUsuario nombre de usuario a asignar
     */
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return contraseña
     */
    public String getContrasena() { return contrasena; }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contrasena contraseña a asignar
     */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    /**
     * Obtiene el rol asignado al usuario.
     *
     * @return rol del usuario
     */
    public Rol getRol() { return rol; }

    /**
     * Establece el rol del usuario.
     *
     * @param rol rol a asignar
     */
    public void setRol(Rol rol) { this.rol = rol; }
}

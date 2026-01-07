package gestion;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombreReal;
    private String nombreUsuario;
    private String contrasena;
    private Rol rol;

    public Usuario(String nombreReal, String nombreUsuario, String contrasena, Rol rol) {
        if (nombreReal == null || nombreUsuario == null || contrasena == null || rol == null)
            throw new IllegalArgumentException("Ningún dato de usuario puede ser nulo.");
        this.nombreReal = nombreReal;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters y setters
    public String getNombreReal() { return nombreReal; }
    public void setNombreReal(String nombreReal) { this.nombreReal = nombreReal; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}

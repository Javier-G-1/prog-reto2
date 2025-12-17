package modelo;

import java.io.Serializable;

/**
  Control de Permisos: Gracias al atributo 'rol', la VentanaPrincipal sabrá si 
  ocultar el botón "Gestionar" o permitir crear nuevos usuarios.
  Al implementar 'Serializable', permites que el usuario se guarde 
  en un archivo cuando cierres la sesión.
 */
public class Usuario implements Serializable {
    

    private static final long serialVersionUID = 1L; 
    
    
    // El login o identificador único (ej: "manager01"). Es lo que se teclea al entrar. 
    private String nombreUsuario; 
    
    //La clave de acceso. En este proyecto se guarda tal cual para simplificar la lógica. 
    private String contrasena;    
    
    /** El Rol (ADMINISTRADOR, MANAGER, ARBITRO, INVITADO).
        Es el atributo más importante para el sistema de permisos. 
     */
    private Rol rol;              

    public Usuario(String nombreUsuario, String contrasena, Rol rol) {

        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    //MÉTODOS DE ACCESO (GETTERS Y SETTERS)
    // Son necesarios para leer y modificar los atributos de forma segura desde fuera.



    // Obtiene el nombre de usuario
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    // Obtiene la contraseña para compararla durante el inicio de sesión.
    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Obtiene el Rol del usuario. 
   // Este método será llamado por la VentanaPrincipal para decidir qué botones activar.
    public Rol getRol() {
        return rol;
    }

    // Permite al Administrador cambiar el rol de un usuario (ej: ascender de Invitado a Manager). 
    public void setRol(Rol rol) {
        this.rol = rol;
    } 
  
}
package logica;

import gestion.Usuario;
import gestion.Rol;

/**
  CLASE: PermisosApp

  Centraliza las reglas de qué puede hacer
  cada usuario según su Rol.

  
 */
public class PermisosApp {

    /**
      Verifica si el usuario tiene permiso para entrar al panel de gestión 
      (Equipos, Jugadores, Usuarios).
      u El usuario que está usando la app.
      true si es Admin o Manager.
     */
    public static boolean puedeGestionarSistema(Usuario u) {
        if (u == null) return false;
        
        // Solo el Administrador y el Manager pueden tocar la estructura.
        return u.getRol() == Rol.ADMINISTRADOR || u.getRol() == Rol.MANAGER;
    }

    /**
      Verifica si el usuario puede modificar resultados de partidos.
      u El usuario actual.
      return true si es Árbitro o Admin.
     */
    public static boolean puedeModificarResultados(Usuario u) {
        if (u == null) return false;

        // El Árbitro tiene permiso específico para esto, y el Admin por ser jefe.
        return u.getRol() == Rol.ARBITRO || u.getRol() == Rol.ADMINISTRADOR;
    }

    /**
      Verifica si el usuario tiene acceso total (Gestión de Usuarios).
      u El usuario actual.
     */
    public static boolean esAccesoTotal(Usuario u) {
        if (u == null) return false;
        return u.getRol() == Rol.ADMINISTRADOR;
    }

    /**
      Regla para los invitados.
      true si el usuario solo puede mirar.
     */
    public static boolean esSoloLectura(Usuario u) {
        if (u == null) return true;
        return u.getRol() == Rol.INVITADO;
    }
}
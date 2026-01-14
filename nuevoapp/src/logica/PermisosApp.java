package logica;

import gestion.*;

/**
 * <h2>Gestor de permisos de la aplicación</h2>
 * <p>
 * Clase utilitaria que centraliza la verificación de permisos de usuarios
 * según su rol en la federación. Facilita decidir qué funcionalidades
 * están disponibles para cada tipo de usuario.
 * </p>
 */
public class PermisosApp {

    /**
     * Verifica si un usuario puede gestionar el sistema.
     * <p>
     * Usuarios con rol ADMINISTRADOR o MANAGER tienen acceso.
     * </p>
     * 
     * @param u Usuario a evaluar
     * @return true si puede gestionar el sistema, false en caso contrario
     */
    public static boolean puedeGestionarSistema(Usuario u) {
        if (u == null) return false;
        return u.getRol() == Rol.ADMINISTRADOR || u.getRol() == Rol.MANAGER;
    }

    /**
     * Verifica si un usuario puede modificar resultados de partidos.
     * <p>
     * Usuarios con rol ARBITRO o ADMINISTRADOR tienen acceso.
     * </p>
     * 
     * @param u Usuario a evaluar
     * @return true si puede modificar resultados, false en caso contrario
     */
    public static boolean puedeModificarResultados(Usuario u) {
        if (u == null) return false;
        return u.getRol() == Rol.ARBITRO || u.getRol() == Rol.ADMINISTRADOR;
    }

    /**
     * Verifica si un usuario tiene acceso total al sistema.
     * <p>
     * Solo el usuario con rol ADMINISTRADOR tiene acceso total.
     * </p>
     * 
     * @param u Usuario a evaluar
     * @return true si tiene acceso total, false en caso contrario
     */
    public static boolean esAccesoTotal(Usuario u) {
        if (u == null) return false;
        return u.getRol() == Rol.ADMINISTRADOR;
    }

    /**
     * Verifica si un usuario tiene acceso solo de lectura.
     * <p>
     * Solo el usuario con rol INVITADO tiene acceso de solo lectura.
     * </p>
     * 
     * @param u Usuario a evaluar
     * @return true si es solo lectura, false en caso contrario
     */
    public static boolean esSoloLectura(Usuario u) {
        if (u == null) return true;
        return u.getRol() == Rol.INVITADO;
    }
}
 
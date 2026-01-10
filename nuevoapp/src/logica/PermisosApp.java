package logica;

import gestion.*;

public class PermisosApp {

    public static boolean puedeGestionarSistema(Usuario u) {
        if (u == null) return false;
        return u.getRol() == Rol.ADMINISTRADOR || u.getRol() == Rol.MANAGER;
    }

    public static boolean puedeModificarResultados(Usuario u) {
        if (u == null) return false;
        return u.getRol() == Rol.ARBITRO || u.getRol() == Rol.ADMINISTRADOR;
    }

    public static boolean esAccesoTotal(Usuario u) {
        if (u == null) return false;
        return u.getRol() == Rol.ADMINISTRADOR;
    }

    public static boolean esSoloLectura(Usuario u) {
        if (u == null) return true;
        return u.getRol() == Rol.INVITADO;
    }
}
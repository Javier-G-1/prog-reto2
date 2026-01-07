package logica;

import gestion.*;

public class ControladorPrincipal {
    private DatosFederacion datos;
    private Usuario usuarioIdentificado;

    public ControladorPrincipal() {
        // Usamos el GestorArchivos para cargar, centralizando el nombre del archivo
        this.datos = GestorArchivos.cargarTodo();
        
        // Si no hay usuarios (primera vez), creamos el admin
        if (this.datos.getListaUsuarios().isEmpty()) {
            inicializarDatosPorDefecto();
        }
    }

    public void guardar() {
        GestorArchivos.guardarTodo(this.datos);
    }

    public boolean login(String user, String pass) {
        Usuario u = datos.buscarUsuario(user);
        if (u != null && u.getContrasena().equals(pass)) {
            this.usuarioIdentificado = u;
            GestorLog.registrarEvento("LOGIN: " + user);
            return true;
        }
        return false;
    }

    private void inicializarDatosPorDefecto() {
        Usuario admin = new Usuario("Admin", "admin", "1234", Rol.ADMINISTRADOR);
        datos.getListaUsuarios().add(admin);
        guardar();
    }

    public DatosFederacion getDatos() { return datos; }
    public Usuario getUsuarioIdentificado() { return usuarioIdentificado; }
}
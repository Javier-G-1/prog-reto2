package logica;

import gestion.*;

/**
 * CLASE: ControladorPrincipal
 * <p>
 * Controlador central de la aplicación. Gestiona la carga y guardado de datos,
 * la identificación de usuarios y la inicialización de datos por defecto.
 * </p>
 */
public class ControladorPrincipal {

    /** Datos maestros de la federación */
    private DatosFederacion datos;

    /** Usuario actualmente identificado (logueado) */
    private Usuario usuarioIdentificado;

    /**
     * Constructor de la clase.
     * <p>
     * Carga los datos desde archivo mediante {@link GestorArchivos}.
     * Si no existen usuarios, inicializa un administrador por defecto.
     * </p>
     */
    public ControladorPrincipal() {
        // Usamos el GestorArchivos para cargar, centralizando el nombre del archivo
        this.datos = GestorArchivos.cargarTodo();

        // Si no hay usuarios (primera vez), creamos el admin
        if (this.datos.getListaUsuarios().isEmpty()) {
            inicializarDatosPorDefecto();
        }
    }

    /**
     * Guarda todos los datos de la federación en archivo.
     */
    public void guardar() {
        GestorArchivos.guardarTodo(this.datos);
    }

    /**
     * Realiza el login de un usuario.
     *
     * @param user nombre de usuario
     * @param pass contraseña
     * @return {@code true} si el login es exitoso, {@code false} en caso contrario
     */
    public boolean login(String user, String pass) {
        Usuario u = datos.buscarUsuario(user);
        if (u != null && u.getContrasena().equals(pass)) {
            this.usuarioIdentificado = u;
            GestorLog.registrarEvento("LOGIN: " + user);
            return true;
        }
        return false;
    }

    /**
     * Inicializa los datos por defecto en caso de primera ejecución.
     * <p>
     * Crea un usuario administrador por defecto y guarda los datos.
     * </p>
     */
    private void inicializarDatosPorDefecto() {
        Usuario admin = new Usuario("Admin", "admin", "1234", Rol.ADMINISTRADOR);
        datos.getListaUsuarios().add(admin);
        guardar();
    }

    /**
     * Obtiene los datos de la federación.
     *
     * @return objeto {@link DatosFederacion} con toda la información
     */
    public DatosFederacion getDatos() {
        return datos;
    }

    /**
     * Obtiene el usuario actualmente identificado.
     *
     * @return objeto {@link Usuario} logueado, o {@code null} si no hay ninguno
     */
    public Usuario getUsuarioIdentificado() {
        return usuarioIdentificado;
    }
}

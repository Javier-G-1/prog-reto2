package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE: DatosFederacion
 * <p>
 * Actúa como el objeto raíz del sistema y es el encargado de almacenar
 * toda la información persistente de la aplicación.
 * </p>
 * <p>
 * Contiene las listas maestras de usuarios, jugadores, equipos y temporadas.
 * Esta clase es serializable para permitir guardar y recuperar el estado
 * completo del sistema.
 * </p>
 */
public class DatosFederacion implements Serializable {

    /** Identificador de versión para la serialización */
    private static final long serialVersionUID = 1L;

    /** Lista de usuarios registrados en el sistema */
    private List<Usuario> listaUsuarios;

    /** Base de datos global de jugadores/personas */
    private List<Jugador> todosLosJugadores;

    /** Lista de equipos (clubes) registrados */
    private List<Equipo> listaEquipos;

    /** Lista de temporadas creadas en el sistema */
    private List<Temporada> listaTemporadas;

    /**
     * Constructor por defecto.
     * <p>
     * Inicializa todas las listas maestras del sistema como listas vacías.
     * </p>
     */
    public DatosFederacion() {
        this.listaUsuarios = new ArrayList<>();
        this.todosLosJugadores = new ArrayList<>();
        this.listaEquipos = new ArrayList<>();
        this.listaTemporadas = new ArrayList<>();
    }

    /**
     * Añade una nueva temporada al sistema.
     *
     * @param t la temporada que se desea añadir
     */
    public void add(Temporada t) {
        if (t != null) {
            this.listaTemporadas.add(t);
        }
    }

    // --- MÉTODOS DE BÚSQUEDA ---

    /**
     * Busca un usuario por su nombre de login.
     * <p>
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     * </p>
     *
     * @param nombreLogin nombre de usuario a buscar
     * @return el {@link Usuario} encontrado o {@code null} si no existe
     */
    public Usuario buscarUsuario(String nombreLogin) {
        if (nombreLogin == null) return null;

        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(nombreLogin)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Busca una temporada por su nombre.
     * <p>
     * La comparación del nombre se realiza sin distinguir mayúsculas.
     * </p>
     *
     * @param nombre nombre de la temporada
     * @return la {@link Temporada} encontrada o {@code null} si no existe
     */
    public Temporada buscarTemporadaPorNombre(String nombre) {
        if (nombre == null) return null;

        for (Temporada t : listaTemporadas) {
            if (t.getNombre().equalsIgnoreCase(nombre)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Busca un equipo registrado por su nombre.
     * <p>
     * Útil para comprobar si un equipo ya existe globalmente
     * antes de inscribirlo en una temporada.
     * </p>
     *
     * @param nombre nombre del equipo a buscar
     * @return el {@link Equipo} encontrado o {@code null} si no existe
     */
    public Equipo buscarEquipoPorNombre(String nombre) {
        if (nombre == null) return null;

        for (Equipo e : listaEquipos) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                return e;
            }
        }
        return null;
    }

    // --- GETTERS ---

    /**
     * Obtiene la lista de usuarios del sistema.
     *
     * @return lista de usuarios
     */
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    /**
     * Obtiene la base de datos global de jugadores.
     *
     * @return lista de todos los jugadores
     */
    public List<Jugador> getTodosLosJugadores() {
        return todosLosJugadores;
    }

    /**
     * Obtiene la lista de equipos registrados.
     *
     * @return lista de equipos
     */
    public List<Equipo> getListaEquipos() {
        return listaEquipos;
    }

    /**
     * Obtiene la lista de temporadas creadas.
     *
     * @return lista de temporadas
     */
    public List<Temporada> getListaTemporadas() {
        return listaTemporadas;
    }
}

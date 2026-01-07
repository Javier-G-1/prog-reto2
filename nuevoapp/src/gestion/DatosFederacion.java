package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE: DatosFederacion
 * Objeto raíz para la persistencia. Contiene toda la información del sistema.
 */
public class DatosFederacion implements Serializable {

    private static final long serialVersionUID = 1L;

    // LISTAS MAESTRAS
    private List<Usuario> listaUsuarios;
    private List<Jugador> todosLosJugadores; // Base de datos global de personas
    private List<Equipo> listaEquipos;       // Clubes registrados
    private List<Temporada> listaTemporadas;

    public DatosFederacion() {
        this.listaUsuarios = new ArrayList<>();
        this.todosLosJugadores = new ArrayList<>();
        this.listaEquipos = new ArrayList<>();
        this.listaTemporadas = new ArrayList<>();
        
        // Sugerencia: Inicializar con un usuario admin por defecto 
        // para no quedarnos bloqueados en el primer inicio.
        // initDefaultAdmin(); 
    }
    public void add(Temporada t) {
        if (t != null) {
            this.listaTemporadas.add(t);
        }
    }
    // --- MÉTODOS DE BÚSQUEDA (Requisito: Login y Gestión) ---

    public Usuario buscarUsuario(String nombreLogin) {
        if (nombreLogin == null) return null;
        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(nombreLogin)) {
                return u;
            }
        }
        return null;
    }

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
     * Requisito: Exportación y Logs.
     * Útil para comprobar si un equipo ya existe globalmente antes de inscribirlo.
     */
    public Equipo buscarEquipoPorNombre(String nombre) {
        for (Equipo e : listaEquipos) {
            if (e.getNombre().equalsIgnoreCase(nombre)) return e;
        }
        return null;
    }

    // --- GETTERS ---
    public List<Usuario> getListaUsuarios() { return listaUsuarios; }
    public List<Jugador> getTodosLosJugadores() { return todosLosJugadores; }
    public List<Equipo> getListaEquipos() { return listaEquipos; }
    public List<Temporada> getListaTemporadas() { return listaTemporadas; }
}
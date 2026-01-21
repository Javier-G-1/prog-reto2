package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * CLASE: Temporada
 * <p>
 * Representa una temporada deportiva dentro del sistema.
 * Cada temporada tiene un ID único basado en su nombre.
 * </p>
 */
public class Temporada implements Serializable {

    /** Constante que indica que la temporada ha finalizado */
    public static final String TERMINADA = "TERMINADA";

    /** Constante que indica que la temporada está en curso */
    public static final String EN_JUEGO = "EN JUEGO";

    /** Constante que indica que la temporada aún no ha comenzado */
    public static final String FUTURA = "FUTURA";

    private static final long serialVersionUID = 1L;
    
    /** ID único de la temporada (formato: 2025_2026, 2026_2027, ...) */
    private String id;

    /** Nombre de la temporada */
    private String nombre;

    /** Estado actual de la temporada */
    private String estado;

    /** Lista de equipos inscritos en la temporada */
    private List<Equipo> equiposParticipantes;

    /** Lista de jornadas programadas */
    private List<Jornada> listaJornadas;

    /**
     * Constructor principal de la temporada.
     * Genera automáticamente un ID basado en el nombre.
     *
     * @param nombre nombre de la temporada
     * @param estado estado inicial ({@link #FUTURA}, {@link #EN_JUEGO}, {@link #TERMINADA})
     */
    public Temporada(String nombre, String estado) {
        this.id = generarIdDesdeNombre(nombre);
        this.nombre = nombre;
        this.estado = estado;
        this.equiposParticipantes = new ArrayList<>();
        this.listaJornadas = new ArrayList<>();
    }

    /**
     * Constructor completo para carga desde archivo.
     *
     * @param id ID de la temporada (puede ser nulo)
     * @param nombre nombre de la temporada
     * @param estado estado inicial
     */
    public Temporada(String id, String nombre, String estado) {
        this.id = (id == null || id.isBlank()) ? generarIdDesdeNombre(nombre) : id;
        this.nombre = nombre;
        this.estado = estado;
        this.equiposParticipantes = new ArrayList<>();
        this.listaJornadas = new ArrayList<>();
    }

    /**
     * Genera un ID basado en el nombre de la temporada.
     * Ejemplos:
     * - "Temporada 2025/26" → "2025_2026"
     * - "2025/26" → "2025_2026"
     * - "Liga 2025" → "Liga_2025"
     *
     * @param nombre nombre de la temporada
     * @return ID generado
     */
    private String generarIdDesdeNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return "temp_" + System.currentTimeMillis();
        }
        
        // Reemplazar espacios y barras por guiones bajos
        String id = nombre.trim()
                         .replace(" ", "_")
                         .replace("/", "_")
                         .replace("\\", "_");
        
        // Si es formato corto como "25/26", expandir a "2025_2026"
        if (id.matches("\\d{2}_\\d{2}")) {
            String[] partes = id.split("_");
            String ano1 = "20" + partes[0];
            String ano2 = "20" + partes[1];
            return ano1 + "_" + ano2;
        }
        
        return id;
    }

    /**
     * Verifica si la temporada tiene al menos 6 equipos inscritos.
     *
     * @return {@code true} si hay 6 o más equipos
     */
    public boolean tieneEquiposSuficientes() {
        return equiposParticipantes != null && equiposParticipantes.size() >= 6;
    }

    /**
     * Cambia el estado de la temporada.
     *
     * @param estado nuevo estado de la temporada
     */
    public void setEstado(String estado) {
        if (estado.equals(FUTURA) || estado.equals(EN_JUEGO) || estado.equals(TERMINADA)) {
            this.estado = estado;
        }
    }

    /**
     * Verifica si un jugador ya está inscrito en algún equipo de la temporada.
     *
     * @param j jugador a verificar
     * @return {@code true} si el jugador ya está inscrito
     */
    public boolean jugadorYaInscrito(Jugador j) {
        for (Equipo e : equiposParticipantes) {
            if (e.getPlantilla().contains(j)) return true;
        }
        return false;
    }

    /**
     * Inscribe un equipo en la temporada.
     *
     * @param e equipo a inscribir
     */
    public void inscribirEquipo(Equipo e) {
        if (e != null && this.estado.equals(FUTURA)) {
            if (!equiposParticipantes.contains(e)) {
                equiposParticipantes.add(e);
            }
        }
    }

    /**
     * Busca un equipo inscrito por su nombre.
     *
     * @param nombre nombre del equipo
     * @return el equipo si se encuentra, {@code null} si no existe
     */
    public Equipo buscarEquipoPorNombre(String nombre) {
        for (Equipo e : equiposParticipantes) {
            if (e.getNombre().equals(nombre)) return e;
        }
        return null;
    }

    /**
     * Agrega una jornada a la temporada.
     *
     * @param j jornada a agregar
     */
    public void agregarJornada(Jornada j) {
        if (this.listaJornadas == null) this.listaJornadas = new ArrayList<>();
        if (j != null) this.listaJornadas.add(j);
    }

    // --- GETTERS Y SETTERS ---

    public String getId() { return id; }
    
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre;
        // Regenerar ID si cambia el nombre
        this.id = generarIdDesdeNombre(nombre);
    }

    public String getEstado() { return estado; }

    public List<Equipo> getEquiposParticipantes() { return equiposParticipantes; }

    public List<Jornada> getListaJornadas() { return listaJornadas; }

    @Override
    public String toString() {
        return nombre + " [" + estado + "] (ID: " + id + ")";
    }
}
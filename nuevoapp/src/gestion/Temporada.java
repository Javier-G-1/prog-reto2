package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE: Temporada
 * <p>
 * Representa una temporada deportiva dentro del sistema.
 * Contiene información sobre los equipos participantes, las jornadas
 * programadas y el estado actual de la temporada.
 * </p>
 * <p>
 * La temporada puede estar en estado {@link #FUTURA}, {@link #EN_JUEGO} o {@link #TERMINADA}.
 * </p>
 */
public class Temporada implements Serializable {

    /** Constante que indica que la temporada ha finalizado */
    public static final String TERMINADA = "TERMINADA";

    /** Constante que indica que la temporada está en curso */
    public static final String EN_JUEGO = "EN JUEGO";

    /** Constante que indica que la temporada aún no ha comenzado */
    public static final String FUTURA = "FUTURA";

    /** Identificador de versión para la serialización */
    private static final long serialVersionUID = 1L;

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
     *
     * @param nombre nombre de la temporada
     * @param estado estado inicial ({@link #FUTURA}, {@link #EN_JUEGO}, {@link #TERMINADA})
     */
    public Temporada(String nombre, String estado) {
        this.nombre = nombre;
        this.estado = estado;
        this.equiposParticipantes = new ArrayList<>();
        this.listaJornadas = new ArrayList<>();
    }

    /**
     * Verifica si la temporada tiene al menos 6 equipos inscritos.
     *
     * @return {@code true} si hay 6 o más equipos, {@code false} en caso contrario
     */
    public boolean tieneEquiposSuficientes() {
        return equiposParticipantes != null && equiposParticipantes.size() >= 6;
    }

    /**
     * Cambia el estado de la temporada.
     * <p>
     * Solo se permiten los valores {@link #FUTURA}, {@link #EN_JUEGO} o {@link #TERMINADA}.
     * </p>
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
     * @return {@code true} si el jugador ya está inscrito, {@code false} en caso contrario
     */
    public boolean jugadorYaInscrito(Jugador j) {
        for (Equipo e : equiposParticipantes) {
            if (e.getPlantilla().contains(j)) return true;
        }
        return false;
    }

    /**
     * Inscribe un equipo en la temporada.
     * <p>
     * Solo se pueden inscribir equipos si la temporada está en estado {@link #FUTURA}.
     * Evita duplicados.
     * </p>
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
     * <p>
     * Inicializa la lista si es nula y evita valores nulos.
     * </p>
     *
     * @param j jornada a agregar
     */
    public void agregarJornada(Jornada j) {
        if (this.listaJornadas == null) this.listaJornadas = new ArrayList<>();
        if (j != null) this.listaJornadas.add(j);
    }

    // --- GETTERS ---

    /**
     * Obtiene el nombre de la temporada.
     *
     * @return nombre de la temporada
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene el estado actual de la temporada.
     *
     * @return estado de la temporada
     */
    public String getEstado() { return estado; }

    /**
     * Obtiene la lista de equipos inscritos.
     *
     * @return lista de equipos participantes
     */
    public List<Equipo> getEquiposParticipantes() { return equiposParticipantes; }

    /**
     * Obtiene la lista de jornadas de la temporada.
     *
     * @return lista de jornadas
     */
    public List<Jornada> getListaJornadas() { return listaJornadas; }

    /**
     * Representación en texto de la temporada.
     *
     * @return cadena con el nombre y el estado de la temporada
     */
    @Override
    public String toString() {
        return nombre + " [" + estado + "]";
    }
}

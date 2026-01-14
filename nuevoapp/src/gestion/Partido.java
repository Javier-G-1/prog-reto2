package gestion;

import java.io.Serializable;

/**
 * CLASE: Partido
 * <p>
 * Representa un partido entre dos equipos dentro de una jornada.
 * Contiene información sobre los equipos, goles, estado de finalización
 * y fecha del partido.
 * </p>
 * <p>
 * Los goles inicialmente se establecen en -1 para indicar que el partido
 * no se ha jugado aún. La clase permite calcular el ganador y los puntos
 * obtenidos por cada equipo según el resultado.
 * </p>
 */
public class Partido implements Serializable {

    /** Identificador de versión para la serialización */
    private static final long serialVersionUID = 1L;

    /** Equipo local */
    private Equipo equipoLocal;

    /** Equipo visitante */
    private Equipo equipoVisitante;

    /** Goles del equipo local (-1 indica partido no jugado) */
    private int golesLocal;

    /** Goles del equipo visitante (-1 indica partido no jugado) */
    private int golesVisitante;

    /** Indica si el partido ha finalizado */
    private boolean finalizado;

    /** Fecha del partido en formato de texto */
    private String fecha;

    /**
     * Constructor principal del partido.
     * <p>
     * Inicializa el partido con los equipos local y visitante.
     * Los goles se inicializan en -1 indicando que aún no se ha jugado.
     * </p>
     *
     * @param local equipo local
     * @param visitante equipo visitante
     * @throws IllegalArgumentException si alguno de los equipos es nulo
     *                                  o si ambos equipos son el mismo
     */
    public Partido(Equipo local, Equipo visitante) {
        if (local == null || visitante == null)
            throw new IllegalArgumentException("Los equipos no pueden ser nulos.");
        if (local.equals(visitante))
            throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo.");

        this.equipoLocal = local;
        this.equipoVisitante = visitante;
        this.golesLocal = -1;      // indica "sin jugar"
        this.golesVisitante = -1;  // indica "sin jugar"
        this.finalizado = false;
        this.fecha = null;
    }

    /**
     * Obtiene el nombre del ganador del partido.
     *
     * @return nombre del equipo ganador, "Empate" si hay empate,
     *         o "Pendiente" si el partido aún no se ha finalizado
     */
    public String obtenerGanador() {
        if (!finalizado) return "Pendiente";
        if (golesLocal > golesVisitante) return equipoLocal.getNombre();
        if (golesVisitante > golesLocal) return equipoVisitante.getNombre();
        return "Empate";
    }

    /**
     * Calcula los puntos obtenidos por el equipo local.
     * <p>
     * Victoria: 2 puntos, Empate: 1 punto, Derrota o pendiente: 0 puntos.
     * </p>
     *
     * @return puntos del equipo local
     */
    public int getPuntosLocal() {
        if (!finalizado) return 0;
        if (golesLocal > golesVisitante) return 2;
        if (golesLocal == golesVisitante) return 1;
        return 0;
    }

    /**
     * Calcula los puntos obtenidos por el equipo visitante.
     * <p>
     * Victoria: 2 puntos, Empate: 1 punto, Derrota o pendiente: 0 puntos.
     * </p>
     *
     * @return puntos del equipo visitante
     */
    public int getPuntosVisitante() {
        if (!finalizado) return 0;
        if (golesVisitante > golesLocal) return 2;
        if (golesVisitante == golesLocal) return 1;
        return 0;
    }

    // --- GETTERS Y SETTERS ---

    public Equipo getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(Equipo equipoLocal) { this.equipoLocal = equipoLocal; }

    public Equipo getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(Equipo equipoVisitante) { this.equipoVisitante = equipoVisitante; }

    public int getGolesLocal() { return golesLocal; }
    public void setGolesLocal(int golesLocal) { this.golesLocal = golesLocal; }

    public int getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(int golesVisitante) { this.golesVisitante = golesVisitante; }

    public boolean isFinalizado() { return finalizado; }
    public void setFinalizado(boolean finalizado) { this.finalizado = finalizado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    /**
     * Representación en texto del partido.
     *
     * @return cadena con el formato "Local golesLocal - golesVisitante Visitante"
     */
    @Override
    public String toString() {
        return equipoLocal.getNombre() + " " + golesLocal + " - " + golesVisitante + " " + equipoVisitante.getNombre();
    }
}

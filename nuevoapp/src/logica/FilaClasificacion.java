package logica;

/**
 * Representa una fila de la clasificación de una temporada de balonmano.
 * Cada fila contiene los datos estadísticos de un equipo: partidos jugados,
 * ganados, empatados, perdidos, goles a favor, goles en contra, diferencia
 * de goles, puntos y posición.
 */
public class FilaClasificacion {

    /** Nombre del equipo */
    private String equipo;

    /** Posición en la clasificación */
    private int posicion;

    /** Partidos jugados */
    private int pj;

    /** Partidos ganados */
    private int pg;

    /** Partidos empatados */
    private int pe;

    /** Partidos perdidos */
    private int pp;

    /** Puntos obtenidos */
    private int puntos;

    /** Goles a favor */
    private int gf;

    /** Goles en contra */
    private int gc;

    /**
     * Crea una nueva fila de clasificación para un equipo.
     *
     * @param equipo Nombre del equipo (no nulo ni vacío)
     * @throws IllegalArgumentException si el nombre del equipo es nulo o vacío
     */
    public FilaClasificacion(String equipo) {
        if (equipo == null || equipo.isBlank())
            throw new IllegalArgumentException("El nombre del equipo no puede ser vacío.");
        this.equipo = equipo;
    }

    /**
     * Registra un partido en la clasificación, actualizando los partidos jugados,
     * ganados, empatados, perdidos, goles a favor, goles en contra y puntos.
     *
     * @param misGoles   Goles marcados por este equipo (no negativos)
     * @param golesRival Goles marcados por el equipo contrario (no negativos)
     * @throws IllegalArgumentException si alguno de los goles es negativo
     */
    public void registrarPartido(int misGoles, int golesRival) {
        if (misGoles < 0 || golesRival < 0)
            throw new IllegalArgumentException("Los goles no pueden ser negativos.");

        this.pj++;
        this.gf += misGoles;
        this.gc += golesRival;

        if (misGoles > golesRival) {
            this.pg++;
            this.puntos += 2; // Victoria
        } else if (misGoles == golesRival) {
            this.pe++;
            this.puntos += 1; // Empate
        } else {
            this.pp++; // Derrota
        }
    }

    /**
     * Devuelve la diferencia de goles formateada como texto con signo.
     * Por ejemplo: "+5", "0", "-3".
     *
     * @return Diferencia de goles con signo
     */
    public String getDifFormateada() {
        int dif = getDf();
        if (dif > 0) return "+" + dif;
        return String.valueOf(dif); // Negativos ya incluyen el signo
    }

    // --- Getters y Setters ---

    /**
     * @return Nombre del equipo
     */
    public String getEquipo() { return equipo; }

    /**
     * @return Posición del equipo en la clasificación
     */
    public int getPosicion() { return posicion; }

    /**
     * Establece la posición del equipo en la clasificación.
     *
     * @param posicion Posición numérica (1, 2, 3, ...)
     */
    public void setPosicion(int posicion) { this.posicion = posicion; }

    /**
     * @return Partidos jugados
     */
    public int getPj() { return pj; }

    /**
     * @return Partidos ganados
     */
    public int getPg() { return pg; }

    /**
     * @return Partidos empatados
     */
    public int getPe() { return pe; }

    /**
     * @return Partidos perdidos
     */
    public int getPp() { return pp; }

    /**
     * @return Puntos obtenidos
     */
    public int getPuntos() { return puntos; }

    /**
     * @return Goles a favor
     */
    public int getGf() { return gf; }

    /**
     * @return Goles en contra
     */
    public int getGc() { return gc; }

    /**
     * @return Diferencia de goles (GF - GC)
     */
    public int getDf() { return gf - gc; }
}

package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE: Jornada
 * <p>
 * Representa una jornada dentro de una temporada deportiva.
 * Contiene una lista de partidos programados y permite gestionar
 * su estado y validaciones.
 * </p>
 */
public class Jornada implements Serializable {

    /** Identificador de versión para la serialización */
    private static final long serialVersionUID = 1L;

    /** Nombre de la jornada */
    private String nombre;

    /** Lista de partidos programados en esta jornada */
    private List<Partido> listaPartidos;

    /**
     * Constructor principal de la jornada.
     *
     * @param nombre nombre de la jornada
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    public Jornada(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre de la jornada no puede estar vacío.");
        this.nombre = nombre;
        this.listaPartidos = new ArrayList<>();
    }

    /**
     * Verifica si todos los partidos de la jornada han finalizado.
     * <p>
     * Útil para saber si la jornada ha terminado y mostrar avisos
     * en la clasificación o progresión de la temporada.
     * </p>
     *
     * @return {@code true} si todos los partidos están finalizados,
     *         {@code false} si alguno está pendiente o la jornada está vacía
     */
    public boolean estaCompleta() {
        if (listaPartidos.isEmpty()) return false;

        for (Partido p : listaPartidos) {
            if (!p.isFinalizado()) return false;
        }
        return true;
    }

    /**
     * Agrega un nuevo partido a la jornada.
     * <p>
     * Valida que el partido no sea nulo y que no exista un enfrentamiento
     * idéntico (mismos equipos local y visitante) en la jornada.
     * </p>
     *
     * @param p partido a agregar
     * @throws IllegalArgumentException si el partido es nulo
     *                                  o ya existe un enfrentamiento igual
     */
    public void agregarPartido(Partido p) {
        if (p == null) throw new IllegalArgumentException("El partido no puede ser nulo.");

        // Evitar partidos repetidos
        for (Partido existente : listaPartidos) {
            boolean mismosEquipos = (existente.getEquipoLocal().equals(p.getEquipoLocal()) &&
                                     existente.getEquipoVisitante().equals(p.getEquipoVisitante()));
            if (mismosEquipos)
                throw new IllegalArgumentException("Este enfrentamiento ya existe en la jornada.");
        }

        listaPartidos.add(p);
    }

    // --- GETTERS Y SETTERS ---

    /**
     * Obtiene el nombre de la jornada.
     *
     * @return nombre de la jornada
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre de la jornada.
     *
     * @param nombre nuevo nombre de la jornada
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la lista de partidos programados en la jornada.
     *
     * @return lista de partidos
     */
    public List<Partido> getListaPartidos() {
        return listaPartidos;
    }

    /**
     * Establece la lista completa de partidos de la jornada.
     *
     * @param listaPartidos nueva lista de partidos
     */
    public void setListaPartidos(List<Partido> listaPartidos) {
        this.listaPartidos = listaPartidos;
    }

    /**
     * Representación en texto de la jornada.
     *
     * @return cadena con el nombre de la jornada
     */
    @Override
    public String toString() {
        return "Jornada " + nombre;
    }
}

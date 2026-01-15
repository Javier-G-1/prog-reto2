package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CLASE: Equipo
 * <p>
 * Representa un equipo o club deportivo dentro del sistema.
 * Contiene información básica como el nombre, el escudo y
 * la plantilla de jugadores que lo componen.
 * </p>
 * <p>
 * Esta clase implementa {@link Serializable} para permitir
 * su persistencia junto al resto de datos del sistema.
 * </p>
 */
public class Equipo implements Serializable {

    /** Identificador de versión para la serialización */
    private static final long serialVersionUID = 1L;

    /** Nombre del equipo */
    private String nombre;

    /** Ruta al archivo de imagen del escudo del equipo */
    private String rutaEscudo;

    /** Lista de jugadores que forman la plantilla del equipo */
    private List<Jugador> plantilla;

    /**
     * Constructor principal del equipo.
     * <p>
     * Si la ruta del escudo es nula o está vacía, se asigna
     * un escudo por defecto.
     * </p>
     *
     * @param nombre nombre del equipo
     * @param rutaEscudo ruta del escudo del equipo
     */
    public Equipo(String nombre, String rutaEscudo) {
        this.nombre = nombre;
        this.rutaEscudo = (rutaEscudo == null || rutaEscudo.isBlank())
                ? "img/default_escudo.png"
                : rutaEscudo;
        this.plantilla = new ArrayList<>();
    }

    /**
     * Constructor simplificado.
     * <p>
     * Crea un equipo asignándole un escudo por defecto.
     * </p>
     *
     * @param nombre nombre del equipo
     */
    public Equipo(String nombre) {
        this(nombre, "img/default_escudo.png");
    }

    /**
     * Ficha un jugador en la plantilla del equipo.
     * <p>
     * Valida que el jugador no sea nulo, que no esté ya en la plantilla
     * y que su dorsal sea único dentro del equipo.
     * </p>
     *
     * @param j jugador a fichar
     * @throws IllegalArgumentException si el jugador es nulo,
     *                                  ya pertenece al equipo
     *                                  o su dorsal está ocupado
     */
    public void ficharJugador(Jugador j) {
        if (j == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo");
        }

        // Verificar que el jugador no esté ya en la plantilla
        if (plantilla.contains(j)) {
            throw new IllegalArgumentException("El jugador ya está en la plantilla");
        }

        // Validación de dorsal único
        if (j.getDorsal() > 0) {
            validarDorsalUnico(j.getDorsal(), j.getId());
        }

        plantilla.add(j);
    }

    /**
     * Valida que un dorsal no esté ocupado por otro jugador del equipo.
     * <p>
     * Permite excluir a un jugador concreto para facilitar
     * modificaciones de dorsal.
     * </p>
     *
     * @param dorsal número de dorsal a validar
     * @param idJugadorActual identificador del jugador actual
     * @throws IllegalArgumentException si el dorsal no es válido
     *                                  o ya está ocupado
     */
    public void validarDorsalUnico(int dorsal, String idJugadorActual) {
        if (dorsal <= 0 || dorsal > 99) {
            throw new IllegalArgumentException("El dorsal debe estar entre 1 y 99");
        }

        for (Jugador jugadorEnPlantilla : plantilla) {

            // Permitir que el jugador mantenga su propio dorsal
            if (jugadorEnPlantilla.getId().equals(idJugadorActual)) {
                continue;
            }

            if (jugadorEnPlantilla.getDorsal() == dorsal) {
                throw new IllegalArgumentException(
                        "El dorsal " + dorsal + " ya está ocupado por "
                                + jugadorEnPlantilla.getNombre()
                );
            }
        }
    }

    /**
     * Comprueba si un dorsal está disponible en el equipo.
     *
     * @param dorsal dorsal a comprobar
     * @return {@code true} si el dorsal está libre, {@code false} en caso contrario
     */
    public boolean dorsalDisponible(int dorsal) {
        return dorsalDisponible(dorsal, null);
    }

    /**
     * Comprueba si un dorsal está disponible, excluyendo a un jugador concreto.
     *
     * @param dorsal dorsal a comprobar
     * @param idJugadorExcluir ID del jugador a excluir de la comprobación
     * @return {@code true} si el dorsal está libre, {@code false} en caso contrario
     */
    public boolean dorsalDisponible(int dorsal, String idJugadorExcluir) {
        if (dorsal <= 0 || dorsal > 99) return false;

        for (Jugador j : plantilla) {
            if (idJugadorExcluir != null && j.getId().equals(idJugadorExcluir)) {
                continue;
            }

            if (j.getDorsal() == dorsal) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtiene el siguiente dorsal disponible en la plantilla.
     *
     * @return el primer dorsal libre entre 1 y 99,
     *         o {@code -1} si no hay dorsales disponibles
     */
    public int obtenerSiguienteDorsalDisponible() {
        for (int dorsal = 1; dorsal <= 99; dorsal++) {
            if (dorsalDisponible(dorsal)) {
                return dorsal;
            }
        }
        return -1;
    }

    /**
     * Obtiene la lista de todos los dorsales disponibles.
     *
     * @return lista de dorsales libres
     */
    public List<Integer> obtenerDorsalesDisponibles() {
        List<Integer> disponibles = new ArrayList<>();

        for (int dorsal = 1; dorsal <= 99; dorsal++) {
            if (dorsalDisponible(dorsal)) {
                disponibles.add(dorsal);
            }
        }
        return disponibles;
    }

    /**
     * Da de baja a un jugador de la plantilla.
     *
     * @param j jugador a eliminar
     */
    public void bajaJugador(Jugador j) {
        plantilla.remove(j);
    }

    // --- GETTERS Y SETTERS ---

    /**
     * Obtiene el nombre del equipo.
     *
     * @return nombre del equipo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre del equipo.
     *
     * @param nombre nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la ruta del escudo del equipo.
     *
     * @return ruta del escudo
     */
    public String getRutaEscudo() {
        return rutaEscudo;
    }

    /**
     * Modifica la ruta del escudo del equipo.
     *
     * @param rutaEscudo nueva ruta del escudo
     */
    public void setRutaEscudo(String rutaEscudo) {
        this.rutaEscudo = rutaEscudo;
    }

    /**
     * Obtiene la plantilla completa del equipo.
     *
     * @return lista de jugadores del equipo
     */
    public List<Jugador> getPlantilla() {
        return plantilla;
    }

    /**
     * Representación en texto del equipo.
     *
     * @return nombre del equipo
     */
    @Override
    public String toString() {
        return nombre;
    }
    
    
    /** 
     * Número mínimo de jugadores requeridos para que un equipo
     * pueda participar en una temporada.
     */
    public static final int JUGADORES_MINIMOS = 9;

    /** 
     * Número recomendado de jugadores para considerar
     * la plantilla como completa.
     */
    public static final int JUGADORES_RECOMENDADOS = 18;

    /**
     * Comprueba si el equipo tiene el número mínimo de jugadores necesarios.
     *
     * @return {@code true} si la plantilla tiene al menos
     *         {@link #JUGADORES_MINIMOS} jugadores, {@code false} en caso contrario
     */
    public boolean tieneJugadoresSuficientes() {
        return plantilla.size() >= JUGADORES_MINIMOS;
    }

    /**
     * Calcula cuántos jugadores faltan para alcanzar el mínimo requerido.
     *
     * @return número de jugadores faltantes, o {@code 0}
     *         si ya se ha alcanzado el mínimo
     */
    public int jugadoresFaltantes() {
        int faltantes = JUGADORES_MINIMOS - plantilla.size();
        return Math.max(0, faltantes);
    }

    /**
     * Obtiene el número total de jugadores en la plantilla.
     *
     * @return cantidad de jugadores del equipo
     */
    public int numeroDeJugadores() {
        return plantilla.size();
    }

    /**
     * Obtiene un mensaje descriptivo del estado actual de la plantilla.
     * <p>
     * El mensaje indica si la plantilla está completa,
     * si ha alcanzado el mínimo o cuántos jugadores faltan.
     * </p>
     *
     * @return mensaje de estado de la plantilla
     */
    public String obtenerMensajeEstadoPlantilla() {
        int actual = plantilla.size();
        
        if (actual >= JUGADORES_RECOMENDADOS) {
            return String.format("✓ Plantilla completa (%d/%d jugadores)", 
                               actual, JUGADORES_RECOMENDADOS);
        } else if (actual >= JUGADORES_MINIMOS) {
            return String.format("✓ Mínimo alcanzado (%d/%d jugadores)", 
                               actual, JUGADORES_MINIMOS);
        } else {
            int faltan = JUGADORES_MINIMOS - actual;
            return String.format("✗ Faltan %d jugador%s (%d/%d)", 
                               faltan, 
                               faltan == 1 ? "" : "es",
                               actual, 
                               JUGADORES_MINIMOS);
        }
    }

    /**
     * Obtiene un mensaje detallado de validación del equipo.
     * <p>
     * El mensaje incluye el nombre del equipo, el número actual
     * de jugadores y si cumple o no el mínimo requerido.
     * </p>
     *
     * @return mensaje de validación de la plantilla
     */
    public String obtenerDetalleValidacion() {
        int actual = plantilla.size();
        int faltan = jugadoresFaltantes();
        
        if (tieneJugadoresSuficientes()) {
            return String.format("%s tiene %d jugador%s (mínimo: %d) ✓", 
                               nombre, 
                               actual,
                               actual == 1 ? "" : "es",
                               JUGADORES_MINIMOS);
        } else {
            return String.format("%s tiene %d jugador%s. Faltan %d para alcanzar el mínimo de %d ✗", 
                               nombre, 
                               actual,
                               actual == 1 ? "" : "es",
                               faltan,
                               JUGADORES_MINIMOS);
        }
    }

    /**
     * Comprueba la igualdad entre equipos.
     * <p>
     * Dos equipos se consideran iguales si tienen el mismo nombre
     * (ignorando mayúsculas y minúsculas).
     * </p>
     *
     * @param obj objeto a comparar
     * @return {@code true} si son iguales, {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Equipo)) return false;
        Equipo e = (Equipo) obj;
        return nombre.equalsIgnoreCase(e.nombre);
    }

    /**
     * Calcula el código hash del equipo.
     *
     * @return valor hash basado en el nombre del equipo
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase());
    }
}

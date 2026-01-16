package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CLASE: Equipo
 * <p>
 * Representa un equipo o club deportivo dentro del sistema.
 * Cada equipo tiene un ID único e incremental que nunca se repite.
 * </p>
 */
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /** Contador global de IDs */
    private static AtomicInteger contadorGlobal = new AtomicInteger(1);
    
    /** ID único del equipo (formato: E001, E002, ...) */
    private String id;

    /** Nombre del equipo */
    private String nombre;

    /** Ruta al archivo de imagen del escudo del equipo */
    private String rutaEscudo;

    /** Lista de jugadores que forman la plantilla del equipo */
    private List<Jugador> plantilla;

    /**
     * Constructor principal del equipo.
     * Genera automáticamente un ID único incremental.
     *
     * @param nombre nombre del equipo
     * @param rutaEscudo ruta del escudo del equipo
     */
    public Equipo(String nombre, String rutaEscudo) {
        this.id = generarIdUnico();
        this.nombre = nombre;
        this.rutaEscudo = (rutaEscudo == null || rutaEscudo.isBlank())
                ? "img/default_escudo.png"
                : rutaEscudo;
        this.plantilla = new ArrayList<>();
    }

    /**
     * Constructor simplificado.
     * Crea un equipo asignándole un escudo por defecto.
     *
     * @param nombre nombre del equipo
     */
    public Equipo(String nombre) {
        this(nombre, "img/default_escudo.png");
    }

    /**
     * Constructor completo para carga desde archivo.
     * Actualiza el contador global si el ID cargado es mayor.
     *
     * @param id ID del equipo (puede ser nulo)
     * @param nombre nombre del equipo
     * @param rutaEscudo ruta del escudo
     */
    public Equipo(String id, String nombre, String rutaEscudo) {
        this.id = (id == null || id.isBlank()) ? generarIdUnico() : id;
        this.nombre = nombre;
        this.rutaEscudo = (rutaEscudo == null || rutaEscudo.isBlank())
                ? "img/default_escudo.png"
                : rutaEscudo;
        this.plantilla = new ArrayList<>();
        
        actualizarContadorSiNecesario(this.id);
    }

    /**
     * Genera un ID único incremental.
     * Formato: E001, E002, ...
     *
     * @return ID único generado
     */
    private String generarIdUnico() {
        int numero = contadorGlobal.getAndIncrement();
        return String.format("E%03d", numero);
    }

    /**
     * Actualiza el contador global si el ID proporcionado es mayor.
     *
     * @param id ID del equipo a evaluar
     */
    private void actualizarContadorSiNecesario(String id) {
        if (id == null || !id.startsWith("E")) return;

        try {
            int numeroId = Integer.parseInt(id.substring(1));
            int valorActual;
            do {
                valorActual = contadorGlobal.get();
                if (numeroId < valorActual) break;
            } while (!contadorGlobal.compareAndSet(valorActual, numeroId + 1));
        } catch (NumberFormatException e) {
            // ID mal formado, ignorar
        }
    }

    /**
     * Sincroniza el contador global al cargar equipos desde almacenamiento.
     *
     * @param todosLosEquipos lista de todos los equipos cargados
     */
    public static void sincronizarContadorGlobal(java.util.List<Equipo> todosLosEquipos) {
        if (todosLosEquipos == null || todosLosEquipos.isEmpty()) {
            contadorGlobal.set(1);
            return;
        }

        int maxId = 0;

        for (Equipo e : todosLosEquipos) {
            if (e.id != null && e.id.startsWith("E")) {
                try {
                    int numeroId = Integer.parseInt(e.id.substring(1));
                    if (numeroId > maxId) {
                        maxId = numeroId;
                    }
                } catch (NumberFormatException ex) {
                    // Ignorar IDs mal formados
                }
            }
        }

        contadorGlobal.set(maxId + 1);
        System.out.println("✓ Contador de equipos sincronizado en: E" + 
                         String.format("%03d", maxId + 1));
    }

    /**
     * Ficha un jugador en la plantilla del equipo.
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

        if (plantilla.contains(j)) {
            throw new IllegalArgumentException("El jugador ya está en la plantilla");
        }

        if (j.getDorsal() > 0) {
            validarDorsalUnico(j.getDorsal(), j.getId());
        }

        plantilla.add(j);
    }

    /**
     * Valida que un dorsal no esté ocupado por otro jugador del equipo.
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
     * @return {@code true} si el dorsal está libre
     */
    public boolean dorsalDisponible(int dorsal) {
        return dorsalDisponible(dorsal, null);
    }

    /**
     * Comprueba si un dorsal está disponible, excluyendo a un jugador concreto.
     *
     * @param dorsal dorsal a comprobar
     * @param idJugadorExcluir ID del jugador a excluir
     * @return {@code true} si el dorsal está libre
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

    /** Número mínimo de jugadores requeridos */
    public static final int JUGADORES_MINIMOS = 9;

    /** Número recomendado de jugadores */
    public static final int JUGADORES_RECOMENDADOS = 18;

    /**
     * Comprueba si el equipo tiene el número mínimo de jugadores.
     *
     * @return {@code true} si tiene al menos {@link #JUGADORES_MINIMOS}
     */
    public boolean tieneJugadoresSuficientes() {
        return plantilla.size() >= JUGADORES_MINIMOS;
    }

    /**
     * Calcula cuántos jugadores faltan para alcanzar el mínimo.
     *
     * @return número de jugadores faltantes, o {@code 0}
     */
    public int jugadoresFaltantes() {
        int faltantes = JUGADORES_MINIMOS - plantilla.size();
        return Math.max(0, faltantes);
    }

    /**
     * Obtiene el número total de jugadores en la plantilla.
     *
     * @return cantidad de jugadores
     */
    public int numeroDeJugadores() {
        return plantilla.size();
    }

    /**
     * Obtiene un mensaje descriptivo del estado de la plantilla.
     *
     * @return mensaje de estado
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
     *
     * @return mensaje de validación
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

    // --- GETTERS Y SETTERS ---

    public String getId() { return id; }
    
    public void setId(String id) { 
        this.id = id;
        actualizarContadorSiNecesario(id);
    }

    public String getNombre() { return nombre; }
    
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRutaEscudo() { return rutaEscudo; }
    
    public void setRutaEscudo(String rutaEscudo) { this.rutaEscudo = rutaEscudo; }

    public List<Jugador> getPlantilla() { return plantilla; }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Equipo)) return false;
        Equipo e = (Equipo) obj;
        return id.equals(e.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
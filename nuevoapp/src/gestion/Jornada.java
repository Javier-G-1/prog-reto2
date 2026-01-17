package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CLASE: Jornada
 * <p>
 * Representa una jornada de partidos dentro de una temporada.
 * Cada jornada tiene un ID único e incremental que se reinicia por temporada.
 * </p>
 */
public class Jornada implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /** Contador global de IDs */
    private static AtomicInteger contadorGlobal = new AtomicInteger(1);
    
    /** ID único de la jornada (formato: J001, J002, ...) */
    private String id;
    
    /** Nombre de la jornada */
    private String nombre;
    
    /** Lista de partidos de la jornada */
    private List<Partido> listaPartidos;

    /**
     * Constructor principal.
     * Genera automáticamente un ID único incremental.
     *
     * @param nombre nombre de la jornada
     */
    public Jornada(String nombre) {
        this.id = generarIdUnico();
        this.nombre = nombre;
        this.listaPartidos = new ArrayList<>();
    }

    /**
     * Constructor completo para carga desde archivo.
     * Actualiza el contador global si el ID cargado es mayor.
     *
     * @param id ID de la jornada (puede ser nulo)
     * @param nombre nombre de la jornada
     */
    public Jornada(String id, String nombre) {
        this.id = (id == null || id.isBlank()) ? generarIdUnico() : id;
        this.nombre = nombre;
        this.listaPartidos = new ArrayList<>();
        
        actualizarContadorSiNecesario(this.id);
    }

    /**
     * Genera un ID único incremental.
     * Formato: J001, J002, ...
     *
     * @return ID único generado
     */
    private String generarIdUnico() {
        int numero = contadorGlobal.getAndIncrement();
        return String.format("J%02d", numero); // ⭐ %02d en lugar de %03d
    }

    /**
     * Actualiza el contador global si el ID proporcionado es mayor.
     *
     * @param id ID de la jornada a evaluar
     */
    private void actualizarContadorSiNecesario(String id) {
        if (id == null || !id.startsWith("J")) return;

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
     * ⭐ NUEVO: Reinicia el contador de IDs para una nueva temporada.
     * <p>
     * Este método debe llamarse al inicio de la generación de cada calendario
     * para que las jornadas empiecen desde J001.
     * </p>
     */
    public static void reiniciarContador() {
        contadorGlobal.set(1);
        System.out.println("🔄 Contador de jornadas reiniciado a J01");
    }

    /**
     * Sincroniza el contador global al cargar jornadas desde almacenamiento.
     *
     * @param todasLasJornadas lista de todas las jornadas cargadas
     */
    public static void sincronizarContadorGlobal(java.util.List<Jornada> todasLasJornadas) {
        if (todasLasJornadas == null || todasLasJornadas.isEmpty()) {
            contadorGlobal.set(1);
            return;
        }

        int maxId = 0;

        for (Jornada j : todasLasJornadas) {
            if (j.id != null && j.id.startsWith("J")) {
                try {
                    int numeroId = Integer.parseInt(j.id.substring(1));
                    if (numeroId > maxId) {
                        maxId = numeroId;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar IDs mal formados
                }
            }
        }

        contadorGlobal.set(maxId + 1);
        System.out.println("✓ Contador de jornadas sincronizado en: J" + 
                         String.format("%02d", maxId + 1)); // ⭐ %02d
    }
    
    /**
     * ⭐ NUEVO: Obtiene el próximo ID que se generará (para debugging).
     *
     * @return próximo ID como cadena
     */
    public static String obtenerProximoId() {
        return String.format("J%02d", contadorGlobal.get()); // ⭐ %02d
    }
    /**
     * Agrega un partido a la jornada.
     *
     * @param p partido a agregar
     */
    public void agregarPartido(Partido p) {
        if (p != null) {
            this.listaPartidos.add(p);
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

    public List<Partido> getListaPartidos() { return listaPartidos; }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jornada jornada = (Jornada) o;
        return Objects.equals(id, jornada.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
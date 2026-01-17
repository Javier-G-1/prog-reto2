package gestion;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CLASE: Partido
 * <p>
 * Representa un partido entre dos equipos dentro de una jornada.
 * Cada partido tiene un ID único e incremental que nunca se repite.
 * </p>
 */
public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /** Contador global de IDs */
    private static AtomicInteger contadorGlobal = new AtomicInteger(1);
    
    /** ID único del partido (formato: P1_1, P1_2, ...) */
    private String id;

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
     * Genera automáticamente un ID único incremental.
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

        this.id = generarIdUnico();
        this.equipoLocal = local;
        this.equipoVisitante = visitante;
        this.golesLocal = -1;
        this.golesVisitante = -1;
        this.finalizado = false;
        this.fecha = null;
    }

    /**
     * Constructor completo para carga desde archivo.
     * Actualiza el contador global si el ID cargado es mayor.
     *
     * @param id ID del partido (puede ser nulo)
     * @param local equipo local
     * @param visitante equipo visitante
     */
    public Partido(String id, Equipo local, Equipo visitante) {
        if (local == null || visitante == null)
            throw new IllegalArgumentException("Los equipos no pueden ser nulos.");
        if (local.equals(visitante))
            throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo.");

        this.id = (id == null || id.isBlank()) ? generarIdUnico() : id;
        this.equipoLocal = local;
        this.equipoVisitante = visitante;
        this.golesLocal = -1;
        this.golesVisitante = -1;
        this.finalizado = false;
        this.fecha = null;
        
        actualizarContadorSiNecesario(this.id);
    }

    /**
     * Genera un ID único incremental.
     * Formato: P001, P002, ...
     *
     * @return ID único generado
     */
    private String generarIdUnico() {
        int numero = contadorGlobal.getAndIncrement();
        return String.format("P%02d", numero); // ⭐ %02d en lugar de %03d
    }
    
    
    public static void reiniciarContador() {
        contadorGlobal.set(1);
        System.out.println("🔄 Contador de partidos reiniciado a P01");
    }

    /**
     * Actualiza el contador global si el ID proporcionado es mayor.
     *
     * @param id ID del partido a evaluar
     */
    private void actualizarContadorSiNecesario(String id) {
        if (id == null || !id.startsWith("P")) return;

        try {
            // Extraer número del formato P1_1, P2_3, etc.
            String numeroStr = id.substring(1);
            
            // Si contiene guión bajo, extraer la parte numérica completa
            numeroStr = numeroStr.replace("_", "");
            
            int numeroId = Integer.parseInt(numeroStr);
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
     * Sincroniza el contador global al cargar partidos desde almacenamiento.
     *
     * @param todosLosPartidos lista de todos los partidos cargados
     */
    public static void sincronizarContadorGlobal(java.util.List<Partido> todosLosPartidos) {
        if (todosLosPartidos == null || todosLosPartidos.isEmpty()) {
            contadorGlobal.set(1);
            return;
        }

        int maxId = 0;

        for (Partido p : todosLosPartidos) {
            if (p.id != null && p.id.startsWith("P")) {
                try {
                    String numeroStr = p.id.substring(1).replace("_", "");
                    int numeroId = Integer.parseInt(numeroStr);
                    if (numeroId > maxId) {
                        maxId = numeroId;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar IDs mal formados
                }
            }
        }

        contadorGlobal.set(maxId + 1);
        System.out.println("✓ Contador de partidos sincronizado en: P" + 
                         String.format("%02d", maxId + 1)); // ⭐ %02d
    }
    
    
    public static String obtenerProximoId() {
        return String.format("P%02d", contadorGlobal.get()); // ⭐ %02d
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
     * Victoria: 2 puntos, Empate: 1 punto, Derrota o pendiente: 0 puntos.
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
     * Victoria: 2 puntos, Empate: 1 punto, Derrota o pendiente: 0 puntos.
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

    public String getId() { return id; }
    
    public void setId(String id) { 
        this.id = id;
        actualizarContadorSiNecesario(id);
    }

    public Equipo getEquipoLocal() {
    	return equipoLocal; }
    
    public void setEquipoLocal(Equipo equipoLocal) {
    	this.equipoLocal = equipoLocal; }

    public Equipo getEquipoVisitante() { 
    	return equipoVisitante; }
    
    public void setEquipoVisitante(Equipo equipoVisitante) {
    	this.equipoVisitante = equipoVisitante; }

    public int getGolesLocal() { 
    	return golesLocal; }
    
    public void setGolesLocal(int golesLocal) {
    	this.golesLocal = golesLocal; }

    public int getGolesVisitante() { 
    	return golesVisitante; }
    
    public void setGolesVisitante(int golesVisitante) { 
    	this.golesVisitante = golesVisitante; }

    public boolean isFinalizado() {
    	return finalizado; }
    
    public void setFinalizado(boolean finalizado) { 
    	this.finalizado = finalizado; }

    public String getFecha() {
    	return fecha; }
    
    public void setFecha(String fecha) {
    	this.fecha = fecha; }

    
    @Override
    public String toString() {
        return id + ": " + equipoLocal.getNombre() + " " + golesLocal + 
               " - " + golesVisitante + " " + equipoVisitante.getNombre();
    }
}
package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private String rutaEscudo;
    private List<Jugador> plantilla;
    
    public Equipo(String nombre, String rutaEscudo) {
        this.nombre = nombre;
        this.rutaEscudo = (rutaEscudo == null || rutaEscudo.isBlank()) ? "img/default_escudo.png" : rutaEscudo;
        this.plantilla = new ArrayList<>();
    }
    
    public Equipo(String nombre) {
        this(nombre, "img/default_escudo.png");
    }
    
    /**
     * ⭐ MEJORADO: Ficha un jugador verificando que el dorsal sea único
     * @throws IllegalArgumentException si el dorsal ya está ocupado
     */
    public void ficharJugador(Jugador j) {
        if (j == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo");
        }
        
        // Verificar que el jugador no esté ya en la plantilla (por ID)
        if (plantilla.contains(j)) {
            throw new IllegalArgumentException("El jugador ya está en la plantilla");
        }
        
        // ⭐ VALIDACIÓN DE DORSAL ÚNICO
        if (j.getDorsal() > 0) {
            validarDorsalUnico(j.getDorsal(), j.getId());
        }
        
        plantilla.add(j);
    }
    
    /**
     * ⭐ NUEVO: Valida que un dorsal no esté ocupado por otro jugador
     * @param dorsal El número de dorsal a validar
     * @param idJugadorActual El ID del jugador actual (para permitir mantener su propio dorsal)
     * @throws IllegalArgumentException si el dorsal ya está ocupado
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
                    "El dorsal " + dorsal + " ya está ocupado por " + jugadorEnPlantilla.getNombre()
                );
            }
        }
    }
    
    /**
     * ⭐ NUEVO: Verifica si un dorsal está disponible
     */
    public boolean dorsalDisponible(int dorsal) {
        return dorsalDisponible(dorsal, null);
    }
    
    /**
     * ⭐ NUEVO: Verifica si un dorsal está disponible (excepto para un jugador específico)
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
     * ⭐ NUEVO: Obtiene el siguiente dorsal disponible
     */
    public int obtenerSiguienteDorsalDisponible() {
        for (int dorsal = 1; dorsal <= 99; dorsal++) {
            if (dorsalDisponible(dorsal)) {
                return dorsal;
            }
        }
        return -1; // No hay dorsales disponibles
    }
    
    /**
     * ⭐ NUEVO: Obtiene una lista de dorsales disponibles
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
     * Da de baja a un jugador
     */
    public void bajaJugador(Jugador j) {
        plantilla.remove(j);
    }
    
    // Getters y Setters
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getRutaEscudo() { return rutaEscudo; }
    public void setRutaEscudo(String rutaEscudo) { this.rutaEscudo = rutaEscudo; }
    
    public List<Jugador> getPlantilla() { return plantilla; }
    
    @Override
    public String toString() { return nombre; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Equipo)) return false;
        Equipo e = (Equipo) obj;
        return nombre.equalsIgnoreCase(e.nombre);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase());
    }
}
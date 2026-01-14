package gestion;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase Jugador con sistema de ID único e incremental.
 * Los IDs nunca se repiten, incluso si se eliminan jugadores.
 */
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // ⭐ CONTADOR GLOBAL COMPARTIDO - Se mantiene entre sesiones
    private static AtomicInteger contadorGlobal = new AtomicInteger(1);
    
    private String id;              // Identificador único e incremental
    private String nombre;
    private String posicion;
    private int edad;
    private String fotoURL;
    private String nacionalidad;
    private String altura;
    private String peso;
    private int dorsal;
    
    // --- Constructores ---
    
    /**
     * Constructor para compatibilidad con código existente.
     * Genera automáticamente un ID único incremental.
     */
    public Jugador(String nombre, String posicion, int edad, String fotoURL) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.edad = edad;
        this.fotoURL = fotoURL;
        this.id = generarIdUnico();
        this.nacionalidad = "España";
        this.altura = "175cm";
        this.peso = "75kg";
        this.dorsal = 0;
    }
    
    /**
     * Constructor completo para carga desde XML o base de datos.
     */
    public Jugador(String id, String nombre, String posicion, int edad, 
                   String fotoURL, String nacionalidad, String altura, 
                   String peso, int dorsal) {
        this.id = (id == null || id.isBlank()) ? generarIdUnico() : id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.edad = edad;
        this.fotoURL = fotoURL;
        this.nacionalidad = nacionalidad;
        this.altura = altura;
        this.peso = peso;
        this.dorsal = dorsal;
        
        // ⭐ ACTUALIZAR CONTADOR si el ID cargado es mayor
        actualizarContadorSiNecesario(this.id);
    }
    
    /**
     * Genera un ID único incremental que nunca se repite.
     * Formato: JU001, JU002, JU003...
     */
    private String generarIdUnico() {
        int numero = contadorGlobal.getAndIncrement();
        return String.format("JU%05d", numero);
    }
    
    /**
     * Actualiza el contador global si se carga un jugador con ID mayor.
     * Esto asegura que al cargar datos desde archivo, no se generen IDs duplicados.
     */
    private void actualizarContadorSiNecesario(String id) {
        if (id == null || !id.startsWith("JU")) return;
        
        try {
            int numeroId = Integer.parseInt(id.substring(2));
            
            // Actualizar el contador si este ID es mayor o igual
            int valorActual;
            do {
                valorActual = contadorGlobal.get();
                if (numeroId < valorActual) break;
            } while (!contadorGlobal.compareAndSet(valorActual, numeroId + 1));
            
        } catch (NumberFormatException e) {
            // ID no válido, ignorar
        }
    }
    
    /**
     * ⭐ MÉTODO ESTÁTICO: Reinicializa el contador al cargar datos.
     * Debe llamarse desde GestorArchivos después de cargar todos los jugadores.
     */
    public static void sincronizarContadorGlobal(java.util.List<Jugador> todosLosJugadores) {
        if (todosLosJugadores == null || todosLosJugadores.isEmpty()) {
            contadorGlobal.set(1);
            return;
        }
        
        int maxId = 0;
        
        for (Jugador j : todosLosJugadores) {
            if (j.id != null && j.id.startsWith("JU")) {
                try {
                    int numeroId = Integer.parseInt(j.id.substring(2));
                    if (numeroId > maxId) {
                        maxId = numeroId;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar IDs mal formados
                }
            }
        }
        
        contadorGlobal.set(maxId + 1);
        System.out.println("✓ Contador de jugadores sincronizado en: JU" + 
                         String.format("%05d", maxId + 1));
    }
    
    // --- Getters y Setters ---
    
    public String getId() { return id; }
    public void setId(String id) { 
        this.id = id;
        actualizarContadorSiNecesario(id);
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
    
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    
    public String getFotoURL() { return fotoURL; }
    public void setFotoURL(String fotoURL) { this.fotoURL = fotoURL; }
    
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    
    public String getAltura() { return altura; }
    public void setAltura(String altura) { this.altura = altura; }
    
    public String getPeso() { return peso; }
    public void setPeso(String peso) { this.peso = peso; }
    
    public int getDorsal() { return dorsal; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }
    
    // --- Overrides ---
    
    @Override
    public String toString() {
        return nombre + " (#" + dorsal + " - " + posicion + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(id, jugador.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Obtiene el siguiente ID que se generará (útil para debugging)
     */
    public static String obtenerProximoId() {
        return String.format("JU%05d", contadorGlobal.get());
    }
}
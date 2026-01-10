package gestion;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase Jugador integrada con atributos extendidos y soporte para serialización.
 */
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;              // Identificador único
    private String nombre;
    private String posicion;
    private int edad;
    private String fotoURL;         // Puede ser null
    private String nacionalidad;
    private String altura;          // ej: "176cm"
    private String peso;            // ej: "73kg"
    private int dorsal;

    // --- Constructores ---

    /**
     * Constructor para compatibilidad con código existente.
     * Genera automáticamente un ID y valores por defecto para los campos nuevos.
     */
    public Jugador(String nombre, String posicion, int edad, String fotoURL) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.edad = edad;
        this.fotoURL = fotoURL;
        this.id = generarId();
        this.nacionalidad = "España"; // Por defecto
        this.altura = "175cm";
        this.peso = "75kg";
        this.dorsal = 0;
    }

    /**
     * Constructor completo para máxima precisión.
     */
    public Jugador(String id, String nombre, String posicion, int edad, 
                   String fotoURL, String nacionalidad, String altura, 
                   String peso, int dorsal) {
        this.id = (id == null || id.isBlank()) ? generarId() : id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.edad = edad;
        this.fotoURL = fotoURL;
        this.nacionalidad = nacionalidad;
        this.altura = altura;
        this.peso = peso;
        this.dorsal = dorsal;
    }

    /**
     * Genera un ID aleatorio para evitar conflictos.
     */
    private String generarId() {
        return "JU" + String.format("%03d", (int)(Math.random() * 999));
    }

    // --- Getters y Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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
        // Ahora comparamos por ID ya que es el identificador único
        return Objects.equals(id, jugador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
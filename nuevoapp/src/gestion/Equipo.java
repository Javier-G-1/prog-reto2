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

    // Constructor completo
    public Equipo(String nombre, String rutaEscudo) {
        this.nombre = nombre;
        this.rutaEscudo = (rutaEscudo == null || rutaEscudo.isBlank()) ? "img/default_escudo.png" : rutaEscudo;
        this.plantilla = new ArrayList<>();
    }

    // Constructor simplificado
    public Equipo(String nombre) {
        this(nombre, "img/default_escudo.png");
    }

    // --- Gestión de jugadores ---
    public void ficharJugador(Jugador j) {
        // Al usar el nuevo equals de Jugador, esto comparará por ID automáticamente
        if (j != null && !plantilla.contains(j)) {
            plantilla.add(j);
        }
    }

    public void bajaJugador(Jugador j) {
        plantilla.remove(j);
    }

    // --- Getters y Setters ---
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
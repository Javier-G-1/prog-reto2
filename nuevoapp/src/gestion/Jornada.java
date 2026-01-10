package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Jornada implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private List<Partido> listaPartidos;

    public Jornada(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre de la jornada no puede estar vacío.");
        this.nombre = nombre;
        this.listaPartidos = new ArrayList<>();
    }
    /**
     * Requisito Fase 2: Útil para saber si la jornada ha terminado 
     * y poder mostrar avisos en la clasificación.
     */
    public boolean estaCompleta() {
        if (listaPartidos.isEmpty()) return false;
        for (Partido p : listaPartidos) {
            if (!p.isFinalizado()) return false;
        }
        return true;
    }
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

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Partido> getListaPartidos() { return listaPartidos; }
    public void setListaPartidos(List<Partido> listaPartidos) { this.listaPartidos = listaPartidos; }

    @Override
    public String toString() {
        return "Jornada " + nombre;
    }
}
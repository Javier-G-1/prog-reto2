package gestion;

import java.io.Serializable;

public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;

    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private int golesLocal;
    private int golesVisitante;
    private boolean finalizado;
    private String fecha; // <-- atributo que faltaba

    public Partido(Equipo local, Equipo visitante) {
        if (local == null || visitante == null)
            throw new IllegalArgumentException("Los equipos no pueden ser nulos.");
        if (local.equals(visitante))
            throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo.");

        this.equipoLocal = local;
        this.equipoVisitante = visitante;
        this.golesLocal = -1;      // <- indica "sin jugar"
        this.golesVisitante = -1;   // <- indica "sin jugar"
        this.finalizado = false;
        this.fecha = null;
    }

    public String obtenerGanador() {
        if (!finalizado) return "Pendiente";
        if (golesLocal > golesVisitante) return equipoLocal.getNombre();
        if (golesVisitante > golesLocal) return equipoVisitante.getNombre();
        return "Empate";
    }
    
    public int getPuntosLocal() {
        if (!finalizado) return 0;
        if (golesLocal > golesVisitante) return 2; // Victoria
        if (golesLocal == golesVisitante) return 1; // Empate
        return 0; // Derrota
    }

    public int getPuntosVisitante() {
        if (!finalizado) return 0;
        if (golesVisitante > golesLocal) return 2;
        if (golesVisitante == golesLocal) return 1;
        return 0;
    }

    // GETTERS Y SETTERS
    public Equipo getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(Equipo equipoLocal) { this.equipoLocal = equipoLocal; }

    public Equipo getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(Equipo equipoVisitante) { this.equipoVisitante = equipoVisitante; }

    public int getGolesLocal() { return golesLocal; }
    public void setGolesLocal(int golesLocal) { this.golesLocal = golesLocal; }

    public int getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(int golesVisitante) { this.golesVisitante = golesVisitante; }

    public boolean isFinalizado() { return finalizado; }
    public void setFinalizado(boolean finalizado) { this.finalizado = finalizado; }

    public String getFecha() { return fecha; }      // <-- método que faltaba
    public void setFecha(String fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return equipoLocal.getNombre() + " " + golesLocal + " - " + golesVisitante + " " + equipoVisitante.getNombre();
    }
}
package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Temporada implements Serializable {
    public static final String TERMINADA = "TERMINADA";
    public static final String EN_JUEGO = "EN JUEGO";
    public static final String FUTURA = "FUTURA";
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String estado;
    private List<Equipo> equiposParticipantes;
    private List<Jornada> listaJornadas;

    public Temporada(String nombre, String estado) {
        this.nombre = nombre;
        this.estado = estado;
        this.equiposParticipantes = new ArrayList<>();
        this.listaJornadas = new ArrayList<>();
    }
 
    public boolean tieneEquiposSuficientes() {
        return equiposParticipantes != null && equiposParticipantes.size() >= 6;
    }
    // Cambiar estado (Usado por el botón +Jornada)
    public void setEstado(String estado) {
        if (estado.equals(FUTURA) || estado.equals(EN_JUEGO) || estado.equals(TERMINADA)) {
            this.estado = estado;
        }
    }
    public boolean jugadorYaInscrito(Jugador j) {
        for (Equipo e : equiposParticipantes) {
            if (e.getPlantilla().contains(j)) return true;
        }
        return false;
    }
    // Inscribir equipo (Regla: Solo en temporadas FUTURAS)
    public void inscribirEquipo(Equipo e) {
        if (e != null && this.estado.equals(FUTURA)) {
            if (!equiposParticipantes.contains(e)) {
                equiposParticipantes.add(e);
            }
        }
    }// Añade esto en Temporada.java si no lo tienes
    public Equipo buscarEquipoPorNombre(String nombre) {
        for (Equipo e : equiposParticipantes) {
            if (e.getNombre().equals(nombre)) return e;
        }
        return null;
    }

    // Añadir jornada (Usado por GeneradorCalendario)
    public void agregarJornada(Jornada j) {
        if (this.listaJornadas == null) this.listaJornadas = new ArrayList<>();
        if (j != null) this.listaJornadas.add(j);
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getEstado() { return estado; }
    public List<Equipo> getEquiposParticipantes() { return equiposParticipantes; }
    public List<Jornada> getListaJornadas() { return listaJornadas; }

    @Override
    public String toString() {
        return nombre + " [" + estado + "]";
    }
}
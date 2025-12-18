package gestion;

import java.io.Serializable;

/**
  CLASE: Partido

  Representa un enfrentamiento individual entre dos equipos. 
  Guarda el marcador y el estado del juego.

     Árbitro y administrador: tiene permiso para cambiar los 'goles' y 
     marcar el partido como 'finalizado'.
  
  	 Invitado/Manager: Solo pueden leer los datos para ver cómo va la tabla.

   Al ser Serializable, los resultados se guardan permanentemente.
 */
public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;

    //ATRIBUTOS DEL PARTIDO 

    // El equipo que juega en casa. 
    private Equipo equipoLocal;

    // El equipo que juega fuera.
    private Equipo equipoVisitante;

    // Goles marcados por el equipo de casa.
    private int golesLocal;

    //Goles marcados por el equipo de fuera. 
    private int golesVisitante;

    /**  Indica si el partido ya se jugó. 
      Si es 'false', el partido aparece como "Pendiente". 
      Si es 'true', el marcador es definitivo.
     */
    private boolean finalizado;

    /**
      CONSTRUCTOR DE PARTIDO
     Se crea un partido inicialmente con 0 goles y como no finalizado.
      local     Objeto Equipo que juega de local.
      visitante Objeto Equipo que juega de visitante.
     */
    public Partido(Equipo local, Equipo visitante) {
        this.equipoLocal = local;
        this.equipoVisitante = visitante;
        this.golesLocal = 0;
        this.golesVisitante = 0;
        this.finalizado = false;
    }

    // MÉTODOS DE LÓGICA

    /**
      Devuelve el nombre del ganador o "Empate".
      Para generar la tabla de clasificación.
     */
    public String obtenerGanador() {
        if (!finalizado) return "Pendiente";
        if (golesLocal > golesVisitante) return equipoLocal.getNombre();
        if (golesVisitante > golesLocal) return equipoVisitante.getNombre();
        return "Empate";
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

    // Muestra el resumen del partido (ej: "Barcelona 2 - 1 Sevilla").
    @Override
    public String toString() {
        return equipoLocal + " " + golesLocal + " - " + golesVisitante + " " + equipoVisitante;
    }
}
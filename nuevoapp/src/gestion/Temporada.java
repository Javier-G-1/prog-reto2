package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
  CLASE: Temporada

  Es el contenedor principal de una temporada específico 
  (ej: "Temporada 2025/2026"). 

  Agrupa las jornadas y los equipos que compiten esa temporada.
  Roles: El Administrador crea la temporada, y el Árbitro accede a sus 
  partidos para modificarlos.

 Al guardarse esta clase, se guarda todo el calendario de partidos y 
  resultados de ese año automáticamente.
 */
public class Temporada implements Serializable {

    private static final long serialVersionUID = 1L;

    //ATRIBUTOS DE LA TEMPORADA

    //Nombre descriptivo (ej: "Liga Nacional 2025").
    private String nombre;

    /** Estado de la temporada. 
      Puede ser: "Futuro", "En Curso" o "Terminado".
     */
    private String estado;

  
    private List<Equipo> equiposParticipantes;

    /**  CALENDARIO DE JORNADAS: 
      Una temporada se divide en varias jornadas (Jornada 1, Jornada 2, etc.).
      Cada jornada contendrá sus respectivos partidos.
     */
    private List<Jornada> listaJornadas;

    /**
     * CONSTRUCTOR DE TEMPORADA
      nombre Nombre de la competición.
      estado Estado inicial de la temporada
     */
    public Temporada(String nombre, String estado) {
        this.nombre = nombre;
        this.estado = estado;
        // Inicializamos las listas para que el Administrador pueda ir añadiendo datos.
        this.equiposParticipantes = new ArrayList<>();
        this.listaJornadas = new ArrayList<>();
    }

    //MÉTODOS DE GESTIÓN

    //Permite añadir un equipo a la competición de este año.
     
    public void inscribirEquipo(Equipo e) {
        this.equiposParticipantes.add(e);
    }

    // Permite añadir una nueva jornada de partidos al calendario.
    
    public void agregarJornada(Jornada j) {
        this.listaJornadas.add(j);
    }

    // GETTERS Y SETTERS 

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Equipo> getEquiposParticipantes() {
        return equiposParticipantes;
    }

    public List<Jornada> getListaJornadas() {
        return listaJornadas;
    }

    // Se usa para mostrar el nombre en el desplegable de la VentanaPrincipal.
     
    @Override
    public String toString() {
        return this.nombre + " (" + this.estado + ")";
    }
}
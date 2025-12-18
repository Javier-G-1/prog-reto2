package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
  CLASE: Jornada

  Representa una fecha específica de la competición (ej: "Jornada 1", "Final").
  Sirve para organizar los partidos en grupos manejables.

	 Árbitro: Es su principal herramienta de navegación.
	 Administrador: Es quien genera estas jornadas al crear el calendario.

   	 Al guardar la Jornada, se guardan todos los objetos 'Partido' que contiene.
 */
public class Jornada implements Serializable {
    private static final long serialVersionUID = 1L;

    //ATRIBUTOS DE LA JORNADA

    // Número o nombre de la jornada (ej: "1" o "Semifinal"). 
    private String nombre;

 
     // Contiene todos los enfrentamientos que ocurren en esta jornada específica.
     
    private List<Partido> listaPartidos;

    /**
      CONSTRUCTOR DE JORNADA
      nombre: El nombre o número identificador de la jornada.
     */
    public Jornada(String nombre) {
        this.nombre = nombre;
        // Inicializamos la lista de partidos para poder empezar a añadir enfrentamientos.
        this.listaPartidos = new ArrayList<>();
    }

    //MÉTODOS DE GESTIÓN
    /**
       Añade un nuevo enfrentamiento a esta jornada.
       El objeto Partido ya configurado con equipo local y visitante.
     */
    public void agregarPartido(Partido p) {
        this.listaPartidos.add(p);
    }

    //GETTERS Y SETTERS

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Partido> getListaPartidos() {
        return listaPartidos;
    }

    public void setListaPartidos(List<Partido> listaPartidos) {
        this.listaPartidos = listaPartidos;
    }

    //Facilita la visualización en JLists o JComboBoxes en la interfaz.
   
    @Override
    public String toString() {
        return "Jornada " + nombre;
    }
}
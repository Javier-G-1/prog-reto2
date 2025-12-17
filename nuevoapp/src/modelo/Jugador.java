package modelo;

import java.io.Serializable;

/**
 * CLASE: Jugador

  Esta es la clase que define a un jugador. 
 
  Como los jugadores cambian de equipo cada temporada, esta clase es INDEPENDIENTE.
  El jugador no "pertenece" a una clase equipo de forma fija, sino que la 
  aplicación lo asocia a un equipo según la temporada.
   
  Es imprescindible que sea Serializable para que, al exportar a XML o guardar
  los datos, no se pierda la información de los jugadores.
 */
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;

    // ATRIBUTOS DEL JUGADOR 

    /**  Nombre completo del jugador. 
      Aquí irían nombres como "Álvaro Mena" o "Sofía Llorente".
     */
    private String nombre;

    //Posición en el campo de balonmano (Portero, Extremo, Pivote, etc.).

     
    private String posicion;

    /**
      CONSTRUCTOR DE JUGADOR
      Se usa para crear cada una de las fichas de los jugadores de la lista.
       nombre:   Nombre y apellidos del jugador.
       posicion: Puesto que ocupa en el campo.
     */
    public Jugador(String nombre, String posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
    }

    //MÉTODOS DE ACCESO (GETTERS Y SETTERS)
    // Permiten leer y modificar los datos de los jugadores de forma segura.

    // Obtiene el nombre del jugador para mostrarlo en las tarjetas de equipo.
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Obtiene la posición (útil para el Manager al organizar el equipo).
    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    /**
      MÉTODO toString()
      Se redefine para que, al mostrar al jugador en un listado, 
      aparezca su nombre directamente.
     */
    @Override
    public String toString() {
        return this.nombre;
    }
    
    /**
      MÉTODO equals()
      sirve para comparar si dos jugadores son el mismo.
      Se usa cuando un jugador cambia de equipo para saber exactamente 
      a quién estamos moviendo de la "raíz".
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Jugador jugador = (Jugador) obj;
        return nombre.equals(jugador.nombre);
    }
}
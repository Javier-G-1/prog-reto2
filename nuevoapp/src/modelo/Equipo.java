package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
  CLASE: Equipo
  Representa a un club de balonmano (ej: Barcelona, Athletic Club).

  Los jugadores cambian de equipo cada nuava temporada. Por eso, 
  esta clase tiene una lista de jugadores que son simplemente "referencias" 
  a los jugadores que están en la raíz global.
  
 */
public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;

    // ATRIBUTOS DEL EQUIPO
    //Nombre oficial del equipo (ej: "Barcelona"). 
    private String nombre;
    
    /**  Ruta del archivo de imagen del escudo. 
      El Manager podrá cambiar esto desde la interfaz.
     */
    private String rutaEscudo;

    /**  Jugadores actuales: 
      Esta es la lista de jugadores que pertenecen al equipo ESTA temporada.
      Al ser una lista de objetos 'Jugador', si un jugador cambia de equipo, 
      solo hay que borrarlo de esta lista y añadirlo a la del nuevo equipo.
     */
    private List<Jugador> plantilla;

    /**
      CONSTRUCTOR DE EQUIPO
      Se usa para registrar un nuevo club en la federación.
       nombre      Nombre del club.
       rutaEscudo  Ruta inicial del logo/escudo.
     */
    public Equipo(String nombre, String rutaEscudo) {
        this.nombre = nombre;
        this.rutaEscudo = rutaEscudo;
        // Inicializamos la lista vacía para evitar errores al añadir jugadores después.
        this.plantilla = new ArrayList<>();
    }

    // MÉTODOS DE GESTIÓN DE PLANTILLA

    // Añade un jugador de la "raíz" a este equipo.
  
    public void ficharJugador(Jugador j) {
        if (!plantilla.contains(j)) {
            plantilla.add(j);
        }
    }

    /**
      Elimina un jugador del equipo (útil cuando acaba la temporada o hay un traspaso).
      El jugador NO desaparece de la aplicación, solo deja de estar en este equipo.
     */
    public void bajaJugador(Jugador j) {
        plantilla.remove(j);
    }

    // GETTERS Y SETTERS

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRutaEscudo() {
        return rutaEscudo;
    }

    public void setRutaEscudo(String rutaEscudo) {
        this.rutaEscudo = rutaEscudo;
    }

    public List<Jugador> getPlantilla() {
        return plantilla;
    }

    // Para mostrar el nombre del equipo en tablas y menús.
     
    @Override
    public String toString() {
        return this.nombre;
    }
}
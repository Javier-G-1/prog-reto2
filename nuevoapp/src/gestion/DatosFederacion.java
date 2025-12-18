package gestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
  CLASE: DatosFederacion

  Es el objeto central de TODA la aplicación. En lugar de guardar 20 archivos 
  distintos, guardamos solo este objeto que contiene todas las listas dentro.
  ESTRUCTURA DE "RAÍZ":
  Aquí es donde vive la lista global de jugadores, permitiendo
  que existan independientemente de los equipos.
 
  Debe ser Serializable porque es el objeto principal que el 'GestorArchivos'
  escribirá en el disco duro.
 */
public class DatosFederacion implements Serializable {

    // Identificador único para la serialización.
    private static final long serialVersionUID = 1L;

    // LISTAS MAESTRAS

    //Lista de todos los usuarios (Admin, Manager, Árbitro, Invitado). 
    private List<Usuario> listaUsuarios;

    // LA RAÍZ: Aquí viven todos los jugadores de la lista (Álvaro, Carla, etc.).
    // Es la base de datos global de personas de la federación.
   
    private List<Jugador> todosLosJugadores;

    // Lista de todos los clubes registrados (Barcelona, Sevilla, etc.). 
    private List<Equipo> listaEquipos;

    // Historial de todas las temporadas creadas por el Administrador.
    private List<Temporada> listaTemporadas;

    /**
      CONSTRUCTOR: Inicialización 
      Es fundamental inicializar las listas como 'ArrayList' para que al 
      abrir la app por primera vez no nos dé errores de "lista nula".
     */
    public DatosFederacion() {
        this.listaUsuarios = new ArrayList<>();
        this.todosLosJugadores = new ArrayList<>();
        this.listaEquipos = new ArrayList<>();
        this.listaTemporadas = new ArrayList<>();
    }

    // MÉTODOS DE ACCESO (GETTERS)
    // Solo necesitamos Getters porque las listas se manipulan directamente 
    // añadiendo o quitando elementos (add/remove).

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public List<Jugador> getTodosLosJugadores() {
        return todosLosJugadores;
    }

    public List<Equipo> getListaEquipos() {
        return listaEquipos;
    }

    public List<Temporada> getListaTemporadas() {
        return listaTemporadas;
    }

    /**
      MÉTODO DE APOYO: buscarUsuario
      Útil para la pantalla de Login. Recorre la lista de usuarios buscando 
      una coincidencia de nombre.
     */
    public Usuario buscarUsuario(String nombreLogin) {
        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(nombreLogin)) {
                return u;
            }
        }
        return null; // Si no lo encuentra, devuelve nulo.
    }
}
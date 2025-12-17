package modelo;

import java.io.Serializable;

/**
 Al incluir implements Serializable, 
nos aseguramos de que cuando guardes los datos de la federación 
en un archivo, el rol de cada usuario se guarde correctamente. 
 */


public enum Rol implements Serializable {
    
    // Solo puede ver equipos y jugadores. No ve el botón "Gestionar".
    INVITADO("Invitado"),
    
    // Puede modificar jornadas y partidos únicamente.
    ARBITRO("Árbitro"),
    
    // Puede modificar equipos, jugadores, escudos y logos.
    MANAGER("Manager"),
    
    // Tiene control total: gestión de usuarios, temporadas y todo lo anterior.
    ADMINISTRADOR("Administrador");

    private final String nombreLegible;

    // Constructor del enum
    Rol(String nombreLegible) {
        this.nombreLegible = nombreLegible;
    }

    /**
     * @return El nombre del rol con formato para mostrar en la interfaz (UI).
     */
    public String getNombreLegible() {
        return nombreLegible;
    }
}
package logica;

import gestion.*;

public class GestorTemporadas {

    /**
     * Finaliza la temporada actual y activa la siguiente.
     */
    public void finalizarTemporada(Temporada actual, Temporada siguiente, DatosFederacion datos) {
        if (actual == null || siguiente == null || datos == null) return;

        System.out.println("--- INICIANDO CIERRE DE TEMPORADA ---");

        // 1. Cambiar estados
        actual.setEstado(Temporada.TERMINADA);
        siguiente.setEstado(Temporada.EN_JUEGO);

        // 2. Reiniciar plantillas
        for (Equipo e : datos.getListaEquipos()) {
            e.getPlantilla().clear();
        }
        System.out.println("LOG: Equipos vaciados. Jugadores listos para nuevos fichajes.");

        // 3. Sincronización web
        SincronizadorWeb.actualizarActivosServidor(datos, siguiente.getNombre());

        // 4. Exportación XML
        ExportadorWebXML.generarXMLParaWeb(datos, "sincronizacion_web.xml");

        System.out.println("LOG: " + actual.getNombre() + " cerrada. " + siguiente.getNombre() + " activa.");
    }

    /**
     * Crea una nueva temporada futura.
     */
    public void crearTemporadaFutura(String nombre, DatosFederacion datos) {
        if (nombre == null || nombre.isBlank() || datos == null) return;
        Temporada nueva = new Temporada(nombre, Temporada.FUTURA);
        datos.getListaTemporadas().add(nueva);
    }
 // Dentro de GestorTemporadas.java
    public void inscribirEquipoEnTemporada(String nombreT, String nombreE, DatosFederacion datos) {
        Temporada t = datos.buscarTemporadaPorNombre(nombreT);
        if (t != null) {if (t == null || !t.getEstado().equals(Temporada.FUTURA)) {
            System.out.println("ERROR: No se pueden inscribir equipos en una temporada ya iniciada o terminada.");
            return; 
        }
            // Buscamos si el equipo ya existe en el club (lista maestra)
            Equipo equipoARegistrar = null;
            for(Equipo e : datos.getListaEquipos()){
                if(e.getNombre().equalsIgnoreCase(nombreE)) equipoARegistrar = e;
            }

            // Si es un club nuevo, lo creamos
            if (equipoARegistrar == null) {
                equipoARegistrar = new Equipo(nombreE, null);
                datos.getListaEquipos().add(equipoARegistrar);
            }
            
            // Lo inscribimos en la temporada específica
            t.inscribirEquipo(equipoARegistrar);
        }
    }
    /**
     * Crea el escenario inicial: una temporada cerrada con 6 equipos y calendario,
     * y una temporada actual lista para jugar.
     */
    public void prepararEscenarioInicial(DatosFederacion datos) {
        if (datos == null) return;

        // 1. CREAR TEMPORADA PASADA
        String nombreT1 = "Temporada 2024/25";
        crearTemporadaFutura(nombreT1, datos); 
        Temporada t1 = datos.buscarTemporadaPorNombre(nombreT1);
        
        if (t1 != null) {
            t1.setEstado(Temporada.TERMINADA); 

            // 2. AQUÍ ESTABA EL ERROR: Definimos la variable 'nombres'
            String[] nombres = {"Barcelona", "Granada", "Sevilla", "Zaragoza", "Valencia", "Athletic Club"};

            // Ahora el bucle ya sabe qué es 'nombres'
            for (String nombreE : nombres) {
                inscribirEquipoEnTemporada(nombreT1, nombreE, datos);
                
                // Lógica para añadir jugadores de prueba
                Equipo eq = t1.buscarEquipoPorNombre(nombreE);
                if (eq != null) {
                    eq.ficharJugador(new Jugador("Capitán " + nombreE, "Cierre", 28, null));
                    eq.ficharJugador(new Jugador("Portero " + nombreE, "Portero", 24, null));
                }
            }
        }
    }}

package logica;

import gestion.*;

public class GestorTemporadas {

    /**
     * Finaliza la temporada actual y activa la siguiente.
     */
    public void finalizarTemporada(Temporada actual, Temporada siguiente, DatosFederacion datos) {
        if (actual == null || siguiente == null || datos == null) {
            GestorLog.error("Parámetros nulos en finalizarTemporada");
            return;
        }

        GestorLog.info("--- INICIANDO CIERRE DE TEMPORADA: " + actual.getNombre() + " ---");

        // 1. Cambiar estados
        String estadoAnteriorActual = actual.getEstado();
        String estadoAnteriorSiguiente = siguiente.getEstado();
        
        actual.setEstado(Temporada.TERMINADA);
        siguiente.setEstado(Temporada.EN_JUEGO);
        
        GestorLog.info("Estado actualizado - " + actual.getNombre() + ": " + 
                      estadoAnteriorActual + " → " + Temporada.TERMINADA);
        GestorLog.info("Estado actualizado - " + siguiente.getNombre() + ": " + 
                      estadoAnteriorSiguiente + " → " + Temporada.EN_JUEGO);

        // 2. Reiniciar plantillas
        int jugadoresLiberados = 0;
        for (Equipo e : datos.getListaEquipos()) {
            jugadoresLiberados += e.getPlantilla().size();
            e.getPlantilla().clear();
        }
        GestorLog.info("Plantillas vaciadas - Jugadores liberados: " + jugadoresLiberados);

        // 3. Sincronización web
        SincronizadorWeb.actualizarActivosServidor(datos, siguiente.getNombre());

        // 4. Exportación XML
        ExportadorWebXML.generarXMLParaWeb(datos, "sincronizacion_web.xml");

        GestorLog.exito("Cierre de temporada completado - " + actual.getNombre() + 
                       " finalizada, " + siguiente.getNombre() + " activa");
    }

    /**
     * Crea una nueva temporada futura.
     */
    public void crearTemporadaFutura(String nombre, DatosFederacion datos) {
        if (nombre == null || nombre.isBlank() || datos == null) {
            GestorLog.error("Parámetros inválidos para crear temporada");
            return;
        }
        
        Temporada nueva = new Temporada(nombre, Temporada.FUTURA);
        datos.getListaTemporadas().add(nueva);
        
        GestorLog.exito("Temporada creada: " + nombre + " | Estado: FUTURA");
    }

    /**
     * Inscribe un equipo en una temporada específica
     */
    public void inscribirEquipoEnTemporada(String nombreT, String nombreE, DatosFederacion datos) {
        Temporada t = datos.buscarTemporadaPorNombre(nombreT);
        
        if (t == null) {
            GestorLog.error("Temporada no encontrada: " + nombreT);
            return;
        }
        
        // Verificar el estado de la temporada
        if (!t.getEstado().equals(Temporada.FUTURA)) {
            GestorLog.advertencia("Intento de inscribir equipo '" + nombreE + 
                                "' en temporada " + t.getEstado() + ": " + nombreT);
            System.out.println("ERROR: No se pueden inscribir equipos en una temporada ya iniciada o terminada.");
            return;
        }
        
        // Buscar si el equipo ya existe en el club (lista maestra)
        Equipo equipoARegistrar = null;
        for(Equipo e : datos.getListaEquipos()) {
            if(e.getNombre().equalsIgnoreCase(nombreE)) {
                equipoARegistrar = e;
                break;
            }
        }

        // Si es un club nuevo, lo creamos
        if (equipoARegistrar == null) {
            equipoARegistrar = new Equipo(nombreE, null);
            datos.getListaEquipos().add(equipoARegistrar);
            GestorLog.info("Nuevo club registrado en la federación: " + nombreE);
        }

        // Lo inscribimos en la temporada específica
        t.inscribirEquipo(equipoARegistrar);
        GestorLog.exito("Equipo inscrito: " + nombreE + " → Temporada: " + nombreT);
        
        
    }// En GestorTemporadas o donde prefieras
    public void asignarDorsalesAutomaticos(Temporada t) {
        for (Equipo eq : t.getEquiposParticipantes()) {
            int dorsal = 1;
            for (Jugador j : eq.getPlantilla()) {
                if (j.getDorsal() == 0) {
                    j.setDorsal(dorsal++);
                }
            }
        }
    }

    /**
     * Crea el escenario inicial con temporadas y equipos de prueba
     */
    public void prepararEscenarioInicial(DatosFederacion datos) {
        if (datos == null) {
            GestorLog.error("DatosFederacion es nulo en prepararEscenarioInicial");
            return;
        }

        GestorLog.info("=== PREPARANDO ESCENARIO INICIAL ===");

        // 1. CREAR TEMPORADA PASADA
        String nombreT1 = "Temporada 2024/25";
        crearTemporadaFutura(nombreT1, datos);
        Temporada t1 = datos.buscarTemporadaPorNombre(nombreT1);

        if (t1 != null) {
            t1.setEstado(Temporada.TERMINADA);
            GestorLog.info("Temporada de prueba configurada como TERMINADA: " + nombreT1);

            String[] nombres = {"Barcelona", "Granada", "Sevilla", "Zaragoza", "Valencia", "Athletic Club"};
            
            int equiposInscritos = 0;
            int jugadoresCreados = 0;

            for (String nombreE : nombres) {
                inscribirEquipoEnTemporada(nombreT1, nombreE, datos);
                equiposInscritos++;

                // Añadir jugadores de prueba
                Equipo eq = t1.buscarEquipoPorNombre(nombreE);
                if (eq != null) {
                    eq.ficharJugador(new Jugador("Capitán " + nombreE, "Cierre", 28, null));
                    eq.ficharJugador(new Jugador("Portero " + nombreE, "Portero", 24, null));
                    jugadoresCreados += 2;
                }
            }
            
            GestorLog.exito("Escenario inicial preparado - Temporada: " + nombreT1 + 
                          " | Equipos: " + equiposInscritos + " | Jugadores: " + jugadoresCreados);
        }
    }
    
}
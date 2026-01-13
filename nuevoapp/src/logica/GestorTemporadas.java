package logica;

import gestion.*;

public class GestorTemporadas {

    /**
     * Finaliza la temporada actual y activa la siguiente.
     * ⭐ ACTUALIZADO: Ahora exporta solo la temporada finalizada a general.xml
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

        // 4. Exportación XML - ⭐ ACTUALIZADO: Exporta solo la temporada finalizada a general.xml
        ExportadorXML exportador = new ExportadorXML(datos);
        boolean exportacionExitosa = exportador.exportarTemporada(actual);
        
        if (exportacionExitosa) {
            GestorLog.exito("Cierre de temporada completado - " + actual.getNombre() + 
                           " finalizada y exportada a general.xml | " + siguiente.getNombre() + " activa");
        } else {
            GestorLog.advertencia("Cierre de temporada completado pero hubo un problema con la exportación");
        }
    }

    /**
     * ⭐ MEJORADO: Crea una nueva temporada futura copiando de una temporada específica.
     * IMPORTANTE: Solo se puede crear si NO hay ninguna temporada EN_JUEGO.
     * @param temporadaOrigen La temporada desde la cual copiar (puede ser null para crear vacía)
     * @return true si se creó exitosamente, false si no se pudo crear
     */
    public boolean crearTemporadaFutura(String nombre, DatosFederacion datos, Temporada temporadaOrigen) {
        if (nombre == null || nombre.isBlank() || datos == null) {
            GestorLog.error("Parámetros inválidos para crear temporada");
            return false;
        }
        
        // ⭐ VALIDACIÓN CRÍTICA: No crear si hay temporada EN_JUEGO
        if (existeTemporadaEnCurso(datos)) {
            Temporada enCurso = obtenerTemporadaEnCurso(datos);
            GestorLog.advertencia("Intento de crear temporada con " + enCurso.getNombre() + " EN_JUEGO");
            return false;
        }
        
        // Crear la nueva temporada FUTURA
        Temporada nueva = new Temporada(nombre, Temporada.FUTURA);
        
        // Si hay temporada origen, copiar sus datos
        if (temporadaOrigen != null) {
            GestorLog.info("Copiando datos desde: " + temporadaOrigen.getNombre());
            
            int equiposCopiados = 0;
            int jugadoresCopiados = 0;
            
            // Copiar equipos y jugadores de la temporada origen
            for (Equipo equipoOriginal : temporadaOrigen.getEquiposParticipantes()) {
                // Crear una COPIA del equipo (no el mismo objeto)
                Equipo equipoNuevo = new Equipo(equipoOriginal.getNombre());
                equipoNuevo.setRutaEscudo(equipoOriginal.getRutaEscudo()); // Mantener escudo
                
                // Copiar todos los jugadores del equipo
                for (Jugador jugadorOriginal : equipoOriginal.getPlantilla()) {
                    // Crear una COPIA del jugador (no el mismo objeto)
                    Jugador jugadorNuevo = new Jugador(
                        jugadorOriginal.getNombre(),
                        jugadorOriginal.getPosicion(),
                        jugadorOriginal.getEdad(),
                        jugadorOriginal.getFotoURL()
                    );
                    
                    // Copiar atributos adicionales
                    jugadorNuevo.setDorsal(jugadorOriginal.getDorsal());
                    jugadorNuevo.setNacionalidad(jugadorOriginal.getNacionalidad());
                    jugadorNuevo.setAltura(jugadorOriginal.getAltura());
                    jugadorNuevo.setPeso(jugadorOriginal.getPeso());
                    
                    equipoNuevo.ficharJugador(jugadorNuevo);
                    jugadoresCopiados++;
                }
                
                nueva.inscribirEquipo(equipoNuevo);
                equiposCopiados++;
            }
            
            GestorLog.exito("Temporada creada: " + nombre + " | Estado: FUTURA | " +
                          "Equipos copiados: " + equiposCopiados + " | " +
                          "Jugadores copiados: " + jugadoresCopiados + " | " +
                          "Origen: " + temporadaOrigen.getNombre());
        } else {
            GestorLog.info("Temporada creada vacía (sin temporada origen)");
            GestorLog.exito("Temporada creada: " + nombre + " | Estado: FUTURA | Sin equipos");
        }
        
        // Añadir la nueva temporada a la federación
        datos.getListaTemporadas().add(nueva);
        return true;
    }
    
    /**
     * ⭐ NUEVO: Obtiene todas las temporadas FINALIZADAS
     */
    public java.util.List<Temporada> obtenerTemporadasFinalizadas(DatosFederacion datos) {
        java.util.List<Temporada> finalizadas = new java.util.ArrayList<>();
        
        if (datos != null) {
            for (Temporada t : datos.getListaTemporadas()) {
                if (t.getEstado().equals(Temporada.TERMINADA)) {
                    finalizadas.add(t);
                }
            }
        }
        
        return finalizadas;
    }
    
    /**
     * ⭐ NUEVO: Obtiene la última temporada que esté FINALIZADA
     */
    public Temporada obtenerUltimaTemporadaFinalizada(DatosFederacion datos) {
        if (datos == null || datos.getListaTemporadas().isEmpty()) {
            return null;
        }
        
        Temporada ultimaFinalizada = null;
        
        // Recorrer todas las temporadas para encontrar la última finalizada
        for (Temporada t : datos.getListaTemporadas()) {
            if (t.getEstado().equals(Temporada.TERMINADA)) {
                // Si no hay ninguna o esta es más reciente (está más al final de la lista)
                ultimaFinalizada = t;
            }
        }
        
        return ultimaFinalizada;
    }

    /**
     * ⭐ NUEVO: Verifica si ya existe una temporada EN_JUEGO
     */
    public boolean existeTemporadaEnCurso(DatosFederacion datos) {
        if (datos == null) return false;
        
        for (Temporada t : datos.getListaTemporadas()) {
            if (t.getEstado().equals(Temporada.EN_JUEGO)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ⭐ NUEVO: Obtiene la temporada que está EN_JUEGO (si existe)
     */
    public Temporada obtenerTemporadaEnCurso(DatosFederacion datos) {
        if (datos == null) return null;
        
        for (Temporada t : datos.getListaTemporadas()) {
            if (t.getEstado().equals(Temporada.EN_JUEGO)) {
                return t;
            }
        }
        return null;
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
    }

    /**
     * Asigna dorsales automáticos
     */
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

        // 1. CREAR TEMPORADA PASADA (sin origen porque es la primera)
        String nombreT1 = "Temporada 2024/25";
        crearTemporadaFutura(nombreT1, datos, null);
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
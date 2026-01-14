package logica;

import gestion.*;


/**
 * <h2>Gestor de Temporadas</h2>
 * <p>
 * Clase encargada de la gestión de temporadas de la federación:
 * finalizar temporadas, crear nuevas, inscribir equipos,
 * asignar dorsales y preparar escenarios iniciales de prueba.
 * </p>
 */
public class GestorTemporadas {

	  /**
     * Finaliza la temporada actual y activa la siguiente.
     * <p>
     * Cambia los estados de las temporadas, vacía las plantillas
     * y exporta la temporada finalizada a XML.
     * </p>
     * 
     * @param actual  Temporada que se va a finalizar
     * @param siguiente Temporada que se va a activar
     * @param datos  Datos de la federación
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
     * Crea una nueva temporada futura.
     * <p>
     * Solo se puede crear si no hay ninguna temporada en curso.
     * Si se proporciona una temporada origen, se copian los equipos y jugadores.
     * </p>
     * 
     * @param nombre Nombre de la nueva temporada
     * @param datos Datos de la federación
     * @param temporadaOrigen Temporada desde la cual copiar datos (puede ser null)
     * @return true si se creó correctamente, false en caso contrario
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
     * Obtiene todas las temporadas que estén finalizadas.
     *
     * @param datos Datos de la federación
     * @return Lista de temporadas finalizadas
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
     * Obtiene la última temporada finalizada.
     *
     * @param datos Datos de la federación
     * @return La última temporada finalizada o null si no hay
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
     * Verifica si ya existe una temporada en curso.
     *
     * @param datos Datos de la federación
     * @return true si hay una temporada EN_JUEGO, false en caso contrario
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
     * Obtiene la temporada que está actualmente en curso.
     *
     * @param datos Datos de la federación
     * @return Temporada en curso o null si no existe
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
     * Inscribe un equipo en una temporada específica.
     * <p>
     * Solo se pueden inscribir equipos en temporadas FUTURAS.
     * Si el equipo no existe en la federación, se crea.
     * </p>
     *
     * @param nombreT Nombre de la temporada
     * @param nombreE Nombre del equipo
     * @param datos Datos de la federación
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
     * Asigna dorsales automáticamente a los jugadores de cada equipo en una temporada.
     *
     * @param t Temporada cuyos jugadores recibirán dorsales
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
     * Crea un escenario inicial con temporadas y equipos de prueba.
     * <p>
     * Solo se ejecuta si no hay temporadas existentes en los datos.
     * </p>
     *
     * @param datos Datos de la federación
     */
    public void prepararEscenarioInicial(DatosFederacion datos) {
    	 if (!datos.getListaTemporadas().isEmpty()) {
    	        GestorLog.info("Datos ya cargados desde archivo. No se crea escenario inicial.");
    	        return;
    	    }
    	    
    	    // Solo crear escenario si NO hay temporadas (primera ejecución)
    	    GestorLog.info("Primera ejecución detectada. Creando escenario inicial...");
    	    
    	    // Crear UNA SOLA temporada inicial
    	    Temporada temporada2025_26 = new Temporada("Temporada 2025/26", Temporada.FUTURA);
    	    
    	    // Equipos de ejemplo
    	    String[][] equiposConJugadores = {
    	        {"Barcelona","Álvaro Mena","Carla Ríos","Ignacio Vela","Sofía Llorente","Mateo Cruz"},
    	        {"Granada","Carlos Muñoz","Marta Domínguez","Andrés Cortés","Lucía Palacios"},
    	        {"Sevilla","Marina Torres","Fernando Vázquez","Ana Beltrán","Rubén Márquez"},
    	        {"Zaragoza","Miguel Ortega","Claudia Rivas","Javier Torres","Isabel Salinas"},
    	        {"Valencia","Raúl Pérez","Andrea Delgado","Luis Navarro","Marta Ramírez"},
    	        {"Athletic Club","Pablo Martínez","Alicia Gómez","Daniel Reyes","Elena López"}
    	    };
    	    
    	    // Crear equipos con jugadores
    	    for (String[] equipoData : equiposConJugadores) {
    	        Equipo eq = new Equipo(equipoData[0], null);
    	        for (int i = 1; i < equipoData.length; i++) {
    	            Jugador j = new Jugador(equipoData[i], "Posición", 25, null);
    	            eq.ficharJugador(j);
    	        }
    	        temporada2025_26.inscribirEquipo(eq);
    	    }
    	    
    	    // Agregar la temporada a los datos
    	    datos.add(temporada2025_26);
    	    
    	    GestorLog.exito("Escenario inicial creado: Temporada 2025/26 con " + 
    	                   equiposConJugadores.length + " equipos");
    	}
}
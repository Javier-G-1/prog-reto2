package logica;

import gestion.*;
import java.util.*;

/**
 * Clase responsable de generar el calendario de partidos de una temporada.
 * Utiliza el algoritmo Round-Robin para crear las jornadas de ida y vuelta.
 *
 * <p>Reglas principales:
 * <ul>
 *     <li>Mínimo de 6 equipos REALES para generar calendario.</li>
 *     <li>Excluye automáticamente el equipo fantasma "_SIN_EQUIPO_".</li>
 *     <li>Si el número de equipos es impar, se agrega un equipo fantasma "DESCANSA".</li>
 *     <li>Se generan jornadas de ida y vuelta automáticamente.</li>
 *     <li>⭐ Los IDs de jornadas se reinician (J001) para cada temporada.</li>
 * </ul>
 * </p>
 */
public class GeneradorCalendario {

    /**
     * Genera el calendario completo para la temporada especificada.
     * Crea jornadas de ida y vuelta según el sistema Round-Robin.
     * 
     * <p><b>⭐ IMPORTANTE:</b> 
     * <ul>
     *   <li>Filtra automáticamente el equipo "_SIN_EQUIPO_" del calendario.</li>
     *   <li>Reinicia los IDs de jornadas a J001 para cada temporada.</li>
     * </ul>
     * </p>
     *
     * @param temp La temporada donde se generará el calendario (no nula)
     * @throws IllegalArgumentException Si la temporada es nula
     * @throws Exception Si el número de equipos REALES es menor al mínimo permitido (6)
     */
    public static void crearCalendario(Temporada temp) throws Exception {
        if (temp == null) {
            GestorLog.error("Intento de generar calendario con temporada nula");
            throw new IllegalArgumentException("La temporada no puede ser nula.");
        }

        // ⭐ CRÍTICO: Reiniciar contadores de IDs para esta nueva temporada
        Jornada.reiniciarContador();
        Partido.reiniciarContador(); // También reiniciar partidos si existe este método
        
        GestorLog.info(" IDs reiniciados para nueva temporada: " + temp.getNombre());

        // ⭐ FILTRAR EQUIPOS REALES (excluir "_SIN_EQUIPO_")
        List<Equipo> todosLosEquipos = temp.getEquiposParticipantes();
        List<Equipo> participantes = new ArrayList<>();
        
        for (Equipo eq : todosLosEquipos) {
            if (!eq.getNombre().equals("_SIN_EQUIPO_")) {
                participantes.add(eq);
            }
        }

        GestorLog.info("Equipos filtrados para calendario: " + participantes.size() + 
                      " (Total en temporada: " + todosLosEquipos.size() + ")");

        // ⭐ VALIDACIÓN: Mínimo 6 equipos REALES
        if (participantes.size() < 6) {
            GestorLog.advertencia("Intento de generar calendario con " + participantes.size() +
                                " equipos REALES en " + temp.getNombre() + " (mínimo: 6)");
            throw new Exception("Reglamento RFBM: Se requiere un mínimo de 6 equipos para iniciar la liga.");
        }

        GestorLog.info("Iniciando generación de calendario: " + temp.getNombre() +
                      " | Equipos participantes: " + participantes.size() +
                      " | Próximo ID de jornada: " + Jornada.obtenerProximoId());

        // ⭐ Crear una copia independiente para rotar
        List<Equipo> equiposRotacion = new ArrayList<>(participantes);

        // Algoritmo Round-Robin (Ida)
        boolean agregarDescanso = false;
        if (equiposRotacion.size() % 2 != 0) {
            equiposRotacion.add(new Equipo("DESCANSA"));
            agregarDescanso = true;
            GestorLog.info("Número impar de equipos - Se agregó equipo fantasma temporal 'DESCANSA'");
        }

        int jornadasIda = equiposRotacion.size() - 1;
        int partidosGenerados = 0;

        // ⭐ Fijar el primer equipo y rotar el resto
        Equipo equipoFijo = equiposRotacion.get(0);
        List<Equipo> equiposRotativos = new ArrayList<>(equiposRotacion.subList(1, equiposRotacion.size()));

        // Generar jornadas de IDA
        for (int i = 0; i < jornadasIda; i++) {
            Jornada j = new Jornada("Jornada " + (i + 1));
            
            GestorLog.info(" Creando: " + j.getNombre() + " con ID: " + j.getId());
            
            // ⭐ EMPAREJAMIENTO: El equipo fijo juega contra el primero de los rotativos
            Equipo rival = equiposRotativos.get(0);
            
            // Alternar local/visitante según la jornada
            if (i % 2 == 0) {
                if (!equipoFijo.getNombre().equals("DESCANSA") && !rival.getNombre().equals("DESCANSA")) {
                    j.agregarPartido(new Partido(equipoFijo, rival));
                    partidosGenerados++;
                }
            } else {
                if (!equipoFijo.getNombre().equals("DESCANSA") && !rival.getNombre().equals("DESCANSA")) {
                    j.agregarPartido(new Partido(rival, equipoFijo));
                    partidosGenerados++;
                }
            }
            
            // ⭐ RESTO DE EMPAREJAMIENTOS: Método espejo (1º con último, 2º con penúltimo...)
            int mitad = equiposRotativos.size() / 2;
            for (int k = 1; k <= mitad; k++) {
                int indexLocal = k;
                int indexVisitante = equiposRotativos.size() - k;
                
                // Solo emparejar si son posiciones diferentes
                if (indexLocal != indexVisitante) {
                    Equipo local = equiposRotativos.get(indexLocal);
                    Equipo visita = equiposRotativos.get(indexVisitante);
                    
                    if (!local.getNombre().equals("DESCANSA") && !visita.getNombre().equals("DESCANSA")) {
                        j.agregarPartido(new Partido(local, visita));
                        partidosGenerados++;
                    }
                }
            }
            
            temp.agregarJornada(j);
            
            // ⭐ ROTACIÓN: Rotar solo la lista de equipos rotativos (sentido horario)
            Collections.rotate(equiposRotativos, 1);
        }

        GestorLog.info("Fase de IDA completada - Jornadas: " + jornadasIda + " | Partidos: " + partidosGenerados);

        // Generar jornadas de VUELTA (invertir local y visitante)
        List<Jornada> ida = new ArrayList<>(temp.getListaJornadas());
        for (Jornada jIda : ida) {
            Jornada jVuelta = new Jornada("Vuelta - " + jIda.getNombre());
            GestorLog.info(" Creando: " + jVuelta.getNombre() + " con ID: " + jVuelta.getId());
            
            for (Partido p : jIda.getListaPartidos()) {
                jVuelta.agregarPartido(new Partido(p.getEquipoVisitante(), p.getEquipoLocal()));
                partidosGenerados++;
            }
            temp.agregarJornada(jVuelta);
        }

        GestorLog.exito(" Calendario generado: " + temp.getNombre() +
                      " | Equipos participantes: " + participantes.size() +
                      " | Jornadas: " + temp.getListaJornadas().size() +
                      " | Partidos: " + partidosGenerados +
                      " | Sistema: Round-Robin" +
                      (agregarDescanso ? " (con descansos)" : "") +
                      " | IDs: " + temp.getListaJornadas().get(0).getId() + " a " + 
                      temp.getListaJornadas().get(temp.getListaJornadas().size() - 1).getId());
    }
}
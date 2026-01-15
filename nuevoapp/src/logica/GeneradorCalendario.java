package logica;

import gestion.*;
import java.util.*;

/**
 * Clase responsable de generar el calendario de partidos de una temporada.
 * Utiliza el algoritmo Round-Robin para crear las jornadas de ida y vuelta.
 *
 * <p>Reglas principales:
 * <ul>
 *     <li>Mínimo de 6 equipos para generar calendario.</li>
 *     <li>Si el número de equipos es impar, se agrega un equipo fantasma "DESCANSA".</li>
 *     <li>Se generan jornadas de ida y vuelta automáticamente.</li>
 * </ul>
 * </p>
 */
public class GeneradorCalendario {

    /**
     * Genera el calendario completo para la temporada especificada.
     * Crea jornadas de ida y vuelta según el sistema Round-Robin.
     *
     * @param temp La temporada donde se generará el calendario (no nula)
     * @throws IllegalArgumentException Si la temporada es nula
     * @throws Exception Si el número de equipos es menor al mínimo permitido (6)
     */
    public static void crearCalendario(Temporada temp) throws Exception {
        if (temp == null) {
            GestorLog.error("Intento de generar calendario con temporada nula");
            throw new IllegalArgumentException("La temporada no puede ser nula.");
        }

        List<Equipo> participantes = new ArrayList<>(temp.getEquiposParticipantes());

        if (participantes.size() < 6) {
            GestorLog.advertencia("Intento de generar calendario con " + participantes.size() +
                                " equipos en " + temp.getNombre() + " (mínimo: 6)");
            throw new Exception("Reglamento RFBM: Se requiere un mínimo de 6 equipos para iniciar la liga.");
        }

        GestorLog.info("Iniciando generación de calendario: " + temp.getNombre() +
                      " | Equipos: " + participantes.size());

        // ⭐ CORRECCIÓN: Crear una copia independiente para rotar
        List<Equipo> equiposRotacion = new ArrayList<>(participantes);

        // Algoritmo Round-Robin (Ida)
        boolean agregarDescanso = false;
        if (equiposRotacion.size() % 2 != 0) {
            equiposRotacion.add(new Equipo("DESCANSA"));
            agregarDescanso = true;
            GestorLog.info("Número impar de equipos - Se agregó equipo fantasma 'DESCANSA'");
        }

        int jornadasIda = equiposRotacion.size() - 1;
        int partidosGenerados = 0;

        // ⭐ CORRECCIÓN: Fijar el primer equipo y rotar el resto
        Equipo equipoFijo = equiposRotacion.get(0);
        List<Equipo> equiposRotativos = new ArrayList<>(equiposRotacion.subList(1, equiposRotacion.size()));

        // Generar jornadas de IDA
        for (int i = 0; i < jornadasIda; i++) {
            Jornada j = new Jornada("Jornada " + (i + 1));
            
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
            for (Partido p : jIda.getListaPartidos()) {
                jVuelta.agregarPartido(new Partido(p.getEquipoVisitante(), p.getEquipoLocal()));
                partidosGenerados++;
            }
            temp.agregarJornada(jVuelta);
        }

        GestorLog.exito("Calendario generado: " + temp.getNombre() +
                      " | Equipos: " + (equiposRotacion.size() - (agregarDescanso ? 1 : 0)) +
                      " | Jornadas: " + temp.getListaJornadas().size() +
                      " | Partidos: " + partidosGenerados +
                      " | Sistema: Round-Robin");
    }
}
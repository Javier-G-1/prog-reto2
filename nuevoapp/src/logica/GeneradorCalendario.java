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

        // Algoritmo Round-Robin (Ida)
        boolean agregarDescanso = false;
        if (participantes.size() % 2 != 0) {
            participantes.add(new Equipo("DESCANSA"));
            agregarDescanso = true;
            GestorLog.info("Número impar de equipos - Se agregó equipo fantasma 'DESCANSA'");
        }

        int jornadasIda = participantes.size() - 1;
        int partidosGenerados = 0;

        // Generar jornadas de IDA
        for (int i = 0; i < jornadasIda; i++) {
            Jornada j = new Jornada("Jornada " + (i + 1));
            for (int k = 0; k < participantes.size() / 2; k++) {
                Equipo local = participantes.get(k);
                Equipo visita = participantes.get(participantes.size() - 1 - k);
                if (!local.getNombre().equals("DESCANSA") && !visita.getNombre().equals("DESCANSA")) {
                    j.agregarPartido(new Partido(local, visita));
                    partidosGenerados++;
                }
            }
            temp.agregarJornada(j);
            Collections.rotate(participantes.subList(1, participantes.size()), 1);
        }

        GestorLog.info("Fase de IDA completada - Jornadas: " + jornadasIda + " | Partidos: " + partidosGenerados);

        // Generar jornadas de VUELTA
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
                      " | Equipos: " + (participantes.size() - (agregarDescanso ? 1 : 0)) +
                      " | Jornadas: " + temp.getListaJornadas().size() +
                      " | Partidos: " + partidosGenerados +
                      " | Sistema: Round-Robin");
    }
}

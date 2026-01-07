package logica;

import gestion.*;
import java.util.*;

public class GeneradorCalendario {
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
        
        // Algoritmo Round Robin (Ida)
        boolean agregarDescanso = false;
        if (participantes.size() % 2 != 0) {
            participantes.add(new Equipo("DESCANSA", ""));
            agregarDescanso = true;
            GestorLog.debug("Número impar de equipos - Se agregó equipo fantasma 'DESCANSA'");
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
        
        GestorLog.debug("Fase de IDA completada - Jornadas: " + jornadasIda + " | Partidos: " + partidosGenerados);
        
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
        
        GestorLog.exito("Calendario generado completamente: " + temp.getNombre() + 
                      " | Equipos: " + (participantes.size() - (agregarDescanso ? 1 : 0)) + 
                      " | Jornadas totales: " + temp.getListaJornadas().size() + 
                      " | Partidos totales: " + partidosGenerados +
                      " | Sistema: Round-Robin (Ida y Vuelta)");
    }
}
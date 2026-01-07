package logica;

import gestion.*;
import java.util.*;

public class GeneradorCalendario {

    public static void crearCalendario(Temporada temp) throws Exception {
        if (temp == null) throw new IllegalArgumentException("La temporada no puede ser nula.");

        List<Equipo> participantes = new ArrayList<>(temp.getEquiposParticipantes());

        if (participantes.size() < 6)
            throw new Exception("Reglamento RFBM: Se requiere un mínimo de 6 equipos para iniciar la liga.");

        // Algoritmo Round Robin (Ida)
        boolean agregarDescanso = false;
        if (participantes.size() % 2 != 0) {
            participantes.add(new Equipo("DESCANSA", ""));
            agregarDescanso = true;
        }

        int jornadasIda = participantes.size() - 1;

        for (int i = 0; i < jornadasIda; i++) {
            Jornada j = new Jornada("Jornada " + (i + 1));
            for (int k = 0; k < participantes.size() / 2; k++) {
                Equipo local = participantes.get(k);
                Equipo visita = participantes.get(participantes.size() - 1 - k);

                if (!local.getNombre().equals("DESCANSA") && !visita.getNombre().equals("DESCANSA")) {
                    j.agregarPartido(new Partido(local, visita));
                }
            }
            temp.agregarJornada(j);
            Collections.rotate(participantes.subList(1, participantes.size()), 1);
        }

        // Generar Vuelta
        List<Jornada> ida = new ArrayList<>(temp.getListaJornadas());
        for (Jornada jIda : ida) {
            Jornada jVuelta = new Jornada("Vuelta - " + jIda.getNombre());
            for (Partido p : jIda.getListaPartidos()) {
                jVuelta.agregarPartido(new Partido(p.getEquipoVisitante(), p.getEquipoLocal()));
            }
            temp.agregarJornada(jVuelta);
        }

        GestorLog.registrarEvento("CALENDARIO: Generado con éxito para " + temp.getNombre());
    }
}

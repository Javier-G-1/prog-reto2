package logica;

import gestion.*;
import java.util.*;

public class CalculadoraClasificacion {

    public static List<FilaClasificacion> calcular(Temporada t) {
        if (t == null) throw new IllegalArgumentException("La temporada no puede ser nula.");

        Map<String, FilaClasificacion> mapa = new HashMap<>();

        // Inicializa la tabla de todos los equipos participantes
        for (Equipo e : t.getEquiposParticipantes()) {
            if (!mapa.containsKey(e.getNombre()))
                mapa.put(e.getNombre(), new FilaClasificacion(e.getNombre()));
        }

        // Procesa todos los partidos finalizados
        for (Jornada j : t.getListaJornadas()) {
            for (Partido p : j.getListaPartidos()) {
                if (p.isFinalizado()) {
                    FilaClasificacion local = mapa.get(p.getEquipoLocal().getNombre());
                    FilaClasificacion visitante = mapa.get(p.getEquipoVisitante().getNombre());
                    if (local != null && visitante != null) {
                        local.registrarPartido(p.getGolesLocal(), p.getGolesVisitante());
                        visitante.registrarPartido(p.getGolesVisitante(), p.getGolesLocal());
                    }
                }
            }
        }

        List<FilaClasificacion> lista = new ArrayList<>(mapa.values());

        // Ordenación: 1º Puntos, 2º Diferencia de goles, 3º Goles a favor
        lista.sort((a, b) -> {
            if (b.getPuntos() != a.getPuntos()) return Integer.compare(b.getPuntos(), a.getPuntos());
            if (b.getDf() != a.getDf()) return Integer.compare(b.getDf(), a.getDf());
            return Integer.compare(b.getGf(), a.getGf());
        });

        return lista;
    }
}

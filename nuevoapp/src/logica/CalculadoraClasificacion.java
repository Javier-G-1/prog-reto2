package logica;

import java.util.*;
import gestion.Temporada;
import gestion.Equipo;
import gestion.Jornada;
import gestion.Partido;

/**
 * CLASE: CalculadoraClasificacion
 * <p>
 * Clase responsable de calcular la clasificación de una temporada deportiva
 * según los resultados de los partidos finalizados.
 * </p>
 * <p>
 * Genera un objeto {@link Clasificacion} que contiene todas las filas de clasificación
 * con puntos, goles a favor, goles en contra y posiciones de los equipos.
 * </p>
 */
public class CalculadoraClasificacion {

    /**
     * Calcula la clasificación completa de una temporada.
     *
     * <p>
     * Para cada equipo de la temporada, se suman los resultados de los partidos
     * finalizados y se genera la tabla de clasificación ordenada por puntos.
     * </p>
     *
     * @param t la temporada a calcular
     * @return un objeto {@link Clasificacion} con todas las filas de clasificación
     * @throws IllegalArgumentException si la temporada es {@code null}
     */
    public static Clasificacion calcular(Temporada t) {
        if (t == null) {
            throw new IllegalArgumentException("La temporada no puede ser nula.");
        }

        Map<String, FilaClasificacion> mapa = new HashMap<>();

        // Inicializa la tabla con todos los equipos participantes
        for (Equipo e : t.getEquiposParticipantes()) {
            if (e != null && e.getNombre() != null) {
                mapa.putIfAbsent(e.getNombre(), new FilaClasificacion(e.getNombre()));
            }
        }

        // Procesa todos los partidos finalizados
        for (Jornada j : t.getListaJornadas()) {
            for (Partido p : j.getListaPartidos()) {
                if (p.getGolesLocal() >= 0 && p.getGolesVisitante() >= 0) {
                    FilaClasificacion local = mapa.get(p.getEquipoLocal().getNombre());
                    FilaClasificacion visitante = mapa.get(p.getEquipoVisitante().getNombre());

                    if (local != null && visitante != null) {
                        local.registrarPartido(p.getGolesLocal(), p.getGolesVisitante());
                        visitante.registrarPartido(p.getGolesVisitante(), p.getGolesLocal());
                    }
                }
            }
        }

        // Convertir el mapa a lista y ordenar por puntos
        List<FilaClasificacion> lista = new ArrayList<>(mapa.values());
        lista.sort((f1, f2) -> Integer.compare(f2.getPuntos(), f1.getPuntos()));

        // Retorna el objeto Clasificacion completo
        return new Clasificacion(t.getNombre(), lista);
    }
}

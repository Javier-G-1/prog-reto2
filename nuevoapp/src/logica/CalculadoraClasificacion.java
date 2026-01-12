package logica;

import gestion.*;
import java.util.*;

/**
 * Clase responsable de calcular la clasificación de una temporada
 * según los resultados de los partidos finalizados.
 */
public class CalculadoraClasificacion {
    
    /**
     * Calcula la clasificación completa de una temporada.

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
        procesarPartidos(t, mapa);
        
        // Convierte el mapa a lista y ordena
        List<FilaClasificacion> lista = new ArrayList<>(mapa.values());
        ordenarClasificacion(lista);
        
        // Asigna las posiciones
        asignarPosiciones(lista);
        
        // Retorna el objeto Clasificacion completo
        return new Clasificacion(t.getNombre(), lista);
    }
    
    /**
     * Procesa todos los partidos finalizados de la temporada.
     */
    private static void procesarPartidos(Temporada t, Map<String, FilaClasificacion> mapa) {
        if (t.getListaJornadas() == null) return;
        
        for (Jornada j : t.getListaJornadas()) {
            if (j == null || j.getListaPartidos() == null) continue;
            
            for (Partido p : j.getListaPartidos()) {
                if (p != null && p.isFinalizado()) {
                    procesarPartido(p, mapa);
                }
            }
        }
    }
    
    /**
     * Procesa un partido individual y actualiza las estadísticas.
     */
    private static void procesarPartido(Partido p, Map<String, FilaClasificacion> mapa) {
        String nombreLocal = p.getEquipoLocal().getNombre();
        String nombreVisitante = p.getEquipoVisitante().getNombre();
        
        FilaClasificacion local = mapa.get(nombreLocal);
        FilaClasificacion visitante = mapa.get(nombreVisitante);
        
        if (local != null && visitante != null) {
            int golesLocal = p.getGolesLocal();
            int golesVisitante = p.getGolesVisitante();
            
            local.registrarPartido(golesLocal, golesVisitante);
            visitante.registrarPartido(golesVisitante, golesLocal);
        }
    }
    
    /**
     * Ordena la clasificación según los criterios establecidos:
     * 1º Puntos (descendente)
     * 2º Diferencia de goles (descendente)
     * 3º Goles a favor (descendente)
     * 4º Nombre alfabético (ascendente) - criterio de desempate final
     */
    private static void ordenarClasificacion(List<FilaClasificacion> lista) {
        lista.sort((a, b) -> {
            // 1º Criterio: Puntos
            if (b.getPuntos() != a.getPuntos()) {
                return Integer.compare(b.getPuntos(), a.getPuntos());
            }
            
            // 2º Criterio: Diferencia de goles
            if (b.getDf() != a.getDf()) {
                return Integer.compare(b.getDf(), a.getDf());
            }
            
            // 3º Criterio: Goles a favor
            if (b.getGf() != a.getGf()) {
                return Integer.compare(b.getGf(), a.getGf());
            }
            
            // 4º Criterio: Orden alfabético (desempate final)
            return a.getEquipo().compareTo(b.getEquipo());
        });
    }
    
    /**
     * Asigna la posición numérica a cada equipo en la clasificación.
     */
    private static void asignarPosiciones(List<FilaClasificacion> lista) {
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).setPosicion(i + 1);
        }
    }
}
package logica;

import gestion.*;

public class SincronizadorWeb {

    private static final String BASE_URL = "https://www.tu-balonmano-web.com/assets/";

    public static void actualizarActivosServidor(DatosFederacion datos, String nombreTemporada) {
        if (datos == null || nombreTemporada == null || nombreTemporada.isBlank()) return;

        String carpeta = nombreTemporada.toLowerCase().replace(" ", "_");

        // Actualizar fotos de jugadores
        for (Jugador j : datos.getTodosLosJugadores()) {
            if (j.getNombre() != null && !j.getNombre().isBlank()) {
                String nombreLimpio = j.getNombre().toLowerCase().replace(" ", "_");
                j.setFotoURL(BASE_URL + carpeta + "/jugadores/" + nombreLimpio + ".png");
            }
        }

        // Actualizar escudos de equipos
        for (Equipo e : datos.getListaEquipos()) {
            if (e.getNombre() != null && !e.getNombre().isBlank()) {
                String equipoLimpio = e.getNombre().toLowerCase().replace(" ", "_");
                e.setRutaEscudo(BASE_URL + carpeta + "/escudos/" + equipoLimpio + ".png");
            }
        }

        System.out.println("WEB: Todas las rutas se han vinculado a la carpeta del servidor: " + carpeta);
    }
}
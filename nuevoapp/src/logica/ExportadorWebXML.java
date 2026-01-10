package logica;

import gestion.*;
import java.io.*;
import java.util.List;

public class ExportadorWebXML {

    public static void generarXMLParaWeb(DatosFederacion datos, String ruta) {
        if (datos == null || ruta == null || ruta.isBlank()) return;

        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<FederacionWeb>");

            for (Temporada t : datos.getListaTemporadas()) {
                writer.println("  <Temporada nombre=\"" + t.getNombre() + "\" estado=\"" + t.getEstado() + "\">");

                // Clasificación
                writer.println("    <Clasificacion>");
                List<FilaClasificacion> filas = CalculadoraClasificacion.calcular(t);
                int pos = 1;
                for (FilaClasificacion f : filas) {
                    writer.println("      <Fila pos=\"" + (pos++) + "\">");
                    writer.println("        <Equipo>" + f.getEquipo() + "</Equipo>");
                    writer.println("        <Puntos>" + f.getPuntos() + "</Puntos>");
                    writer.println("        <PJ>" + f.getPj() + "</PJ>");
                    writer.println("        <PG>" + f.getPg() + "</PG>");
                    writer.println("        <PE>" + f.getPe() + "</PE>");
                    writer.println("        <PP>" + f.getPp() + "</PP>");
                    writer.println("        <GF>" + f.getGf() + "</GF>");
                    writer.println("        <GC>" + f.getGc() + "</GC>");
                    writer.println("        <DF>" + f.getDf() + "</DF>");
                    writer.println("      </Fila>");
                }
                writer.println("    </Clasificacion>");

                // Equipos y jugadores
                writer.println("    <EquiposInscritos>");
                for (Equipo e : t.getEquiposParticipantes()) {
                    writer.println("      <Equipo nombre=\"" + e.getNombre() + "\" escudoURL=\"" + e.getRutaEscudo() + "\">");
                    for (Jugador j : e.getPlantilla()) {
                        writer.println("        <Jugador nombre=\"" + j.getNombre() + "\" fotoURL=\"" + j.getFotoURL() + "\" />");
                    }
                    writer.println("      </Equipo>");
                }
                writer.println("    </EquiposInscritos>");
                writer.println("  </Temporada>");
            }

            writer.println("</FederacionWeb>");
            GestorLog.registrarEvento("WEB SYNC: XML generado correctamente en " + ruta);
        }  catch (Exception e) {
            GestorLog.registrarEvento("ERROR WEB SYNC: " + e.getMessage());
            System.err.println("Error en exportación web: " + e.getMessage());
        
        }
    }
}

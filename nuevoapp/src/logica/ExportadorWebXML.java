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

                // --- Clasificación Detallada ---
                writer.println("    <Clasificacion>");
                Clasificacion clasificacionCompleta = CalculadoraClasificacion.calcular(t);
                List<FilaClasificacion> filas = clasificacionCompleta.getFilas();
                
                for (FilaClasificacion f : filas) {
                    writer.println("      <Fila pos=\"" + f.getPosicion() + "\">");
                    writer.println("        <Equipo>" + f.getNombre() + "</Equipo>");
                    writer.println("        <Puntos>" + f.getPuntos() + "</Puntos>");
                    writer.println("        <Estadisticas PJ=\"" + f.getPj() + "\" PG=\"" + f.getPg() + "\" PE=\"" + f.getPe() + "\" PP=\"" + f.getPp() + "\"/>");
                    writer.println("        <Goles favor=\"" + f.getGf() + "\" contra=\"" + f.getGc() + "\" dif=\"" + f.getDifFormateada() + "\"/>");
                    writer.println("      </Fila>");
                }
                writer.println("    </Clasificacion>");

                // --- Equipos e Información Detallada de Jugadores ---
                writer.println("    <EquiposInscritos>");
                for (Equipo e : t.getEquiposParticipantes()) {
                    writer.println("      <Equipo nombre=\"" + e.getNombre() + "\" escudoURL=\"" + e.getRutaEscudo() + "\">");
                    writer.println("        <Plantilla>");
                    for (Jugador j : e.getPlantilla()) {
                        writer.println("          <Jugador id=\"" + j.getId() + "\">");
                        writer.println("            <Nombre>" + j.getNombre() + "</Nombre>");
                        writer.println("            <Dorsal>" + j.getDorsal() + "</Dorsal>");
                        writer.println("            <Posicion>" + j.getPosicion() + "</Posicion>");
                        writer.println("            <Nacionalidad>" + j.getNacionalidad() + "</Nacionalidad>");
                        writer.println("            <InfoFisica edad=\"" + j.getEdad() + "\" altura=\"" + j.getAltura() + "\" peso=\"" + j.getPeso() + "\"/>");
                        writer.println("            <Foto>" + j.getFotoURL() + "</Foto>");
                        writer.println("          </Jugador>");
                    }
                    writer.println("        </Plantilla>");
                    writer.println("      </Equipo>");
                }
                writer.println("    </EquiposInscritos>");
                writer.println("  </Temporada>");
            }

            writer.println("</FederacionWeb>");
            GestorLog.registrarEvento("WEB SYNC: XML completo generado en " + ruta);
            
        } catch (Exception e) {
            GestorLog.registrarEvento("ERROR WEB SYNC: " + e.getMessage());
            System.err.println("Error en exportación web: " + e.getMessage());
        }
    }
}
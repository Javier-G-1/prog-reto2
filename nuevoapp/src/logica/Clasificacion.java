package logica;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase que encapsula una clasificación completa con todos sus equipos.
 * Facilita la exportación a diferentes formatos (PDF, XML, etc.)
 */
public class Clasificacion {
    private String temporada;
    private List<FilaClasificacion> filas;
    
    public Clasificacion(String temporada, List<FilaClasificacion> filas) {
        this.temporada = temporada;
        this.filas = filas != null ? filas : new ArrayList<>();
    }
    
    public String getTemporada() {
        return temporada;
    }
    
    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }
    
    public List<FilaClasificacion> getFilas() {
        return Collections.unmodifiableList(filas);
    }
    
    /**
     * Retorna la fila de clasificación de un equipo específico.
     */
    public FilaClasificacion getFilaEquipo(String nombreEquipo) {
        for (FilaClasificacion fila : filas) {
            if (fila.getEquipo().equals(nombreEquipo)) {
                return fila;
            }
        }
        return null;
    }
    
    /**
     * Retorna el número total de equipos en la clasificación.
     */
    public int getNumeroEquipos() {
        return filas.size();
    }
    
    /**
     * Genera una representación en formato XML de la clasificación.
     * Compatible con la plantilla proporcionada.
     */
    public String toXML() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<clasificacion>\n");
        xml.append("    <temporada>").append(escaparXML(temporada)).append("</temporada>\n");
        
        for (FilaClasificacion fila : filas) {
            xml.append("    <equipo>\n");
            xml.append("        <posicion>").append(fila.getPosicion()).append("</posicion>\n");
            xml.append("        <nombre>").append(escaparXML(fila.getEquipo())).append("</nombre>\n");
            xml.append("        <pj>").append(fila.getPj()).append("</pj>\n");
            xml.append("        <pg>").append(fila.getPg()).append("</pg>\n");
            xml.append("        <pe>").append(fila.getPe()).append("</pe>\n");
            xml.append("        <pp>").append(fila.getPp()).append("</pp>\n");
            xml.append("        <gf>").append(fila.getGf()).append("</gf>\n");
            xml.append("        <gc>").append(fila.getGc()).append("</gc>\n");
            xml.append("        <dif>").append(fila.getDifFormateada()).append("</dif>\n");
            xml.append("        <pts>").append(fila.getPuntos()).append("</pts>\n");
            xml.append("    </equipo>\n");
        }
        
        xml.append("</clasificacion>");
        return xml.toString();
    }
    
    /**
     * Escapa caracteres especiales para XML.
     */
    private String escaparXML(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
    
    /**
     * Genera una representación en texto plano de la clasificación.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CLASIFICACIÓN - TEMPORADA ").append(temporada).append("\n");
        sb.append("=".repeat(80)).append("\n");
        sb.append(String.format("%-3s %-25s %3s %3s %3s %3s %4s %4s %6s %4s\n",
            "POS", "EQUIPO", "PJ", "PG", "PE", "PP", "GF", "GC", "DIF", "PTS"));
        sb.append("-".repeat(80)).append("\n");
        
        for (FilaClasificacion fila : filas) {
            sb.append(fila.toString()).append("\n");
        }
        
        return sb.toString();
    }
}
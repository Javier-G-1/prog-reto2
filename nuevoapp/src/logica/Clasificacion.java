package logica;

import java.util.List;
import java.util.Collections;

/**
 * CLASE: Clasificacion
 * <p>
 * Representa la clasificación completa de una temporada deportiva.
 * Contiene todas las filas de clasificación de los equipos participantes
 * y facilita la exportación a diferentes formatos como XML o texto plano.
 * </p>
 */
public class Clasificacion {

    /** Nombre de la temporada asociada a esta clasificación */
    private String temporada;

    /** Lista de filas de clasificación, cada una representando un equipo */
    private List<FilaClasificacion> filas;

    /**
     * Constructor de la clase Clasificacion.
     *
     * @param nombre nombre de la temporada
     * @param filas lista de {@link FilaClasificacion} con los equipos y sus estadísticas
     */
    public Clasificacion(String nombre, List<FilaClasificacion> filas) {
        this.temporada = nombre;
        this.filas = filas;
    }

    /**
     * Obtiene el nombre de la temporada.
     *
     * @return nombre de la temporada
     */
    public String getTemporada() {
        return temporada;
    }

    /**
     * Establece el nombre de la temporada.
     *
     * @param temporada nombre de la temporada a asignar
     */
    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    /**
     * Obtiene la lista de filas de clasificación.
     * <p>
     * La lista retornada es de solo lectura para evitar modificaciones externas.
     * </p>
     *
     * @return lista inmodificable de {@link FilaClasificacion}
     */
    public List<FilaClasificacion> getFilas() {
        return Collections.unmodifiableList(filas);
    }

    /**
     * Retorna la fila de clasificación de un equipo específico.
     *
     * @param nombreEquipo nombre del equipo a buscar
     * @return {@link FilaClasificacion} correspondiente al equipo, o {@code null} si no se encuentra
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
     *
     * @return cantidad de equipos
     */
    public int getNumeroEquipos() {
        return filas.size();
    }

    /**
     * Genera una representación en formato XML de la clasificación.
     * <p>
     * Compatible con plantillas de exportación y facilita la integración con otros sistemas.
     * </p>
     *
     * @return cadena en formato XML
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
     *
     * @param texto texto a escapar
     * @return texto con caracteres especiales convertidos a entidades XML
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
     * <p>
     * Incluye encabezados y formateo tabular para visualización en consola.
     * </p>
     *
     * @return cadena con la clasificación en formato legible
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

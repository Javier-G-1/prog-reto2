package logica;

import gestion.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;

/**
 * EXPORTADOR XML adaptado al formato ligaBalonmano.xml
 * 
 * Estructura exacta del XML de referencia con IDs únicos persistentes.
 */
public class ExportadorXMLLiga {
    
    private DatosFederacion datosFederacion;
    private static final String ARCHIVO_XML = "ligaBalonmano.xml";
    private File carpetaImagenes;
    
    public ExportadorXMLLiga(DatosFederacion datos) {
        this.datosFederacion = datos;
    }
    
    /**
     * Exporta una temporada al archivo ligaBalonmano.xml
     * @param temporada La temporada a exportar
     * @return true si la exportación fue exitosa
     */
    public boolean exportarTemporada(Temporada temporada) {
        try {
            // Crear carpetas de imágenes
            carpetaImagenes = new File("imagenes");
            File carpetaLogos = new File(carpetaImagenes, "imagenes_Logos");
            File carpetaJugadores = new File(carpetaImagenes, "imagenes_Jugadores");
            
            carpetaLogos.mkdirs();
            carpetaJugadores.mkdirs();
            
            GestorLog.info("Carpetas de imágenes verificadas");
            
            // Leer XML existente o crear estructura base
            File archivoXML = new File(ARCHIVO_XML);
            StringBuilder xmlExistente = new StringBuilder();
            
            if (archivoXML.exists()) {
                String contenido = new String(Files.readAllBytes(archivoXML.toPath()));
                
                // Verificar si la temporada ya existe
                String idTemporada = generarIdTemporada(temporada.getNombre());
                if (contenido.contains("id=\"" + idTemporada + "\"")) {
                    int respuesta = JOptionPane.showConfirmDialog(null,
                        "⚠️ La temporada '" + temporada.getNombre() + "' ya existe en ligaBalonmano.xml\n\n" +
                        "¿Desea reemplazarla?",
                        "Temporada existente",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    
                    if (respuesta != JOptionPane.YES_OPTION) {
                        GestorLog.info("Exportación cancelada");
                        return false;
                    }
                    
                    contenido = eliminarTemporadaExistente(contenido, idTemporada);
                }
                
                int posicionCierre = contenido.lastIndexOf("</temporadas>");
                
                if (posicionCierre != -1) {
                    xmlExistente.append(contenido.substring(0, posicionCierre));
                } else {
                    xmlExistente = crearEstructuraBase();
                }
                
            } else {
                xmlExistente = crearEstructuraBase();
                GestorLog.info("Creando nuevo archivo ligaBalonmano.xml");
            }
            
            // Agregar nueva temporada
            exportarTemporadaAlXML(xmlExistente, temporada);
            
            // Cerrar tags
            xmlExistente.append("    </temporadas>\n");
            xmlExistente.append("</federacionBalonmano>");
            
            // Escribir archivo
            try (FileWriter writer = new FileWriter(archivoXML)) {
                writer.write(xmlExistente.toString());
            }
            
            GestorLog.exito("Temporada exportada: " + temporada.getNombre());
            JOptionPane.showMessageDialog(null,
                "✅ Temporada exportada exitosamente\n\n" +
                "📄 Archivo: " + ARCHIVO_XML + "\n" +
                "📁 Temporada: " + temporada.getNombre(),
                "Exportación exitosa",
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
            
        } catch (IOException e) {
            GestorLog.error("Error al exportar: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "❌ Error: " + e.getMessage(),
                "Error de exportación",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Crea la estructura base del XML
     */
    private StringBuilder crearEstructuraBase() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<federacionBalonmano xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        xml.append("    xsi:noNamespaceSchemaLocation=\"general.xsd\">\n\n");
        xml.append("    <temporadas>\n");
        return xml;
    }
    
    /**
     * Elimina una temporada existente del XML
     */
    private String eliminarTemporadaExistente(String contenido, String idTemporada) {
        try {
            String inicioTag = "<temporada id=\"" + idTemporada + "\">";
            int posInicio = contenido.indexOf(inicioTag);
            
            if (posInicio == -1) return contenido;
            
            int posActual = posInicio + inicioTag.length();
            int nivel = 1;
            
            while (nivel > 0 && posActual < contenido.length()) {
                if (contenido.startsWith("<temporada", posActual)) {
                    nivel++;
                } else if (contenido.startsWith("</temporada>", posActual)) {
                    nivel--;
                    if (nivel == 0) {
                        posActual += "</temporada>".length();
                        break;
                    }
                }
                posActual++;
            }
            
            String antes = contenido.substring(0, posInicio);
            String despues = contenido.substring(posActual);
            
            GestorLog.info("Temporada existente eliminada del XML");
            return antes + despues;
            
        } catch (Exception e) {
            GestorLog.error("Error al eliminar temporada: " + e.getMessage());
            return contenido;
        }
    }
    
    /**
     * Exporta una temporada completa al XML
     */
    private void exportarTemporadaAlXML(StringBuilder xml, Temporada temporada) {
        String idTemporada = generarIdTemporada(temporada.getNombre());
        
        xml.append("        <temporada id=\"").append(idTemporada).append("\">\n");
        
        // Exportar equipos
        xml.append("            <equipos>\n");
        
        for (Equipo equipo : temporada.getEquiposParticipantes()) {
            exportarEquipo(xml, equipo, temporada);
        }
        
        xml.append("            </equipos>\n");
        
        // Exportar jornadas
        xml.append("            <jornadas>\n");
        
        for (Jornada jornada : temporada.getListaJornadas()) {
            exportarJornada(xml, jornada, temporada);
        }
        
        xml.append("            </jornadas>\n");
        xml.append("        </temporada>\n");
    }
    
    /**
     * Exporta un equipo con todos sus jugadores
     */
    private void exportarEquipo(StringBuilder xml, Equipo equipo, Temporada temporada) {
        String idEquipo = equipo.getId();
        
        xml.append("                <equipo id=\"").append(idEquipo).append("\">\n");
        xml.append("                    <nombre>").append(escaparXML(equipo.getNombre())).append("</nombre>\n");
        
        // Calcular estadísticas
        EstadisticasEquipo stats = calcularEstadisticasEquipo(equipo, temporada);
        
        xml.append("                    <ganados>").append(stats.ganados).append("</ganados>\n");
        xml.append("                    <perdidos>").append(stats.perdidos).append("</perdidos>\n");
        xml.append("                    <empatados>").append(stats.empatados).append("</empatados>\n");
        xml.append("                    <golesFavor>").append(stats.golesFavor).append("</golesFavor>\n");
        xml.append("                    <golesContra>").append(stats.golesContra).append("</golesContra>\n");
        
        // Exportar jugadores
        xml.append("                    <jugadores>\n");
        
        for (Jugador jugador : equipo.getPlantilla()) {
            exportarJugador(xml, jugador, idEquipo, equipo.getNombre());
        }
        
        xml.append("                    </jugadores>\n");
        
        // Escudo
        String rutaEscudo = copiarEscudo(equipo);
        xml.append("                    <escudo url=\"").append(escaparXML(rutaEscudo)).append("\" />\n");
        
        xml.append("                </equipo>\n");
    }
    
    /**
     * Exporta un jugador
     */
    private void exportarJugador(StringBuilder xml, Jugador jugador, String idEquipo, String nombreEquipo) {
        String idJugador = jugador.getId();
        
        xml.append("                        <jugador id=\"").append(idJugador).append("\" equipo=\"").append(idEquipo).append("\">\n");
        xml.append("                            <nombre>").append(escaparXML(jugador.getNombre())).append("</nombre>\n");
        xml.append("                            <edad>").append(jugador.getEdad()).append("</edad>\n");
        xml.append("                            <nacionalidad>").append(escaparXML(jugador.getNacionalidad())).append("</nacionalidad>\n");
        xml.append("                            <altura>").append(escaparXML(jugador.getAltura())).append("</altura>\n");
        xml.append("                            <peso>").append(escaparXML(jugador.getPeso())).append("</peso>\n");
        xml.append("                            <dorsal>").append(jugador.getDorsal()).append("</dorsal>\n");
        xml.append("                            <posicion>").append(escaparXML(jugador.getPosicion())).append("</posicion>\n");
        
        // Foto
        String fotoUrl = copiarFotoJugador(jugador, nombreEquipo);
        xml.append("                            <foto url=\"").append(escaparXML(fotoUrl)).append("\" />\n");
        
        xml.append("                        </jugador>\n");
    }
    
    /**
     * Copia el escudo de un equipo
     */
    private String copiarEscudo(Equipo equipo) {
        String rutaOrigen = equipo.getRutaEscudo();
        
        if (rutaOrigen == null || rutaOrigen.isEmpty()) {
            return "";
        }
        
        try {
            File archivoOrigen = new File(rutaOrigen);
            
            if (!archivoOrigen.exists()) {
                GestorLog.advertencia("Escudo no encontrado: " + rutaOrigen);
                return "";
            }
            
            String extension = obtenerExtension(archivoOrigen.getName());
            String nombreNormalizado = normalizarNombre(equipo.getNombre()) + extension;
            
            File carpetaLogos = new File(carpetaImagenes, "imagenes_Logos");
            File archivoDestino = new File(carpetaLogos, nombreNormalizado);
            
            Files.copy(archivoOrigen.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            return "./imagenes/imagenes_Logos/" + nombreNormalizado;
            
        } catch (IOException e) {
            GestorLog.error("Error al copiar escudo: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Copia la foto de un jugador
     */
    private String copiarFotoJugador(Jugador jugador, String nombreEquipo) {
        String rutaOrigen = jugador.getFotoURL();
        
        if (rutaOrigen == null || rutaOrigen.isEmpty()) {
            return "";
        }
        
        try {
            File archivoOrigen = new File(rutaOrigen);
            
            if (!archivoOrigen.exists()) {
                return "";
            }
            
            String extension = obtenerExtension(archivoOrigen.getName());
            String nombreArchivo = jugador.getNombre().replace(" ", "\\ ") + extension;
            
            File carpetaJugadores = new File(carpetaImagenes, "imagenes_Jugadores");
            File archivoDestino = new File(carpetaJugadores, nombreArchivo);
            
            Files.copy(archivoOrigen.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            return nombreArchivo;
            
        } catch (IOException e) {
            return "";
        }
    }
    
    /**
     * Normaliza un nombre para archivo
     */
    private String normalizarNombre(String nombre) {
        return nombre.replaceAll("[^a-zA-Z0-9]", "_").toUpperCase();
    }
    
    /**
     * Obtiene la extensión de un archivo
     */
    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        return (ultimoPunto > 0) ? nombreArchivo.substring(ultimoPunto) : ".png";
    }
    
    /**
     * Exporta una jornada con todos sus partidos
     */
    private void exportarJornada(StringBuilder xml, Jornada jornada, Temporada temporada) {
        String idJornada = jornada.getId();
        
        xml.append("                <jornada id=\"").append(idJornada).append("\">\n");
        xml.append("                    <partidos>\n");
        
        for (Partido partido : jornada.getListaPartidos()) {
            exportarPartido(xml, partido, temporada);
        }
        
        xml.append("                    </partidos>\n");
        xml.append("                </jornada>\n");
    }
    
    /**
     * Exporta un partido
     */
    private void exportarPartido(StringBuilder xml, Partido partido, Temporada temporada) {
        String idPartido = partido.getId();
        String idLocal = partido.getEquipoLocal().getId();
        String idVisitante = partido.getEquipoVisitante().getId();
        
        xml.append("                        <partido id=\"").append(idPartido).append("\" ");
        xml.append("local=\"").append(idLocal).append("\" ");
        xml.append("visitante=\"").append(idVisitante).append("\">\n");
        
        xml.append("                            <golesLocal>").append(partido.getGolesLocal()).append("</golesLocal>\n");
        xml.append("                            <golesVisitante>").append(partido.getGolesVisitante()).append("</golesVisitante>\n");
        
        String estado = partido.isFinalizado() ? "Finalizado" : "Sin jugar";
        xml.append("                            <estado>").append(estado).append("</estado>\n");
        
        xml.append("                        </partido>\n");
    }
    
    /**
     * Calcula estadísticas de un equipo
     */
    private EstadisticasEquipo calcularEstadisticasEquipo(Equipo equipo, Temporada temporada) {
        EstadisticasEquipo stats = new EstadisticasEquipo();
        
        for (Jornada jornada : temporada.getListaJornadas()) {
            for (Partido partido : jornada.getListaPartidos()) {
                if (!partido.isFinalizado()) continue;
                
                if (partido.getEquipoLocal().getNombre().equals(equipo.getNombre())) {
                    stats.golesFavor += partido.getGolesLocal();
                    stats.golesContra += partido.getGolesVisitante();
                    
                    if (partido.getGolesLocal() > partido.getGolesVisitante()) {
                        stats.ganados++;
                    } else if (partido.getGolesLocal() < partido.getGolesVisitante()) {
                        stats.perdidos++;
                    } else {
                        stats.empatados++;
                    }
                    
                } else if (partido.getEquipoVisitante().getNombre().equals(equipo.getNombre())) {
                    stats.golesFavor += partido.getGolesVisitante();
                    stats.golesContra += partido.getGolesLocal();
                    
                    if (partido.getGolesVisitante() > partido.getGolesLocal()) {
                        stats.ganados++;
                    } else if (partido.getGolesVisitante() < partido.getGolesLocal()) {
                        stats.perdidos++;
                    } else {
                        stats.empatados++;
                    }
                }
            }
        }
        
        return stats;
    }
    
    /**
     * Genera ID de temporada basado en el nombre
     */
    private String generarIdTemporada(String nombreTemporada) {
        if (nombreTemporada == null || nombreTemporada.isEmpty()) {
            return "temp_" + System.currentTimeMillis();
        }
        
        return nombreTemporada.trim().replace(" ", "_").replace("/", "_");
    }
    
    /**
     * Escapa caracteres especiales para XML
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
     * Clase auxiliar para estadísticas
     */
    private static class EstadisticasEquipo {
        int ganados = 0;
        int perdidos = 0;
        int empatados = 0;
        int golesFavor = 0;
        int golesContra = 0;
    }
}
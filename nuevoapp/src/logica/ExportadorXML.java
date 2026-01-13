package logica;

import gestion.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ExportadorXML {
    
    private DatosFederacion datosFederacion;
    private static final String ARCHIVO_XML = "general.xml";
    private File carpetaImagenes;
    
    public ExportadorXML(DatosFederacion datos) {
        this.datosFederacion = datos;
    }
    
    /**
     * Exporta una temporada al archivo general.xml (añadiéndola si no existe)
     * @param temporada La temporada a exportar
     * @return true si la exportación fue exitosa, false en caso contrario
     */
    public boolean exportarTemporada(Temporada temporada) {
        try {
            // Crear estructura de carpetas para imágenes
            carpetaImagenes = new File("imagenes");
            File carpetaLogos = new File(carpetaImagenes, "imagenes_Logos");
            File carpetaJugadores = new File(carpetaImagenes, "imagenes_Jugadores");
            
            carpetaLogos.mkdirs();
            carpetaJugadores.mkdirs();
            
            GestorLog.info("Carpetas de imágenes verificadas/creadas");
            
            // Leer XML existente o crear estructura base
            File archivoXML = new File(ARCHIVO_XML);
            StringBuilder xmlExistente = new StringBuilder();
            
            if (archivoXML.exists()) {
                // Leer contenido existente
                String contenido = new String(Files.readAllBytes(archivoXML.toPath()));
                
                // Verificar si la temporada ya existe
                String idTemporada = generarIdTemporada(temporada.getNombre());
                if (contenido.contains("id=\"" + idTemporada + "\"")) {
                    int respuesta = JOptionPane.showConfirmDialog(null,
                        "⚠️ La temporada '" + temporada.getNombre() + "' ya existe en general.xml\n\n" +
                        "¿Desea reemplazarla?",
                        "Temporada existente",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    
                    if (respuesta != JOptionPane.YES_OPTION) {
                        GestorLog.info("Exportación cancelada - temporada ya existe");
                        return false;
                    }
                    
                    // Eliminar temporada existente del contenido
                    contenido = eliminarTemporadaExistente(contenido, idTemporada);
                }
                
                // Separar antes y después de </temporadas>
                int posicionCierre = contenido.lastIndexOf("</temporadas>");
                
                if (posicionCierre != -1) {
                    xmlExistente.append(contenido.substring(0, posicionCierre));
                } else {
                    // XML malformado, recrear estructura
                    GestorLog.advertencia("XML malformado, recreando estructura base");
                    xmlExistente = crearEstructuraBase();
                }
                
            } else {
                // Crear estructura base nueva
                xmlExistente = crearEstructuraBase();
                GestorLog.info("Creando nuevo archivo general.xml");
            }
            
            // Agregar nueva temporada
            exportarTemporadaAlXML(xmlExistente, temporada);
            
            // Cerrar tags
            xmlExistente.append("    </temporadas>\n");
            xmlExistente.append("</federacionBalonmano>\n");
            
            // Escribir archivo completo
            try (FileWriter writer = new FileWriter(archivoXML)) {
                writer.write(xmlExistente.toString());
            }
            
            GestorLog.exito("Temporada '" + temporada.getNombre() + "' exportada a general.xml");
            JOptionPane.showMessageDialog(null,
                "✅ Temporada exportada exitosamente\n\n" +
                "📄 Archivo: " + ARCHIVO_XML + "\n" +
                "📁 Temporada: " + temporada.getNombre() + "\n" +
                "🖼️ Imágenes actualizadas en: ./imagenes/",
                "Exportación exitosa",
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
            
        } catch (IOException e) {
            GestorLog.error("Error al exportar temporada: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "❌ Error al exportar la temporada:\n" + e.getMessage(),
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
        xml.append("<federacionBalonmano xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        xml.append("xsi:noNamespaceSchemaLocation=\"general.xsd\">\n\n");
        xml.append("    <temporadas>\n");
        return xml;
    }
    
    /**
     * Elimina una temporada existente del contenido XML
     */
    private String eliminarTemporadaExistente(String contenido, String idTemporada) {
        try {
            // Buscar inicio de temporada
            String inicioTag = "<temporada id=\"" + idTemporada + "\">";
            int posInicio = contenido.indexOf(inicioTag);
            
            if (posInicio == -1) return contenido;
            
            // Buscar cierre de temporada (contar niveles de anidación)
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
            
            // Eliminar temporada completa
            String antes = contenido.substring(0, posInicio);
            String despues = contenido.substring(posActual);
            
            GestorLog.info("Temporada existente eliminada del XML");
            return antes + despues;
            
        } catch (Exception e) {
            GestorLog.error("Error al eliminar temporada existente: " + e.getMessage());
            return contenido;
        }
    }
    
    /**
     * Exporta una temporada completa al StringBuilder XML
     */
    private void exportarTemporadaAlXML(StringBuilder xml, Temporada temporada) {
        String idTemporada = generarIdTemporada(temporada.getNombre());
        
        xml.append("        <temporada id=\"").append(idTemporada).append("\">\n");
        
        // Exportar equipos
        xml.append("            <equipos>\n");
        
        int contadorEquipo = 1;
        for (Equipo equipo : temporada.getEquiposParticipantes()) {
            exportarEquipo(xml, equipo, contadorEquipo, temporada);
            contadorEquipo++;
        }
        
        xml.append("            </equipos>\n");
        
        // Exportar jornadas
        xml.append("            <jornadas>\n");
        
        int contadorJornada = 1;
        for (Jornada jornada : temporada.getListaJornadas()) {
            exportarJornada(xml, jornada, contadorJornada, temporada);
            contadorJornada++;
        }
        
        xml.append("            </jornadas>\n");
        xml.append("        </temporada>\n\n");
    }
    
    /**
     * Exporta un equipo con todos sus jugadores
     */
    private void exportarEquipo(StringBuilder xml, Equipo equipo, int numeroEquipo, Temporada temporada) {
        String idEquipo = String.format("E%03d", numeroEquipo);
        
        xml.append("                <equipo id=\"").append(idEquipo).append("\">\n");
        xml.append("                    <nombre>").append(escaparXML(equipo.getNombre())).append("</nombre>\n");
        
        // Calcular estadísticas del equipo
        EstadisticasEquipo stats = calcularEstadisticasEquipo(equipo, temporada);
        
        xml.append("                    <ganados>").append(stats.ganados).append("</ganados>\n");
        xml.append("                    <perdidos>").append(stats.perdidos).append("</perdidos>\n");
        xml.append("                    <empatados>").append(stats.empatados).append("</empatados>\n");
        xml.append("                    <golesFavor>").append(stats.golesFavor).append("</golesFavor>\n");
        xml.append("                    <golesContra>").append(stats.golesContra).append("</golesContra>\n");
        
        // Exportar jugadores
        xml.append("                    <jugadores>\n");
        
        int contadorJugador = 1;
        for (Jugador jugador : equipo.getPlantilla()) {
            exportarJugador(xml, jugador, contadorJugador, idEquipo, equipo.getNombre());
            contadorJugador++;
        }
        
        xml.append("                    </jugadores>\n");
        
        // Escudo - Copiar imagen y generar ruta relativa
        String rutaEscudoRelativa = copiarEscudo(equipo, numeroEquipo);
        xml.append("                    <escudo url=\"").append(escaparXML(rutaEscudoRelativa)).append("\"/>\n");
        
        xml.append("                </equipo>\n");
    }
    
    /**
     * Exporta un jugador
     */
    private void exportarJugador(StringBuilder xml, Jugador jugador, int numeroJugador, String idEquipo, String nombreEquipo) {
        String idJugador = String.format("JU%03d", numeroJugador);
        
        xml.append("                        <jugador id=\"").append(idJugador).append("\" equipo=\"").append(idEquipo).append("\">\n");
        xml.append("                            <nombre>").append(escaparXML(jugador.getNombre())).append("</nombre>\n");
        xml.append("                            <nacionalidad>").append(escaparXML(jugador.getNacionalidad())).append("</nacionalidad>\n");
        xml.append("                            <altura>").append(escaparXML(jugador.getAltura())).append("</altura>\n");
        xml.append("                            <peso>").append(escaparXML(jugador.getPeso())).append("</peso>\n");
        xml.append("                            <dorsal>").append(jugador.getDorsal()).append("</dorsal>\n");
        xml.append("                            <posicion>").append(escaparXML(jugador.getPosicion())).append("</posicion>\n");
        
        // Foto - Copiar imagen y generar ruta relativa
        String fotoUrlRelativa = copiarFotoJugador(jugador, nombreEquipo, numeroJugador);
        xml.append("                            <foto url=\"").append(escaparXML(fotoUrlRelativa)).append("\"/>\n");
        
        xml.append("                        </jugador>\n");
    }
    
    /**
     * Copia el escudo de un equipo a la carpeta de exportación
     */
    private String copiarEscudo(Equipo equipo, int numeroEquipo) {
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
            
            GestorLog.info("Escudo copiado: " + equipo.getNombre() + " → " + nombreNormalizado);
            
            return "./imagenes/imagenes_Logos/" + nombreNormalizado;
            
        } catch (IOException e) {
            GestorLog.error("Error al copiar escudo de " + equipo.getNombre() + ": " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Copia la foto de un jugador a la carpeta de exportación
     */
    private String copiarFotoJugador(Jugador jugador, String nombreEquipo, int numeroJugador) {
        String rutaOrigen = jugador.getFotoURL();
        
        if (rutaOrigen == null || rutaOrigen.isEmpty()) {
            return "";
        }
        
        try {
            File archivoOrigen = new File(rutaOrigen);
            
            if (!archivoOrigen.exists()) {
                GestorLog.advertencia("Foto no encontrada: " + rutaOrigen);
                return "";
            }
            
            String extension = obtenerExtension(archivoOrigen.getName());
            String nombreNormalizado = normalizarNombre(jugador.getNombre()) + "_" + 
                                       normalizarNombre(nombreEquipo) + extension;
            
            File carpetaJugadores = new File(carpetaImagenes, "imagenes_Jugadores");
            File archivoDestino = new File(carpetaJugadores, nombreNormalizado);
            
            Files.copy(archivoOrigen.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            GestorLog.info("Foto copiada: " + jugador.getNombre() + " → " + nombreNormalizado);
            
            return "./imagenes/imagenes_Jugadores/" + nombreNormalizado;
            
        } catch (IOException e) {
            GestorLog.error("Error al copiar foto de " + jugador.getNombre() + ": " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Normaliza un nombre para usar como nombre de archivo
     */
    private String normalizarNombre(String nombre) {
        return nombre.replaceAll("[^a-zA-Z0-9]", "_")
                    .replaceAll("_+", "_")
                    .toUpperCase();
    }
    
    /**
     * Obtiene la extensión de un archivo
     */
    private String obtenerExtension(String nombreArchivo) {
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto > 0) {
            return nombreArchivo.substring(ultimoPunto);
        }
        return ".png";
    }
    
    /**
     * Exporta una jornada con todos sus partidos
     */
    private void exportarJornada(StringBuilder xml, Jornada jornada, int numeroJornada, Temporada temporada) {
        String idJornada = String.format("J%03d", numeroJornada);
        
        xml.append("                <jornada id=\"").append(idJornada).append("\">\n");
        xml.append("                    <partidos>\n");
        
        int contadorPartido = 1;
        for (Partido partido : jornada.getListaPartidos()) {
            exportarPartido(xml, partido, numeroJornada, contadorPartido, temporada);
            contadorPartido++;
        }
        
        xml.append("                    </partidos>\n");
        xml.append("                </jornada>\n");
    }
    
    /**
     * Exporta un partido
     */
    private void exportarPartido(StringBuilder xml, Partido partido, int numeroJornada, int numeroPartido, Temporada temporada) {
        String idPartido = String.format("P%d_%d", numeroJornada, numeroPartido);
        
        String idLocal = obtenerIdEquipo(partido.getEquipoLocal(), temporada);
        String idVisitante = obtenerIdEquipo(partido.getEquipoVisitante(), temporada);
        
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
     * Calcula las estadísticas de un equipo en una temporada
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
     * Obtiene el ID de un equipo en formato E00X
     */
    private String obtenerIdEquipo(Equipo equipo, Temporada temporada) {
        int posicion = 1;
        for (Equipo e : temporada.getEquiposParticipantes()) {
            if (e.getNombre().equals(equipo.getNombre())) {
                return String.format("E%03d", posicion);
            }
            posicion++;
        }
        return "E001";
    }
    
    /**
     * Genera un ID válido para la temporada
     */
    /**
     * Genera un ID basado directamente en el nombre de la temporada
     */
    private String generarIdTemporada(String nombreTemporada) {
        if (nombreTemporada == null || nombreTemporada.isEmpty()) {
            return "temp_" + System.currentTimeMillis();
        }
        
        // El ID de un XML no puede contener espacios. 
        // Reemplazamos espacios por guiones bajos para que sea válido.
        return nombreTemporada.trim().replace(" ", "_");
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
     * Clase auxiliar para almacenar estadísticas de un equipo
     */
    private static class EstadisticasEquipo {
        int ganados = 0;
        int perdidos = 0;
        int empatados = 0;
        int golesFavor = 0;
        int golesContra = 0;
    }
}
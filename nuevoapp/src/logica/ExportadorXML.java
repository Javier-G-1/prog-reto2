package logica;

import gestion.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExportadorXML {
    
    private DatosFederacion datosFederacion;
    private File carpetaExportacion;
    private File carpetaImagenes;
    
    public ExportadorXML(DatosFederacion datos) {
        this.datosFederacion = datos;
    }
    
    /**
     * Exporta todos los datos de la federación a un archivo XML con imágenes
     * @return true si la exportación fue exitosa, false en caso contrario
     */
    public boolean exportarTodo() {
        return exportarConDialogo(null);
    }
    
    /**
     * Exporta una temporada específica a un archivo XML con imágenes
     * @param temporada La temporada a exportar (null para exportar todas)
     * @return true si la exportación fue exitosa, false en caso contrario
     */
    public boolean exportarTemporada(Temporada temporada) {
        return exportarConDialogo(temporada);
    }
    
    /**
     * Método privado que maneja la exportación con diálogo
     */
    private boolean exportarConDialogo(Temporada temporadaEspecifica) {
        // Diálogo para seleccionar ubicación y nombre del archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar datos de la federación");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos XML (*.xml)", "xml"));
        
        String nombreArchivo = temporadaEspecifica != null 
            ? "federacion_" + temporadaEspecifica.getNombre().replaceAll("[^a-zA-Z0-9]", "_") + ".xml"
            : "federacion_balonmano.xml";
        
        fileChooser.setSelectedFile(new File(nombreArchivo));
        
        int resultado = fileChooser.showSaveDialog(null);
        
        if (resultado != JFileChooser.APPROVE_OPTION) {
            GestorLog.info("Exportación cancelada por el usuario");
            return false;
        }
        
        File archivo = fileChooser.getSelectedFile();
        
        // Asegurar que el archivo tenga extensión .xml
        if (!archivo.getName().toLowerCase().endsWith(".xml")) {
            archivo = new File(archivo.getAbsolutePath() + ".xml");
        }
        
        try {
            // Crear estructura de carpetas
            carpetaExportacion = archivo.getParentFile();
            carpetaImagenes = new File(carpetaExportacion, "imagenes");
            
            // Crear subcarpetas para imágenes
            File carpetaLogos = new File(carpetaImagenes, "imagenes_Logos");
            File carpetaJugadores = new File(carpetaImagenes, "imagenes_Jugadores");
            
            carpetaLogos.mkdirs();
            carpetaJugadores.mkdirs();
            
            GestorLog.info("Carpetas de imágenes creadas: " + carpetaImagenes.getAbsolutePath());
            
            // Exportar XML y copiar imágenes
            exportarAXML(archivo, temporadaEspecifica);
            
            String mensajeExito = temporadaEspecifica != null
                ? "✅ Exportación de " + temporadaEspecifica.getNombre() + " completada\n\n"
                : "✅ Exportación de todas las temporadas completada\n\n";
            
            GestorLog.exito("Datos exportados exitosamente a: " + archivo.getAbsolutePath());
            JOptionPane.showMessageDialog(null, 
                mensajeExito +
                "📄 XML: " + archivo.getName() + "\n" +
                "📁 Carpeta: " + carpetaExportacion.getAbsolutePath() + "\n" +
                "🖼️ Imágenes copiadas a: ./imagenes/",
                "Exportación exitosa",
                JOptionPane.INFORMATION_MESSAGE);
            return true;
            
        } catch (IOException e) {
            GestorLog.error("Error al exportar datos: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "❌ Error al exportar los datos:\n" + e.getMessage(),
                "Error de exportación",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Genera el contenido XML y lo escribe en el archivo
     */
    private void exportarAXML(File archivo, Temporada temporadaEspecifica) throws IOException {
        StringBuilder xml = new StringBuilder();
        
        // Cabecera XML
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<federacionBalonmano xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
        xml.append("xsi:noNamespaceSchemaLocation=\"general.xsd\">\n\n");
        
        // Inicio de temporadas
        xml.append("    <temporadas>\n");
        
        // Exportar solo la temporada especificada o todas
        if (temporadaEspecifica != null) {
            exportarTemporada(xml, temporadaEspecifica);
        } else {
            for (Temporada temporada : datosFederacion.getListaTemporadas()) {
                exportarTemporada(xml, temporada);
            }
        }
        
        // Cierre de temporadas
        xml.append("    </temporadas>\n");
        xml.append("</federacionBalonmano>\n");
        
        // Escribir al archivo
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(xml.toString());
        }
    }
    
    /**
     * Exporta una temporada completa
     */
    private void exportarTemporada(StringBuilder xml, Temporada temporada) {
        // ID de temporada (convertir nombre a formato válido)
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
        xml.append("        </temporada>\n");
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
     * @return Ruta relativa del escudo para el XML
     */
    private String copiarEscudo(Equipo equipo, int numeroEquipo) {
        String rutaOrigen = equipo.getRutaEscudo();
        
        if (rutaOrigen == null || rutaOrigen.isEmpty()) {
            return ""; // Sin escudo
        }
        
        try {
            File archivoOrigen = new File(rutaOrigen);
            
            if (!archivoOrigen.exists()) {
                GestorLog.advertencia("Escudo no encontrado: " + rutaOrigen);
                return "";
            }
            
            // Generar nombre normalizado basado en el nombre del equipo
            String extension = obtenerExtension(archivoOrigen.getName());
            String nombreNormalizado = normalizarNombre(equipo.getNombre()) + extension;
            
            File carpetaLogos = new File(carpetaImagenes, "imagenes_Logos");
            File archivoDestino = new File(carpetaLogos, nombreNormalizado);
            
            // Copiar archivo
            Files.copy(archivoOrigen.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            GestorLog.info("Escudo copiado: " + equipo.getNombre() + " → " + nombreNormalizado);
            
            // Retornar ruta relativa para el XML
            return "./imagenes/imagenes_Logos/" + nombreNormalizado;
            
        } catch (IOException e) {
            GestorLog.error("Error al copiar escudo de " + equipo.getNombre() + ": " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Copia la foto de un jugador a la carpeta de exportación
     * @return Ruta relativa de la foto para el XML
     */
    private String copiarFotoJugador(Jugador jugador, String nombreEquipo, int numeroJugador) {
        String rutaOrigen = jugador.getFotoURL();
        
        if (rutaOrigen == null || rutaOrigen.isEmpty()) {
            return ""; // Sin foto
        }
        
        try {
            File archivoOrigen = new File(rutaOrigen);
            
            if (!archivoOrigen.exists()) {
                GestorLog.advertencia("Foto no encontrada: " + rutaOrigen);
                return "";
            }
            
            // Generar nombre normalizado
            String extension = obtenerExtension(archivoOrigen.getName());
            String nombreNormalizado = normalizarNombre(jugador.getNombre()) + "_" + 
                                       normalizarNombre(nombreEquipo) + extension;
            
            File carpetaJugadores = new File(carpetaImagenes, "imagenes_Jugadores");
            File archivoDestino = new File(carpetaJugadores, nombreNormalizado);
            
            // Copiar archivo
            Files.copy(archivoOrigen.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            GestorLog.info("Foto copiada: " + jugador.getNombre() + " → " + nombreNormalizado);
            
            // Retornar ruta relativa para el XML
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
        return ".png"; // Extensión por defecto
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
        
        // Obtener IDs de equipos
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
                    // Equipo jugó como local
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
                    // Equipo jugó como visitante
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
        return "E001"; // Fallback
    }
    
    /**
     * Genera un ID válido para la temporada
     */
    private String generarIdTemporada(String nombreTemporada) {
        // Convertir "Temporada 2024/25" a "2024_2025"
        String id = nombreTemporada.replaceAll("[^0-9/]", "").replace("/", "_");
        
        // Si el segundo año tiene solo 2 dígitos, completarlo
        String[] partes = id.split("_");
        if (partes.length == 2 && partes[1].length() == 2) {
            String siglo = partes[0].substring(0, 2);
            id = partes[0] + "_" + siglo + partes[1];
        }
        
        return id.isEmpty() ? "temp_" + System.currentTimeMillis() : id;
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
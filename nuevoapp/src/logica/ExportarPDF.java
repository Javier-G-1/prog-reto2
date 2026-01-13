package logica;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.List;

import logica.FilaClasificacion;
import gestion.Temporada;

public class ExportarPDF {

    /**
     * Exporta la clasificación de una temporada a un archivo PDF
     * @param temporada La temporada a exportar
     * @param filas Las filas de clasificación ordenadas
     * @param rutaArchivo Ruta donde guardar el PDF
     * @return true si se exportó correctamente, false en caso contrario
     */
    public static boolean exportarClasificacion(Temporada temporada, List<FilaClasificacion> filas, String rutaArchivo) {
        Document documento = new Document(PageSize.A4);
        
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();
            
            // ===== TÍTULO =====
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph titulo = new Paragraph("Clasificación - " + temporada.getNombre(), fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            documento.add(titulo);
            
            // ===== INFORMACIÓN DE LA TEMPORADA =====
            Font fuenteInfo = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            Paragraph info = new Paragraph("Generado el: " + sdf.format(new java.util.Date()), fuenteInfo);
            info.setAlignment(Element.ALIGN_CENTER);
            info.setSpacingAfter(20);
            documento.add(info);
            
            // ===== CREAR TABLA =====
            PdfPTable tabla = new PdfPTable(10); // 10 columnas
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            
            // Establecer anchos relativos de columnas
            float[] anchos = {1f, 3f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1.2f};
            tabla.setWidths(anchos);
            
            // ===== ENCABEZADOS =====
            Font fuenteHeader = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
            String[] headers = {"Pos", "Equipo", "PJ", "PG", "PE", "PP", "GF", "GC", "Dif", "PTS"};
            
            for (String header : headers) {
                PdfPCell celda = new PdfPCell(new Phrase(header, fuenteHeader));
                celda.setBackgroundColor(new BaseColor(45, 55, 140));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setPadding(8);
                tabla.addCell(celda);
            }
            
            // ===== DATOS =====
            Font fuenteDatos = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            int posicion = 1;
            
            for (FilaClasificacion fila : filas) {
                // Color alternado para filas
                BaseColor colorFondo = (posicion % 2 == 0) ? 
                    new BaseColor(240, 240, 240) : BaseColor.WHITE;
                
                agregarCelda(tabla, String.valueOf(posicion), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, fila.getEquipo(), fuenteDatos, colorFondo, Element.ALIGN_LEFT);
                agregarCelda(tabla, String.valueOf(fila.getPj()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getPg()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getPe()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getPp()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getGf()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getGc()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getDf()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                
                // Puntos en negrita
                Font fuentePuntos = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
                agregarCelda(tabla, String.valueOf(fila.getPuntos()), fuentePuntos, colorFondo, Element.ALIGN_CENTER);
                
                posicion++;
            }
            
            documento.add(tabla);
            
            // ===== PIE DE PÁGINA =====
            Paragraph pie = new Paragraph("\nPJ: Partidos Jugados | PG: Partidos Ganados | PE: Partidos Empatados | PP: Partidos Perdidos", 
                new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY));
            pie.setAlignment(Element.ALIGN_CENTER);
            pie.setSpacingBefore(15);
            documento.add(pie);
            
            Paragraph pie2 = new Paragraph("GF: Goles a Favor | GC: Goles en Contra | Dif: Diferencia de Goles | PTS: Puntos", 
                new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY));
            pie2.setAlignment(Element.ALIGN_CENTER);
            documento.add(pie2);
            
            documento.close();
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            if (documento.isOpen()) {
                documento.close();
            }
            return false;
        }
    }
    
    /**
     * Método auxiliar para agregar celdas a la tabla
     */
    private static void agregarCelda(PdfPTable tabla, String texto, Font fuente, BaseColor colorFondo, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(colorFondo);
        celda.setHorizontalAlignment(alineacion);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(6);
        celda.setBorder(Rectangle.BOTTOM);
        celda.setBorderColor(new BaseColor(200, 200, 200));
        tabla.addCell(celda);
    }
}
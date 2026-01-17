package logica;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import gestion.Temporada;

/**
 * Clase utilitaria para exportar la clasificación de una temporada a PDF.
 * <p>
 * Esta clase utiliza iText 5 para generar un archivo PDF con la tabla de clasificación,
 * incluyendo título, fecha de generación, encabezados de tabla y pie explicativo.
 * </p>
 */
public class ExportarPDF {
    
    /**
     * Exporta la clasificación de una temporada a un archivo PDF.
     *
     * @param temporada La temporada de la que se desea exportar la clasificación. No puede ser null.
     * @param filas Lista de filas de clasificación, ordenadas por posición. No puede ser null.
     * @param rutaArchivo Ruta completa del archivo PDF donde se guardará la clasificación.
     * @return true si el PDF se generó correctamente; false si ocurrió un error.
     * @throws IllegalArgumentException si temporada o filas son null.
     */
    public static boolean exportarClasificacion(Temporada temporada, List<FilaClasificacion> filas, String rutaArchivo) {
        if (temporada == null || filas == null) {
            throw new IllegalArgumentException("La temporada y las filas no pueden ser nulas.");
        }
        
        Document documento = new Document(PageSize.A4);
        
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
            documento.open();
            
            // ========== TÍTULO ==========
            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph titulo = new Paragraph("Clasificación - " + temporada.getNombre(), fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            documento.add(titulo);
            
            // ========== FECHA DE GENERACIÓN ==========
            Font fuenteFecha = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            String fechaActual = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            Paragraph fecha = new Paragraph("Generado el: " + fechaActual, fuenteFecha);
            fecha.setAlignment(Element.ALIGN_CENTER);
            fecha.setSpacingAfter(20);
            documento.add(fecha);
            
            // ========== CREAR TABLA ==========
            PdfPTable tabla = new PdfPTable(10); // 10 columnas
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10);
            
            // Anchos relativos de las columnas
            float[] anchos = {1f, 4f, 1.5f, 1f, 1f, 1f, 1f, 1.5f, 1.5f, 1.5f};
            tabla.setWidths(anchos);
            
            // ========== ENCABEZADOS ==========
            Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            BaseColor colorEncabezado = new BaseColor(45, 55, 140); // Azul oscuro
            
            agregarCelda(tabla, "Pos", fuenteEncabezado, colorEncabezado, Element.ALIGN_CENTER);
            agregarCelda(tabla, "Equipo", fuenteEncabezado, colorEncabezado, Element.ALIGN_LEFT);
            agregarCelda(tabla, "Pts", fuenteEncabezado, colorEncabezado, Element.ALIGN_CENTER);
            agregarCelda(tabla, "PJ", fuenteEncabezado, colorEncabezado, Element.ALIGN_CENTER);
            agregarCelda(tabla, "PG", fuenteEncabezado, colorEncabezado, Element.ALIGN_CENTER);
            agregarCelda(tabla, "PE", fuenteEncabezado, colorEncabezado, Element.ALIGN_CENTER);
            agregarCelda(tabla, "PP", fuenteEncabezado, colorEncabezado, Element.ALIGN_CENTER);
            agregarCelda(tabla, "GF", fuenteEncabezado, colorEncabezado, Element.ALIGN_CENTER);
            agregarCelda(tabla, "GC", fuenteEncabezado, colorEncabezado, Element.ALIGN_CENTER);
            agregarCelda(tabla, "DIF", fuenteEncabezado, colorEncabezado, Element.ALIGN_CENTER);
            
            // ========== FILAS DE DATOS ==========
            Font fuenteDatos = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            BaseColor colorFondoClaro = BaseColor.WHITE;
            BaseColor colorFondoOscuro = new BaseColor(240, 240, 240);
            
            // ⭐ FILTRAR el equipo fantasma "_SIN_EQUIPO_"
            int posicionReal = 1;
            for (int i = 0; i < filas.size(); i++) {
                FilaClasificacion fila = filas.get(i);
                
                // ⭐ SALTAR el equipo fantasma
                if (fila.getEquipo().equals("_SIN_EQUIPO_")) {
                    continue;
                }
                
                // ⭐ ASIGNAR LA POSICIÓN REAL (sin contar el fantasma)
                fila.setPosicion(posicionReal);
                
                BaseColor colorFondo = ((posicionReal - 1) % 2 == 0) ? colorFondoClaro : colorFondoOscuro;
                
                agregarCelda(tabla, String.valueOf(fila.getPosicion()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, fila.getEquipo(), fuenteDatos, colorFondo, Element.ALIGN_LEFT);
                agregarCelda(tabla, String.valueOf(fila.getPuntos()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getPj()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getPg()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getPe()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getPp()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getGf()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, String.valueOf(fila.getGc()), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                agregarCelda(tabla, fila.getDifFormateada(), fuenteDatos, colorFondo, Element.ALIGN_CENTER);
                
                // ⭐ INCREMENTAR posición solo para equipos reales
                posicionReal++;
            }
            
            documento.add(tabla);
            
            // ========== PIE DE PÁGINA ==========
            Font fuentePie = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);
            Paragraph pie = new Paragraph("\nPts: Puntos | PJ: Partidos Jugados | PG: Partidos Ganados | PE: Partidos Empatados | PP: Partidos Perdidos\nGF: Goles a Favor | GC: Goles en Contra | DIF: Diferencia de Goles", fuentePie);
            pie.setAlignment(Element.ALIGN_LEFT);
            pie.setSpacingBefore(15);
            documento.add(pie);
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }
    
    /**
     * Método auxiliar para agregar una celda a la tabla del PDF.
     *
     * @param tabla La tabla donde se agregará la celda.
     * @param texto Contenido de la celda.
     * @param fuente Fuente a usar en el texto.
     * @param colorFondo Color de fondo de la celda.
     * @param alineacion Alineación horizontal (por ejemplo, Element.ALIGN_CENTER).
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
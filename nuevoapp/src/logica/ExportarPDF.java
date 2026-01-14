package logica;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.util.List;

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

            // --- Aquí va el resto de tu código de generación del PDF ---

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (documento.isOpen()) {
                documento.close();
            }
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
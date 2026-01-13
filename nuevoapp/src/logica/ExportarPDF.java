package logica;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import gestion.Temporada;
import javax.swing.*;
import java.io.FileOutputStream;
import java.util.List;

public class ExportarPDF {
    
    public static void exportarClasificacion(Temporada temporada) {
        if (temporada == null) {
            JOptionPane.showMessageDialog(null, "No hay temporada seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar clasificación como PDF");
        fileChooser.setSelectedFile(new java.io.File("Clasificacion_" + temporada.getNombre() + ".pdf"));
        
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String ruta = fileChooser.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";
            
            try {
                generarPDF(ruta, temporada);
                JOptionPane.showMessageDialog(null, "PDF generado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private static void generarPDF(String ruta, Temporada temporada) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(ruta));
        document.open();
        
        // Usamos la fuente explícita de iText para evitar errores
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph titulo = new Paragraph("Clasificación: " + temporada.getNombre(), tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);
        
        Clasificacion clasificacion = CalculadoraClasificacion.calcular(temporada);
        List<FilaClasificacion> filas = clasificacion.getFilas();
        
        PdfPTable tabla = new PdfPTable(10);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{8f, 25f, 8f, 8f, 8f, 8f, 8f, 8f, 8f, 10f});
        
        // Encabezados
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        String[] headers = {"Pos", "Equipo", "PJ", "PG", "PE", "PP", "GF", "GC", "Dif", "PTS"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new BaseColor(45, 55, 140));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            tabla.addCell(cell);
        }
        
        // Datos
        Font dataFont = new Font(Font.FontFamily.HELVETICA, 9);
        int pos = 1;
        for (FilaClasificacion f : filas) {
            tabla.addCell(new Phrase(String.valueOf(pos++), dataFont));
            tabla.addCell(new Phrase(f.getEquipo(), dataFont));
            tabla.addCell(new Phrase(String.valueOf(f.getPj()), dataFont));
            tabla.addCell(new Phrase(String.valueOf(f.getPg()), dataFont));
            tabla.addCell(new Phrase(String.valueOf(f.getPe()), dataFont));
            tabla.addCell(new Phrase(String.valueOf(f.getPp()), dataFont));
            tabla.addCell(new Phrase(String.valueOf(f.getGf()), dataFont));
            tabla.addCell(new Phrase(String.valueOf(f.getGc()), dataFont));
            tabla.addCell(new Phrase(String.valueOf(f.getDf()), dataFont));
            tabla.addCell(new Phrase(String.valueOf(f.getPuntos()), 
                    new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD)));
        }
        
        document.add(tabla);
        document.close();
    }
}
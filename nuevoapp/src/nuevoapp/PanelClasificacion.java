package nuevoapp;

import javax.swing.*;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.awt.*;
import java.util.List;

import logica.CalculadoraClasificacion;
import logica.Clasificacion;
import logica.FilaClasificacion;
import gestion.Temporada;
import gestion.DatosFederacion;

public class PanelClasificacion extends JPanel {

    private static final long serialVersionUID = 1L;
    
    // ✅ ATRIBUTO PRINCIPAL - Solo una declaración
    private JComboBox<String> comboTemporadasClasificacion;
    private DatosFederacion datosFederacion;
    
    // ===== MODELOS =====
    private DefaultListModel<Integer> dlmPosicion = new DefaultListModel<>();
    private DefaultListModel<String> dlmEquipo = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPJ = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPG = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPE = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPP = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmGF = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmGC = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmDIF = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPTS = new DefaultListModel<>();

    // ===== LISTAS =====
    private JList<Integer> listPosicion;
    private JList<String> listEquipo;
    private JList<Integer> listPJ;
    private JList<Integer> listPE;
    private JList<Integer> listPG;
    private JList<Integer> listPP;
    private JList<Integer> listGF;
    private JList<Integer> listGC;
    private JList<Integer> listDIF;
    private JList<Integer> listPTS;
    
    // ============================================
    // ✅ GETTER PARA SINCRONIZACIÓN
    // ============================================
    /**
     * Retorna el combo de temporadas para sincronización externa
     * @return JComboBox con las temporadas
     */
    public JComboBox<String> getComboTemporadas() {
        return comboTemporadasClasificacion;
    }
    
    // ============================================
    // ✅ MÉTODO PÚBLICO PARA RECARGAR TEMPORADAS
    // ============================================
    /**
     * Recarga el combo de temporadas con los datos actuales
     * Útil cuando se crean nuevas temporadas desde otras vistas
     */
    public void recargarTemporadas() {
        if (comboTemporadasClasificacion == null || datosFederacion == null) {
            return;
        }
        
        // Guardar la selección actual
        Object seleccionActual = comboTemporadasClasificacion.getSelectedItem();
        
        // Limpiar y recargar
        comboTemporadasClasificacion.removeAllItems();
        for (Temporada t : datosFederacion.getListaTemporadas()) {
            comboTemporadasClasificacion.addItem(t.getNombre());
        }
        
        // Intentar restaurar la selección anterior
        if (seleccionActual != null) {
            comboTemporadasClasificacion.setSelectedItem(seleccionActual);
        }
    }
    
    // ============================================
    // CONSTRUCTOR
    // ============================================
    public PanelClasificacion(DatosFederacion datos) {
        this.datosFederacion = datos;
        setLayout(new BorderLayout());
        setBackground(new Color(20, 24, 31));
        
        // ===== PANEL SUPERIOR =====
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelHeader.setBackground(new Color(30, 34, 45));

        // ===== TÍTULO =====
        JLabel lblTitulo = new JLabel("Clasificación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelHeader.add(lblTitulo);
        
        // Separador visual
        panelHeader.add(Box.createHorizontalStrut(20));
        
        JLabel lblTemp = new JLabel("Temporada:");
        lblTemp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTemp.setForeground(Color.WHITE);
        panelHeader.add(lblTemp);
        
        

        // ===== COMBO DE TEMPORADAS =====
        comboTemporadasClasificacion = new JComboBox<>();
        comboTemporadasClasificacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Cargar temporadas inicialmente
        for (Temporada t : datosFederacion.getListaTemporadas()) {
            comboTemporadasClasificacion.addItem(t.getNombre());
        }
        
        // Listener para actualizar cuando se cambie de temporada
        comboTemporadasClasificacion.addActionListener(e -> {
            String nombreT = (String) comboTemporadasClasificacion.getSelectedItem();
            Temporada t = datosFederacion.buscarTemporadaPorNombre(nombreT);
            cargarClasificacion(t);
        });
        
        panelHeader.add(comboTemporadasClasificacion);
        
        // ✅ BOTÓN EXPORTAR PDF (dentro del panelHeader)
        JButton btnExportarPDF = new JButton("📄 Exportar PDF");
        btnExportarPDF.addActionListener(e -> exportarClasificacionPDF());
        btnExportarPDF.setForeground(Color.WHITE);
        btnExportarPDF.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExportarPDF.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnExportarPDF.setBackground(new Color(220, 53, 69));
        btnExportarPDF.setFocusPainted(false);
        panelHeader.add(btnExportarPDF);
        
        add(panelHeader, BorderLayout.NORTH);
        
        // ===== CUERPO (TABLA) =====
        JPanel panelBody = new JPanel(new GridLayout(1, 10, 0, 0)); 
        add(panelBody, BorderLayout.CENTER);

        panelBody.add(crearColumna("Pos", listPosicion = new JList<>(dlmPosicion)));
        panelBody.add(crearColumna("Equipo", listEquipo = new JList<>(dlmEquipo)));
        panelBody.add(crearColumna("PJ", listPJ = new JList<>(dlmPJ)));
        panelBody.add(crearColumna("PG", listPG = new JList<>(dlmPG)));
        panelBody.add(crearColumna("PE", listPE = new JList<>(dlmPE)));
        panelBody.add(crearColumna("PP", listPP = new JList<>(dlmPP)));
        panelBody.add(crearColumna("GF", listGF = new JList<>(dlmGF)));
        panelBody.add(crearColumna("GC", listGC = new JList<>(dlmGC)));
        panelBody.add(crearColumna("Dif", listDIF = new JList<>(dlmDIF)));
        panelBody.add(crearColumna("PTS", listPTS = new JList<>(dlmPTS)));
        
        // Cargar la primera temporada si existe
        if (comboTemporadasClasificacion.getItemCount() > 0) {
            comboTemporadasClasificacion.setSelectedIndex(0);
        }
    }

    // ===== CREA UNA COLUMNA =====
    private JPanel crearColumna(String titulo, JList<?> lista) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(100, 100, 100)));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(45, 55, 140));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        panelTitulo.add(lbl);

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lista.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);

        panel.add(panelTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ===== CARGAR CLASIFICACIÓN =====
    public void cargarClasificacion(Temporada temporada) {
        // Limpiar todos los modelos
        dlmPosicion.clear();
        dlmEquipo.clear();
        dlmPJ.clear();
        dlmPG.clear();
        dlmPE.clear();
        dlmPP.clear();
        dlmGF.clear();
        dlmGC.clear();
        dlmDIF.clear();
        dlmPTS.clear();

        if (temporada == null) return;
        
        // Calcular clasificación
        Clasificacion clasificacionObjeto = CalculadoraClasificacion.calcular(temporada);
        List<FilaClasificacion> filas = clasificacionObjeto.getFilas();

        int pos = 1;
        for (FilaClasificacion f : filas) {
            dlmPosicion.addElement(pos++);
            dlmEquipo.addElement(f.getEquipo());
            dlmPJ.addElement(f.getPj());
            dlmPG.addElement(f.getPg());
            dlmPE.addElement(f.getPe());
            dlmPP.addElement(f.getPp());
            dlmGF.addElement(f.getGf());
            dlmGC.addElement(f.getGc());
            dlmDIF.addElement(f.getDf());
            dlmPTS.addElement(f.getPuntos());
        }
    }
    
    // ===== EXPORTAR PDF =====
    public void exportarClasificacionPDF() {
        // 1. Elegir ubicación del archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Clasificación");
        fileChooser.setSelectedFile(new java.io.File("Clasificacion_" + comboTemporadasClasificacion.getSelectedItem() + ".pdf"));
        
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion != JFileChooser.APPROVE_OPTION) return;

        Document doc = new Document(PageSize.A4.rotate()); // Horizontal para que quepan las 10 columnas mejor
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(fileChooser.getSelectedFile()));
            doc.open();

            // --- FUENTES (Usando nombres completos para evitar conflictos con java.awt.Font) ---
            com.itextpdf.text.Font fontTitulo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontCabecera = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
            com.itextpdf.text.Font fontCelda = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);

            // --- TÍTULO ---
            String nombreTemp = (String) comboTemporadasClasificacion.getSelectedItem();
            Paragraph titulo = new Paragraph("CLASIFICACIÓN - TEMPORADA: " + nombreTemp, fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            doc.add(titulo);

            // --- TABLA ---
            // Definimos 10 columnas y sus anchos proporcionales (Equipo es más ancho)
            float[] columnWidths = {3f, 15f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 4f};
            PdfPTable tabla = new PdfPTable(columnWidths);
            tabla.setWidthPercentage(100);

            // Cabeceras
            String[] headers = {"Pos", "Equipo", "PJ", "PG", "PE", "PP", "GF", "GC", "Dif", "PTS"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontCabecera));
                cell.setBackgroundColor(new BaseColor(45, 55, 140));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                tabla.addCell(cell);
            }

            // --- CARGAR DATOS DESDE LOS MODELOS (DLM) ---
            int totalFilas = dlmEquipo.size();
            for (int i = 0; i < totalFilas; i++) {
                BaseColor bgFila = (i % 2 == 0) ? new BaseColor(245, 245, 245) : BaseColor.WHITE;

                // Añadimos cada celda extrayendo el dato del modelo correspondiente por el índice i
                tabla.addCell(crearCeldaPDF(dlmPosicion.get(i).toString(), fontCelda, bgFila, Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaPDF(dlmEquipo.get(i), fontCelda, bgFila, Element.ALIGN_LEFT));
                tabla.addCell(crearCeldaPDF(dlmPJ.get(i).toString(), fontCelda, bgFila, Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaPDF(dlmPG.get(i).toString(), fontCelda, bgFila, Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaPDF(dlmPE.get(i).toString(), fontCelda, bgFila, Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaPDF(dlmPP.get(i).toString(), fontCelda, bgFila, Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaPDF(dlmGF.get(i).toString(), fontCelda, bgFila, Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaPDF(dlmGC.get(i).toString(), fontCelda, bgFila, Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaPDF(dlmDIF.get(i).toString(), fontCelda, bgFila, Element.ALIGN_CENTER));
                tabla.addCell(crearCeldaPDF(dlmPTS.get(i).toString(), fontCelda, bgFila, Element.ALIGN_CENTER));
            }

            doc.add(tabla);
            doc.close();

            JOptionPane.showMessageDialog(this, "PDF generado con éxito en:\n" + fileChooser.getSelectedFile().getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para crear celdas con estilo rápidamente
    private PdfPCell crearCeldaPDF(String contenido, com.itextpdf.text.Font fuente, BaseColor colorFondo, int alineacion) {
        PdfPCell cell = new PdfPCell(new Phrase(contenido, fuente));
        cell.setBackgroundColor(colorFondo);
        cell.setHorizontalAlignment(alineacion);
        cell.setPadding(5);
        cell.setBorderColor(new BaseColor(200, 200, 200));
        return cell;
    }
}
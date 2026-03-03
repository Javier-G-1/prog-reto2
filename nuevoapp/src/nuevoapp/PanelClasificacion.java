package nuevoapp;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import vista.Paleta;

import logica.CalculadoraClasificacion;
import logica.Clasificacion;
import logica.FilaClasificacion;
import gestion.Temporada;
import gestion.DatosFederacion;
import logica.ExportarPDF;

/**
 * PanelClasificacion - Panel de clasificación con botón de exportar PDF
 * 
 * Muestra la tabla de clasificación y permite exportarla a PDF.
 * La temporada se controla desde VentanaMain a través del método actualizarClasificacion(Temporada).
 */
public class PanelClasificacion extends JPanel {

    private static final long serialVersionUID = 1L;
    
    /** Datos de la federación */
    private DatosFederacion datosFederacion;
    
    /** Temporada actualmente mostrada */
    private Temporada temporadaActual;
    
    /** Botón para exportar a PDF */
    private JButton btnExportarPDF;
    
    // ===== MODELOS DE COLUMNAS =====
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

    // ===== LISTAS VISUALES =====
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
    
    /**
     * Constructor del panel de clasificación.
     */
    public PanelClasificacion(DatosFederacion datos) {
        this.datosFederacion = datos;
        setLayout(new BorderLayout(5, 5));
        setBackground(Paleta.PRIMARIO);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ===== PANEL SUPERIOR CON BOTÓN EXPORTAR =====
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelSuperior.setBackground(Paleta.PRIMARIO);

        btnExportarPDF = new JButton("📄 Exportar PDF");
        btnExportarPDF.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportarPDF.setBackground(Paleta.ACENTO);
        btnExportarPDF.setForeground(Color.WHITE);
        btnExportarPDF.setFocusPainted(false);
        btnExportarPDF.setBorderPainted(false);
        btnExportarPDF.setPreferredSize(new Dimension(150, 35));
        btnExportarPDF.addActionListener(e -> exportarPDF());
        
        panelSuperior.add(btnExportarPDF);
        add(panelSuperior, BorderLayout.NORTH);
        
        // ===== PANEL CENTRAL CON COLUMNAS =====
        JPanel panelBody = new JPanel(new GridLayout(1, 10, 0, 0));
        panelBody.setBackground(Paleta.PRIMARIO);
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
    }

    /**
     * Crea un panel con título y lista para una columna.
     */
    private JPanel crearColumna(String titulo, JList<?> lista) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Paleta.PRIMARIO);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Paleta.NEUTRO1));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Paleta.SECUNDARIO);

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        panelTitulo.add(lbl);

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lista.setBackground(Paleta.NEUTRO2);
        lista.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.setBackground(Paleta.PRIMARIO);

        panel.add(panelTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    /**
     * ⭐ MÉTODO PÚBLICO para actualizar la clasificación desde VentanaMain
     * SOBRECARGADO: Acepta la temporada como parámetro
     */
    public void actualizarClasificacion(Temporada temporada) {
        this.temporadaActual = temporada;
        cargarClasificacion(temporada);
    }
    
    /**
     * ⭐ MÉTODO PÚBLICO para actualizar sin cambiar la temporada
     * (Recalcula con la temporada actual)
     */
    public void actualizarClasificacion() {
        if (temporadaActual != null) {
            cargarClasificacion(temporadaActual);
        }
    }

    /**
     * Carga la clasificación de la temporada en los modelos de JList.
     * ⭐ EXCLUYE el equipo fantasma "_SIN_EQUIPO_"
     */
    private void cargarClasificacion(Temporada temporada) {
        // Limpiar modelos
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

        if (temporada == null) {
            System.out.println("⚠PanelClasificacion: Temporada es null");
            return;
        }
        
        System.out.println(" Actualizando clasificación: " + temporada.getNombre());
        
        try {
            Clasificacion clasificacionObjeto = CalculadoraClasificacion.calcular(temporada);
            List<FilaClasificacion> filas = clasificacionObjeto.getFilas();

            System.out.println("✅ Filas de clasificación cargadas: " + filas.size());

            // ⭐ FILTRAR el equipo fantasma "_SIN_EQUIPO_"
            int pos = 1;
            for (FilaClasificacion f : filas) {
                // ⭐ SALTAR el equipo fantasma
                if (f.getEquipo().equals("_SIN_EQUIPO_")) {
                    continue;
                }
                
                // Solo agregar equipos reales
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
            
            // Forzar actualización visual
            revalidate();
            repaint();
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar clasificación: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Exporta la clasificación actual a PDF.
     */
    /**
     * Exporta la clasificación actual a PDF directamente en la carpeta de Descargas.
     */
    public void exportarPDF() {
        if (temporadaActual == null) {
            JOptionPane.showMessageDialog(this,
                "No hay ninguna temporada cargada.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (dlmEquipo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay datos de clasificación para exportar.\n" +
                "Asegúrate de que hay partidos jugados en la temporada.",
                "Sin datos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // ⭐ OBTENER LA RUTA DE DESCARGAS
        String carpetaDescargas = System.getProperty("user.home") + "/Downloads";
        String nombreArchivo = "Clasificacion_" + temporadaActual.getNombre() + ".pdf";
        String rutaArchivo = carpetaDescargas + "/" + nombreArchivo;
        
        // ⭐ SI EL ARCHIVO YA EXISTE, agregar un número
        java.io.File archivo = new java.io.File(rutaArchivo);
        int contador = 1;
        while (archivo.exists()) {
            nombreArchivo = "Clasificacion_" + temporadaActual.getNombre() + "_(" + contador + ").pdf";
            rutaArchivo = carpetaDescargas + "/" + nombreArchivo;
            archivo = new java.io.File(rutaArchivo);
            contador++;
        }
        
        Clasificacion clasificacion = CalculadoraClasificacion.calcular(temporadaActual);
        List<FilaClasificacion> filas = clasificacion.getFilas();
        
        boolean exito = ExportarPDF.exportarClasificacion(temporadaActual, filas, rutaArchivo);
        
        if (exito) {
            int opcion = JOptionPane.showConfirmDialog(this,
                "PDF exportado correctamente en:\n" + rutaArchivo + "\n\n¿Deseas abrir el archivo?",
                "Éxito",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
            
            if (opcion == JOptionPane.YES_OPTION) {
                try {
                    java.awt.Desktop.getDesktop().open(new java.io.File(rutaArchivo));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo abrir el PDF automáticamente.\nÁbrelo manualmente desde:\n" + rutaArchivo,
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al exportar el PDF. Verifica los permisos de escritura.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
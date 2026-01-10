package nuevoapp;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import logica.CalculadoraClasificacion;
import logica.FilaClasificacion;
import gestion.Temporada;

public class PanelClasificacion extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ===== MODELOS =====
    private DefaultListModel<Integer> dlmPosicion = new DefaultListModel<>();
    private DefaultListModel<String> dlmEquipo = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPJ = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPG = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPE = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPP = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmGF = new DefaultListModel<>();
    private DefaultListModel<Integer> dlmGC= new DefaultListModel<>();
    private DefaultListModel<Integer> dlmDIF= new DefaultListModel<>();
    private DefaultListModel<Integer> dlmPTS= new DefaultListModel<>();

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

    public PanelClasificacion() {

        setLayout(new BorderLayout());
        setBackground(new Color(20, 24, 31));

        // ===== TITULO =====
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(128, 128, 128));

        JLabel lblTitulo = new JLabel("Clasificación");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // ===== CUERPO =====
        JPanel panelBody = new JPanel(new GridLayout(0, 8, 0, 0));
        add(panelBody, BorderLayout.CENTER);

        panelBody.add(crearColumna("Pos", listPosicion = new JList<>(dlmPosicion)));
        panelBody.add(crearColumna("Equipo", listEquipo = new JList<>(dlmEquipo)));
        panelBody.add(crearColumna("PJ", listPJ = new JList<>(dlmPJ)));
		panelBody.add(crearColumna("PE", listPE= new JList<>(dlmPE)));
        panelBody.add(crearColumna("PP", listPP = new JList<>(dlmPP)));
        panelBody.add(crearColumna("GF", listGF = new JList<>(dlmGF)));
        panelBody.add(crearColumna("GC", listGC = new JList<>(dlmGC)));
        panelBody.add(crearColumna("Dif", listDIF = new JList<>(dlmDIF)));
		panelBody.add(crearColumna("PTS", listPTS = new JList<>(dlmPTS)));
    }

    // ===== CREA UNA COLUMNA =====
    private JPanel crearColumna(String titulo, JList<?> lista) {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(180, 180, 180));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));

        panelTitulo.add(lbl);

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);

        panel.add(panelTitulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ===== CARGAR CLASIFICACIÓN =====
    public void cargarClasificacion(Temporada temporada) {

        dlmPosicion.clear();
        dlmEquipo.clear();
        dlmPJ.clear();
        dlmPG.clear();      
        dlmPP.clear();     
        dlmGF.clear();      
        dlmDIF.clear();     
        dlmPTS.clear();     

        if (temporada == null) return;

        List<FilaClasificacion> filas = CalculadoraClasificacion.calcular(temporada);

        int pos = 1;
        for (FilaClasificacion f : filas) {
            dlmPosicion.addElement(pos++);
            dlmEquipo.addElement(f.getEquipo());
            dlmPJ.addElement(f.getPj());
            dlmPG.addElement(f.getPg());
            dlmPP.addElement(f.getPp());
            dlmGF.addElement(f.getGf());
            dlmGC.addElement(f.getGc());
            dlmDIF.addElement(f.getDf());
           // dlmPTS.addElement(f.getPTS()); // 
        }
    }

}
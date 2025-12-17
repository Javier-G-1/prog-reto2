package nuevoapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class VentanaPrincipal extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelContenido;
    private CardLayout layoutTarjetas;
    private JButton botonInicio, botonTemporada, botonEquipos, botonPartidos, botonResultados, botonCerrarSesion;
    
    private static final String TARJETA_INICIO = "inicio";
    private static final String TARJETA_TEMPORADAS = "temporadas";
    private static final String TARJETA_EQUIPOS = "equipos";
    private static final String TARJETA_PARTIDOS = "partidos";
    private static final String TARJETA_RESULTADOS = "resultados";
    
    private static final Color COLOR_FONDO = new Color(240, 240, 240);
    private static final Color COLOR_MENU = new Color(45, 45, 45);
    private static final Color COLOR_AZUL = new Color(70, 130, 220);
    private static final Color COLOR_BLANCO = Color.WHITE;
    private static final Color COLOR_TEXTO = new Color(50, 50, 50);
    private static final Color COLOR_VERDE = new Color(60, 180, 75);
    private static final Color COLOR_AMARILLO = new Color(255, 200, 50);
    private static final Color COLOR_NARANJA = new Color(255, 150, 50);

    public VentanaPrincipal() {
    	 ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
         setIconImage(icono.getImage());
        setTitle("Federación de Balonmano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);
        
        crearMenuLateral();
        crearPanelContenido();
        mostrarTarjeta(TARJETA_INICIO);
        actualizarBotonSeleccionado(botonInicio);
    }

    private void crearMenuLateral() {
        JPanel panelLateral = new JPanel(new BorderLayout());
        panelLateral.setPreferredSize(new Dimension(250, 0));
        panelLateral.setBackground(COLOR_MENU);
        panelLateral.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel titulo = new JLabel("Balonmano");
        titulo.setForeground(COLOR_BLANCO);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(new EmptyBorder(10, 5, 30, 5));

        JPanel panelMenu = new JPanel(new GridLayout(5, 1, 0, 15));
        panelMenu.setBackground(COLOR_MENU);

        botonInicio = crearBotonMenu("Inicio");
        botonTemporada = crearBotonMenu("Temporadas");
        botonEquipos = crearBotonMenu("Equipos");
        botonPartidos = crearBotonMenu("Partidos");
        botonResultados = crearBotonMenu("Resultados");

        panelMenu.add(botonInicio);
        panelMenu.add(botonTemporada);
        panelMenu.add(botonEquipos);
        panelMenu.add(botonPartidos);
        panelMenu.add(botonResultados);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_MENU);
        panelSuperior.add(titulo, BorderLayout.NORTH);
        panelSuperior.add(panelMenu, BorderLayout.CENTER);

        botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.setPreferredSize(new Dimension(220, 40));
        botonCerrarSesion.setBackground(new Color(0, 0, 128));
        botonCerrarSesion.setForeground(COLOR_BLANCO);
        botonCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 14));
        botonCerrarSesion.setFocusPainted(false);
        botonCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonCerrarSesion.addActionListener(this);

        panelLateral.add(panelSuperior, BorderLayout.NORTH);
        panelLateral.add(botonCerrarSesion, BorderLayout.SOUTH);
        getContentPane().add(panelLateral, BorderLayout.WEST);
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(220, 45));
        boton.setFont(new Font("Arial", Font.PLAIN, 15));
        boton.setForeground(COLOR_BLANCO);
        boton.setBackground(COLOR_MENU);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addActionListener(this);
        return boton;
    }

    private void crearPanelContenido() {
        layoutTarjetas = new CardLayout();
        panelContenido = new JPanel(layoutTarjetas);
        panelContenido.setBackground(new Color(240, 240, 240));

        panelContenido.add(crearPanelInicio(), TARJETA_INICIO);
        panelContenido.add(crearPanelTemporadas(), TARJETA_TEMPORADAS);
        panelContenido.add(crearPanelEquipos(), TARJETA_EQUIPOS);
        panelContenido.add(crearPanelPartidos(), TARJETA_PARTIDOS);
        panelContenido.add(crearPanelResultados(), TARJETA_RESULTADOS);

        getContentPane().add(panelContenido, BorderLayout.CENTER);
    }

    private void mostrarTarjeta(String tarjeta) {
        layoutTarjetas.show(panelContenido, tarjeta);
    }

    private void actualizarBotonSeleccionado(JButton activo) {
        JButton[] botones = {botonInicio, botonTemporada, botonEquipos, botonPartidos, botonResultados};
        for (JButton btn : botones) {
            if (btn == activo) {
                btn.setBackground(COLOR_AZUL);
            } else {
                btn.setBackground(COLOR_MENU);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonInicio) {
            mostrarTarjeta(TARJETA_INICIO);
            actualizarBotonSeleccionado(botonInicio);
        } else if (e.getSource() == botonTemporada) {
            mostrarTarjeta(TARJETA_TEMPORADAS);
            actualizarBotonSeleccionado(botonTemporada);
        } else if (e.getSource() == botonEquipos) {
            mostrarTarjeta(TARJETA_EQUIPOS);
            actualizarBotonSeleccionado(botonEquipos);
        } else if (e.getSource() == botonPartidos) {
            mostrarTarjeta(TARJETA_PARTIDOS);
            actualizarBotonSeleccionado(botonPartidos);
        } else if (e.getSource() == botonResultados) {
            mostrarTarjeta(TARJETA_RESULTADOS);
            actualizarBotonSeleccionado(botonResultados);
        } else if (e.getSource() == botonCerrarSesion) {
            JOptionPane.showMessageDialog(this, "Sesión cerrada", "Adiós", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    private JPanel crearPanelInicio() {
        JPanel panelInicio = new JPanel(new BorderLayout());
        panelInicio.setBackground(new Color(35, 31, 124));
        panelInicio.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titulo = new JLabel("FEDERACIÓN ESPAÑOLA DE BALONMANO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(COLOR_TEXTO);
        titulo.setBorder(new EmptyBorder(20, 0, 30, 0));

        JLabel imagen = new JLabel();
        imagen.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/assets/BALON.jpg"));
            Image img = icono.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            imagen.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imagen.setText("Imagen no disponible");
            imagen.setForeground(Color.GRAY);
        }

        panelInicio.add(titulo, BorderLayout.NORTH);
        panelInicio.add(imagen, BorderLayout.CENTER);
        return panelInicio;
    }

    private JPanel crearPanelTemporadas() {
        JPanel panelTemporada = new JPanel(new BorderLayout());
        panelTemporada.setBackground(new Color(0, 0, 0));
        panelTemporada.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel cabeceraTemporada = new JPanel(new BorderLayout());
        cabeceraTemporada.setBackground(new Color(240, 240, 240));
        cabeceraTemporada.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitulo = new JLabel("Gestión de Temporadas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_TEXTO);

        JButton btnCrearTemporada = new JButton("+ Nueva Temporada");
        btnCrearTemporada.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCrearTemporada.setForeground(COLOR_BLANCO);
        btnCrearTemporada.setBackground(COLOR_AZUL);
        btnCrearTemporada.setFocusPainted(false);
        btnCrearTemporada.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCrearTemporada.addActionListener(e -> JOptionPane.showMessageDialog(this, "Función no implementada"));

        cabeceraTemporada.add(lblTitulo, BorderLayout.WEST);
        cabeceraTemporada.add(btnCrearTemporada, BorderLayout.EAST);

        String[] columnas = {"ID", "Temporada", "Inicio", "Fin", "Estado"};
        Object[][] datos = {
            {"1", "2025/2026", "01/09/2025", "30/06/2026", "Futuro"},
            {"2", "2024/2025", "01/09/2024", "30/06/2025", "En Curso"},
            {"3", "2023/2024", "01/09/2023", "30/06/2024", "Terminado"},
            {"4", "2022/2023", "01/09/2022", "30/06/2023", "Terminado"}
        };

        JTable tablaTemporada = new JTable(new DefaultTableModel(datos, columnas) {
            public boolean isCellEditable(int row, int column) { return false; }
        });
        
        tablaTemporada.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaTemporada.setRowHeight(30);
        tablaTemporada.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaTemporada.getTableHeader().setBackground(COLOR_BLANCO);

        JScrollPane scrollTablaTemporada = new JScrollPane(tablaTemporada);

        panelTemporada.add(cabeceraTemporada, BorderLayout.NORTH);
        panelTemporada.add(scrollTablaTemporada, BorderLayout.CENTER);
        return panelTemporada;
    }

    private JPanel crearPanelEquipos() {
        JPanel panelEquipos = new JPanel(new BorderLayout());
        panelEquipos.setBackground(new Color(240, 240, 240));
        panelEquipos.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblTituloEquipo = new JLabel("Equipos");
        lblTituloEquipo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTituloEquipo.setForeground(COLOR_TEXTO);
        lblTituloEquipo.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel contenedor = new JPanel(new GridLayout(0, 1, 0, 12));
        contenedor.setBackground(COLOR_FONDO);

        String[] equipos = {"Barcelona", "Athletic Club", "Granada", "Sevilla", "Zaragoza", "Valencia"};
        for (String equipo : equipos) {
            contenedor.add(crearTarjetaEquipo(equipo));
        }

        JScrollPane scrollEquipo = new JScrollPane(contenedor);
        scrollEquipo.setBorder(null);
        scrollEquipo.getViewport().setBackground(COLOR_FONDO);

        panelEquipos.add(lblTituloEquipo, BorderLayout.NORTH);
        panelEquipos.add(scrollEquipo, BorderLayout.CENTER);
        return panelEquipos;
    }

    private JPanel crearTarjetaEquipo(String nombre) {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 0));
        tarjeta.setBackground(COLOR_BLANCO);
        tarjeta.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel escudo = new JLabel(cargarEscudo(nombre));
        
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setForeground(COLOR_TEXTO);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setBackground(COLOR_BLANCO);

        JButton btnEscudo = new JButton("Cambiar Escudo");
        btnEscudo.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEscudo.setForeground(COLOR_TEXTO);
        btnEscudo.setBackground(COLOR_FONDO);
        btnEscudo.setFocusPainted(false);
        btnEscudo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEscudo.addActionListener(e -> cambiarEscudo(escudo, nombre));

        JButton btnGestionar = new JButton("Gestionar");
        btnGestionar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnGestionar.setForeground(COLOR_BLANCO);
        btnGestionar.setBackground(COLOR_AZUL);
        btnGestionar.setFocusPainted(false);
        btnGestionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGestionar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Gestionar " + nombre));

        panelBotones.add(btnEscudo);
        panelBotones.add(btnGestionar);

        tarjeta.add(escudo, BorderLayout.WEST);
        tarjeta.add(lblNombre, BorderLayout.CENTER);
        tarjeta.add(panelBotones, BorderLayout.EAST);
        return tarjeta;
    }

    private JPanel crearPanelPartidos() {
        JPanel panelPartido = new JPanel(new BorderLayout());
        panelPartido.setBackground(new Color(240, 240, 240));
        panelPartido.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel cabeceraEquipo = new JPanel(new BorderLayout());
        cabeceraEquipo.setBackground(COLOR_FONDO);
        cabeceraEquipo.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTituloPartido = new JLabel("Partidos");
        lblTituloPartido.setFont(new Font("Arial", Font.BOLD, 24));
        lblTituloPartido.setForeground(COLOR_TEXTO);

        JPanel filtrosEquipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filtrosEquipo.setBackground(COLOR_FONDO);

        JLabel lblTemp = new JLabel("Temporada:");
        lblTemp.setFont(new Font("Arial", Font.PLAIN, 13));
        JComboBox<String> comboTemp = new JComboBox<>(new String[]{"2024/2025", "2023/2024"});
        comboTemp.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblJor = new JLabel("Jornada:");
        lblJor.setFont(new Font("Arial", Font.PLAIN, 13));
        JComboBox<String> comboJor = new JComboBox<>();
        comboJor.setFont(new Font("Arial", Font.PLAIN, 13));
        for (int i = 1; i <= 30; i++) comboJor.addItem("Jornada " + i);

        filtrosEquipo.add(lblTemp);
        filtrosEquipo.add(comboTemp);
        filtrosEquipo.add(lblJor);
        filtrosEquipo.add(comboJor);

        cabeceraEquipo.add(lblTituloPartido, BorderLayout.NORTH);
        cabeceraEquipo.add(filtrosEquipo, BorderLayout.CENTER);

        JPanel panelPartidos = new JPanel(new GridLayout(0, 1, 0, 12));
        panelPartidos.setBackground(COLOR_FONDO);
        panelPartidos.add(crearPartido("Barcelona", "Athletic Club", "TERMINADO", "30 - 25"));
        panelPartidos.add(crearPartido("Granada", "Sevilla", "EN JUEGO", "15 - 12"));
        panelPartidos.add(crearPartido("Zaragoza", "Valencia", "POR JUGAR", "18:00"));

        JScrollPane scrollPartido = new JScrollPane(panelPartidos);
        scrollPartido.setBorder(null);
        scrollPartido.getViewport().setBackground(COLOR_FONDO);

        panelPartido.add(cabeceraEquipo, BorderLayout.NORTH);
        panelPartido.add(scrollPartido, BorderLayout.CENTER);
        return panelPartido;
    }

    private JPanel crearPartido(String local, String visitante, String estado, String resultado) {
        JPanel paneltarjeta = new JPanel(new BorderLayout());
        paneltarjeta.setBackground(COLOR_BLANCO);
        paneltarjeta.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblEstado = new JLabel(estado);
        lblEstado.setFont(new Font("Arial", Font.BOLD, 11));
        lblEstado.setForeground(COLOR_BLANCO);
        lblEstado.setOpaque(true);
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
        lblEstado.setBorder(new EmptyBorder(4, 8, 4, 8));

        if (estado.equals("TERMINADO")) lblEstado.setBackground(COLOR_VERDE);
        else if (estado.equals("EN JUEGO")) lblEstado.setBackground(COLOR_AMARILLO);
        else lblEstado.setBackground(COLOR_NARANJA);

        JPanel panelPartido = new JPanel(new GridLayout(1, 3, 10, 0));
        panelPartido.setBackground(COLOR_BLANCO);

        JLabel lblLocal = new JLabel(local, SwingConstants.RIGHT);
        lblLocal.setFont(new Font("Arial", Font.BOLD, 15));
        lblLocal.setForeground(COLOR_TEXTO);

        JLabel lblRes = new JLabel(resultado, SwingConstants.CENTER);
        lblRes.setFont(new Font("Arial", Font.BOLD, 22));
        lblRes.setForeground(COLOR_AZUL);

        JLabel lblVis = new JLabel(visitante, SwingConstants.LEFT);
        lblVis.setFont(new Font("Arial", Font.BOLD, 15));
        lblVis.setForeground(COLOR_TEXTO);

        panelPartido.add(lblLocal);
        panelPartido.add(lblRes);
        panelPartido.add(lblVis);

        paneltarjeta.add(lblEstado, BorderLayout.WEST);
        paneltarjeta.add(panelPartido, BorderLayout.CENTER);
        return paneltarjeta;
    }

    private JPanel crearPanelResultados() {
        JPanel panelClasificacion = new JPanel(new BorderLayout());
        panelClasificacion.setBackground(new Color(240, 240, 240));
        panelClasificacion.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel cabeceraClasificacion = new JPanel(new BorderLayout());
        cabeceraClasificacion.setBackground(COLOR_FONDO);
        cabeceraClasificacion.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTituloClasificacion = new JLabel("Clasificación");
        lblTituloClasificacion.setFont(new Font("Arial", Font.BOLD, 24));
        lblTituloClasificacion.setForeground(COLOR_TEXTO);

        JComboBox<String> comboClasificacion = new JComboBox<>(new String[]{"Temporada 2024/2025", "Temporada 2023/2024"});
        comboClasificacion.setFont(new Font("Arial", Font.PLAIN, 13));

        cabeceraClasificacion.add(lblTituloClasificacion, BorderLayout.WEST);
        cabeceraClasificacion.add(comboClasificacion, BorderLayout.EAST);

        String[] columnas = {"Pos", "Equipo", "PJ", "PG", "PE", "PP", "Pts", "GF", "GC", "DG"};
        Object[][] datos = {
            {"1", "Athletic Club", "14", "12", "1", "1", "37", "420", "300", "+120"},
            {"2", "Barcelona", "14", "10", "1", "3", "31", "380", "310", "+70"},
            {"3", "Granada", "14", "8", "2", "4", "26", "360", "350", "+10"},
            {"4", "Sevilla", "14", "7", "2", "5", "23", "340", "345", "-5"},
            {"5", "Zaragoza", "14", "7", "1", "6", "22", "335", "360", "-25"},
            {"6", "Valencia", "14", "6", "3", "5", "21", "350", "340", "+10"}
        };

        JTable tablaClasificacion = new JTable(new DefaultTableModel(datos, columnas) {
            public boolean isCellEditable(int row, int column) { return false; }
        });
        
        tablaClasificacion.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaClasificacion.setRowHeight(28);
        tablaClasificacion.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaClasificacion.getTableHeader().setBackground(COLOR_BLANCO);
        
        JScrollPane scrollClasificacion = new JScrollPane(tablaClasificacion);

        panelClasificacion.add(cabeceraClasificacion, BorderLayout.NORTH);
        panelClasificacion.add(scrollClasificacion, BorderLayout.CENTER);
        return panelClasificacion;
    }

    private ImageIcon cargarEscudo(String equipo) {
        String ruta = "/assets/escudos/" + equipo.toLowerCase().replaceAll("\\s+", "") + ".png";
        try {
            Image img = new ImageIcon(getClass().getResource(ruta)).getImage();
            return new ImageIcon(img.getScaledInstance(35, 35, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return new ImageIcon();
        }
    }

    private void cambiarEscudo(JLabel etiqueta, String equipo) {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccionar Escudo para " + equipo);
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));

        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = selector.getSelectedFile();
            try {
                Image img = new ImageIcon(archivo.getAbsolutePath()).getImage();
                etiqueta.setIcon(new ImageIcon(img.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
                JOptionPane.showMessageDialog(this, "Escudo actualizado para " + equipo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar imagen", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

	public void despuesDelLogin(int nivel, String nombre) {
		// TODO Auto-generated method stub
		
	}
}
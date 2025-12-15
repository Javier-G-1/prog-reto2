package nuevoapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelLateral;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    
    private JButton btnInicio, btnEquipos, btnJornadas, btnClasificacion;
    private static final String CARD_INICIO = "inicio";
    private static final String CARD_EQUIPOS = "equipos";
    private static final String CARD_JORNADAS = "jornadas";
    private static final String CARD_CLASIFICACION = "clasificacion";

    public VentanaPrincipal() {
    	
    	//esto hace poder cambiar el icono de la aplicacion 
    	ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
    	setIconImage(icono.getImage());
        initUI();
    }

    private void initUI() {
        setTitle("Federación de Balonmano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(24, 24, 27));

        crearPanelLateral();
        crearPanelPrincipal();

        setVisible(true);
    }

    private void crearPanelLateral() {
        panelLateral = new JPanel();
        panelLateral.setPreferredSize(new Dimension(280, 0));
        panelLateral.setBackground(new Color(24, 24, 27));
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(new EmptyBorder(30, 20, 30, 20));

        // Logo y título
        JPanel panelLogo = new JPanel();
        panelLogo.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelLogo.setOpaque(false);
        panelLogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel lblIcono = new JLabel();
        lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        lblIcono.setForeground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("<html>Federación<br>de Balonmano</html>");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        panelLogo.add(lblIcono);
        panelLogo.add(lblTitulo);
        panelLateral.add(panelLogo);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 40)));

        // Botones del menú
        btnInicio = crearBotonMenu("", "Inicio", false);

        btnEquipos = crearBotonMenu("", "Equipos", false);
        btnJornadas = crearBotonMenu("", "Jornadas", false);
        btnClasificacion = crearBotonMenu("", "Clasificación", false);

        panelLateral.add(btnInicio);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 8)));
;
        panelLateral.add(Box.createRigidArea(new Dimension(0, 8)));
        panelLateral.add(btnEquipos);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 8)));
       
        panelLateral.add(btnJornadas);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 8)));
        
        panelLateral.add(btnClasificacion);

        panelLateral.add(Box.createVerticalGlue());

        // Acciones de botones
        btnInicio.addActionListener(e -> {
            mostrarPanel(CARD_INICIO);
            actualizarSeleccion(btnInicio);
        });
      
        btnEquipos.addActionListener(e -> {
            mostrarPanel(CARD_EQUIPOS);
            actualizarSeleccion(btnEquipos);
        });
      
        btnJornadas.addActionListener(e -> {
            mostrarPanel(CARD_JORNADAS);
            actualizarSeleccion(btnJornadas);
        });
     
        btnClasificacion.addActionListener(e -> {
            mostrarPanel(CARD_CLASIFICACION);
            actualizarSeleccion(btnClasificacion);
        });

        getContentPane().add(panelLateral, BorderLayout.WEST);
        
        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setBackground(new Color(0, 0, 64));
        panelLateral.add(btnCerrarSesion);
    }

    private JButton crearBotonMenu(String icono, String texto, boolean seleccionado) {
        JButton btn = new JButton(" " + icono + "   " + texto);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setPreferredSize(new Dimension(240, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(seleccionado);
        btn.setForeground(Color.WHITE);
        
        if (seleccionado) {
            btn.setBackground(new Color(37, 99, 235));
        } else {
            btn.setBackground(new Color(24, 24, 27));
        }
        
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.isContentAreaFilled() || !btn.getBackground().equals(new Color(37, 99, 235))) {
                    btn.setBackground(new Color(39, 39, 42));
                    btn.setContentAreaFilled(true);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getBackground().equals(new Color(37, 99, 235))) {
                    btn.setContentAreaFilled(false);
                }
            }
        });
        
        return btn;
    }

    private void actualizarSeleccion(JButton botonSeleccionado) {
        JButton[] botones = {btnInicio, btnEquipos, btnJornadas, btnClasificacion};
        
        for (JButton btn : botones) {
            if (btn == botonSeleccionado) {
                btn.setBackground(new Color(37, 99, 235));
                btn.setContentAreaFilled(true);
            } else {
                btn.setBackground(new Color(24, 24, 27));
                btn.setContentAreaFilled(false);
            }
        }
    }

    private void crearPanelPrincipal() {
        panelPrincipal = new JPanel();
        cardLayout = new CardLayout();
        panelPrincipal.setLayout(cardLayout);
        panelPrincipal.setBackground(new Color(24, 24, 27));

        panelPrincipal.add(crearPanelInicio(), CARD_INICIO);
        panelPrincipal.add(crearPanelEquipo(), CARD_EQUIPOS);
        panelPrincipal.add(crearPanelJornada(), CARD_JORNADAS);
        panelPrincipal.add(crearPanelClasificacion(), CARD_CLASIFICACION);

        getContentPane().add(panelPrincipal, BorderLayout.CENTER);
        
   
    }

    private JPanel crearPanelInicio() {
        JPanel panelInicio = new JPanel(new BorderLayout());
        panelInicio.setBackground(new Color(24, 24, 27));
        panelInicio.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lblTituloInicio = new JLabel("Inicio");
        lblTituloInicio.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloInicio.setForeground(Color.WHITE);
        panelInicio.add(lblTituloInicio, BorderLayout.NORTH);

        return panelInicio;
    }

    private JPanel crearPanelEquipo() {
        JPanel panelBodyTemporada = new JPanel(new BorderLayout());
        panelBodyTemporada.setBackground(new Color(24, 24, 27));
        panelBodyTemporada.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Header
        JPanel panelHeaderTemporada = new JPanel(new BorderLayout());
        panelHeaderTemporada.setOpaque(false);
        panelHeaderTemporada.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JLabel lblTituloFederacion = new JLabel("Federación de Balonmano");
        lblTituloFederacion.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTituloFederacion.setForeground(Color.WHITE);
        
        JPanel headerContentInicio = new JPanel();
        headerContentInicio.setLayout(new BoxLayout(headerContentInicio, BoxLayout.Y_AXIS));
        headerContentInicio.setOpaque(false);
        headerContentInicio.add(lblTituloFederacion);
        
        JComboBox<String> comboBoxTemporada = new JComboBox<>();
        comboBoxTemporada.addItem("Temporada 2023/2024");
        comboBoxTemporada.addItem("Temporada 2024/2025");

        /* 🔴 CLAVE: tamaño */
        comboBoxTemporada.setPreferredSize(new Dimension(250, 35));
        comboBoxTemporada.setMaximumSize(new Dimension(250, 35));

        /* Estilo general del JComboBox (la caja) */
        comboBoxTemporada.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBoxTemporada.setForeground(Color.WHITE);
        comboBoxTemporada.setBackground(new Color(39, 39, 42));
        comboBoxTemporada.setBorder(BorderFactory.createLineBorder(new Color(63, 63, 70)));
        comboBoxTemporada.setFocusable(false);
        comboBoxTemporada.setOpaque(true);

        // Añado el ComboBox al panel del Header, alineado a la derecha (EAST)
        panelHeaderTemporada.add(headerContentInicio, BorderLayout.WEST); // Título a la izquierda
        panelHeaderTemporada.add(comboBoxTemporada, BorderLayout.WEST);   // ComboBox a la derecha
        
        panelHeaderTemporada.add(headerContentInicio, BorderLayout.NORTH);
        panelBodyTemporada.add(panelHeaderTemporada, BorderLayout.NORTH);

        // Título sección
        JLabel lbltituloEquipos = new JLabel("Equipos");
        lbltituloEquipos.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbltituloEquipos.setForeground(Color.WHITE);
        lbltituloEquipos.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JPanel panelTituloEquipo = new JPanel(new BorderLayout());
        panelTituloEquipo.setOpaque(false);
        panelTituloEquipo.add(lbltituloEquipos, BorderLayout.WEST);
        panelTituloEquipo.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Contenedor de tarjetas
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setOpaque(false);
        contenedor.add(panelTituloEquipo);

        // Lista de equipos (solo nombres)
        String[] equipos = {"Barcelona", "Atlético de Madrid", "Granada", "Sevilla", "Zaragoza", "Valencia", "ATHLETIC CLUB", "Vigo"};

        for (String nombre : equipos) {
            JPanel tarjeta = new JPanel(new BorderLayout());
            tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            tarjeta.setPreferredSize(new Dimension(800, 80));
            tarjeta.setBackground(new Color(39, 39, 42));
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(63, 63, 70), 1, true),
                new EmptyBorder(15, 20, 15, 20)
            ));

            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblNombre.setForeground(Color.WHITE);

            tarjeta.add(lblNombre, BorderLayout.WEST);

            // Botón Gestionar
            JButton btnGestionar = new JButton("Gestionar");
            btnGestionar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnGestionar.setForeground(new Color(161, 161, 170));
            btnGestionar.setBackground(new Color(39, 39, 42));
            btnGestionar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            btnGestionar.setFocusPainted(false);
            btnGestionar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            tarjeta.add(btnGestionar, BorderLayout.EAST);

            contenedor.add(tarjeta);
            contenedor.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);//hace que el scroll no se vea
        scroll.setViewportBorder(null);
        scroll.setEnabled(false);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        
        scroll.setBorder(null);
        panelBodyTemporada.add(scroll, BorderLayout.CENTER);

        return panelBodyTemporada;
    }


    private JPanel crearPanelJornada() {
        JPanel panelTituloJornadas = new JPanel(new BorderLayout());
        panelTituloJornadas.setBackground(new Color(24, 24, 27));
        panelTituloJornadas.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lblTituloJornada = new JLabel("Jornadas");
        lblTituloJornada.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloJornada.setForeground(Color.WHITE);
        panelTituloJornadas.add(lblTituloJornada, BorderLayout.NORTH);

        return panelTituloJornadas;
    }



    private JPanel crearPanelClasificacion() {
        JPanel panelTituloClasificacion = new JPanel(new BorderLayout());
        panelTituloClasificacion.setBackground(new Color(24, 24, 27));
        panelTituloClasificacion.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lblTituloClasificacion = new JLabel("Clasificación");
        lblTituloClasificacion.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloClasificacion.setForeground(Color.WHITE);
        panelTituloClasificacion.add(lblTituloClasificacion, BorderLayout.NORTH);

        return panelTituloClasificacion;
    }

    private void mostrarPanel(String nombre) {
        cardLayout.show(panelPrincipal, nombre);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal());
    }
}
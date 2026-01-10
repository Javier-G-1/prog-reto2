package nuevoapp;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import gestion.Jugador;
import gestion.Partido;
import logica.GeneradorCalendario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import gestion.*;
import logica.*;
import logica.CalculadoraClasificacion;
import gestion.Equipo;
import gestion.Jornada;
import gestion.Temporada;

public class newVentanaPrincipal extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private DatosFederacion datosFederacion;
    private JPanel contentPane;
    private JPanel panelAdminPartidos;
    
    private JPanel panelMenu;
    private JPanel panelCards;
    private CardLayout cardLayout;
    private Jugador jugadorSeleccionado;
    private JLabel lblTitulo, lblSubtitulo, lblBienvenido, lblUsuario, lblTemp, lblEstado;
    private JButton btnInicio, btnEquipos, btnJugadores, btnPartidos, btnClasificacin, btnCerrarSesion;
    private JScrollPane scrollEquipos;
    private JPanel panelTarjetasEquipo;
    private JButton btnAgregarEquipo;
    private JComboBox<String> comboTemporadas;
    private JPanel panelInicio, panelEquipos, panelJugadores, panelPartidos, panelClasificacion, panelSuperior;

    private JComboBox<String> comboTemporadasJugadores;
    private JComboBox<String> comboEquiposJugadores;
  
    private JPanel panelListaPartidos;
    private JTable tablaClasificacion;
    private DefaultTableModel modeloTabla;

    private JPanel panelTarjetasJugadores;
    private JScrollPane scrollJugadores;

    private JButton btnVerFoto;
    private JButton btnCambiarFoto;
    private JButton btnCambiarEquipo;
    private JButton btnAgregarJugador;
    
    private JButton btnNuevaTemp, btnNuevaJor, btnNuevoPart;
    
    private Component verticalStrut;
    private Component verticalStrut_1;
    private Component verticalStrut_2;
    private Component verticalStrut_3;
    private Component verticalStrut_4;
    private Component verticalStrut_5;
    private JLabel lblTemporadaJugador;
    private JLabel lblEquipoJugadores;

    private JComboBox<String> comboTemporadasPartidos;
    private JComboBox<String> comboJornadasPartidos;
    private JButton btnInscribirEquipo;
    private JLabel lblTemporadaPartido;
    private JLabel lblJornadaPartido;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                newVentanaPrincipal frame = new newVentanaPrincipal();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public newVentanaPrincipal() {
        this.datosFederacion = new DatosFederacion(); 
        
        // Registrar inicio de sesión
        GestorLog.iniciarSesion("Admin");
        
        new GestorTemporadas().prepararEscenarioInicial(this.datosFederacion); 
        
        btnNuevaTemp = new JButton("+ Temporada");
        btnNuevaJor = new JButton("+ Jornada");
        btnInscribirEquipo = new JButton("Inscribir Equipo");
        comboTemporadasPartidos = new JComboBox<>();
        comboJornadasPartidos = new JComboBox<>();
        
        ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
        setIconImage(icono.getImage());
        setTitle("Federación de Balonmano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 700);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // ===== PANEL MENU =====
        panelMenu = new JPanel();
        panelMenu.setBackground(new Color(30, 30, 30));
        panelMenu.setPreferredSize(new Dimension(250, 0));
        panelMenu.setLayout(new BorderLayout());
        contentPane.add(panelMenu, BorderLayout.WEST);

        JPanel panelArriba = new JPanel();
        panelArriba.setBackground(new Color(30, 30, 30));
        panelArriba.setLayout(new BoxLayout(panelArriba, BoxLayout.Y_AXIS));
        panelMenu.add(panelArriba, BorderLayout.NORTH);

        lblTitulo = new JLabel("FEDERACIÓN");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelArriba.add(lblTitulo);

        lblSubtitulo = new JLabel("BALONMANO");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(45, 55, 140));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelArriba.add(lblSubtitulo);

        lblBienvenido = new JLabel("Bienvenid@:");
        lblBienvenido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBienvenido.setForeground(new Color(180, 180, 180));
        lblBienvenido.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelArriba.add(lblBienvenido);

        lblUsuario = new JLabel("Admin");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelArriba.add(lblUsuario);
        
        verticalStrut = Box.createVerticalStrut(20);
        panelArriba.add(verticalStrut);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(30, 30, 30));
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelMenu.add(panelBotones, BorderLayout.CENTER);

        btnInicio = new JButton("Inicio");
        btnInicio.setBorder(null);
        btnInicio.setBackground(new Color(45, 55, 140));
        btnInicio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnInicio.setForeground(new Color(255, 255, 255));
        btnInicio.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInicio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnInicio.addActionListener(this);
        panelBotones.add(btnInicio);

        btnEquipos = new JButton("Equipos");
        btnEquipos.setBorder(null);
        btnEquipos.setBackground(new Color(45, 55, 140));
        btnEquipos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEquipos.setForeground(Color.WHITE);
        btnEquipos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEquipos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnEquipos.addActionListener(this);
        
        verticalStrut_1 = Box.createVerticalStrut(20);
        panelBotones.add(verticalStrut_1);
        panelBotones.add(btnEquipos);

        btnJugadores = new JButton("Jugadores");
        btnJugadores.setBorder(null);
        btnJugadores.setBackground(new Color(45, 55, 140));
        btnJugadores.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnJugadores.setForeground(Color.WHITE);
        btnJugadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJugadores.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnJugadores.addActionListener(this);
        
        verticalStrut_2 = Box.createVerticalStrut(20);
        panelBotones.add(verticalStrut_2);
        panelBotones.add(btnJugadores);

        btnPartidos = new JButton("Partidos");
        btnPartidos.setBorder(null);
        btnPartidos.setBackground(new Color(45, 55, 140));
        btnPartidos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPartidos.setForeground(Color.WHITE);
        btnPartidos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPartidos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnPartidos.addActionListener(this);
        
        verticalStrut_3 = Box.createVerticalStrut(20);
        panelBotones.add(verticalStrut_3);
        panelBotones.add(btnPartidos);

        btnClasificacin = new JButton("Clasificación");
        btnClasificacin.setBorder(null);
        btnClasificacin.setBackground(new Color(45, 55, 140));
        btnClasificacin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClasificacin.setForeground(Color.WHITE);
        btnClasificacin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnClasificacin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnClasificacin.addActionListener(this);
        
        verticalStrut_4 = Box.createVerticalStrut(20);
        panelBotones.add(verticalStrut_4);
        panelBotones.add(btnClasificacin);

        btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setBorder(null);
        btnCerrarSesion.setBackground(new Color(140, 45, 45));
        btnCerrarSesion.setFont(new Font("Segoe UI Black", Font.ITALIC, 14));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnCerrarSesion.addActionListener(this);
        
        verticalStrut_5 = Box.createVerticalStrut(100);
        panelBotones.add(verticalStrut_5);
        panelBotones.add(btnCerrarSesion);

        // ===== PANEL CENTRAL (CARDS) =====
        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);
        contentPane.add(panelCards, BorderLayout.CENTER);

        panelInicio = new JPanel();
        panelInicio.setBackground(new Color(20, 24, 31));
        panelCards.add(panelInicio, "inicio");
        ImageIcon iconoBalon = new ImageIcon(getClass().getResource("/assets/handball.png"));
        panelInicio.setLayout(new BorderLayout(0, 0));
        JLabel lblImagen = new JLabel(iconoBalon);
        panelInicio.add(lblImagen);

        panelEquipos = new JPanel();
        panelEquipos.setBackground(new Color(20, 24, 31));
        panelCards.add(panelEquipos, "equipos");
        panelEquipos.setLayout(new BorderLayout(10, 10));
        panelEquipos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelEquipos.setLayout(new BorderLayout(10, 10));
        panelEquipos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(20, 24, 31));
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelEquipos.add(panelSuperior, BorderLayout.NORTH);

        lblTemp = new JLabel("Temporada:");
        lblTemp.setForeground(Color.WHITE);
        lblTemp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelSuperior.add(lblTemp);

        comboTemporadas = new JComboBox<>(new String[] {"Temporada 2025/26", "Temporada 2024/25"});
        comboTemporadas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelSuperior.add(comboTemporadas);

        btnAgregarEquipo = new JButton("Agregar Equipo");
        btnAgregarEquipo.setBorder(null);
        btnAgregarEquipo.setBackground(new Color(45, 55, 140));
        btnAgregarEquipo.setForeground(Color.WHITE);
        btnAgregarEquipo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelSuperior.add(btnAgregarEquipo);

        panelTarjetasEquipo = new JPanel();
        panelTarjetasEquipo.setLayout(new BoxLayout(panelTarjetasEquipo, BoxLayout.Y_AXIS));
        panelTarjetasEquipo.setBackground(new Color(30, 34, 41));

        scrollEquipos = new JScrollPane(panelTarjetasEquipo);
        scrollEquipos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollEquipos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollEquipos.getVerticalScrollBar().setUnitIncrement(16);
        panelEquipos.add(scrollEquipos, BorderLayout.CENTER);

        btnAgregarEquipo.addActionListener(ae -> {
            String nombreT = (String) comboTemporadas.getSelectedItem();
            Temporada t = datosFederacion.buscarTemporadaPorNombre(nombreT);
            
            // VALIDACIÓN DE ESTADO
            if (t != null && !t.getEstado().equals(Temporada.FUTURA)) {
                JOptionPane.showMessageDialog(this,
                    "Solo se pueden agregar equipos a temporadas FUTURAS",
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE);
                GestorLog.advertencia("Intento de agregar equipo en temporada " + t.getEstado() + ": " + nombreT);
                return;
            }
            
            String nombreE = JOptionPane.showInputDialog(this, "Nombre del nuevo equipo:");

            if (nombreE != null && !nombreE.trim().isEmpty() && nombreT != null) {
                if (t != null) {
                    boolean existe = false;
                    for (Equipo eq : t.getEquiposParticipantes()) {
                        if (eq.getNombre().equalsIgnoreCase(nombreE.trim())) {
                            existe = true;
                            break;
                        }
                    }
                    
                    if (existe) {
                        JOptionPane.showMessageDialog(this, 
                            "El equipo '" + nombreE + "' ya existe en esta temporada.",
                            "Equipo duplicado",
                            JOptionPane.WARNING_MESSAGE);
                        GestorLog.advertencia("Intento de agregar equipo duplicado: " + nombreE + " en " + nombreT);
                        return;
                    }
                    
                    Equipo nuevoEquipo = new Equipo(nombreE.trim());
                    t.inscribirEquipo(nuevoEquipo);
                    
                    actualizarVistaEquipos(); 
                    sincronizarCombos();
                    
                    GestorLog.exito("Nuevo equipo creado: " + nombreE + " | Temporada: " + nombreT);
                    
                    JOptionPane.showMessageDialog(this, 
                        "Equipo '" + nombreE + "' creado con éxito en " + nombreT,
                        "Equipo creado",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        panelJugadores = new JPanel(new BorderLayout(10, 10));
        panelJugadores.setBackground(new Color(20, 24, 31));
        panelJugadores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCards.add(panelJugadores, "jugadores");

        JPanel panelSuperiorJugadores = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperiorJugadores.setBackground(new Color(20, 24, 31));
        panelJugadores.add(panelSuperiorJugadores, BorderLayout.NORTH);
        
        lblTemporadaJugador = new JLabel("Temporada:");
        panelSuperiorJugadores.add(lblTemporadaJugador);
        comboTemporadasJugadores = new JComboBox<>();
        panelSuperiorJugadores.add(comboTemporadasJugadores);
        
        lblEquipoJugadores = new JLabel("Equipo:");
        panelSuperiorJugadores.add(lblEquipoJugadores);
        comboEquiposJugadores = new JComboBox<>();
        comboEquiposJugadores.setPreferredSize(new Dimension(160, 25));
        panelSuperiorJugadores.add(comboEquiposJugadores);

        panelTarjetasJugadores = new JPanel();
        panelTarjetasJugadores.setLayout(new BoxLayout(panelTarjetasJugadores, BoxLayout.Y_AXIS));
        panelTarjetasJugadores.setBackground(new Color(30, 34, 41));
        scrollJugadores = new JScrollPane(panelTarjetasJugadores);
        scrollJugadores.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelJugadores.add(scrollJugadores, BorderLayout.CENTER);

        JPanel panelBotonesJugadores = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotonesJugadores.setBackground(new Color(20, 24, 31));
        btnVerFoto = new JButton("Ver foto");
        btnCambiarFoto = new JButton("Cambiar foto");
        btnCambiarEquipo = new JButton("Cambiar equipo");
        btnAgregarJugador = new JButton("Agregar jugador");
        btnVerFoto.addActionListener(this);
        btnCambiarFoto.addActionListener(this);
        btnCambiarEquipo.addActionListener(this);
        btnAgregarJugador.addActionListener(this);
        
        panelBotonesJugadores.add(btnVerFoto);
        panelBotonesJugadores.add(btnCambiarFoto);
        panelBotonesJugadores.add(btnCambiarEquipo);
        panelBotonesJugadores.add(btnAgregarJugador);
        panelJugadores.add(panelBotonesJugadores, BorderLayout.SOUTH);

        Temporada temporada2025_26 = new Temporada("Temporada 2025/26", Temporada.FUTURA);
        datosFederacion.add(temporada2025_26);
        
        String[][] equiposConJugadores = {
            {"Barcelona","Álvaro Mena","Carla Ríos","Ignacio Vela","Sofía Llorente","Mateo Cruz"},
            {"Granada","Carlos Muñoz","Marta Domínguez","Andrés Cortés","Lucía Palacios"},
            {"Sevilla","Marina Torres","Fernando Vázquez","Ana Beltrán","Rubén Márquez"},
            {"Zaragoza","Miguel Ortega","Claudia Rivas","Javier Torres","Isabel Salinas"},
            {"Valencia","Raúl Pérez","Andrea Delgado","Luis Navarro","Marta Ramírez"},
            {"Athletic Club","Pablo Martínez","Alicia Gómez","Daniel Reyes","Elena López"}
        };

        for (String[] equipoData : equiposConJugadores) {
            Equipo eq = new Equipo(equipoData[0], null);
            for (int i = 1; i < equipoData.length; i++) {
                Jugador j = new Jugador(equipoData[i], "Posición", 25, null);
                eq.ficharJugador(j);
            }
            temporada2025_26.inscribirEquipo(eq);
        }

        comboTemporadas.addActionListener(e -> {
            actualizarVistaEquipos();
        });
        
        comboTemporadasJugadores.addActionListener(e -> {
            actualizarComboEquipos();
            // Actualizar estado de botones según temporada
            actualizarEstadoBotonesJugadores();
        });

        comboEquiposJugadores.addActionListener(e -> {
            String equipoSel = (String) comboEquiposJugadores.getSelectedItem();
            String tempSel = (String) comboTemporadasJugadores.getSelectedItem();
            if (equipoSel != null && tempSel != null) {
                actualizarJugadoresPorTemporada(tempSel, equipoSel);
            }
        });

        panelPartidos = new JPanel();
        panelPartidos.setBackground(new Color(20, 24, 31));
        panelPartidos.setLayout(new BorderLayout(0, 0));
        panelCards.add(panelPartidos, "partidos");
       
        panelAdminPartidos = new JPanel();
        btnInscribirEquipo = new JButton("Inscribir Equipo");
        btnNuevaJor = new JButton("+ Jornada");
        comboTemporadasPartidos = new JComboBox<>();
        
        btnInscribirEquipo.addActionListener(this);
        btnNuevaJor.addActionListener(this);
        
        panelSuperior.add(btnInscribirEquipo);
        panelAdminPartidos.add(btnNuevaJor);
        
        panelAdminPartidos = new JPanel();
        panelAdminPartidos.setBackground(new Color(30, 34, 45));
        panelAdminPartidos.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelPartidos.add(panelAdminPartidos, BorderLayout.NORTH);

        btnNuevaTemp = new JButton("+ Temporada");
        btnNuevaJor = new JButton("+ Jornada");
        btnNuevoPart = new JButton("+ Partido");

        btnNuevaTemp.setBackground(new Color(46, 204, 113)); 
        btnNuevaTemp.setForeground(Color.WHITE);
        btnNuevaTemp.setFocusPainted(false);

        btnNuevaTemp.addActionListener(this);
        btnNuevaJor.addActionListener(this);
        btnNuevoPart.addActionListener(this);

        panelAdminPartidos.add(btnNuevaTemp);
        panelAdminPartidos.add(btnNuevaJor);
        panelAdminPartidos.add(btnNuevoPart);

        panelAdminPartidos.add(new JLabel(" | "));

        comboTemporadasPartidos = new JComboBox<>();
        comboJornadasPartidos = new JComboBox<>();
        
        lblTemporadaPartido = new JLabel("Temporada:");
        panelAdminPartidos.add(lblTemporadaPartido);
        panelAdminPartidos.add(comboTemporadasPartidos);
        
        lblJornadaPartido = new JLabel("Jornada:");
        panelAdminPartidos.add(lblJornadaPartido);
        panelAdminPartidos.add(comboJornadasPartidos);

        JScrollPane scrollPartidos = new JScrollPane();
        panelListaPartidos = new JPanel();
        panelListaPartidos.setLayout(new BoxLayout(panelListaPartidos, BoxLayout.Y_AXIS));
        panelListaPartidos.setBackground(new Color(20, 24, 31));
        scrollPartidos.setViewportView(panelListaPartidos);
        panelPartidos.add(scrollPartidos, BorderLayout.CENTER);

        comboTemporadasPartidos.addActionListener(e -> {
            actualizarComboJornadas();
            actualizarVistaPartidos();
        });
        comboJornadasPartidos.addActionListener(e -> {
            actualizarVistaPartidos();
        });///////////////////////////////////////////////////////////////////////////
        
        panelClasificacion = new JPanel(new BorderLayout());
        panelClasificacion.setBackground(new Color(20, 24, 31));
        panelCards.add(panelClasificacion, "clasificacion");
        panelClasificacion.add(new PanelClasificacion(), BorderLayout.CENTER);////////////Metido por Maha




        JLabel lblInicioTitulo = new JLabel("Bienvenido a la Federación de Balonmano");
        lblInicioTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblInicioTitulo.setForeground(Color.WHITE);
        panelInicio.add(lblInicioTitulo, BorderLayout.NORTH);

        cardLayout.show(panelCards, "inicio");
        
        sincronizarCombos();
        actualizarVistaEquipos();
        actualizarComboEquipos();
        
        if (comboTemporadasJugadores.getItemCount() > 0) {
            comboTemporadasJugadores.setSelectedIndex(0);
            if (comboEquiposJugadores.getItemCount() > 0) {
                comboEquiposJugadores.setSelectedIndex(0);
                String tempSel = (String) comboTemporadasJugadores.getSelectedItem();
                String equipoSel = (String) comboEquiposJugadores.getSelectedItem();
                actualizarJugadoresPorTemporada(tempSel, equipoSel);
                actualizarEstadoBotonesJugadores();
            }
        }
        
        actualizarVistaPartidos();
    }
    
    /**
     * Actualiza el estado de los botones de jugadores según el estado de la temporada
     */
    private void actualizarEstadoBotonesJugadores() {
        String tempNom = (String) comboTemporadasJugadores.getSelectedItem();
        if (tempNom == null) return;
        
        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
        if (t == null) return;
        
        boolean esFutura = t.getEstado().equals(Temporada.FUTURA);
        
        // Habilitar/deshabilitar botones de modificación
        btnAgregarJugador.setEnabled(esFutura);
        btnCambiarEquipo.setEnabled(esFutura);
        btnCambiarFoto.setEnabled(esFutura);
        
        // El botón de ver foto siempre habilitado
        btnVerFoto.setEnabled(true);
        
        // Agregar tooltips informativos
        if (!esFutura) {
            String estado = t.getEstado().equals(Temporada.EN_JUEGO) ? "en curso" : "finalizada";
            btnAgregarJugador.setToolTipText("No se pueden agregar jugadores a temporadas " + estado);
            btnCambiarEquipo.setToolTipText("No se pueden hacer traspasos en temporadas " + estado);
            btnCambiarFoto.setToolTipText("No se pueden cambiar fotos en temporadas " + estado);
        } else {
            btnAgregarJugador.setToolTipText("Agregar nuevo jugador");
            btnCambiarEquipo.setToolTipText("Cambiar jugador de equipo");
            btnCambiarFoto.setToolTipText("Cambiar foto del jugador");
        }
        btnVerFoto.setToolTipText("Ver foto del jugador seleccionado");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnInicio) {
            cardLayout.show(panelCards, "inicio");
            GestorLog.info("Navegación: Inicio");
        }
        else if (e.getSource() == btnEquipos) {
            cardLayout.show(panelCards, "equipos");
            GestorLog.info("Navegación: Equipos");
        }
        else if (e.getSource() == btnJugadores) {
            cardLayout.show(panelCards, "jugadores");
            GestorLog.info("Navegación: Jugadores");
            // Refrescar con los valores actuales de los combos
            String tempSel = (String) comboTemporadasJugadores.getSelectedItem();
            String equipoSel = (String) comboEquiposJugadores.getSelectedItem();
            if (tempSel != null && equipoSel != null) {
                actualizarJugadoresPorTemporada(tempSel, equipoSel);
                actualizarEstadoBotonesJugadores();
            }
        }
        else if (e.getSource() == btnPartidos) {
            cardLayout.show(panelCards, "partidos");
            GestorLog.info("Navegación: Partidos");
        }
        else if (e.getSource() == btnClasificacin) {
            cardLayout.show(panelCards, "clasificacion");
            GestorLog.info("Navegación: Clasificación");
        }
        else if (e.getSource() == btnCerrarSesion) {
            GestorLog.cerrarSesion("Admin");
            System.exit(0);
        }

        else if (e.getSource() == btnVerFoto) {
            if (jugadorSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un jugador primero");
                GestorLog.advertencia("Intento de ver foto sin jugador seleccionado");
                return;
            }
            if (jugadorSeleccionado.getFotoURL() != null) {
                ImageIcon icon = new ImageIcon(new ImageIcon(jugadorSeleccionado.getFotoURL())
                        .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(this, "", jugadorSeleccionado.getNombre(), JOptionPane.INFORMATION_MESSAGE, icon);
                GestorLog.info("Visualización de foto: " + jugadorSeleccionado.getNombre());
            } else {
                JOptionPane.showMessageDialog(this, "Este jugador no tiene foto");
                GestorLog.advertencia("Jugador sin foto: " + jugadorSeleccionado.getNombre());
            }
        }

        else if (e.getSource() == btnCambiarFoto) {
            if (jugadorSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un jugador");
                GestorLog.advertencia("Intento de cambiar foto sin jugador seleccionado");
                return;
            }
            
            String tempNom = (String) comboTemporadasJugadores.getSelectedItem();
            Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
            
            if (t != null && !t.getEstado().equals(Temporada.FUTURA)) {
                JOptionPane.showMessageDialog(this,
                    "Solo se pueden cambiar fotos en temporadas FUTURAS",
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE);
                GestorLog.advertencia("Intento de cambiar foto en temporada " + t.getEstado() + ": " + tempNom);
                return;
            }
            
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String rutaAnterior = jugadorSeleccionado.getFotoURL();
                jugadorSeleccionado.setFotoURL(chooser.getSelectedFile().getAbsolutePath());
                actualizarJugadoresPorTemporada((String) comboTemporadasJugadores.getSelectedItem(), 
                                               (String) comboEquiposJugadores.getSelectedItem());
                GestorLog.exito("Foto actualizada para: " + jugadorSeleccionado.getNombre() + 
                              " | Ruta: " + chooser.getSelectedFile().getAbsolutePath());
            }
        }

        else if (e.getSource() == btnCambiarEquipo) {
            if (jugadorSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un jugador de la lista");
                GestorLog.advertencia("Intento de cambiar equipo sin jugador seleccionado");
                return;
            }

            String nombreTempActual = (String) comboTemporadasJugadores.getSelectedItem();
            Temporada t = datosFederacion.buscarTemporadaPorNombre(nombreTempActual);

            if (t == null) {
                JOptionPane.showMessageDialog(this, "Error: No se encontró la temporada seleccionada.");
                GestorLog.error("Temporada no encontrada: " + nombreTempActual);
                return;
            }
            
            if (!t.getEstado().equals(Temporada.FUTURA)) {
                JOptionPane.showMessageDialog(this,
                    "Solo se pueden cambiar jugadores de equipo en temporadas FUTURAS",
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE);
                GestorLog.advertencia("Intento de cambiar equipo en temporada " + t.getEstado() + ": " + nombreTempActual);
                return;
            }

            int total = comboEquiposJugadores.getItemCount();
            if (total <= 1) { 
                JOptionPane.showMessageDialog(this, "No hay equipos disponibles en esta temporada.");
                GestorLog.advertencia("No hay equipos disponibles en: " + nombreTempActual);
                return; 
            }
            
            Object[] equiposDisponibles = new Object[total - 1];
            for (int i = 1; i < total; i++) {
                equiposDisponibles[i - 1] = comboEquiposJugadores.getItemAt(i);
            }

            Object seleccion = JOptionPane.showInputDialog(this, 
                    "Mover a " + jugadorSeleccionado.getNombre() + " al equipo:", 
                    "Cambiar equipo",
                    JOptionPane.QUESTION_MESSAGE, null, equiposDisponibles, equiposDisponibles[0]);

            if (seleccion != null) {
                String nuevoNombreEquipo = seleccion.toString();
                String equipoAnterior = "";

                // Encontrar equipo anterior
                for (Equipo eq : t.getEquiposParticipantes()) {
                    if (eq.getPlantilla().contains(jugadorSeleccionado)) {
                        equipoAnterior = eq.getNombre();
                        break;
                    }
                }

                for (Equipo eq : t.getEquiposParticipantes()) {
                    eq.getPlantilla().remove(jugadorSeleccionado);
                }

                for (Equipo eq : t.getEquiposParticipantes()) {
                    if (eq.getNombre().equals(nuevoNombreEquipo)) {
                        eq.ficharJugador(jugadorSeleccionado);
                        break;
                    }
                }

                actualizarJugadoresPorTemporada((String) comboTemporadasJugadores.getSelectedItem(),
                                               (String) comboEquiposJugadores.getSelectedItem());
                
                GestorLog.exito("Traspaso completado: " + jugadorSeleccionado.getNombre() + 
                              " | De: " + equipoAnterior + " → A: " + nuevoNombreEquipo + 
                              " | Temporada: " + nombreTempActual);
                
                JOptionPane.showMessageDialog(this, "Jugador movido correctamente a " + nuevoNombreEquipo);
            }
        }

        else if (e.getSource() == btnAgregarJugador) {
            String equipoSel = (String) comboEquiposJugadores.getSelectedItem();
            String tempNom = (String) comboTemporadasJugadores.getSelectedItem();

            if (equipoSel == null || equipoSel.equals("Todos")) {
                JOptionPane.showMessageDialog(this, "Selecciona un equipo específico para añadir al jugador");
                GestorLog.advertencia("Intento de agregar jugador sin equipo específico seleccionado");
                return;
            }
            
            Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
            
            if (t != null && !t.getEstado().equals(Temporada.FUTURA)) {
                JOptionPane.showMessageDialog(this,
                    "Solo se pueden agregar jugadores a temporadas FUTURAS",
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE);
                GestorLog.advertencia("Intento de agregar jugador en temporada " + t.getEstado() + ": " + tempNom);
                return;
            }

            String nombre = JOptionPane.showInputDialog(this, "Nombre del jugador:");
            if (nombre == null || nombre.trim().isEmpty()) return;
            
            String pos = JOptionPane.showInputDialog(this, "Posición (ej: Portero, Extremo):");
            if (pos == null || pos.trim().isEmpty()) pos = "Sin posición";
            
            String edadS = JOptionPane.showInputDialog(this, "Edad:");
            if (edadS == null || edadS.trim().isEmpty()) return;
            
            try {
                int edad = Integer.parseInt(edadS);
                
                Jugador nuevo = new Jugador(nombre.trim(), pos.trim(), edad, null);
                
                if (t != null) {
                    boolean encontrado = false;
                    for (Equipo eq : t.getEquiposParticipantes()) {
                        if (eq.getNombre().equals(equipoSel)) {
                            eq.ficharJugador(nuevo);
                            encontrado = true;
                            break;
                        }
                    }
                    
                    if (encontrado) {
                        actualizarJugadoresPorTemporada((String) comboTemporadasJugadores.getSelectedItem(),
                                                       (String) comboEquiposJugadores.getSelectedItem());
                        GestorLog.exito("Nuevo fichaje: " + nombre + " | Equipo: " + equipoSel + 
                                      " | Posición: " + pos + " | Edad: " + edad + " | Temporada: " + tempNom);
                        JOptionPane.showMessageDialog(this, "Jugador " + nombre + " fichado con éxito en " + equipoSel);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                GestorLog.error("Error al agregar jugador: edad inválida (" + edadS + ")");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al agregar el jugador: " + ex.getMessage());
                GestorLog.error("Error al agregar jugador", ex);
            }
        }

        if (e.getSource() == btnNuevaTemp) {
            String nombre = JOptionPane.showInputDialog(this, "Nombre de la Temporada (ej: 2025/26):");
            if (nombre != null && !nombre.trim().isEmpty()) {
                new GestorTemporadas().crearTemporadaFutura(nombre, datosFederacion);
                sincronizarCombos();
                GestorLog.exito("Nueva temporada creada: " + nombre + " | Estado: FUTURA");
                JOptionPane.showMessageDialog(this, "Temporada " + nombre + " creada con éxito.");
            }
        }

        else if (e.getSource() == btnNuevaJor) {
            Temporada t = obtenerTemporadaSeleccionada();
            if (t == null) {
                GestorLog.advertencia("Intento de crear jornada sin temporada seleccionada");
                return;
            }

            try {
                int equiposInscritos = t.getEquiposParticipantes().size();
                String estadoAnterior = t.getEstado();
                
                // Generar calendario (el log ya está dentro de GeneradorCalendario)
                GeneradorCalendario.crearCalendario(t);
                
                t.setEstado(Temporada.EN_JUEGO);
                actualizarComboJornadas();
                actualizarVistaPartidos();
                
                // Log adicional con detalles del cambio de estado
                GestorLog.exito("Temporada activada: " + t.getNombre() + 
                              " | Equipos: " + equiposInscritos + 
                              " | Jornadas creadas: " + t.getListaJornadas().size() +
                              " | Estado: " + estadoAnterior + " → " + Temporada.EN_JUEGO);
                
                JOptionPane.showMessageDialog(this, "Calendario Generado (Reglamento RFBM cumplido)");
                
            } catch (Exception ex) {
                GestorLog.error("Error al generar calendario para " + t.getNombre() + ": " + ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }
        }

        else if (e.getSource() == btnNuevoPart) {
            GestorLog.info("Iniciando creación de partido manual");
            crearDialogoNuevoPartido();
        }
        
        else if (e.getSource() == btnInscribirEquipo) {
            GestorLog.info("Iniciando proceso de inscripción de equipo");
            ejecutarInscripcionEquipo();
        }
    }

    private JPanel crearTarjetaEquipo(String nombreEquipo) {
        JPanel panelTarjetaEquiposInterior = new JPanel();
        panelTarjetaEquiposInterior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        panelTarjetaEquiposInterior.setBackground(new Color(24, 25, 50));
        panelTarjetaEquiposInterior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelTarjetaEquiposInterior.setLayout(new BorderLayout(10, 10));

        JLabel lblFotoEquipo = new JLabel();
        lblFotoEquipo.setPreferredSize(new Dimension(80, 80));
        lblFotoEquipo.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoEquipo.setVerticalAlignment(SwingConstants.CENTER);
        lblFotoEquipo.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        lblFotoEquipo.setText("Sin foto");
        panelTarjetaEquiposInterior.add(lblFotoEquipo, BorderLayout.WEST);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setOpaque(false);
        
        JLabel lblNombre = new JLabel(nombreEquipo);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNombre.setForeground(Color.WHITE);
        panelCentro.add(lblNombre);
        
        panelTarjetaEquiposInterior.add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotonesEquipo = new JPanel();
        panelBotonesEquipo.setOpaque(false);
        panelBotonesEquipo.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        JButton btnCambiarEscudo = new JButton("Cambiar Escudo");
        btnCambiarEscudo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCambiarEscudo.addActionListener(e -> {
            String tempNom = (String) comboTemporadas.getSelectedItem();
            Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
            
            if (t != null && !t.getEstado().equals(Temporada.FUTURA)) {
                JOptionPane.showMessageDialog(this, 
                    "Solo se pueden hacer cambios en temporadas FUTURAS",
                    "Operación no permitida", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JFileChooser chooser = new JFileChooser();
            int resultado = chooser.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                ImageIcon icon = new ImageIcon(
                        new ImageIcon(chooser.getSelectedFile().getAbsolutePath())
                                .getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)
                );
                lblFotoEquipo.setIcon(icon);
                lblFotoEquipo.setText("");
            }
        });
        panelBotonesEquipo.add(btnCambiarEscudo);

        JButton btnVerEscudo = new JButton("Ver Escudo");
        btnVerEscudo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVerEscudo.addActionListener(e -> {
            Icon icon = lblFotoEquipo.getIcon();
            if (icon != null) {
                ImageIcon img = new ImageIcon(
                        ((ImageIcon) icon).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)
                );
                JOptionPane.showMessageDialog(this, "", nombreEquipo, JOptionPane.INFORMATION_MESSAGE, img);
            } else {
                JOptionPane.showMessageDialog(this, "No tiene escudo", nombreEquipo, JOptionPane.WARNING_MESSAGE);
            }
        });
        panelBotonesEquipo.add(btnVerEscudo);

        panelTarjetaEquiposInterior.add(panelBotonesEquipo, BorderLayout.EAST);

        return panelTarjetaEquiposInterior;
    }

    private JPanel crearTarjetaJugador(Jugador jugador) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 10));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        tarjeta.setBackground(new Color(24, 25, 50));
        tarjeta.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        JLabel lblFoto = new JLabel("Sin foto", SwingConstants.CENTER);
        lblFoto.setPreferredSize(new Dimension(80, 80));
        lblFoto.setForeground(Color.WHITE);
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        if (jugador.getFotoURL() != null) {
            ImageIcon icon = new ImageIcon(
                new ImageIcon(jugador.getFotoURL())
                    .getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)
            );
            lblFoto.setIcon(icon);
            lblFoto.setText("");
        }
        tarjeta.add(lblFoto, BorderLayout.WEST);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);

        JLabel lblNombre = new JLabel(jugador.getNombre());
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblPos = new JLabel("Posición: " + jugador.getPosicion());
        lblPos.setForeground(Color.WHITE);

        JLabel lblEdad = new JLabel("Edad: " + jugador.getEdad());
        lblEdad.setForeground(Color.WHITE);

        panelInfo.add(lblNombre);
        panelInfo.add(lblPos);
        panelInfo.add(lblEdad);
        tarjeta.add(panelInfo, BorderLayout.CENTER);

        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jugadorSeleccionado = jugador;

                for (Component c : panelTarjetasJugadores.getComponents()) {
                    c.setBackground(new Color(24, 25, 50));
                }

                tarjeta.setBackground(new Color(60, 60, 120));
            }
        });

        return tarjeta;
    }

    private void actualizarComboJornadas() {
        String tempNom = (String) comboTemporadasPartidos.getSelectedItem();
        if (tempNom == null) return;

        comboJornadasPartidos.removeAllItems();
        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
        if (t != null) {
            for (Jornada j : t.getListaJornadas()) {
                comboJornadasPartidos.addItem(j.getNombre());
            }
        }
    }

    private void actualizarVistaPartidos() {
        if (panelListaPartidos == null) return;
        panelListaPartidos.removeAll();

        Temporada temp = obtenerTemporadaSeleccionada();
        String jorNom = (String) comboJornadasPartidos.getSelectedItem();

        if (temp != null && jorNom != null) {
            for (Jornada jor : temp.getListaJornadas()) {
                if (jor.getNombre().equals(jorNom)) {
                    for (Partido p : jor.getListaPartidos()) {
                        panelListaPartidos.add(crearTarjetaPartido(p));
                    }
                }
            }
        }
        panelListaPartidos.revalidate();
        panelListaPartidos.repaint();
        
        actualizarIndicadorEstadoPartidos();
    }

    private JPanel crearTarjetaPartido(Partido p) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(new Color(24, 25, 50));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel panelIzquierda = new JPanel(new BorderLayout(10, 0));
        panelIzquierda.setOpaque(false);
        
        JLabel lblEstadoPartido = new JLabel("●");
        lblEstadoPartido.setFont(new Font("Segoe UI", Font.BOLD, 24));
        if (p.isFinalizado()) {
            lblEstadoPartido.setForeground(new Color(231, 76, 60));
            lblEstadoPartido.setToolTipText("Partido Finalizado");
        } else {
            lblEstadoPartido.setForeground(new Color(52, 152, 219));
            lblEstadoPartido.setToolTipText("Partido Pendiente");
        }
        panelIzquierda.add(lblEstadoPartido, BorderLayout.WEST);

        String textoPartido = p.getEquipoLocal().getNombre() + " vs " + p.getEquipoVisitante().getNombre();
        if (p.isFinalizado()) {
            textoPartido += "  (" + p.getGolesLocal() + " - " + p.getGolesVisitante() + ")";
        }
        
        JLabel lblEquipos = new JLabel(textoPartido);
        lblEquipos.setForeground(Color.WHITE);
        lblEquipos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelIzquierda.add(lblEquipos, BorderLayout.CENTER);

        card.add(panelIzquierda, BorderLayout.CENTER);

        JButton btnGoles = new JButton(p.isFinalizado() ? "Editar Resultado" : "Anotar Goles");
        btnGoles.setFocusPainted(false);
        btnGoles.setBackground(p.isFinalizado() ? new Color(70, 70, 70) : new Color(45, 55, 140));
        btnGoles.setForeground(Color.WHITE);

        btnGoles.addActionListener(e -> {
            String tempNom = (String) comboTemporadasPartidos.getSelectedItem();
            Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
            
            if (t != null && t.getEstado().equals(Temporada.TERMINADA)) {
                JOptionPane.showMessageDialog(this, 
                    "No se pueden modificar resultados de temporadas FINALIZADAS",
                    "Operación no permitida", 
                    JOptionPane.WARNING_MESSAGE);
                GestorLog.advertencia("Intento de modificar resultado en temporada FINALIZADA: " + tempNom);
                return;
            }
            
            boolean eraFinalizado = p.isFinalizado();
            
            DialogoResultado diag = new DialogoResultado(this, p);
            diag.setVisible(true);

            if (diag.isAceptado()) {
                String accion = eraFinalizado ? "Resultado editado" : "Resultado registrado";
                GestorLog.exito(accion + ": " + p.getEquipoLocal().getNombre() + " " + 
                              p.getGolesLocal() + " - " + p.getGolesVisitante() + " " + 
                              p.getEquipoVisitante().getNombre() + " | Temporada: " + tempNom);
                
                actualizarVistaPartidos();
                if (panelClasificacion.isVisible()) {
                    actualizarTablaClasificacionGrafica();
                }
            }
        });

        card.add(btnGoles, BorderLayout.EAST);
        
        return card;
    }

    private void actualizarIndicadorEstadoTemporada() {
        JLabel lblEstadoTemp = null;
        for (Component c : panelSuperior.getComponents()) {
            if (c instanceof JLabel && ((JLabel) c).getName() != null 
                && ((JLabel) c).getName().equals("lblEstadoTemporada")) {
                lblEstadoTemp = (JLabel) c;
                break;
            }
        }
        
        if (lblEstadoTemp == null) {
            lblEstadoTemp = new JLabel();
            lblEstadoTemp.setName("lblEstadoTemporada");
            lblEstadoTemp.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panelSuperior.add(lblEstadoTemp, 2);
        }
        
        String tempNom = (String) comboTemporadas.getSelectedItem();
        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
        
        if (t != null) {
            switch (t.getEstado()) {
                case Temporada.FUTURA:
                    lblEstadoTemp.setText("● FUTURA");
                    lblEstadoTemp.setForeground(new Color(52, 152, 219));
                    btnAgregarEquipo.setEnabled(true);
                    btnAgregarEquipo.setToolTipText("Agregar nuevo equipo");
                    break;
                case Temporada.EN_JUEGO:
                    lblEstadoTemp.setText("● EN CURSO");
                    lblEstadoTemp.setForeground(new Color(241, 196, 15));
                    btnAgregarEquipo.setEnabled(false);
                    btnAgregarEquipo.setToolTipText("No se pueden agregar equipos a temporadas en curso");
                    break;
                case Temporada.TERMINADA:
                    lblEstadoTemp.setText("● FINALIZADA");
                    lblEstadoTemp.setForeground(new Color(231, 76, 60));
                    btnAgregarEquipo.setEnabled(false);
                    btnAgregarEquipo.setToolTipText("No se pueden agregar equipos a temporadas finalizadas");
                    break;
            }
        }
        
        panelSuperior.revalidate();
        panelSuperior.repaint();
    }

    private void actualizarIndicadorEstadoPartidos() {
        JLabel lblEstadoTempPartidos = null;
        for (Component c : panelAdminPartidos.getComponents()) {
            if (c instanceof JLabel && ((JLabel) c).getName() != null 
                && ((JLabel) c).getName().equals("lblEstadoTempPartidos")) {
                lblEstadoTempPartidos = (JLabel) c;
                break;
            }
        }
        
        if (lblEstadoTempPartidos == null) {
            lblEstadoTempPartidos = new JLabel();
            lblEstadoTempPartidos.setName("lblEstadoTempPartidos");
            lblEstadoTempPartidos.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panelAdminPartidos.add(lblEstadoTempPartidos);
        }
        
        String tempNom = (String) comboTemporadasPartidos.getSelectedItem();
        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
        
        if (t != null) {
            switch (t.getEstado()) {
                case Temporada.FUTURA:
                    lblEstadoTempPartidos.setText("  |  ● TEMPORADA FUTURA");
                    lblEstadoTempPartidos.setForeground(new Color(52, 152, 219));
                    btnNuevaJor.setEnabled(true);
                    btnNuevoPart.setEnabled(true);
                    btnInscribirEquipo.setEnabled(true);
                    break;
                case Temporada.EN_JUEGO:
                    lblEstadoTempPartidos.setText("  |  ● EN CURSO");
                    lblEstadoTempPartidos.setForeground(new Color(241, 196, 15));
                    btnNuevaJor.setEnabled(true);
                    btnNuevoPart.setEnabled(true);
                    btnInscribirEquipo.setEnabled(false);
                    break;
                case Temporada.TERMINADA:
                    lblEstadoTempPartidos.setText("  |  ● FINALIZADA");
                    lblEstadoTempPartidos.setForeground(new Color(231, 76, 60));
                    btnNuevaJor.setEnabled(false);
                    btnNuevoPart.setEnabled(false);
                    btnInscribirEquipo.setEnabled(false);
                    break;
            }
        }
        
        String jorNom = (String) comboJornadasPartidos.getSelectedItem();
        if (jorNom != null && t != null) {
            for (Jornada j : t.getListaJornadas()) {
                if (j.getNombre().equals(jorNom)) {
                    JLabel lblEstadoJornada = null;
                    for (Component c : panelAdminPartidos.getComponents()) {
                        if (c instanceof JLabel && ((JLabel) c).getName() != null 
                            && ((JLabel) c).getName().equals("lblEstadoJornada")) {
                            lblEstadoJornada = (JLabel) c;
                            break;
                        }
                    }
                    
                    if (lblEstadoJornada == null) {
                        lblEstadoJornada = new JLabel();
                        lblEstadoJornada.setName("lblEstadoJornada");
                        lblEstadoJornada.setFont(new Font("Segoe UI", Font.BOLD, 13));
                        panelAdminPartidos.add(lblEstadoJornada);
                    }
                    
                    boolean todosFinalizados = true;
                    for (Partido p : j.getListaPartidos()) {
                        if (!p.isFinalizado()) {
                            todosFinalizados = false;
                            break;
                        }
                    }
                    
                    if (j.getListaPartidos().isEmpty()) {
                        lblEstadoJornada.setText("  |  ● JORNADA SIN PARTIDOS");
                        lblEstadoJornada.setForeground(new Color(149, 165, 166));
                    } else if (todosFinalizados) {
                        lblEstadoJornada.setText("  |  ● JORNADA FINALIZADA");
                        lblEstadoJornada.setForeground(new Color(46, 204, 113));
                    } else {
                        lblEstadoJornada.setText("  |  ● JORNADA EN CURSO");
                        lblEstadoJornada.setForeground(new Color(241, 196, 15));
                    }
                    break;
                }
            }
        }
        
        panelAdminPartidos.revalidate();
        panelAdminPartidos.repaint();
    }

    private void actualizarComboEquipos() {
        comboEquiposJugadores.removeAllItems();
        String nombreTemporada = (String) comboTemporadasJugadores.getSelectedItem(); 
        
        if (nombreTemporada == null) return;

        Temporada t = datosFederacion.buscarTemporadaPorNombre(nombreTemporada);
        
        if (t != null) {
            comboEquiposJugadores.addItem("Todos");
            for (Equipo e : t.getEquiposParticipantes()) {
                comboEquiposJugadores.addItem(e.getNombre());
            }
        }
    }

    private void actualizarJugadoresPorTemporada(String nombreTemporada, String nombreEquipo) {
        panelTarjetasJugadores.removeAll();
        
        if (nombreTemporada == null) return;
        
        Temporada temporadaSeleccionada = datosFederacion.buscarTemporadaPorNombre(nombreTemporada);
        
        if (temporadaSeleccionada == null) return;

        for (Equipo e : temporadaSeleccionada.getEquiposParticipantes()) {
            if (nombreEquipo != null && 
                !nombreEquipo.equals("Todos") && 
                !nombreEquipo.equals(e.getNombre())) {
                continue;
            }

            for (Jugador j : e.getPlantilla()) {
                panelTarjetasJugadores.add(crearTarjetaJugador(j));
            }
        }

        panelTarjetasJugadores.revalidate();
        panelTarjetasJugadores.repaint();
    }

    private void crearDialogoNuevoPartido() {
        Temporada tSel = obtenerTemporadaSeleccionada();

        if (tSel == null || tSel.getEquiposParticipantes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Esta temporada no tiene equipos inscritos.");
            return;
        }

        String jorNom = (String) comboJornadasPartidos.getSelectedItem();
        JComboBox<String> comboLocal = new JComboBox<>();
        JComboBox<String> comboVisitante = new JComboBox<>();

        for (Equipo eq : tSel.getEquiposParticipantes()) {
            comboLocal.addItem(eq.getNombre());
            comboVisitante.addItem(eq.getNombre());
        }

        JPanel panelDialogo = new JPanel(new GridLayout(0, 1, 5, 5));
        panelDialogo.add(new JLabel("Selecciona Equipo Local:"));
        panelDialogo.add(comboLocal);
        panelDialogo.add(new JLabel("Selecciona Equipo Visitante:"));
        panelDialogo.add(comboVisitante);

        int result = JOptionPane.showConfirmDialog(this, panelDialogo, 
                "Nuevo Partido - " + jorNom, JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String localNom = (String) comboLocal.getSelectedItem();
            String visitNom = (String) comboVisitante.getSelectedItem();

            if (localNom.equals(visitNom)) {
                JOptionPane.showMessageDialog(this, "Un equipo no puede jugar contra sí mismo.");
                return;
            }

            Equipo local = tSel.buscarEquipoPorNombre(localNom);
            Equipo visitante = tSel.buscarEquipoPorNombre(visitNom);

            for (Jornada j : tSel.getListaJornadas()) {
                if (j.getNombre().equals(jorNom)) {
                    j.agregarPartido(new Partido(local, visitante));
                    GestorLog.exito("Partido creado manualmente: " + localNom + " vs " + visitNom + 
                                  " | Jornada: " + jorNom + " | Temporada: " + tSel.getNombre());
                    actualizarVistaPartidos();
                    break;
                }
            }
        }
    }

    private void ejecutarInscripcionEquipo() {
        if (datosFederacion.getListaTemporadas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay temporadas creadas.");
            return;
        }

        String[] nombresTemps = datosFederacion.getListaTemporadas().stream()
                                .map(Temporada::getNombre)
                                .toArray(String[]::new);

        String tempSeleccionada = (String) JOptionPane.showInputDialog(this, 
                "Selecciona la Temporada:", "Inscripción", 
                JOptionPane.QUESTION_MESSAGE, null, nombresTemps, nombresTemps[0]);

        if (tempSeleccionada == null) return;

        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempSeleccionada);
        if (t == null) return;

        ArrayList<String> equiposDisponibles = new ArrayList<>();
        
        for (Temporada temp : datosFederacion.getListaTemporadas()) {
            for (Equipo eq : temp.getEquiposParticipantes()) {
                String nombreEquipo = eq.getNombre();
                
                if (!equiposDisponibles.contains(nombreEquipo)) {
                    boolean yaInscrito = false;
                    for (Equipo eqTemp : t.getEquiposParticipantes()) {
                        if (eqTemp.getNombre().equals(nombreEquipo)) {
                            yaInscrito = true;
                            break;
                        }
                    }
                    
                    if (!yaInscrito) {
                        equiposDisponibles.add(nombreEquipo);
                    }
                }
            }
        }

        if (equiposDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay equipos disponibles para inscribir.\n" +
                "Todos los equipos ya están inscritos en esta temporada o no existen equipos.",
                "Sin equipos disponibles",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] nombresEquipos = equiposDisponibles.toArray(new String[0]);

        String equipoSeleccionado = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona el equipo a inscribir en " + tempSeleccionada + ":",
                "Inscribir Equipo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresEquipos,
                nombresEquipos[0]
        );

        if (equipoSeleccionado != null) {
            Equipo equipoOriginal = null;
            for (Temporada temp : datosFederacion.getListaTemporadas()) {
                equipoOriginal = temp.buscarEquipoPorNombre(equipoSeleccionado);
                if (equipoOriginal != null) break;
            }

            if (equipoOriginal != null) {
                Equipo nuevoEquipo = new Equipo(equipoOriginal.getNombre());
                t.inscribirEquipo(nuevoEquipo);
                
                actualizarVistaEquipos();
                sincronizarCombos();
                actualizarComboJornadas();
                
                GestorLog.exito("Equipo inscrito: " + equipoSeleccionado + " → Temporada: " + tempSeleccionada);
                
                JOptionPane.showMessageDialog(this, 
                    equipoSeleccionado + " inscrito con éxito en " + tempSeleccionada,
                    "Inscripción exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private Temporada obtenerTemporadaSeleccionada() {
        String nombreSeleccionado = (String) comboTemporadasPartidos.getSelectedItem();
        if (nombreSeleccionado == null) return null;

        return datosFederacion.buscarTemporadaPorNombre(nombreSeleccionado);
    }

    private void actualizarTablaClasificacionGrafica() {
        Temporada temp = obtenerTemporadaSeleccionada();
        if (temp == null || modeloTabla == null) return;

        modeloTabla.setRowCount(0);

        java.util.List<FilaClasificacion> ranking = CalculadoraClasificacion.calcular(temp);

        for (FilaClasificacion fila : ranking) {
            Object[] datosFila = {
                fila.getEquipo(),
                fila.getPj(),
                fila.getPg(),
                fila.getPe(),
                fila.getPp(),
                fila.getGf(),
                fila.getGc(),
                fila.getDf(),
                fila.getPuntos()
            };
            modeloTabla.addRow(datosFila);
        }
    }

    private void actualizarVistaEquipos() {
        panelTarjetasEquipo.removeAll();
        
        String tempNombre = (String) comboTemporadas.getSelectedItem();
        if (tempNombre == null) return;

        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNombre);
        
        if (t != null) {
            for (Equipo eq : t.getEquiposParticipantes()) {
                panelTarjetasEquipo.add(crearTarjetaEquipo(eq.getNombre()));
            }
        }
        
        panelTarjetasEquipo.revalidate();
        panelTarjetasEquipo.repaint();
        
        actualizarIndicadorEstadoTemporada();
    }

    private void sincronizarCombos() {
        Object tempSelEquipos = comboTemporadas.getSelectedItem();
        Object tempSelJugadores = comboTemporadasJugadores.getSelectedItem();
        Object tempSelPartidos = comboTemporadasPartidos.getSelectedItem();

        JComboBox[] combosTemp = {comboTemporadas, comboTemporadasJugadores, comboTemporadasPartidos};
        for (JComboBox c : combosTemp) {
            if (c == null) continue;
            c.removeAllItems();
            for (Temporada t : datosFederacion.getListaTemporadas()) {
                c.addItem(t.getNombre());
            }
        }

        comboTemporadas.setSelectedItem(tempSelEquipos);
        comboTemporadasJugadores.setSelectedItem(tempSelJugadores);
        comboTemporadasPartidos.setSelectedItem(tempSelPartidos);

        actualizarComboEquipos();
    }

    private void actualizarEstadoInterfaz() {
        String tempNom = (String) comboTemporadas.getSelectedItem();
        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);

        if (t != null) {
            if (t.getEstado().equals(Temporada.TERMINADA)) {
                btnNuevaJor.setEnabled(false);
                if (lblEstado != null) {
                    lblEstado.setText("Estado: Temporada Finalizada");
                    lblEstado.setForeground(Color.RED);
                }
            } else {
                btnNuevaJor.setEnabled(true);
                if (lblEstado != null) {
                    lblEstado.setText("Estado: En Competición");
                    lblEstado.setForeground(Color.GREEN);
                }
            }
        }
    }

    private void prepararDatosFederacion() {
        Temporada tPasada = new Temporada("Temporada 2024/25", Temporada.TERMINADA);
        datosFederacion.getListaTemporadas().add(tPasada);

        Temporada tActual = new Temporada("Temporada 2025/26", Temporada.EN_JUEGO);
        datosFederacion.getListaTemporadas().add(tActual);
        
        String[] nombres = {"Barcelona", "Granada", "Sevilla", "Zaragoza", "Valencia", "Athletic"};
        for(String n : nombres) {
            tActual.inscribirEquipo(new Equipo(n));
        }
    }
}
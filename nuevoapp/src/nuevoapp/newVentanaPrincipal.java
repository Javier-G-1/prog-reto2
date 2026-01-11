package nuevoapp;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
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
    private JPanel panelAdminPartidos_1;
    private Rol rolUsuario; // Ahora usamos el Enum de tu paquete gestion
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
    private JButton btnEditarJugador;
    private JPanel panelListaPartidos;

    private DefaultTableModel modeloTabla;

    private JPanel panelTarjetasJugadores;
    private JScrollPane scrollJugadores;

    private JButton btnVerFoto;
    private JButton btnCambiarFoto;
    private JButton btnCambiarEquipo;
    private JButton btnAgregarJugador;
    
    private JButton  btnNuevaJor, btnNuevoPart;
    private JButton btnNuevaTemp_1;


 

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
    private JButton btnTxema;
    private JButton btnFinalizarTemporada;

    public static void main(String[] args) {
    	  EventQueue.invokeLater(() -> {
              try {
                  Login frame = new Login();
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
        
   
        btnNuevaJor = new JButton("+ Jornada");
        btnInscribirEquipo = new JButton("Inscribir Equipo");
        comboTemporadasPartidos = new JComboBox<>();
        comboJornadasPartidos = new JComboBox<>();
        
        ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
        setIconImage(icono.getImage());
        setTitle("Federación de Balonmano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1685, 846);

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
     // En la sección de botones de jugadores (línea ~250)
        JButton btnEditarJugador = new JButton("Editar jugador");
        btnEditarJugador.addActionListener(this);
        panelBotonesJugadores.add(btnEditarJugador);
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
            actualizarIndicadorEstadoTemporada(); 
        });

        comboTemporadasPartidos.addActionListener(e -> {
            actualizarComboJornadas();
            actualizarVistaPartidos();
            actualizarIndicadorEstadoPartidos(); 
        });

        comboJornadasPartidos.addActionListener(e -> {
            actualizarVistaPartidos();
            actualizarIndicadorEstadoPartidos();
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
        
        panelAdminPartidos_1 = new JPanel();
        panelAdminPartidos_1.setBackground(new Color(30, 34, 45));
        panelAdminPartidos_1.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelPartidos.add(panelAdminPartidos_1, BorderLayout.NORTH);

        btnNuevaTemp_1 = new JButton("+ Temporada");
        btnNuevaTemp_1.setBorder(null);
        btnNuevaJor = new JButton("+ Jornada");
        btnNuevoPart = new JButton("+ Partido");

        btnNuevaTemp_1.setBackground(new Color(0, 128, 0)); 
        btnNuevaTemp_1.setForeground(Color.WHITE);
        btnNuevaTemp_1.setFocusPainted(false);

        btnNuevaTemp_1.addActionListener(this);
        btnNuevaJor.addActionListener(this);
        btnNuevoPart.addActionListener(this);

        panelAdminPartidos_1.add(btnNuevaTemp_1);
        panelAdminPartidos_1.add(btnNuevaJor);
        panelAdminPartidos_1.add(btnNuevoPart);
        
        btnFinalizarTemporada = new JButton("Finalizar temporada");
        btnFinalizarTemporada.addActionListener(this);
        btnFinalizarTemporada.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnFinalizarTemporada.setBackground(Color.PINK);
        panelAdminPartidos_1.add(btnFinalizarTemporada);
        
        btnTxema = new JButton("Txema");
        btnTxema.addActionListener(this);
        btnTxema.setBackground(new Color(255, 0, 0));
        btnTxema.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelAdminPartidos_1.add(btnTxema);

        panelAdminPartidos_1.add(new JLabel(" | "));

        comboTemporadasPartidos = new JComboBox<>();
        comboJornadasPartidos = new JComboBox<>();
        
        lblTemporadaPartido = new JLabel("Temporada:");
        panelAdminPartidos_1.add(lblTemporadaPartido);
        panelAdminPartidos_1.add(comboTemporadasPartidos);
        
        lblJornadaPartido = new JLabel("Jornada:");
        panelAdminPartidos_1.add(lblJornadaPartido);
        panelAdminPartidos_1.add(comboJornadasPartidos);

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
        //panelClasificacion.add(new PanelClasificacion(), BorderLayout.CENTER);////////////Metido por Maha




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
        	cerrarSesion();
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
            // --- PROTECCIÓN DE SEGURIDAD ---
            if (rolUsuario != Rol.ADMINISTRADOR && rolUsuario != Rol.MANAGER) {
                JOptionPane.showMessageDialog(this, "No tienes permisos para realizar esta acción.");
                return;
            }

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
                jugadorSeleccionado.setFotoURL(chooser.getSelectedFile().getAbsolutePath());
                actualizarJugadoresPorTemporada((String) comboTemporadasJugadores.getSelectedItem(), 
                                               (String) comboEquiposJugadores.getSelectedItem());
                GestorLog.exito("Foto actualizada para: " + jugadorSeleccionado.getNombre());
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

        if (e.getSource() == btnNuevaTemp_1) {
            String nombre = JOptionPane.showInputDialog(this, "Nombre de la Temporada (ej: 2026/27):");
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                GestorTemporadas gestor = new GestorTemporadas();
                
                // ⭐ VALIDACIÓN: Verificar que no haya temporada EN_JUEGO
                if (gestor.existeTemporadaEnCurso(datosFederacion)) {
                    Temporada enCurso = gestor.obtenerTemporadaEnCurso(datosFederacion);
                    
                    JOptionPane.showMessageDialog(this,
                        "No se puede crear una nueva temporada.\n\n" +
                        "❌ Temporada en curso: " + enCurso.getNombre() + "\n\n" +
                        "Debes finalizarla antes de crear una nueva temporada.",
                        "Temporada en curso activa",
                        JOptionPane.WARNING_MESSAGE);
                    
                    GestorLog.advertencia("Intento de crear temporada con " + enCurso.getNombre() + " EN_JUEGO");
                    return;
                }
                
                // ⭐ NUEVO: Obtener todas las temporadas finalizadas
                java.util.List<Temporada> temporadasFinalizadas = gestor.obtenerTemporadasFinalizadas(datosFederacion);
                Temporada temporadaOrigen = null;
                
                if (!temporadasFinalizadas.isEmpty()) {
                    // Crear array de opciones incluyendo "Crear vacía"
                    String[] opciones = new String[temporadasFinalizadas.size() + 1];
                    opciones[0] = "--- Crear temporada vacía ---";
                    
                    for (int i = 0; i < temporadasFinalizadas.size(); i++) {
                        Temporada t = temporadasFinalizadas.get(i);
                        int equipos = t.getEquiposParticipantes().size();
                        int jugadores = 0;
                        for (Equipo eq : t.getEquiposParticipantes()) {
                            jugadores += eq.getPlantilla().size();
                        }
                        opciones[i + 1] = t.getNombre() + " (" + equipos + " equipos, " + jugadores + " jugadores)";
                    }
                    
                    String seleccion = (String) JOptionPane.showInputDialog(
                        this,
                        "Selecciona de qué temporada copiar los datos:\n" +
                        "(Equipos, jugadores y configuraciones)",
                        "Origen de datos para " + nombre,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[opciones.length - 1] // Última temporada por defecto
                    );
                    
                    if (seleccion == null) {
                        GestorLog.info("Creación de temporada cancelada por el usuario");
                        return;
                    }
                    
                    // Si NO eligió "Crear vacía", buscar la temporada seleccionada
                    if (!seleccion.equals(opciones[0])) {
                        String nombreTemp = seleccion.split(" \\(")[0]; // Extraer solo el nombre
                        temporadaOrigen = datosFederacion.buscarTemporadaPorNombre(nombreTemp);
                        
                        // Confirmar la copia
                        int equipos = temporadaOrigen.getEquiposParticipantes().size();
                        int jugadores = 0;
                        for (Equipo eq : temporadaOrigen.getEquiposParticipantes()) {
                            jugadores += eq.getPlantilla().size();
                        }
                        
                        int confirmar = JOptionPane.showConfirmDialog(this,
                            "Se copiarán automáticamente:\n\n" +
                            " Equipos: " + equipos + "\n" +
                            " Jugadores: " + jugadores + "\n\n" +
                            "Desde: " + temporadaOrigen.getNombre() + "\n" +
                            "Hacia: " + nombre + "\n\n" +
                            "¿Continuar?",
                            "Confirmar creación de temporada",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        if (confirmar != JOptionPane.YES_OPTION) {
                            GestorLog.info("Creación de temporada cancelada por el usuario");
                            return;
                        }
                    }
                }
                
                // ⭐ Crear la temporada con el origen seleccionado (o null para vacía)
                boolean creada = gestor.crearTemporadaFutura(nombre, datosFederacion, temporadaOrigen);
                
                if (creada) {
                    sincronizarCombos();
                    actualizarVistaEquipos();
                    actualizarIndicadorEstadoTemporada();
                    
                    // Mostrar resumen final
                    Temporada nuevaCreada = datosFederacion.buscarTemporadaPorNombre(nombre);
                    if (nuevaCreada != null) {
                        int equiposFinales = nuevaCreada.getEquiposParticipantes().size();
                        int jugadoresFinales = 0;
                        for (Equipo eq : nuevaCreada.getEquiposParticipantes()) {
                            jugadoresFinales += eq.getPlantilla().size();
                        }
                        
                        String mensaje;
                        if (temporadaOrigen != null) {
                            mensaje = "Temporada " + nombre + " creada con éxito.\n\n" +
                                      " " + equiposFinales + " equipos inscritos\n" +
                                      " " + jugadoresFinales + " jugadores fichados\n\n" +
                                      "Copiados desde: " + temporadaOrigen.getNombre();
                        } else {
                            mensaje = equiposFinales > 0 
                                ? "Temporada " + nombre + " creada con éxito.\n\n" +
                                  " " + equiposFinales + " equipos inscritos\n" +
                                  " " + jugadoresFinales + " jugadores fichados"
                                : "Temporada " + nombre + " creada con éxito.\n\n" +
                                  " Temporada vacía (sin equipos)";
                        }
                        
                        JOptionPane.showMessageDialog(this, mensaje, 
                            "Temporada creada", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo crear la temporada. Revisa los logs.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        else if (e.getSource() == btnNuevaJor) {
            Temporada t = obtenerTemporadaSeleccionada();
            if (t == null) {
                GestorLog.advertencia("Intento de crear jornada sin temporada seleccionada");
                return;
            }  GestorTemporadas gestor = new GestorTemporadas();
            if (gestor.existeTemporadaEnCurso(datosFederacion)) {
                Temporada temporadaEnCurso = gestor.obtenerTemporadaEnCurso(datosFederacion);
                
                JOptionPane.showMessageDialog(this,
                    "Ya existe una temporada en curso: " + temporadaEnCurso.getNombre() + "\n" +
                    "Debes finalizarla antes de iniciar una nueva temporada.",
                    "Temporada en curso activa",
                    JOptionPane.WARNING_MESSAGE);
                
                GestorLog.advertencia("Intento de activar " + t.getNombre() + 
                                    " mientras " + temporadaEnCurso.getNombre() + " está EN_JUEGO");
                return;
            }

            try {
                int equiposInscritos = t.getEquiposParticipantes().size();
                String estadoAnterior = t.getEstado();
                
                // Generar calendario (el log ya está dentro de GeneradorCalendario)
                GeneradorCalendario.crearCalendario(t);
                
                t.setEstado(Temporada.EN_JUEGO);
                sincronizarCombos(); // 
                actualizarComboJornadas();
                actualizarVistaPartidos(); 
                actualizarIndicadorEstadoTemporada();
                actualizarIndicadorEstadoPartidos(); 
                actualizarVistaEquipos();
                
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
        else if (e.getSource() == btnEditarJugador) {
            if (jugadorSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un jugador primero");
                return;
            }
            
            String tempNom = (String) comboTemporadasJugadores.getSelectedItem();
            Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
            
            if (t != null && !t.getEstado().equals(Temporada.FUTURA)) {
                JOptionPane.showMessageDialog(this,
                    "Solo se pueden editar jugadores en temporadas FUTURAS",
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            DialogoEditarJugador dialogo = new DialogoEditarJugador(this, jugadorSeleccionado);
            dialogo.setVisible(true);
            
            if (dialogo.isAceptado()) {
                // Refrescar vista
                actualizarJugadoresPorTemporada(
                    (String) comboTemporadasJugadores.getSelectedItem(),
                    (String) comboEquiposJugadores.getSelectedItem()
                );
                GestorLog.exito("Jugador editado: " + jugadorSeleccionado.getNombre());
            }
        }
        else if (e.getSource() == btnFinalizarTemporada) {
        	// Mirar si todos los partidos están jugados
        	finalizarTemporada();
        }
        else if (e.getSource() == btnTxema) {
        	funcionTxema();
        }
    }


 

 // ============================================
 // MÉTODOS MEJORADOS PARA newVentanaPrincipal
 // ============================================

 /**
  * Crea una tarjeta mejorada de jugador con todos los datos del XML
  */
 private JPanel crearTarjetaJugador(Jugador jugador) {
     JPanel tarjeta = new JPanel(new BorderLayout(10, 5));
     tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
     tarjeta.setBackground(new Color(24, 25, 50));
     tarjeta.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
         BorderFactory.createEmptyBorder(10, 10, 10, 10)
     ));

     // ===== FOTO =====
     JLabel lblFoto = new JLabel("Sin foto", SwingConstants.CENTER);
     lblFoto.setPreferredSize(new Dimension(100, 100));
     lblFoto.setForeground(Color.WHITE);
   
     lblFoto.setBackground(new Color(30, 34, 41));
     lblFoto.setOpaque(true);

     if (jugador.getFotoURL() != null && !jugador.getFotoURL().isEmpty()) {
         try {
             ImageIcon icon = new ImageIcon(
                 new ImageIcon(jugador.getFotoURL())
                     .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)
             );
             lblFoto.setIcon(icon);
             lblFoto.setText("");
         } catch (Exception e) {
             // Mantener "Sin foto" si hay error
         }
     }
     tarjeta.add(lblFoto, BorderLayout.WEST);

     // ===== INFORMACIÓN PRINCIPAL =====
     JPanel panelInfo = new JPanel();
     panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
     panelInfo.setOpaque(false);
     panelInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

     // Nombre y Dorsal
     JPanel panelNombreDorsal = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
     panelNombreDorsal.setOpaque(false);
     
     JLabel lblNombre = new JLabel(jugador.getNombre());
     lblNombre.setForeground(Color.WHITE);
     lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
     panelNombreDorsal.add(lblNombre);
     
     if (jugador.getDorsal() > 0) {
         JLabel lblDorsal = new JLabel("#" + jugador.getDorsal());
         lblDorsal.setForeground(new Color(45, 55, 140));
         lblDorsal.setFont(new Font("Segoe UI", Font.BOLD, 16));
         panelNombreDorsal.add(lblDorsal);
     }
     panelInfo.add(panelNombreDorsal);

     // Posición
     JLabel lblPos = new JLabel(" " + jugador.getPosicion());
     lblPos.setForeground(new Color(100, 181, 246));
     lblPos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
     panelInfo.add(lblPos);

     // Nacionalidad y Edad
     JPanel panelNacEdad = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
     panelNacEdad.setOpaque(false);
     
     JLabel lblNacionalidad = new JLabel(" " + jugador.getNacionalidad());
     lblNacionalidad.setForeground(new Color(220, 220, 220));
     lblNacionalidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
     panelNacEdad.add(lblNacionalidad);
     
     JLabel lblEdad = new JLabel(" " + jugador.getEdad() + " años");
     lblEdad.setForeground(new Color(220, 220, 220));
     lblEdad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
     panelNacEdad.add(lblEdad);
     
     panelInfo.add(panelNacEdad);

     // Altura y Peso
     JPanel panelFisico = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
     panelFisico.setOpaque(false);
     
     JLabel lblAltura = new JLabel(" " + jugador.getAltura());
     lblAltura.setForeground(new Color(220, 220, 220));
     lblAltura.setFont(new Font("Segoe UI", Font.PLAIN, 12));
     panelFisico.add(lblAltura);
     
     JLabel lblPeso = new JLabel(" " + jugador.getPeso());
     lblPeso.setForeground(new Color(220, 220, 220));
     lblPeso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
     panelFisico.add(lblPeso);
     
     panelInfo.add(panelFisico);

     tarjeta.add(panelInfo, BorderLayout.CENTER);

     // ===== EVENTO DE SELECCIÓN =====
     tarjeta.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
             jugadorSeleccionado = jugador;

             // Desmarcar todas las tarjetas
             for (Component c : panelTarjetasJugadores.getComponents()) {
                 if (c instanceof JPanel) {
                     c.setBackground(new Color(24, 25, 50));
                     ((JPanel) c).setBorder(BorderFactory.createCompoundBorder(
                         BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
                         BorderFactory.createEmptyBorder(10, 10, 10, 10)
                     ));
                 }
             }

             // Marcar tarjeta seleccionada
             tarjeta.setBackground(new Color(45, 55, 140));
             tarjeta.setBorder(BorderFactory.createCompoundBorder(
                 BorderFactory.createLineBorder(new Color(100, 150, 255), 2),
                 BorderFactory.createEmptyBorder(10, 10, 10, 10)
             ));
         }
         
         @Override
         public void mouseEntered(MouseEvent e) {
             if (jugadorSeleccionado != jugador) {
                 tarjeta.setBorder(BorderFactory.createCompoundBorder(
                     BorderFactory.createLineBorder(new Color(80, 90, 140), 2),
                     BorderFactory.createEmptyBorder(10, 10, 10, 10)
                 ));
             }
         }
         
         @Override
         public void mouseExited(MouseEvent e) {
             if (jugadorSeleccionado != jugador) {
                 tarjeta.setBorder(BorderFactory.createCompoundBorder(
                     BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
                     BorderFactory.createEmptyBorder(10, 10, 10, 10)
                 ));
             }
         }
     });

     return tarjeta;
 }

 /**
  * Crea una tarjeta mejorada de equipo con estadísticas
  */
 private JPanel crearTarjetaEquipo(String nombreEquipo) {
     // Buscar el equipo en la temporada actual
     String tempNom = (String) comboTemporadas.getSelectedItem();
     Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
     Equipo equipo = t != null ? t.buscarEquipoPorNombre(nombreEquipo) : null;
     
     JPanel panelTarjeta = new JPanel();
     panelTarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
     panelTarjeta.setBackground(new Color(24, 25, 50));
     panelTarjeta.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
         BorderFactory.createEmptyBorder(10, 10, 10, 10)
     ));
     panelTarjeta.setLayout(new BorderLayout(15, 10));

     // ===== ESCUDO =====
     JLabel lblEscudo = new JLabel();
     lblEscudo.setPreferredSize(new Dimension(90, 90));
     lblEscudo.setHorizontalAlignment(SwingConstants.CENTER);
     lblEscudo.setVerticalAlignment(SwingConstants.CENTER);
    
     lblEscudo.setBackground(new Color(30, 34, 41));
     lblEscudo.setOpaque(true);
     lblEscudo.setText("🏆");
     lblEscudo.setFont(new Font("Segoe UI", Font.PLAIN, 40));
     
     if (equipo != null && equipo.getRutaEscudo() != null && !equipo.getRutaEscudo().isEmpty()) {
         try {
             ImageIcon icon = new ImageIcon(
                 new ImageIcon(equipo.getRutaEscudo())
                     .getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)
             );
             lblEscudo.setIcon(icon);
             lblEscudo.setText("");
         } catch (Exception e) {
             // Mantener emoji si hay error
         }
     }
     panelTarjeta.add(lblEscudo, BorderLayout.WEST);

     // ===== INFORMACIÓN DEL EQUIPO =====
     JPanel panelInfo = new JPanel();
     panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
     panelInfo.setOpaque(false);
     
     // Nombre del equipo
     JLabel lblNombre = new JLabel(nombreEquipo);
     lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
     lblNombre.setForeground(Color.WHITE);
     panelInfo.add(lblNombre);
     
     panelInfo.add(Box.createVerticalStrut(5));
     
     // Número de jugadores
     if (equipo != null) {
         JLabel lblJugadores = new JLabel("👥 " + equipo.getPlantilla().size() + " jugadores");
         lblJugadores.setFont(new Font("Segoe UI", Font.PLAIN, 13));
         lblJugadores.setForeground(new Color(180, 180, 180));
         panelInfo.add(lblJugadores);
         
         // ID del equipo
         if (equipo.getNombre() != null) {
             JLabel lblId = new JLabel("ID: " + equipo.getNombre());
             lblId.setFont(new Font("Segoe UI", Font.PLAIN, 11));
             lblId.setForeground(new Color(120, 120, 120));
             panelInfo.add(lblId);
         }
     }
     
     panelTarjeta.add(panelInfo, BorderLayout.CENTER);

     // ===== BOTONES =====
     JPanel panelBotones = new JPanel();
     panelBotones.setOpaque(false);
     panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

     JButton btnCambiarEscudo = new JButton("Cambiar Escudo");
     btnCambiarEscudo.setFont(new Font("Segoe UI", Font.PLAIN, 11));

     btnCambiarEscudo.setFocusPainted(false);
     btnCambiarEscudo.setBorderPainted(false);
     btnCambiarEscudo.setPreferredSize(new Dimension(130, 30));
     
     btnCambiarEscudo.addActionListener(e -> {
         String tempNombre = (String) comboTemporadas.getSelectedItem();
         Temporada temp = datosFederacion.buscarTemporadaPorNombre(tempNombre);
         
         if (temp != null && !temp.getEstado().equals(Temporada.FUTURA)) {
             JOptionPane.showMessageDialog(this, 
                 "Solo se pueden hacer cambios en temporadas FUTURAS",
                 "Operación no permitida", 
                 JOptionPane.WARNING_MESSAGE);
             return;
         }
         
         JFileChooser chooser = new JFileChooser();
         chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
             "Imágenes", "jpg", "jpeg", "png", "gif"));
         
         int resultado = chooser.showOpenDialog(this);
         if (resultado == JFileChooser.APPROVE_OPTION) {
             String rutaEscudo = chooser.getSelectedFile().getAbsolutePath();
             
             if (equipo != null) {
            	 equipo.setRutaEscudo(rutaEscudo);
             }
             
             ImageIcon icon = new ImageIcon(
                 new ImageIcon(rutaEscudo).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)
             );
             lblEscudo.setIcon(icon);
             lblEscudo.setText("");
             
             GestorLog.exito("Escudo actualizado: " + nombreEquipo + " | Ruta: " + rutaEscudo);
         }
     });
     panelBotones.add(btnCambiarEscudo);

     JButton btnVerEscudo = new JButton("Ver Escudo");
     btnVerEscudo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
     btnVerEscudo.setBackground(new Color(52, 152, 219));
     
     btnVerEscudo.setFocusPainted(false);
     btnVerEscudo.setBorderPainted(false);
     btnVerEscudo.setPreferredSize(new Dimension(110, 30));
     
     btnVerEscudo.addActionListener(e -> {
         Icon icon = lblEscudo.getIcon();
         if (icon != null) {
             ImageIcon img = new ImageIcon(
                 ((ImageIcon) icon).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)
             );
             JOptionPane.showMessageDialog(this, "", nombreEquipo, 
                 JOptionPane.INFORMATION_MESSAGE, img);
         } else {
             JOptionPane.showMessageDialog(this, "No tiene escudo", nombreEquipo, 
                 JOptionPane.WARNING_MESSAGE);
         }
     });
     panelBotones.add(btnVerEscudo);

     panelTarjeta.add(panelBotones, BorderLayout.EAST);

     return panelTarjeta;
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
                actualizarIndicadorEstadoPartidos(); // ← AÑADIR ESTA LÍNEA
                
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

        // Buscar el label existente
        for (Component c : panelSuperior.getComponents()) {
            if (c instanceof JLabel && ((JLabel) c).getName() != null
                && ((JLabel) c).getName().equals("lblEstadoTemporada")) {
                lblEstadoTemp = (JLabel) c;
                break;
            }
        }

        // ⭐ Si no existe, crearlo AL FINAL del panel
        if (lblEstadoTemp == null) {
            lblEstadoTemp = new JLabel();
            lblEstadoTemp.setName("lblEstadoTemporada");
            lblEstadoTemp.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            // Agregar espaciador antes del label
            panelSuperior.add(Box.createHorizontalStrut(15));
            
            // Agregar al final (sin especificar posición)
            panelSuperior.add(lblEstadoTemp);
            
        }

        // Obtener la temporada seleccionada
        String tempNom = (String) comboTemporadas.getSelectedItem();
        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);

        if (t != null) {
            switch (t.getEstado()) {
                case Temporada.FUTURA:
                    lblEstadoTemp.setText("● FUTURA");
                    lblEstadoTemp.setForeground(new Color(52, 152, 219));
                    btnAgregarEquipo.setEnabled(true);
                    btnAgregarEquipo.setToolTipText("Agregar nuevo equipo");
                    
                    // ⭐ Control de botones según estado
                    btnFinalizarTemporada.setEnabled(false);
                    btnTxema.setEnabled(false);
                    break;
                    
                case Temporada.EN_JUEGO:
                    lblEstadoTemp.setText("● EN CURSO");
                    lblEstadoTemp.setForeground(new Color(241, 196, 15));
                    btnAgregarEquipo.setEnabled(false);
                    btnAgregarEquipo.setToolTipText("No se pueden agregar equipos a temporadas en curso");
                    
                    // ⭐ Habilitar botones de gestión
                    btnFinalizarTemporada.setEnabled(true);
                    btnTxema.setEnabled(true);
                    break;
                    
                case Temporada.TERMINADA:
                    lblEstadoTemp.setText("● FINALIZADA");
                    lblEstadoTemp.setForeground(new Color(231, 76, 60));
                    btnAgregarEquipo.setEnabled(false);
                    btnAgregarEquipo.setToolTipText("No se pueden agregar equipos a temporadas finalizadas");
                    
                    // ⭐ Deshabilitar todos los botones de modificación
                    btnFinalizarTemporada.setEnabled(false);
                    btnTxema.setEnabled(false);
                    break;
            }
        }

        panelSuperior.revalidate();
        panelSuperior.repaint();
    }

    private void actualizarIndicadorEstadoPartidos() {
        JLabel lblEstadoTempPartidos = null;
        for (Component c : panelAdminPartidos_1.getComponents()) {
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
            panelAdminPartidos_1.add(lblEstadoTempPartidos);
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
                    btnFinalizarTemporada.setEnabled(false);
                    btnTxema.setEnabled(false);
                    break;
                case Temporada.EN_JUEGO:
                    lblEstadoTempPartidos.setText("  |  ● EN CURSO");
                    lblEstadoTempPartidos.setForeground(new Color(241, 196, 15));
                    btnNuevaJor.setEnabled(true);
                    btnNuevoPart.setEnabled(true);
                    btnInscribirEquipo.setEnabled(false);
                    btnFinalizarTemporada.setEnabled(true);
                    btnTxema.setEnabled(true);
                    break;
                case Temporada.TERMINADA:
                    lblEstadoTempPartidos.setText("  |  ● FINALIZADA");
                    lblEstadoTempPartidos.setForeground(new Color(231, 76, 60));
                    btnNuevaJor.setEnabled(false);
                    btnNuevoPart.setEnabled(false);
                    btnInscribirEquipo.setEnabled(false);
                    btnFinalizarTemporada.setEnabled(false);
                    btnTxema.setEnabled(false);
                    break;
            }
        }
        
        String jorNom = (String) comboJornadasPartidos.getSelectedItem();
        if (jorNom != null && t != null) {
            for (Jornada j : t.getListaJornadas()) {
                if (j.getNombre().equals(jorNom)) {
                    JLabel lblEstadoJornada = null;
                    for (Component c : panelAdminPartidos_1.getComponents()) {
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
                        panelAdminPartidos_1.add(lblEstadoJornada);
                    }
                    
                  
                    }
                    
                  
                    break;
                }
            }
        
        
        panelAdminPartidos_1.revalidate();
        panelAdminPartidos_1.repaint();
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
        // Verificamos que la temporada no sea nula y que el modelo de la tabla exista
        if (temp == null || modeloTabla == null) return;

        // Limpiamos la tabla antes de volver a llenarla
        modeloTabla.setRowCount(0);

        try {
            // 1. CORRECCIÓN: Obtener el objeto Clasificacion y luego su lista de filas
            Clasificacion clasificacion = CalculadoraClasificacion.calcular(temp);
            java.util.List<FilaClasificacion> ranking = clasificacion.getFilas();

            for (FilaClasificacion fila : ranking) {
                Object[] datosFila = {
                    fila.getPosicion(),     
                    fila.getNombre(),        
                    fila.getPuntos(),        
                    fila.getPj(),
                    fila.getPg(),
                    fila.getPe(),
                    fila.getPp(),
                    fila.getGf(),
                    fila.getGc(),
                    fila.getDifFormateada()  // 4. MEJORA: Usar la diferencia con signo (+5, -2, etc.)
                };
                modeloTabla.addRow(datosFila);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar la tabla: " + e.getMessage());
            // Aquí podrías mostrar un mensaje de error al usuario con JOptionPane
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

    
    private void finalizarTemporada() {
		// TODO Auto-generated method stub
    	Temporada temp = obtenerTemporadaSeleccionada();
    	if (!temp.getEstado().equals(Temporada.EN_JUEGO)) { 
    		JOptionPane.showMessageDialog(this, "Solo se pueden finalizar temporadas EN CURSO", "Operación no permitida", JOptionPane.WARNING_MESSAGE); return; } 
    	if (!todosLosPartidosFinalizados(temp)) { 
    		int partidosPendientes = 0; 
    		for (Jornada j : temp.getListaJornadas()) { 
    			for (Partido p : j.getListaPartidos()) { 
    				if (!p.isFinalizado()) { 
    					partidosPendientes++; 
					} 
				} 
			} 
    		 JOptionPane.showMessageDialog(this, 
    		            "No se puede finalizar la temporada.\n" + 
    		            "Aún hay " + partidosPendientes + " partido(s) sin jugar.", 
    		            "Partidos pendientes", 
    		            JOptionPane.WARNING_MESSAGE);
    		        GestorLog.advertencia("Intento de finalizar temporada con " + partidosPendientes + " partidos pendientes | " + temp.getNombre());
    		        return;
    		    }
    		    
    		    // Confirmar con el usuario
    		    int confirmar = JOptionPane.showConfirmDialog(this,
    		        "¿Estás seguro de finalizar la temporada " + temp.getNombre() + "?\n" +
    		        "Esta acción no se puede deshacer.",
    		        "Confirmar finalización",
    		        JOptionPane.YES_NO_OPTION,
    		        JOptionPane.WARNING_MESSAGE);
    		    
    		    if (confirmar != JOptionPane.YES_OPTION) {
    		        GestorLog.info("Finalización de temporada cancelada por el usuario | " + temp.getNombre());
    		        return;
    		    }
    		    
    		    // Finalizar temporada
    		    String estadoAnterior = temp.getEstado();
    		    temp.setEstado(Temporada.TERMINADA);
    		    
      		    sincronizarCombos(); // Recargar combos
    		    actualizarIndicadorEstadoPartidos(); // Actualizar indicadores de partidos
    		    actualizarIndicadorEstadoTemporada(); // Actualizar indicadores de equipos
    		    actualizarEstadoInterfaz(); // Actualizar estado general
    		    actualizarVistaPartidos(); // Refrescar vista de partidos
    		    actualizarVistaEquipos(); // Refrescar vista de equipos
    		    
    		    // Log de éxito
    		    GestorLog.exito("Temporada finalizada: " + temp.getNombre() + 
    		                  " | Estado: " + estadoAnterior + " → " + Temporada.TERMINADA +
    		                  " | Equipos: " + temp.getEquiposParticipantes().size() +
    		                  " | Jornadas: " + temp.getListaJornadas().size());
    		    
    		    // Mensaje al usuario
    		    JOptionPane.showMessageDialog(this,
    		        "Temporada " + temp.getNombre() + " finalizada con éxito.\n" +
    		        "Puedes consultar la clasificación final en la pestaña Clasificación.",
    		        "Temporada finalizada",
    		        JOptionPane.INFORMATION_MESSAGE);
    		  
    		  
   
  
    		}
    
    private void funcionTxema() {
		// TODO Auto-generated method stub
    	Temporada temp = obtenerTemporadaSeleccionada();
		Random random = new Random();
		if(!todosLosPartidosFinalizados(temp)) {
			for (Jornada j : temp.getListaJornadas()) {
				for (Partido p : j.getListaPartidos()) {
					if (!p.isFinalizado()) {
						int golesLocal = random.nextInt(49)+1;
						int golesVisitante = random.nextInt(49)+1;
						p.setGolesLocal(golesLocal);
						p.setGolesVisitante(golesVisitante);
						p.setFinalizado(true);
					}
				}
			}
		}
	    // ⭐ ACTUALIZAR TODO DESPUÉS DE LA SIMULACIÓN
        actualizarVistaPartidos(); // Refrescar vista de partidos
        actualizarIndicadorEstadoPartidos(); // Actualizar indicadores
        
        // Si estás en la vista de clasificación, actualizarla también
        if (panelClasificacion.isVisible()) {
            actualizarTablaClasificacionGrafica();
        }
        
        GestorLog.exito("Función Txema ejecutada | " + temp.getNombre());
        
       
    

    }

  
    private boolean todosLosPartidosFinalizados(Temporada temp) {
		// TODO Auto-generated method stub
    	int partidosSinJugar = 0;
    	for (Jornada j : temp.getListaJornadas()) { 
			for (Partido p : j.getListaPartidos()) { 
				if (!p.isFinalizado()) { 
					partidosSinJugar++; 
				} 
			} 
		}
    	if (partidosSinJugar == 0) {
    		return true;
    	}
    	else {
    		return false;
    	}
	}

	public void despuesDelLogin(Rol rol, String nombre) {
        this.rolUsuario = rol; // Guardamos el rol para validaciones en tarjetas
        this.lblBienvenido.setText("Bienvenido, " + nombre);
        this.lblUsuario.setText("Rol: " + rol.getNombreLegible());
        
        // 1. Limpieza de seguridad
        ocultarTodosLosControles();

        // 2. Aplicación de permisos específicos
        switch (rol) {
            case ADMINISTRADOR:
                mostrarTodo(true);
                break;

            case ARBITRO:
                habilitarNavegacionBasica();
                panelAdminPartidos_1.setVisible(true); 
                btnNuevaJor.setVisible(true);
                btnNuevoPart.setVisible(true);
                // El árbitro no crea temporadas, solo gestiona las existentes
                btnNuevaTemp_1.setVisible(false); 
                break;

            case MANAGER:
                habilitarNavegacionBasica();
                // El Manager se centra en la gestión de la plantilla y el club
                btnJugadores.setVisible(true);
                btnVerFoto.setVisible(true);
                btnCambiarFoto.setVisible(true); // Cambiar escudo/foto
                btnAgregarJugador.setVisible(true);
                btnCambiarEquipo.setVisible(true);
                btnInscribirEquipo.setVisible(true);
                break;

            case INVITADO:
            default:
                habilitarNavegacionBasica();
                btnVerFoto.setVisible(true); 
                panelAdminPartidos_1.setVisible(true); // El invitado solo puede ver
                btnCambiarFoto.setVisible(false);
                btnInscribirEquipo.setVisible(false);
                break;
        }
    }
    private void cerrarSesion() {
        // 1. Preguntar al usuario para evitar cierres accidentales
        int respuesta = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que quieres cerrar la sesión?", 
                "Cerrar Sesión", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            // 2. Limpiar el rol por seguridad
            this.rolUsuario = null; 
            
            // 3. Cerrar la ventana principal actual
            this.dispose(); 
            
            // 4. Crear y mostrar una nueva instancia del Login
            Login ventanaLogin = new Login();
            ventanaLogin.setVisible(true);
            
            // Log de éxito (opcional)
            System.out.println("Sesión cerrada correctamente.");
        }
    }
    /**
     * Oculta absolutamente todos los elementos de edición y gestión.
     */
    private void ocultarTodosLosControles() {
        // Navegación principal
        btnInicio.setVisible(false);
        btnEquipos.setVisible(false);
        btnJugadores.setVisible(false);
        btnPartidos.setVisible(false);
        btnClasificacin.setVisible(false);

        // Gestión de Partidos y Temporadas
        panelAdminPartidos_1.setVisible(false);
        btnNuevaTemp_1.setVisible(false);
        btnNuevaJor.setVisible(false);
        btnNuevoPart.setVisible(false);
        
        // Gestión de Equipos
        btnAgregarEquipo.setVisible(false);
        btnInscribirEquipo.setVisible(false);
        
        // Gestión de Jugadores
        btnAgregarJugador.setVisible(false);
        btnCambiarEquipo.setVisible(false);
        btnCambiarFoto.setVisible(false);
        btnVerFoto.setVisible(false);
    }

    /**
     * Muestra los botones de navegación que son comunes para todos los usuarios.
     */
    private void habilitarNavegacionBasica() {
        btnInicio.setVisible(true);
        btnEquipos.setVisible(true);
        btnJugadores.setVisible(true);
        btnPartidos.setVisible(true);
        btnClasificacin.setVisible(true);
    }

    /**
     * Activa todos los componentes de la interfaz (Exclusivo para Administrador).
     */
    private void mostrarTodo(boolean estado) {
        habilitarNavegacionBasica();
        
        // Paneles y botones de administración
        panelAdminPartidos_1.setVisible(estado);
        btnNuevaTemp_1.setVisible(estado);
        btnNuevaJor.setVisible(estado);
        btnNuevoPart.setVisible(estado);
        
        // Botones de equipos
        btnAgregarEquipo.setVisible(estado);
        btnInscribirEquipo.setVisible(estado);
        
        // Botones de jugadores
        btnAgregarJugador.setVisible(estado);
        btnCambiarEquipo.setVisible(estado);
       
        btnVerFoto.setVisible(estado);
    }}
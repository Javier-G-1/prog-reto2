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
import gestion.Equipo;
import gestion.Jornada;
import gestion.Temporada;

public class newVentanaPrincipal extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    // Este objeto guardará TODA la información de la app
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

    // Lista global de temporadas

    
    private JComboBox<String> comboTemporadasJugadores;
    private JComboBox<String> comboEquiposJugadores;
  
    private JPanel panelListaPartidos; // Importante para limpiar y redibujar
    private JTable tablaClasificacion; // Para la sección de clasificación
    private DefaultTableModel modeloTabla;

    // Lista de jugadores
    private JPanel panelTarjetasJugadores;
    private JScrollPane scrollJugadores;


    // Botones
    private JButton btnVerFoto;
    private JButton btnCambiarFoto;
    private JButton btnCambiarEquipo;
    private JButton btnAgregarJugador;
    
    // Botones de gestión de partidos

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
        
        // 2. SEGUNDO: Intentar cargar de archivo o preparar iniciales
        // Aquí es donde llamabas a GestorTemporadas
        new GestorTemporadas().prepararEscenarioInicial(this.datosFederacion); 
        
        // 3. TERCERO: Iniciar los componentes visuales
        // ❌ REMOVIDO: sincronizarCombos(); - Se moverá al final

        

        
        // 3. CREAR COMPONENTES (Botones, Combos)
        btnNuevaTemp = new JButton("+ Temporada");
        btnNuevaJor = new JButton("+ Jornada");
        btnInscribirEquipo = new JButton("Inscribir Equipo");
        comboTemporadasPartidos = new JComboBox<>();
        comboJornadasPartidos = new JComboBox<>();
        
        
        // Configuración ventana
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

        // --- Panel superior con títulos y usuario
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

        // --- Panel central con botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(30, 30, 30));
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelMenu.add(panelBotones, BorderLayout.CENTER);

        // Botones agregados individualmente, cada uno con "this"
        btnInicio = new JButton("Inicio");
        btnInicio.setBorder(null);
        btnInicio.setBackground(new Color(45, 55, 140));
        btnInicio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnInicio.setForeground(new Color(255, 255, 255));
        btnInicio.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInicio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnInicio.addActionListener(this);  // <-- this
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
        // Imagen decorativa (opcional)
        ImageIcon iconoBalon = new ImageIcon(getClass().getResource("/assets/handball.png"));
        panelInicio.setLayout(new BorderLayout(0, 0));
        JLabel lblImagen = new JLabel(iconoBalon);
        panelInicio.add(lblImagen);

        panelEquipos = new JPanel();
        panelEquipos.setBackground(new Color(20, 24, 31));
        panelCards.add(panelEquipos, "equipos");
        panelEquipos.setLayout(new BorderLayout(10, 10));
        panelEquipos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===================== PANEL EQUIPOS =====================
        panelEquipos.setLayout(new BorderLayout(10, 10));
        panelEquipos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Panel superior: Combobox y botón agregar ----
        panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(20, 24, 31));
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelEquipos.add(panelSuperior, BorderLayout.NORTH);

        // Etiqueta Temporada
        lblTemp = new JLabel("Temporada:");
        lblTemp.setForeground(Color.WHITE);
        lblTemp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelSuperior.add(lblTemp);

        // Combobox de temporadas
        comboTemporadas = new JComboBox<>(new String[] {"Temporada 2025/26", "Temporada 2024/25"});
        comboTemporadas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelSuperior.add(comboTemporadas);

        // Botón Agregar Equipo
        btnAgregarEquipo = new JButton("Agregar Equipo");
        btnAgregarEquipo.setBorder(null);
        btnAgregarEquipo.setBackground(new Color(45, 55, 140));
        btnAgregarEquipo.setForeground(Color.WHITE);
        btnAgregarEquipo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelSuperior.add(btnAgregarEquipo);

        // ---- Panel central: tarjetas con scroll ----
        panelTarjetasEquipo = new JPanel();
        panelTarjetasEquipo.setLayout(new BoxLayout(panelTarjetasEquipo, BoxLayout.Y_AXIS));
        panelTarjetasEquipo.setBackground(new Color(30, 34, 41));

        scrollEquipos = new JScrollPane(panelTarjetasEquipo);
        scrollEquipos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollEquipos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollEquipos.getVerticalScrollBar().setUnitIncrement(16); // scroll suave
        panelEquipos.add(scrollEquipos, BorderLayout.CENTER);
        
    
        // ---- NO AGREGAR EQUIPOS AQUÍ - Se cargarán desde datosFederacion ----
        // Los equipos se añadirán más adelante con los datos reales

        // ---- Botón Agregar Equipo ----
        btnAgregarEquipo.addActionListener(ae -> {
            String nombreE = JOptionPane.showInputDialog(this, "Nombre del nuevo equipo:");
            String nombreT = (String) comboTemporadas.getSelectedItem();

            if (nombreE != null && !nombreE.trim().isEmpty() && nombreT != null) {
                // Verificar que el equipo no exista ya en esta temporada
                Temporada t = datosFederacion.buscarTemporadaPorNombre(nombreT);
                
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
                        return;
                    }
                    
                    // Crear e inscribir el equipo
                    Equipo nuevoEquipo = new Equipo(nombreE.trim());
                    t.inscribirEquipo(nuevoEquipo);
                    
                    actualizarVistaEquipos(); 
                    sincronizarCombos();
                    
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

        // ======= AGREGAR TEMPORADA, EQUIPOS Y JUGADORES COMPLETOS =======
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

        	// 1. CREACIÓN DE DATOS (Se guardan en el modelo)
        	for (String[] equipoData : equiposConJugadores) {
        	    Equipo eq = new Equipo(equipoData[0], null);
        	    for (int i = 1; i < equipoData.length; i++) {
        	        Jugador j = new Jugador(equipoData[i], "Posición", 25, null);
        	        eq.ficharJugador(j);
        	    }
        	    // IMPORTANTE: Guardamos el equipo en la temporada
        	    temporada2025_26.inscribirEquipo(eq);
        	}

        	// 2. CONFIGURACIÓN DE ESCUCHADORES (Listeners)
        	// Cuando cambies de temporada en equipos, se actualizan las tarjetas
        	comboTemporadas.addActionListener(e -> {
        	    actualizarVistaEquipos();
        	});
        	
        	// Cuando cambies de temporada en jugadores, se actualizan sus equipos
        	comboTemporadasJugadores.addActionListener(e -> {
        	    actualizarComboEquipos();
        	});

        	// Cuando cambies de equipo en jugadores, se actualiza la lista de personas
        	comboEquiposJugadores.addActionListener(e -> {
        	    String equipoSel = (String) comboEquiposJugadores.getSelectedItem();
        	    if (equipoSel != null) {
        	        actualizarJugadoresPorTemporada(equipoSel);
        	    }
        	});


        /// ======================= PANEL PARTIDOS =======================
        panelPartidos = new JPanel();
        panelPartidos.setBackground(new Color(20, 24, 31));
        panelPartidos.setLayout(new BorderLayout(0, 0));
        panelCards.add(panelPartidos, "partidos");
       
        // 1. Inicializar listas de datos
      
            
        // 2. Crear los paneles (CONTENEDORES)
         
        panelAdminPartidos = new JPanel();
            
        // 3. Crear los botones y combos (COMPONENTES)
        btnInscribirEquipo = new JButton("Inscribir Equipo");
        btnNuevaJor = new JButton("+ Jornada");
        comboTemporadasPartidos = new JComboBox<>();
            
        // 4. Configurar eventos
        btnInscribirEquipo.addActionListener(this);
        btnNuevaJor.addActionListener(this);
            
        // 5. Montar la interfaz (AÑADIR)
        panelSuperior.add(btnInscribirEquipo);
        panelAdminPartidos.add(btnNuevaJor);
            
        // 6. Carga inicial se hará al final del constructor
        
        // --- 1. BARRA SUPERIOR DE ADMINISTRACIÓN ---
        panelAdminPartidos = new JPanel();
        panelAdminPartidos.setBackground(new Color(30, 34, 45));
        panelAdminPartidos.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelPartidos.add(panelAdminPartidos, BorderLayout.NORTH);

        // Inicializamos los botones GLOBALES (sin el _1)
        btnNuevaTemp = new JButton("+ Temporada");
        btnNuevaJor = new JButton("+ Jornada");
        btnNuevoPart = new JButton("+ Partido");

        // Estilo para el botón de temporada
        btnNuevaTemp.setBackground(new Color(46, 204, 113)); 
        btnNuevaTemp.setForeground(Color.WHITE);
        btnNuevaTemp.setFocusPainted(false);

        // REGISTRO ÚNICO (Muy importante)
        btnNuevaTemp.addActionListener(this);
        btnNuevaJor.addActionListener(this);
        btnNuevoPart.addActionListener(this);

        // Añadimos a la barra
        panelAdminPartidos.add(btnNuevaTemp);
        panelAdminPartidos.add(btnNuevaJor);
        panelAdminPartidos.add(btnNuevoPart);

        panelAdminPartidos.add(new JLabel(" | "));

        // Filtros y Combos
        comboTemporadasPartidos = new JComboBox<>();
        comboJornadasPartidos = new JComboBox<>();
        
        lblTemporadaPartido = new JLabel("Temporada:");
        panelAdminPartidos.add(lblTemporadaPartido);
        panelAdminPartidos.add(comboTemporadasPartidos);
        
        lblJornadaPartido = new JLabel("Jornada:");
        panelAdminPartidos.add(lblJornadaPartido);
        panelAdminPartidos.add(comboJornadasPartidos);
        // --- 2. LISTA DINÁMICA DE PARTIDOS ---
        JScrollPane scrollPartidos = new JScrollPane();
        panelListaPartidos = new JPanel();
        panelListaPartidos.setLayout(new BoxLayout(panelListaPartidos, BoxLayout.Y_AXIS));
        panelListaPartidos.setBackground(new Color(20, 24, 31));
        scrollPartidos.setViewportView(panelListaPartidos);
        panelPartidos.add(scrollPartidos, BorderLayout.CENTER);

        // --- 3. LOGICA DE REFRESCO AUTOMÁTICO ---
        // Esto hace que cuando cambies de temporada, se actualicen los partidos solos
        comboTemporadasPartidos.addActionListener(e -> {
            actualizarComboJornadas();
            actualizarVistaPartidos();
        });
        comboJornadasPartidos.addActionListener(e -> {
            actualizarVistaPartidos();
        });
        // Al añadirlo aquí, aparecerá en el preview
        
        panelClasificacion = new JPanel();
        panelClasificacion.setBackground(new Color(20, 24, 31));
        panelCards.add(panelClasificacion, "clasificacion");

        JLabel lblNewLabel = new JLabel("New label");
        panelClasificacion.add(lblNewLabel);

        // Ejemplo título panelInicio
        JLabel lblInicioTitulo = new JLabel("Bienvenido a la Federación de Balonmano");
        lblInicioTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblInicioTitulo.setForeground(Color.WHITE);
        panelInicio.add(lblInicioTitulo, BorderLayout.NORTH);

        cardLayout.show(panelCards, "inicio"); // mostrar panel inicial
        
        // ✅ AHORA SÍ: Sincronizamos cuando todo ya existe
        sincronizarCombos();
        actualizarVistaEquipos();
        actualizarComboEquipos();
        
        // Seleccionamos la primera temporada por defecto después de sincronizar
        if (comboTemporadasJugadores.getItemCount() > 0) {
            comboTemporadasJugadores.setSelectedIndex(0);
            if (comboEquiposJugadores.getItemCount() > 0) {
                comboEquiposJugadores.setSelectedIndex(0);
                actualizarJugadoresPorTemporada((String) comboEquiposJugadores.getSelectedItem());
            }
        }
        
        actualizarVistaPartidos();
    }
    
    
    
       

       

   
  


    

        @Override
        public void actionPerformed(ActionEvent e) {
            // --- NAVEGACIÓN ---
            if (e.getSource() == btnInicio) cardLayout.show(panelCards, "inicio");
            else if (e.getSource() == btnEquipos) cardLayout.show(panelCards, "equipos");
            else if (e.getSource() == btnJugadores) {
                cardLayout.show(panelCards, "jugadores");
                // ¡Obligatorio refrescar al entrar!
                actualizarJugadoresPorTemporada((String) comboEquiposJugadores.getSelectedItem());
            }
            else if (e.getSource() == btnPartidos) cardLayout.show(panelCards, "partidos");
            else if (e.getSource() == btnClasificacin) cardLayout.show(panelCards, "clasificacion");
            else if (e.getSource() == btnCerrarSesion) System.exit(0);

            // --- GESTIÓN DE FOTOS ---
            else if (e.getSource() == btnVerFoto) {
                if (jugadorSeleccionado == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un jugador primero");
                    return;
                }
                if (jugadorSeleccionado.getFotoURL() != null) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(jugadorSeleccionado.getFotoURL())
                            .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(this, "", jugadorSeleccionado.getNombre(), JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    JOptionPane.showMessageDialog(this, "Este jugador no tiene foto");
                }
            }

            else if (e.getSource() == btnCambiarFoto) {
                if (jugadorSeleccionado == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un jugador");
                    return;
                }
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    jugadorSeleccionado.setFotoURL(chooser.getSelectedFile().getAbsolutePath());
                    actualizarJugadoresPorTemporada((String) comboTemporadasJugadores.getSelectedItem());
                }
            }

            // --- LÓGICA: CAMBIAR EQUIPO ---
            else if (e.getSource() == btnCambiarEquipo) {
                // 1. Verificación inicial
                if (jugadorSeleccionado == null) {
                    JOptionPane.showMessageDialog(this, "Selecciona un jugador de la lista");
                    return;
                }

                // 2. Obtener la temporada actual desde el combo y buscarla en datosFederacion
                String nombreTempActual = (String) comboTemporadasJugadores.getSelectedItem();
                Temporada t = datosFederacion.buscarTemporadaPorNombre(nombreTempActual);

                if (t == null) {
                    JOptionPane.showMessageDialog(this, "Error: No se encontró la temporada seleccionada.");
                    return;
                }

                // 3. Preparar la lista de equipos destinos (quitando la opción "Todos")
                int total = comboEquiposJugadores.getItemCount();
                if (total <= 1) { 
                    JOptionPane.showMessageDialog(this, "No hay equipos disponibles en esta temporada.");
                    return; 
                }
                
                Object[] equiposDisponibles = new Object[total - 1];
                for (int i = 1; i < total; i++) {
                    equiposDisponibles[i - 1] = comboEquiposJugadores.getItemAt(i);
                }

                // 4. Mostrar el diálogo de selección
                Object seleccion = JOptionPane.showInputDialog(this, 
                        "Mover a " + jugadorSeleccionado.getNombre() + " al equipo:", 
                        "Cambiar equipo",
                        JOptionPane.QUESTION_MESSAGE, null, equiposDisponibles, equiposDisponibles[0]);

                if (seleccion != null) {
                    String nuevoNombreEquipo = seleccion.toString();

                    // 5. LÓGICA DE MOVIMIENTO:
                    // Primero lo quitamos de CUALQUIER equipo donde esté en esta temporada
                    for (Equipo eq : t.getEquiposParticipantes()) {
                        eq.getPlantilla().remove(jugadorSeleccionado);
                    }

                    // Ahora lo añadimos al equipo que el usuario eligió
                    for (Equipo eq : t.getEquiposParticipantes()) {
                        if (eq.getNombre().equals(nuevoNombreEquipo)) {
                            eq.ficharJugador(jugadorSeleccionado);
                            break;
                        }
                    }

                    // 6. REFRESCAR LA INTERFAZ
                    // Volvemos a cargar la lista de jugadores para que se vea el cambio
                    actualizarJugadoresPorTemporada(nombreTempActual);
                    
                    JOptionPane.showMessageDialog(this, "Jugador movido correctamente a " + nuevoNombreEquipo);
                }
            }

            // --- LÓGICA: AGREGAR JUGADOR ---
            else if (e.getSource() == btnAgregarJugador) {
                // 1. Obtener el equipo y la temporada seleccionada de los combos
                String equipoSel = (String) comboEquiposJugadores.getSelectedItem();
                String tempNom = (String) comboTemporadasJugadores.getSelectedItem();

                if (equipoSel == null || equipoSel.equals("Todos")) {
                    JOptionPane.showMessageDialog(this, "Selecciona un equipo específico para añadir al jugador");
                    return;
                }

                // 2. Pedir los datos del nuevo jugador
                String nombre = JOptionPane.showInputDialog(this, "Nombre del jugador:");
                if (nombre == null || nombre.trim().isEmpty()) return;
                
                String pos = JOptionPane.showInputDialog(this, "Posición (ej: Portero, Extremo):");
                if (pos == null || pos.trim().isEmpty()) pos = "Sin posición";
                
                String edadS = JOptionPane.showInputDialog(this, "Edad:");
                if (edadS == null || edadS.trim().isEmpty()) return;
                
                try {
                    int edad = Integer.parseInt(edadS);
                    
                    // 3. Crear el objeto Jugador
                    Jugador nuevo = new Jugador(nombre.trim(), pos.trim(), edad, null);
                    
                    // 4. Buscar la Temporada en el corazón de datos (Sincronización total)
                    Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
                    
                    if (t != null) {
                        boolean encontrado = false;
                        // 5. Buscar el equipo dentro de esa temporada y fichar al jugador
                        for (Equipo eq : t.getEquiposParticipantes()) {
                            if (eq.getNombre().equals(equipoSel)) {
                                eq.ficharJugador(nuevo);
                                encontrado = true;
                                break;
                            }
                        }
                        
                        if (encontrado) {
                            // 6. REFRESCAR LA VISTA
                            actualizarJugadoresPorTemporada((String) comboEquiposJugadores.getSelectedItem());
                            JOptionPane.showMessageDialog(this, "Jugador " + nombre + " fichado con éxito en " + equipoSel);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ocurrió un error al agregar el jugador: " + ex.getMessage());
                }
           
            }// --- DENTRO DE actionPerformed ---
            if (e.getSource() == btnNuevaTemp) {
                String nombre = JOptionPane.showInputDialog(this, "Nombre de la Temporada (ej: 2025/26):");
                if (nombre != null && !nombre.trim().isEmpty()) {
                    // 1. Usamos tu gestor (Lógica centralizada)
                    new GestorTemporadas().crearTemporadaFutura(nombre, datosFederacion);
                    
                    // 2. Sincronizamos para que aparezca en TODOS los combos de la app
                    sincronizarCombos();
                    
                    JOptionPane.showMessageDialog(this, "Temporada " + nombre + " creada con éxito.");
                }
            }
            // --- NUEVA JORNADA ---
            else if (e.getSource() == btnNuevaJor) {
                Temporada t = obtenerTemporadaSeleccionada();
                if (t == null) return;

                try {
                    // Usamos tu clase Lógica: GeneradorCalendario
                    // Ella ya sabe si hay 6 equipos y creará Ida y Vuelta
                    GeneradorCalendario.crearCalendario(t);
                    
                    t.setEstado(Temporada.EN_JUEGO);
                    actualizarComboJornadas();
                    actualizarVistaPartidos();
                    
                    JOptionPane.showMessageDialog(this, "Calendario Generado (Reglamento RFBM cumplido)");
                    
                } catch (Exception ex) {
                    // Aquí capturamos el mensaje de "Mínimo 6 equipos" de tu clase
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
                }
            }
            // --- NUEVO PARTIDO (Selector de equipos) ---
            else if (e.getSource() == btnNuevoPart) {
                crearDialogoNuevoPartido();
            }else if (e.getSource() == btnInscribirEquipo) {
                ejecutarInscripcionEquipo();
            }
            
        
        }

    

    
    private JPanel crearTarjetaEquipo(String nombreEquipo) {
        JPanel panelTarjetaEquiposInterior = new JPanel();
        panelTarjetaEquiposInterior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        panelTarjetaEquiposInterior.setBackground(new Color(24, 25, 50));
        panelTarjetaEquiposInterior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelTarjetaEquiposInterior.setLayout(new BorderLayout(10, 10));

        // FOTO DEL EQUIPO
        JLabel lblFotoEquipo = new JLabel();
        lblFotoEquipo.setPreferredSize(new Dimension(80, 80));
        lblFotoEquipo.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoEquipo.setVerticalAlignment(SwingConstants.CENTER);
        lblFotoEquipo.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        lblFotoEquipo.setText("Sin foto"); // Por defecto

        panelTarjetaEquiposInterior.add(lblFotoEquipo, BorderLayout.WEST);

        // Nombre del equipo
        JLabel lblNombre = new JLabel(nombreEquipo);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNombre.setForeground(Color.WHITE);
        panelTarjetaEquiposInterior.add(lblNombre, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotonesEquipo = new JPanel();
        panelBotonesEquipo.setOpaque(false);
        panelBotonesEquipo.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        // Botón Cambiar Escudo
        JButton btnCambiarEscudo = new JButton("Cambiar Escudo");
        btnCambiarEscudo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCambiarEscudo.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int resultado = chooser.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                ImageIcon icon = new ImageIcon(
                        new ImageIcon(chooser.getSelectedFile().getAbsolutePath())
                                .getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)
                );
                lblFotoEquipo.setIcon(icon);
                lblFotoEquipo.setText(""); // quitar "Sin foto"
            }
        });
        panelBotonesEquipo.add(btnCambiarEscudo);

        // Botón Ver Escudo
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
        // 1. Cambiamos el nombre a 'tarjeta' para no confundirlo con el panel global
        JPanel tarjeta = new JPanel(new BorderLayout(10, 10));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        tarjeta.setBackground(new Color(24, 25, 50));
        tarjeta.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // --- FOTO ---
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

        // --- INFO ---
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

        // --- SELECCIÓN DEL JUGADOR (CORREGIDO) ---
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jugadorSeleccionado = jugador;

                // 🟢 panelTarjetasJugadores es el contenedor GLOBAL (el que tiene el scroll)
                // Quitamos el color de selección a todas las tarjetas que hay dentro
                for (Component c : panelTarjetasJugadores.getComponents()) {
                    c.setBackground(new Color(24, 25, 50));
                }

                // 🟢 Marcamos esta tarjeta específica
                tarjeta.setBackground(new Color(60, 60, 120));
            }
        });

        return tarjeta;
    }


    
    
    // =========================================================
    // LÓGICA DE PARTIDOS
    // =========================================================
    
    private void actualizarComboJornadas() {
        String tempNom = (String) comboTemporadasPartidos.getSelectedItem();
        if (tempNom == null) return;

        comboJornadasPartidos.removeAllItems();
        // BUSCAMOS EN DATOS FEDERACIÓN
        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
        if (t != null) {
            for (Jornada j : t.getListaJornadas()) {
                comboJornadasPartidos.addItem(j.getNombre());
            }
        }
    }

    private void actualizarVistaPartidos() {
        // Protección para evitar errores de null
        if (panelListaPartidos == null) return;
        panelListaPartidos.removeAll(); 

        Temporada temp = obtenerTemporadaSeleccionada();
        String jorNom = (String) comboJornadasPartidos.getSelectedItem();

        if (temp != null && jorNom != null) {
            // Buscamos la jornada seleccionada
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
    }

    private JPanel crearTarjetaPartido(Partido p) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(new Color(24, 25, 50));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 1. Nombre de los equipos y marcador si ya terminó
        String textoPartido = p.getEquipoLocal().getNombre() + " vs " + p.getEquipoVisitante().getNombre();
        if (p.isFinalizado()) {
            textoPartido += "  (" + p.getGolesLocal() + " - " + p.getGolesVisitante() + ")";
        }
        
        JLabel lblEquipos = new JLabel(textoPartido);
        lblEquipos.setForeground(Color.WHITE);
        lblEquipos.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // 2. CREAR EL BOTÓN LOCAL (Importante: 'JButton' delante para que sea nuevo)
        JButton btnGoles = new JButton(p.isFinalizado() ? "Editar Resultado" : "Anotar Goles");
        btnGoles.setFocusPainted(false);
        btnGoles.setBackground(p.isFinalizado() ? new Color(70, 70, 70) : new Color(45, 55, 140));
        btnGoles.setForeground(Color.WHITE);

        // 3. ACCIÓN DEL BOTÓN
        btnGoles.addActionListener(e -> {
            // Abrimos tu DialogoResultado (Clase 17)
            DialogoResultado diag = new DialogoResultado(this, p);
            diag.setVisible(true);

            if (diag.isAceptado()) {
                // A) RECALCULAR: Usamos tu lógica profesional
                // Asegúrate de que el método en CalculadoraClasificacion sea static
            	actualizarVistaPartidos(); // Refresca la lista de partidos
      
                // B) REFRESCAR: Volvemos a dibujar los partidos para ver el marcador nuevo
             
                
                // C) CLASIFICACIÓN: Si tienes el método de la tabla, lo llamamos
                if (panelClasificacion.isVisible()) {
                    actualizarTablaClasificacionGrafica(); 
                }
            }
        });

        card.add(lblEquipos, BorderLayout.WEST);
        card.add(btnGoles, BorderLayout.EAST);
        
        return card;
    }
    // =========================================================
    // LÓGICA DE JUGADORES Y EQUIPOS
    // =========================================================
    
    private void actualizarComboEquipos() {
        comboEquiposJugadores.removeAllItems();
        // Importante: Usar el combo de temporada del panel de jugadores
        String nombreTemporada = (String) comboTemporadasJugadores.getSelectedItem(); 
        
        if (nombreTemporada == null) return;

        // Buscamos la temporada en los datos centrales
        Temporada t = datosFederacion.buscarTemporadaPorNombre(nombreTemporada);
        
        if (t != null) {
            comboEquiposJugadores.addItem("Todos");
            for (Equipo e : t.getEquiposParticipantes()) {
                comboEquiposJugadores.addItem(e.getNombre());
            }
        }
    }

    private void actualizarJugadoresPorTemporada(String nombreEquipo) {
        panelTarjetasJugadores.removeAll();
        
        // Obtenemos la temporada seleccionada del combo
        String nombreTemporada = (String) comboTemporadasJugadores.getSelectedItem();
        if (nombreTemporada == null) return;
        
        // BUSCAMOS EN DATOS FEDERACIÓN
        Temporada temporadaSeleccionada = datosFederacion.buscarTemporadaPorNombre(nombreTemporada);
        
        if (temporadaSeleccionada == null) return;

        // Si es "Todos", mostramos todos los jugadores de todos los equipos
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
        Temporada tSel = obtenerTemporadaSeleccionada(); // Este ya usa datosFederacion internamente

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

        // 1. Seleccionar la temporada
        String[] nombresTemps = datosFederacion.getListaTemporadas().stream()
                                .map(Temporada::getNombre)
                                .toArray(String[]::new);

        String tempSeleccionada = (String) JOptionPane.showInputDialog(this, 
                "Selecciona la Temporada:", "Inscripción", 
                JOptionPane.QUESTION_MESSAGE, null, nombresTemps, nombresTemps[0]);

        if (tempSeleccionada == null) return;

        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempSeleccionada);
        if (t == null) return;

        // 2. Recopilar equipos disponibles (que NO estén ya inscritos en esta temporada)
        ArrayList<String> equiposDisponibles = new ArrayList<>();
        
        for (Temporada temp : datosFederacion.getListaTemporadas()) {
            for (Equipo eq : temp.getEquiposParticipantes()) {
                String nombreEquipo = eq.getNombre();
                
                // Solo agregar si no está duplicado en la lista Y no está ya inscrito en la temporada actual
                if (!equiposDisponibles.contains(nombreEquipo)) {
                    // Verificar que NO esté ya inscrito en la temporada seleccionada
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

        // 3. Convertir a array para el selector
        String[] nombresEquipos = equiposDisponibles.toArray(new String[0]);

        // 4. Mostrar selector de equipos
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
            // Buscar el equipo original para copiar su estructura
            Equipo equipoOriginal = null;
            for (Temporada temp : datosFederacion.getListaTemporadas()) {
                equipoOriginal = temp.buscarEquipoPorNombre(equipoSeleccionado);
                if (equipoOriginal != null) break;
            }

            if (equipoOriginal != null) {
                // Crear una nueva instancia del equipo (sin jugadores, solo el nombre)
                Equipo nuevoEquipo = new Equipo(equipoOriginal.getNombre());
                t.inscribirEquipo(nuevoEquipo);
                
                actualizarVistaEquipos();
                sincronizarCombos();
                actualizarComboJornadas();
                
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

        // EL CORAZÓN DE LA APP:
        return datosFederacion.buscarTemporadaPorNombre(nombreSeleccionado);
    }
    private void actualizarTablaClasificacionGrafica() {
        Temporada temp = obtenerTemporadaSeleccionada();
        if (temp == null || modeloTabla == null) return;

        modeloTabla.setRowCount(0);

        // ✅ CAMBIO AQUÍ: Usamos 'calcular' en lugar de 'obtenerRanking'
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
        // 1. Limpiamos el panel de tarjetas para no duplicar dibujos
        panelTarjetasEquipo.removeAll();
        
        // 2. Obtenemos la temporada que el usuario tiene seleccionada en el combo de arriba
        String tempNombre = (String) comboTemporadas.getSelectedItem();
        if (tempNombre == null) return;

        // 3. Buscamos esa temporada en nuestro almacén central de datos
        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNombre);
        
        if (t != null) {
            // 4. Por cada equipo inscrito en esa temporada, creamos su tarjeta visual
            for (Equipo eq : t.getEquiposParticipantes()) {
                panelTarjetasEquipo.add(crearTarjetaEquipo(eq.getNombre()));
            }
        }
        
        // 5. Refrescamos la interfaz para que Swing pinte los cambios
        panelTarjetasEquipo.revalidate();
        panelTarjetasEquipo.repaint();
    }
    private void sincronizarCombos() {
        // 1. Guardamos qué tenían seleccionado para no molestar al usuario
        Object tempSelEquipos = comboTemporadas.getSelectedItem();
        Object tempSelJugadores = comboTemporadasJugadores.getSelectedItem();
        Object tempSelPartidos = comboTemporadasPartidos.getSelectedItem();

        // 2. Limpiamos y recargamos TODOS los combos de temporadas
        JComboBox[] combosTemp = {comboTemporadas, comboTemporadasJugadores, comboTemporadasPartidos};
        for (JComboBox c : combosTemp) {
            if (c == null) continue;
            c.removeAllItems();
            for (Temporada t : datosFederacion.getListaTemporadas()) {
                c.addItem(t.getNombre());
            }
        }

        // 3. Restauramos la selección si todavía existe
        comboTemporadas.setSelectedItem(tempSelEquipos);
        comboTemporadasJugadores.setSelectedItem(tempSelJugadores);
        comboTemporadasPartidos.setSelectedItem(tempSelPartidos);

        // 4. Actualizamos también los combos de EQUIPOS (el de jugadores y partidos)
        actualizarComboEquipos(); // Este es el que ya tenías
        // actualizarComboEquiposPartidos(); (Si tienes uno en partidos)
    }
    // Ejemplo de lógica en tu ventana al cambiar de temporada
    private void actualizarEstadoInterfaz() {
        String tempNom = (String) comboTemporadas.getSelectedItem();
        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);

        if (t != null) {
            if (t.getEstado().equals(Temporada.TERMINADA)) {
                btnNuevaJor.setEnabled(false); // No se pueden crear jornadas en el pasado
                lblEstado.setText("Estado: Temporada Finalizada");
                lblEstado.setForeground(Color.RED);
            } else {
                btnNuevaJor.setEnabled(true);
                lblEstado.setText("Estado: En Competición");
                lblEstado.setForeground(Color.GREEN);
            }
        }
    }
    // Esto asegura que al abrir la app ya hay datos coherentes
    // REEMPLAZA LAS ÚLTIMAS LÍNEAS DEL ARCHIVO (después de prepararDatosFederacion())

    private void prepararDatosFederacion() {
        // 1. Temporada Pasada
        Temporada tPasada = new Temporada("Temporada 2024/25", Temporada.TERMINADA);
        // (Opcional: añadir equipos y resultados aquí para que no esté vacía)
        datosFederacion.getListaTemporadas().add(tPasada);

        // 2. Temporada Actual
        Temporada tActual = new Temporada("Temporada 2025/26", Temporada.EN_JUEGO);
        datosFederacion.getListaTemporadas().add(tActual);
        
        // 3. Inscribir los 6 equipos obligatorios en la actual
        String[] nombres = {"Barcelona", "Granada", "Sevilla", "Zaragoza", "Valencia", "Athletic"};
        for(String n : nombres) {
            tActual.inscribirEquipo(new Equipo(n));
        }
    }
}
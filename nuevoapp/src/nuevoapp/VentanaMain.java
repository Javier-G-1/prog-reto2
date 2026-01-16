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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import gestion.Temporada;

import logica.ExportadorXMLLiga;

/**
 * Ventana principal de la aplicación de gestión de la Federación de Balonmano.
 * <p>
 * Esta clase representa la interfaz gráfica principal del sistema, permitiendo
 * la gestión completa de temporadas, equipos, jugadores, partidos y clasificaciones.
 * Implementa un sistema de navegación por pestañas (CardLayout) y proporciona
 * diferentes funcionalidades según el rol del usuario.
 * </p>
 * <p>
 * La ventana incluye:
 * <ul>
 *   <li>Panel de menú lateral con navegación</li>
 *   <li>Gestión de equipos por temporada</li>
 *   <li>Administración de jugadores y traspasos</li>
 *   <li>Control de partidos y jornadas</li>
 *   <li>Visualización de clasificaciones</li>
 *   <li>Sistema de usuarios con control de permisos</li>
 *   <li>Exportación de datos a XML</li>
 * </ul>
 * </p>
 * 
 * @see DatosFederacion
 * @see Temporada
 * @see Equipo
 * @see Jugador
 * @see Partido
 */
public class VentanaMain extends JFrame implements ActionListener, WindowListener {
    private static final long serialVersionUID = 1L;

    //ATRIBUTOS DE DATOS
    
    /** Panel que muestra la clasificación de equipos */
    private PanelClasificacion panelClasificacionObjeto;
    
    /** Objeto central que contiene todos los datos de la federación */
    private DatosFederacion datosFederacion;
    
    /** Rol del usuario actual en el sistema */
    private Rol rolUsuario;
    
    /** Jugador seleccionado actualmente en la interfaz */
    private Jugador jugadorSeleccionado;

    //COMPONENTES DE INTERFAZ
    
    /** Panel contenedor principal de la ventana */
    private JPanel contentPane;
    

    
    /** Panel de administración de partidos (sección de controles) */
    private JPanel panelAdminPartidos_1;
    
    /** Panel del menú lateral izquierdo */
    private JPanel panelMenu;
    
    /** Panel principal que contiene las diferentes vistas (CardLayout) */
    private JPanel panelCards;
    
    /** Gestor de diseño para cambiar entre vistas */
    private CardLayout cardLayout;

    //ETIQUETAS
    
    /** Etiqueta del título principal "FEDERACIÓN" */
    private JLabel lblTitulo;
    
    /** Etiqueta del subtítulo "BALONMANO" */
    private JLabel lblSubtitulo;
    
    /** Etiqueta "Bienvenid@" en el menú */
    private JLabel lblBienvenido;
    
    /** Etiqueta que muestra el nombre del usuario actual */
    private JLabel lblUsuario;
    
    /** Etiqueta "Temporada:" en la vista de equipos */
    private JLabel lblTemp;
    
    /** Etiqueta que muestra el estado de la temporada actual */
    private JLabel lblEstado;
    
    /** Etiqueta "Temporada:" en la vista de jugadores */
    private JLabel lblTemporadaJugador;
    
    /** Etiqueta "Equipo:" en la vista de jugadores */
    private JLabel lblEquipoJugadores;
    
    /** Etiqueta "Temporada:" en la vista de partidos */
    private JLabel lblTemporadaPartido;
    
    /** Etiqueta "Jornada:" en la vista de partidos */
    private JLabel lblJornadaPartido;

    //BOTONES DE NAVEGACIÓN
    
    /** Botón para ir a la vista de inicio */
 
    
    /** Botón para ir a la vista de equipos */
    private JButton btnEquipos;
    
    /** Botón para ir a la vista de jugadores */
    private JButton btnJugadores;
    
    /** Botón para ir a la vista de partidos */
    private JButton btnPartidos;
    
    /** Botón para cerrar sesión y volver al login */
    private JButton btnCerrarSesion;

    // ==================== BOTONES DE EQUIPOS ====================
    
    /** Botón para agregar un nuevo equipo a la temporada */
    private JButton btnAgregarEquipo;
    
    /** Botón para inscribir un equipo existente en una temporada */
    private JButton btnInscribirEquipo;

    
    // ==================== BOTONES DE JUGADORES ====================
    
    /** Botón para ver la foto del jugador seleccionado */
    private JButton btnVerFoto;
    
    /** Botón para cambiar la foto del jugador seleccionado */
    private JButton btnCambiarFoto;
    
    /** Botón para cambiar el jugador de equipo (traspaso) */
    private JButton btnCambiarEquipo;
    
    /** Botón para agregar un nuevo jugador al equipo */
    private JButton btnAgregarJugador;
    
    /** Botón para editar los datos de un jugador existente */
    private JButton btnEditarJugador;

    // ==================== BOTONES DE PARTIDOS ====================
    
    /** Botón para crear una nueva jornada y generar calendario */
    private JButton btnNuevaJor;
    
    /** Botón para crear un nuevo partido manualmente */
    private JButton btnNuevoPart;
    
    /** Botón para crear una nueva temporada */
    private JButton btnNuevaTemp_1;
    
    /** Botón para finalizar la temporada actual */
    private JButton btnFinalizarTemporada;
    
    /** Botón de función especial (testing/debug) */
    private JButton btnTxema;

    //BOTONES DE SISTEMA
    
    /** Botón para acceder a la gestión de usuarios */
    private JButton btnGestionUsuario;
    
    /** Botón para exportar datos a XML */
    private JButton btnExportar;
    
    

    //COMBOBOX
    
    /** ComboBox para seleccionar temporada en la vista de equipos */
    private JComboBox<String> comboTemporadas;
    
    /** ComboBox para seleccionar temporada en la vista de jugadores */
    private JComboBox<String> comboTemporadasJugadores;
    
    /** ComboBox para seleccionar equipo en la vista de jugadores */
    private JComboBox<String> comboEquiposJugadores;
    
    /** ComboBox para seleccionar temporada en la vista de partidos */
    private JComboBox<String> comboTemporadasPartidos;
    
    /** ComboBox para seleccionar jornada en la vista de partidos */
    private JComboBox<String> comboJornadasPartidos;
    


    //PANELES DE VISTAS
    

    
    /** Panel de la vista de equipos */
    private JPanel panelEquipos;
    
    /** Panel de la vista de jugadores */
    private JPanel panelJugadores;
    
    /** Panel de la vista de partidos */
    private JPanel panelPartidos;
    
    private PanelGestionTemporadas panelGestionTemporadas;
    
    /** Panel superior de controles en la vista de equipos */
    private JPanel panelSuperior;
    
    /** Panel que contiene las tarjetas de equipos */
    private JPanel panelTarjetasEquipo;
    
    /** Panel que contiene las tarjetas de jugadores */
    private JPanel panelTarjetasJugadores;
    
    /** Panel que contiene la lista de partidos */
    private JPanel panelListaPartidos;
    
    /** Panel de gestión de usuarios del sistema */
    private PanelGestionUsuarios panelUsuarios;

    //SCROLLPANES
    
    /** ScrollPane para la lista de equipos */
    private JScrollPane scrollEquipos;
    
    /** ScrollPane para la lista de jugadores */
    private JScrollPane scrollJugadores;

    // OTROS COMPONENTES 
    
    /** Modelo de datos para tablas (si se utiliza) */
    private DefaultTableModel modeloTabla;
    
    /** Timer para autoguardado periódico de datos */
    private javax.swing.Timer autoSaveTimer;
    
    

    // ==================== ESPACIADORES ====================
    
    /** Espaciador vertical entre componentes del menú */
    private Component verticalStrut;
    
    /** Espaciador vertical entre componentes del menú */
    private Component verticalStrut_1;
    
    /** Espaciador vertical entre componentes del menú */
    private Component verticalStrut_2;
    
    /** Espaciador vertical entre componentes del menú */
    private Component verticalStrut_3;
    
    /** Espaciador vertical entre componentes del menú */
    private Component verticalStrut_4;
    
    /** Espaciador vertical entre componentes del menú */
    private Component verticalStrut_5;
    
    /** Espaciador vertical entre componentes del menú */
    private Component verticalStrut_6;
    
    /** Espaciador vertical entre componentes del menú */
    private Component verticalStrut_7;

	private JButton btnTemporadas;

	private Component verticalStrut_8;

   
    // MÉTODOS PRINCIPALES

    /**
     * Método principal que inicia la aplicación.
     * Crea y muestra la ventana de login en el Event Dispatch Thread.
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
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
        
    /**
     * Constructor de la ventana principal.
     * Inicializa todos los componentes de la interfaz, configura los listeners,
     * prepara el escenario inicial de datos y establece el sistema de autoguardado.
     * 
     * @param datosFederacion objeto central con todos los datos del sistema
     * @throws NullPointerException si datosFederacion es null
     */
    public VentanaMain(DatosFederacion datosFederacion) {
        this.datosFederacion = datosFederacion;
        limpiarTemporadasDuplicadas();
        
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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
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
        
                btnPartidos = new JButton("Partidos");
                btnPartidos.setBorder(null);
                btnPartidos.setBackground(new Color(45, 55, 140));
                btnPartidos.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnPartidos.setForeground(Color.WHITE);
                btnPartidos.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnPartidos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                btnPartidos.addActionListener(this);
                panelBotones.add(btnPartidos);
        
        verticalStrut_3 = Box.createVerticalStrut(20);
        panelBotones.add(verticalStrut_3);
        panelBotones.add(btnEquipos);
        btnTemporadas = new JButton("Temporadas");
        btnTemporadas.setBorder(null);
        btnTemporadas.setBackground(TemaColores.BOTON_PRIMARIO);
        btnTemporadas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTemporadas.setForeground(TemaColores.TEXTO_PRIMARIO);
        btnTemporadas.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTemporadas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnTemporadas.addActionListener(this);

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
        
        verticalStrut_4 = Box.createVerticalStrut(20);
        panelBotones.add(verticalStrut_4);
        
        verticalStrut_8 = Box.createVerticalStrut(20);
        panelBotones.add(verticalStrut_8);
        panelBotones.add(btnTemporadas);

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
        
        
        btnExportar = new JButton("Exportar");
        btnExportar.addActionListener(e -> {
            // Obtener todas las temporadas disponibles
            java.util.List<Temporada> temporadas = datosFederacion.getListaTemporadas();
            
            if (temporadas.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No hay temporadas disponibles para exportar", 
                    "Sin datos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Crear opciones dinámicas
            java.util.List<String> opcionesLista = new java.util.ArrayList<>();
            
            // Añadir opción para la temporada actual visible
            Temporada temporadaActual = obtenerTemporadaActualVisible();
            if (temporadaActual != null) {
                opcionesLista.add("Temporada actual (" + temporadaActual.getNombre() + ")");
            }
            
            // Añadir opción para seleccionar una temporada específica
            opcionesLista.add("Seleccionar temporada específica...");
            
            // Añadir opción para exportar todas
            opcionesLista.add("Exportar todas las temporadas");
            
            String[] opciones = opcionesLista.toArray(new String[0]);
            
            int eleccion = JOptionPane.showOptionDialog(
                this,
                "¿Qué deseas exportar a ligaBalonmano.xml?",
                "Exportar datos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            if (eleccion == -1) return; // Usuario canceló

            GestorLog.info("Iniciando exportación de datos");
            
            // ⭐ USAR EL NUEVO EXPORTADOR
            ExportadorXMLLiga exportador = new ExportadorXMLLiga(datosFederacion);

            if (eleccion == 0 && temporadaActual != null) {
                // Exportar temporada actual visible
                exportador.exportarTemporada(temporadaActual);
                
            } else if (eleccion == (temporadaActual != null ? 1 : 0)) {
                // Seleccionar temporada específica
                Temporada temporadaSeleccionada = mostrarDialogoSeleccionTemporada();
                
                if (temporadaSeleccionada != null) {
                    exportador.exportarTemporada(temporadaSeleccionada);
                }
                
            } else {
                // Exportar todas las temporadas
                int temporadasExportadas = 0;
                int temporadasFallidas = 0;
                
                for (Temporada temp : temporadas) {
                    boolean exito = exportador.exportarTemporada(temp);
                    if (exito) {
                        temporadasExportadas++;
                    } else {
                        temporadasFallidas++;
                    }
                }
                
                String mensaje = "✅ Exportación masiva completada\n\n" +
                                "Temporadas exportadas: " + temporadasExportadas + "\n" +
                                "Fallidas: " + temporadasFallidas + "\n" +
                                "Archivo: ligaBalonmano.xml";
                
                JOptionPane.showMessageDialog(this, 
                    mensaje,
                    "Exportación completada",
                    JOptionPane.INFORMATION_MESSAGE);
                
                GestorLog.exito("Exportación masiva: " + temporadasExportadas + " exitosas, " + 
                               temporadasFallidas + " fallidas");
            }
        });
        
        
        
        btnExportar.setMaximumSize(new Dimension(2147483647, 40));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFont(new Font("Segoe UI Black", Font.ITALIC, 14));
        btnExportar.setBorder(null);
        btnExportar.setBackground(Color.GRAY);
        btnExportar.setAlignmentX(0.5f);
        panelBotones.add(btnExportar);
 
        verticalStrut_7 = Box.createVerticalStrut(20);
        panelBotones.add(verticalStrut_7);
        
        btnGestionUsuario = new JButton("Gestión de Usuarios");
        btnGestionUsuario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelCards, "usuarios");
                GestorLog.info("Navegación: Gestión de Usuarios");
                
                // Refrescar la vista
                panelUsuarios.cargarUsuarios();
            }
        });  
       
        btnGestionUsuario.setMaximumSize(new Dimension(2147483647, 40));
        btnGestionUsuario.setForeground(Color.WHITE);
        btnGestionUsuario.setFont(new Font("Segoe UI Black", Font.ITALIC, 14));
        btnGestionUsuario.setBorder(null);
        btnGestionUsuario.setBackground(new Color(128, 128, 128));
        btnGestionUsuario.setAlignmentX(0.5f);
        panelBotones.add(btnGestionUsuario);
        
        verticalStrut_6 = Box.createVerticalStrut(20);
        panelBotones.add(verticalStrut_6);
        panelBotones.add(btnCerrarSesion);

        // ===== PANEL CENTRAL (CARDS) =====
        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);
        contentPane.add(panelCards, BorderLayout.CENTER);

       

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

        comboTemporadas = new JComboBox<>();
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
        lblTemporadaJugador.setForeground(new Color(0, 128, 192));
        panelSuperiorJugadores.add(lblTemporadaJugador);
        comboTemporadasJugadores = new JComboBox<>();
        panelSuperiorJugadores.add(comboTemporadasJugadores);
        
        lblEquipoJugadores = new JLabel("Equipo:");
        lblEquipoJugadores.setForeground(new Color(0, 128, 192));
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
     // ✅ CORRECTO
        btnEditarJugador = new JButton("Editar jugador");
        btnEditarJugador.addActionListener(this);
        panelBotonesJugadores.add(btnEditarJugador);
    
        panelJugadores.add(panelBotonesJugadores, BorderLayout.SOUTH);


        comboTemporadas.addActionListener(e -> {
            actualizarVistaEquipos();
            actualizarIndicadorEstadoTemporada(); 
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
        panelPartidos.setLayout(new BorderLayout(10, 10));
        panelPartidos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCards.add(panelPartidos, "partidos");

        // ===== PANEL SUPERIOR (Controles) =====
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
        lblTemporadaPartido.setForeground(new Color(0, 128, 192));
        panelAdminPartidos_1.add(lblTemporadaPartido);
        panelAdminPartidos_1.add(comboTemporadasPartidos);

        lblJornadaPartido = new JLabel("Jornada:");
        lblJornadaPartido.setForeground(new Color(0, 128, 192));
        panelAdminPartidos_1.add(lblJornadaPartido);
        panelAdminPartidos_1.add(comboJornadasPartidos);
        panelAdminPartidos_1.revalidate();
        panelAdminPartidos_1.repaint();
        
        comboTemporadasPartidos.addActionListener(e -> {
            actualizarComboJornadas();
            actualizarVistaPartidos();
            actualizarIndicadorEstadoPartidos();
            sincronizarCombos();
            
            // ⭐ ACTUALIZAR CLASIFICACIÓN INMEDIATAMENTE
            String tempNom = (String) comboTemporadasPartidos.getSelectedItem();
            if (tempNom != null && panelClasificacionObjeto != null) {
                Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNom);
                if (t != null) {
                    System.out.println("🔄 Cambiando clasificación a: " + tempNom);
                    panelClasificacionObjeto.actualizarClasificacion(t);
                }
            }
        });

        comboJornadasPartidos.addActionListener(e -> {
            actualizarVistaPartidos();
            actualizarIndicadorEstadoPartidos();
        });
        
        
        
        panelGestionTemporadas = new PanelGestionTemporadas(datosFederacion, this);
        panelCards.add(panelGestionTemporadas, "temporadas");


        
 
     // ===== PANEL CENTRAL (Jornadas + Clasificación) =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(500); // Altura fija para partidos
        splitPane.setResizeWeight(0.5); // 50% para cada panel
   
        splitPane.setBackground(new Color(20, 24, 31));
        splitPane.setBorder(null);
        panelPartidos.add(splitPane, BorderLayout.CENTER);

        // ===== PANEL IZQUIERDO: LISTA DE PARTIDOS =====
        JPanel panelPartidosContenedor = new JPanel(new BorderLayout(5, 5));
        panelPartidosContenedor.setBackground(new Color(20, 24, 31));

        JLabel lblTituloPartidos = new JLabel("📋 PARTIDOS DE LA JORNADA");
        lblTituloPartidos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloPartidos.setForeground(Color.WHITE);
        lblTituloPartidos.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        lblTituloPartidos.setOpaque(true);
        lblTituloPartidos.setBackground(new Color(30, 34, 45));
        panelPartidosContenedor.add(lblTituloPartidos, BorderLayout.NORTH);
        
        
     // Panel de leyenda (LOCAL vs VISITANTE)
        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelLeyenda.setBackground(new Color(30, 34, 45));
        panelLeyenda.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel lblLeyendaLocal = new JLabel("🏠 LOCAL");
        lblLeyendaLocal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLeyendaLocal.setForeground(new Color(100, 181, 246)); // Azul claro
        panelLeyenda.add(lblLeyendaLocal);

        JLabel lblSeparador = new JLabel("vs");
        lblSeparador.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSeparador.setForeground(Color.WHITE);
        panelLeyenda.add(lblSeparador);

        JLabel lblLeyendaVisitante = new JLabel("✈️ VISITANTE");
        lblLeyendaVisitante.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLeyendaVisitante.setForeground(new Color(255, 183, 77)); // Naranja
        panelLeyenda.add(lblLeyendaVisitante);

        panelPartidosContenedor.add(panelLeyenda, BorderLayout.SOUTH);

        panelListaPartidos = new JPanel();
        panelListaPartidos.setLayout(new BoxLayout(panelListaPartidos, BoxLayout.Y_AXIS));
        panelListaPartidos.setBackground(new Color(20, 24, 31));

        JScrollPane scrollPartidos = new JScrollPane(panelListaPartidos);
        scrollPartidos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPartidos.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPartidos.getVerticalScrollBar().setUnitIncrement(16);
        scrollPartidos.setBorder(null);
        panelPartidosContenedor.add(scrollPartidos, BorderLayout.CENTER);

        splitPane.setLeftComponent(panelPartidosContenedor);
        
        
        
        

    
        
        comboJornadasPartidos.addActionListener(e -> {
            actualizarVistaPartidos();
            
        });///////////////////////////////////////////////////////////////////////////
        
     // ===== PANEL DERECHO: CLASIFICACIÓN =====
        JPanel panelClasificacionContenedor = new JPanel(new BorderLayout());
        panelClasificacionContenedor.setBackground(new Color(20, 24, 31));

        JLabel lblTituloClasificacion = new JLabel("🏆 CLASIFICACIÓN");
        lblTituloClasificacion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloClasificacion.setForeground(Color.WHITE);
        lblTituloClasificacion.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        lblTituloClasificacion.setOpaque(true);
        lblTituloClasificacion.setBackground(new Color(30, 34, 45));
        panelClasificacionContenedor.add(lblTituloClasificacion, BorderLayout.NORTH);

        // Panel de clasificación con scroll
        panelClasificacionObjeto = new PanelClasificacion(datosFederacion);
        JScrollPane scrollClasificacion = new JScrollPane(panelClasificacionObjeto);
        scrollClasificacion.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollClasificacion.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollClasificacion.getVerticalScrollBar().setUnitIncrement(16);
        scrollClasificacion.setBorder(null);
        panelClasificacionContenedor.add(scrollClasificacion, BorderLayout.CENTER);

        splitPane.setRightComponent(panelClasificacionContenedor);
        if (datosFederacion.getListaTemporadas() != null && 
        	    !datosFederacion.getListaTemporadas().isEmpty()) {
        	    Temporada primeraTemp = datosFederacion.getListaTemporadas().get(0);
        	    panelClasificacionObjeto.actualizarClasificacion(primeraTemp);
        	}
        
        panelUsuarios = new PanelGestionUsuarios(datosFederacion);
        panelCards.add(panelUsuarios, "usuarios");



      
       

        cardLayout.show(panelCards, "partidos");
        
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
        iniciarAutoGuardado();
    }
    
    /**
     * Elimina temporadas duplicadas del sistema.
     * Recorre todas las temporadas y mantiene solo una instancia de cada nombre único.
     * Si se encuentran duplicados, se eliminan y se guardan los cambios.
     * <p>
     * Este método se ejecuta al iniciar la aplicación para garantizar la integridad
     * de los datos.
     * </p>
     */
    private void limpiarTemporadasDuplicadas() {
        java.util.List<Temporada> temporadas = datosFederacion.getListaTemporadas();
        java.util.Set<String> nombresVistos = new java.util.HashSet<>();
        java.util.List<Temporada> temporadasUnicas = new java.util.ArrayList<>();
        
        for (Temporada t : temporadas) {
            if (!nombresVistos.contains(t.getNombre())) {
                nombresVistos.add(t.getNombre());
                temporadasUnicas.add(t);
            } else {
                GestorLog.advertencia("Temporada duplicada eliminada: " + t.getNombre());
            }
        }
        
        if (temporadasUnicas.size() < temporadas.size()) {
            temporadas.clear();
            temporadas.addAll(temporadasUnicas);
            GestorLog.exito("Limpieza: " + temporadasUnicas.size() + " temporadas únicas");
            GestorArchivos.guardarTodo(datosFederacion);
        }
    }


    /**
     * Actualiza el estado de los botones de jugadores según el estado de la temporada.
     * <p>
     * Habilita/deshabilita los botones de modificación según si la temporada es FUTURA:
     * <ul>
     *   <li>Agregar jugador: solo en FUTURA</li>
     *   <li>Cambiar equipo: solo en FUTURA</li>
     *   <li>Cambiar foto: solo en FUTURA</li>
     *   <li>Ver foto: siempre habilitado</li>
     * </ul>
     * También actualiza los tooltips informativos de cada botón.
     * </p>
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

    /**
     * Maneja todos los eventos de acción de los componentes de la interfaz.
     * <p>
     * Este método procesa:
     * <ul>
     *   <li>Navegación entre vistas (inicio, equipos, jugadores, partidos, clasificación)</li>
     *   <li>Gestión de jugadores (agregar, editar, cambiar foto, cambiar equipo)</li>
     *   <li>Gestión de temporadas (crear, finalizar)</li>
     *   <li>Gestión de partidos (crear jornadas, crear partidos)</li>
     *   <li>Inscripción de equipos</li>
     *   <li>Cierre de sesión</li>
     *   <li>Exportación de datos</li>
     * </ul>
     * </p>
     * 
     * @param e evento de acción generado por el componente
     * @see ActionListener#actionPerformed(ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == btnEquipos) {
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
        else if (e.getSource() == btnTemporadas) {
            cardLayout.show(panelCards, "temporadas");
            GestorLog.info("Navegación: Temporadas");
            panelGestionTemporadas.cargarTemporadas();
        
        }
       
        else if (e.getSource() == btnPartidos) {
            cardLayout.show(panelCards, "partidos");
            GestorLog.info("Navegación: Partidos");
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
        	 if (rolUsuario != Rol.ADMINISTRADOR && rolUsuario != Rol.MANAGER) {
				 JOptionPane.showMessageDialog(this,
					 "No tienes permisos para cambiar fotos de jugadores.",
					 "Acceso denegado",
					 JOptionPane.ERROR_MESSAGE);
				 GestorLog.advertencia("Usuario sin permisos intentó cambiar foto: " + rolUsuario);
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
            
            // ⭐ CONFIGURAR FILECHOOSER CON FILTRO DE IMÁGENES
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Seleccionar foto del jugador");
            chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(java.io.File f) {
                    if (f.isDirectory()) return true;
                    String nombre = f.getName().toLowerCase();
                    return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || 
                           nombre.endsWith(".png") || nombre.endsWith(".gif");
                }
                
                @Override
                public String getDescription() {
                    return "Imágenes (*.jpg, *.png, *.gif)";
                }
            });
            
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String rutaOriginal = chooser.getSelectedFile().getAbsolutePath();
                
                // ⭐ COPIAR FOTO A LA CARPETA DE LA APP
                String equipoActual = obtenerEquipoDeJugador(jugadorSeleccionado);
                String rutaRelativa = GestorArchivos.copiarFotoJugador(
                    rutaOriginal, 
                    jugadorSeleccionado.getNombre(),
                    equipoActual
                );
                
                if (rutaRelativa != null) {
                    jugadorSeleccionado.setFotoURL(rutaRelativa);
                    
                    // Refrescar vista
                    actualizarJugadoresPorTemporada(
                        (String) comboTemporadasJugadores.getSelectedItem(), 
                        (String) comboEquiposJugadores.getSelectedItem()
                    );
                    
                    // ⭐ GUARDAR INMEDIATAMENTE
                    GestorArchivos.guardarTodo(datosFederacion);
                    
                    GestorLog.exito("Foto actualizada para: " + jugadorSeleccionado.getNombre());
                    
                    JOptionPane.showMessageDialog(this,
                        "Foto actualizada correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error al copiar la foto. Revisa los logs.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
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

            // ⭐ Buscar el equipo destino
            Equipo equipoDestino = t != null ? t.buscarEquipoPorNombre(equipoSel) : null;
            
            if (equipoDestino == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el equipo seleccionado");
                return;
            }
            
            // ⭐ Crear diálogo y pasar el equipo para validar dorsales
            DialogoAgregarJugador dialogo = new DialogoAgregarJugador((Frame) this);
            dialogo.setEquipoDestino(equipoDestino);
            dialogo.setVisible(true);          
            if (dialogo.isAceptado()) {
                Jugador nuevo = dialogo.getJugadorCreado();
                
                if (nuevo != null) {
                    try {
                        // ⭐ La validación de dorsal único se hace automáticamente en ficharJugador
                        equipoDestino.ficharJugador(nuevo);
                        
                        actualizarJugadoresPorTemporada(tempNom, equipoSel);
                        
                        GestorLog.exito("Nuevo fichaje: " + nuevo.getNombre() + " | Equipo: " + equipoSel + 
                                      " | Posición: " + nuevo.getPosicion() + " | Edad: " + nuevo.getEdad() + 
                                      " | Dorsal: " + nuevo.getDorsal() + 
                                      " | Nacionalidad: " + nuevo.getNacionalidad() + 
                                      " | Altura: " + nuevo.getAltura() + 
                                      " | Peso: " + nuevo.getPeso() + 
                                      " | Temporada: " + tempNom);
                        
                        JOptionPane.showMessageDialog(this, 
                            "Jugador " + nuevo.getNombre() + " fichado con éxito en " + equipoSel,
                            "Fichaje exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                            
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Error al fichar jugador",
                            JOptionPane.ERROR_MESSAGE);
                        GestorLog.advertencia("Error al fichar: " + ex.getMessage());
                    }
                }
            }
        }

        if (e.getSource() == btnNuevaTemp_1) {
        	String nombre = JOptionPane.showInputDialog(this, "Nombre de la Temporada (ej: 2026/27):");
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                // ⭐ VALIDACIÓN: Verificar que no exista ya una temporada con ese nombre
                if (datosFederacion.buscarTemporadaPorNombre(nombre) != null) {
                    JOptionPane.showMessageDialog(this,
                        "Ya existe una temporada con el nombre: " + nombre,
                        "Temporada duplicada",
                        JOptionPane.WARNING_MESSAGE);
                    GestorLog.advertencia("Intento de crear temporada duplicada: " + nombre);
                    return;
                }
                
                GestorTemporadas gestor = new GestorTemporadas();}
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
            }

            GestorTemporadas gestor = new GestorTemporadas();
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

            // Corregido: La validación debe ir ANTES del proceso, no después de un return
            if (!validarJugadoresMinimosPorTemporada(t)) {
                return;
            }

            try {
                int equiposInscritos = t.getEquiposParticipantes().size();
                String estadoAnterior = t.getEstado();
                
                // Generar calendario
                GeneradorCalendario.crearCalendario(t);
                
                t.setEstado(Temporada.EN_JUEGO);
                
                // Actualización de la interfaz
                sincronizarCombos(); 
                actualizarComboJornadas();
                actualizarVistaPartidos(); 
                actualizarIndicadorEstadoTemporada();
                actualizarIndicadorEstadoPartidos(); 
                actualizarVistaEquipos();
                
                GestorLog.exito("Temporada activada: " + t.getNombre() + 
                              " | Equipos: " + equiposInscritos + 
                              " | Jornadas creadas: " + t.getListaJornadas().size() +
                              " | Estado: " + estadoAnterior + " → " + Temporada.EN_JUEGO);
                
                JOptionPane.showMessageDialog(this, "Calendario Generado (Reglamento RFBM cumplido)");
                
            } catch (Exception ex) {
                GestorLog.error("Error al generar calendario para " + t.getNombre() + ": " + ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }
        } // <--- Llave de cierre que faltaba

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
                actualizarJugadoresPorTemporada(
                    (String) comboTemporadasJugadores.getSelectedItem(),
                    (String) comboEquiposJugadores.getSelectedItem()
                );
                GestorLog.exito("Jugador editado: " + jugadorSeleccionado.getNombre());
            }
        }

        else if (e.getSource() == btnFinalizarTemporada) {
            finalizarTemporada();
        }

        else if (e.getSource() == btnTxema) {
            funcionTxema();
        }}



    /**
     * Crea una tarjeta mejorada de jugador con todos los datos del XML
     */
    /**
     * Crea una tarjeta visual para mostrar la información completa de un jugador.
     * 
     * <p>La tarjeta incluye:</p>
     * <ul>
     *   <li>Foto del jugador (100x100 px)</li>
     *   <li>Nombre y número de dorsal</li>
     *   <li>Posición en el campo</li>
     *   <li>Nacionalidad y edad</li>
     *   <li>Altura y peso</li>
     *   <li>Botón de eliminación (solo para ADMINISTRADOR y MANAGER)</li>
     * </ul>
     * 
     * <p><b>Interactividad:</b></p>
     * <ul>
     *   <li>Click en la tarjeta para seleccionar el jugador</li>
     *   <li>Cambio de color al seleccionar (azul destacado)</li>
     *   <li>Botón eliminar con confirmación</li>
     * </ul>
     * 
     * @param jugador El jugador cuyos datos se mostrarán en la tarjeta
     * @return JPanel configurado como tarjeta visual del jugador
     * 
     * @see Jugador
     * @see #jugadorSeleccionado
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
        JLabel lblPos = new JLabel("⚽ " + jugador.getPosicion());
        lblPos.setForeground(new Color(100, 181, 246));
        lblPos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelInfo.add(lblPos);

        // Nacionalidad y Edad
        JPanel panelNacEdad = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelNacEdad.setOpaque(false);
        
        JLabel lblNacionalidad = new JLabel("🌍 " + jugador.getNacionalidad());
        lblNacionalidad.setForeground(new Color(220, 220, 220));
        lblNacionalidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelNacEdad.add(lblNacionalidad);
        
        JLabel lblEdad = new JLabel("📅 " + jugador.getEdad() + " años");
        lblEdad.setForeground(new Color(220, 220, 220));
        lblEdad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelNacEdad.add(lblEdad);
        
        panelInfo.add(panelNacEdad);

        // Altura y Peso
        JPanel panelFisico = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelFisico.setOpaque(false);
        
        JLabel lblAltura = new JLabel("📏 " + jugador.getAltura());
        lblAltura.setForeground(new Color(220, 220, 220));
        lblAltura.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelFisico.add(lblAltura);
        
        JLabel lblPeso = new JLabel("⚖️ " + jugador.getPeso());
        lblPeso.setForeground(new Color(220, 220, 220));
        lblPeso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelFisico.add(lblPeso);
        
        panelInfo.add(panelFisico);

        tarjeta.add(panelInfo, BorderLayout.CENTER);

        // ===== PANEL BOTONES (DERECHA) =====
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);

        JButton btnEliminarJugador = new JButton("Eliminar");
        btnEliminarJugador.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnEliminarJugador.setBackground(new Color(231, 76, 60));
        btnEliminarJugador.setForeground(Color.WHITE);
        btnEliminarJugador.setFocusPainted(false);
        btnEliminarJugador.setBorderPainted(false);
        btnEliminarJugador.setPreferredSize(new Dimension(90, 30));

        // Control de visibilidad por rol
        if (rolUsuario != Rol.ADMINISTRADOR && rolUsuario != Rol.MANAGER) {
            btnEliminarJugador.setVisible(false);
        }

        btnEliminarJugador.addActionListener(e -> {
            String tempNombre = (String) comboTemporadasJugadores.getSelectedItem();
            Temporada temp = datosFederacion.buscarTemporadaPorNombre(tempNombre);
            
            if (temp != null && !temp.getEstado().equals(Temporada.FUTURA)) {
                JOptionPane.showMessageDialog(this, "Solo temporadas FUTURAS", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar jugador?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION && temp != null) {
                String eqNombre = (String) comboEquiposJugadores.getSelectedItem();
                Equipo eq = temp.buscarEquipoPorNombre(eqNombre);
                if (eq != null) {
                    eq.getPlantilla().remove(jugador);
                    actualizarJugadoresPorTemporada(tempNombre, eqNombre);
                    GestorLog.exito("Jugador eliminado: " + jugador.getNombre());
                }
            }
        });

        panelBotones.add(btnEliminarJugador);
        tarjeta.add(panelBotones, BorderLayout.EAST);

        // ===== EVENTO DE SELECCIÓN =====
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jugadorSeleccionado = jugador;
                // Resetear todas las tarjetas
                for (Component c : panelTarjetasJugadores.getComponents()) {
                    if (c instanceof JPanel) {
                        c.setBackground(new Color(24, 25, 50));
                        ((JPanel) c).setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ));
                    }
                }
                // Destacar tarjeta seleccionada
                tarjeta.setBackground(new Color(45, 55, 140));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 150, 255), 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });

        return tarjeta;
    }

    /**
     * Crea una tarjeta visual para mostrar la información de un equipo.
     * 
     * <p>La tarjeta muestra:</p>
     * <ul>
     *   <li>Escudo del equipo (90x90 px)</li>
     *   <li>Nombre del equipo</li>
     *   <li>Número de jugadores en la plantilla</li>
     *   <li>Botones de acción según permisos del usuario</li>
     * </ul>
     * 
     * <p><b>Botones disponibles:</b></p>
     * <ul>
     *   <li><b>Cambiar Escudo:</b> Solo ADMINISTRADOR y MANAGER, solo en temporadas FUTURAS</li>
     *   <li><b>Ver Escudo:</b> Todos los usuarios</li>
     *   <li><b>Eliminar:</b> Solo ADMINISTRADOR y MANAGER, solo en temporadas FUTURAS</li>
     * </ul>
     * 
     * @param nombreEquipo El nombre del equipo a mostrar
     * @return JPanel configurado como tarjeta visual del equipo
     * 
     * @see Equipo
     * @see GestorArchivos#copiarEscudo(String, String)
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

        // ⭐ CREAR BOTÓN GESTIONAR
        JButton btnGestionar = new JButton("Gestionar");
        btnGestionar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnGestionar.setBackground(TemaColores.ACENTO_VERDE);
        btnGestionar.setForeground(Color.WHITE);
        btnGestionar.setFocusPainted(false);
        btnGestionar.setBorderPainted(false);
        btnGestionar.setPreferredSize(new Dimension(110, 30));

        if (rolUsuario != Rol.ADMINISTRADOR && rolUsuario != Rol.MANAGER) {
            btnGestionar.setVisible(false);
        }

        btnGestionar.addActionListener(e -> {
            String nombreTemporada = (String) comboTemporadas.getSelectedItem();
            Temporada temporadaActual = datosFederacion.buscarTemporadaPorNombre(nombreTemporada);
            
            if (temporadaActual != null && equipo != null) {
                PanelGestionJugadoresEquipo dialogo = new PanelGestionJugadoresEquipo(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    datosFederacion,
                    temporadaActual,
                    equipo
                );
                dialogo.setVisible(true);
                
                // Refrescar vista
                actualizarVistaEquipos();
                GestorLog.info("Gestión de jugadores completada para: " + nombreEquipo);
            }
        });

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
            } catch (Exception e) {}
        }
        panelTarjeta.add(lblEscudo, BorderLayout.WEST);

        // ===== INFORMACIÓN DEL EQUIPO =====
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        
        JLabel lblNombre = new JLabel(nombreEquipo);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(Color.WHITE);
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(5));
        
        if (equipo != null) {
            JLabel lblJugadores = new JLabel("👥 " + equipo.getPlantilla().size() + " jugadores");
            lblJugadores.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblJugadores.setForeground(new Color(180, 180, 180));
            panelInfo.add(lblJugadores);
        }
        panelTarjeta.add(panelInfo, BorderLayout.CENTER);

        // ===== PANEL BOTONES =====
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        // ⭐ AHORA SÍ AGREGAR EL BOTÓN GESTIONAR
        panelBotones.add(btnGestionar);

        // Botón Cambiar Escudo
        JButton btnCambiarEscudo = new JButton("Cambiar Escudo");
        btnCambiarEscudo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnCambiarEscudo.setFocusPainted(false);
        btnCambiarEscudo.setBorderPainted(false);
        btnCambiarEscudo.setPreferredSize(new Dimension(130, 30));
        
        if (rolUsuario != Rol.ADMINISTRADOR && rolUsuario != Rol.MANAGER) {
            btnCambiarEscudo.setVisible(false);
        }
        
        btnCambiarEscudo.addActionListener(e -> {
            // ... código del listener ...
        });
        panelBotones.add(btnCambiarEscudo);

        // Botón Ver Escudo
        JButton btnVerEscudo = new JButton("Ver Escudo");
        btnVerEscudo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnVerEscudo.setBackground(new Color(52, 152, 219));
        btnVerEscudo.setFocusPainted(false);
        btnVerEscudo.setBorderPainted(false);
        btnVerEscudo.setPreferredSize(new Dimension(110, 30));
        btnVerEscudo.addActionListener(e -> {
            Icon icon = lblEscudo.getIcon();
            if (icon != null) {
                ImageIcon img = new ImageIcon(((ImageIcon) icon).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(this, "", nombreEquipo, JOptionPane.INFORMATION_MESSAGE, img);
            }
        });
        panelBotones.add(btnVerEscudo);

        // Botón Eliminar Equipo
        JButton btnEliminarEquipo = new JButton("Eliminar");
        btnEliminarEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnEliminarEquipo.setBackground(new Color(231, 76, 60));
        btnEliminarEquipo.setForeground(Color.WHITE);
        btnEliminarEquipo.setFocusPainted(false);
        btnEliminarEquipo.setBorderPainted(false);
        btnEliminarEquipo.setPreferredSize(new Dimension(90, 30));
        
        if (rolUsuario != Rol.ADMINISTRADOR && rolUsuario != Rol.MANAGER) {
            btnEliminarEquipo.setVisible(false);
        }

        
        btnEliminarEquipo.addActionListener(e -> {
            String tempNombre = (String) comboTemporadas.getSelectedItem();
            Temporada temp = datosFederacion.buscarTemporadaPorNombre(tempNombre);
            
            if (temp != null && !temp.getEstado().equals(Temporada.FUTURA)) {
                JOptionPane.showMessageDialog(this, "Solo se pueden eliminar equipos de temporadas FUTURAS", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar el equipo '" + nombreEquipo + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION && temp != null) {
                Equipo equipoAEliminar = temp.buscarEquipoPorNombre(nombreEquipo);
                if (equipoAEliminar != null) {
                    temp.getEquiposParticipantes().remove(equipoAEliminar);
                    actualizarVistaEquipos();
                    sincronizarCombos();
                    GestorLog.exito("Equipo eliminado: " + nombreEquipo);
                }
            }
        });
        panelBotones.add(btnEliminarEquipo);

        panelTarjeta.add(panelBotones, BorderLayout.EAST);
        return panelTarjeta;
    }

    /**
     * Actualiza el ComboBox de jornadas según la temporada seleccionada.
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Limpia el ComboBox actual</li>
     *   <li>Obtiene la temporada seleccionada</li>
     *   <li>Agrega todas las jornadas de esa temporada</li>
     * </ul>
     * 
     * @see #comboJornadasPartidos
     * @see Temporada#getListaJornadas()
     */
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

    /**
     * Actualiza la vista de partidos mostrando los partidos de la jornada seleccionada.
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Limpia el panel de partidos</li>
     *   <li>Obtiene la temporada y jornada seleccionadas</li>
     *   <li>Crea tarjetas para cada partido</li>
     *   <li>Actualiza los indicadores de estado</li>
     * </ul>
     * 
     * @see #crearTarjetaPartido(Partido)
     * @see #actualizarIndicadorEstadoPartidos()
     */
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

    /**
     * Crea una tarjeta visual para mostrar la información de un partido.
     * 
     * <p>La tarjeta muestra:</p>
     * <ul>
     *   <li>Indicador de estado (●) en color azul (pendiente) o rojo (finalizado)</li>
     *   <li>Equipo local (en azul claro)</li>
     *   <li>Resultado o "vs" si no está jugado</li>
     *   <li>Equipo visitante (en naranja)</li>
     *   <li>Botón para anotar o editar resultado</li>
     * </ul>
     * 
     * @param p El partido a mostrar
     * @return JPanel configurado como tarjeta visual del partido
     * 
     * @see Partido
     * @see DialogoResultado
     */
    private JPanel crearTarjetaPartido(Partido p) {
    	
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(new Color(24, 25, 50));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110)); // Altura aumentada
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
       
        JPanel panelIzquierda = new JPanel(new BorderLayout(10, 0));
        panelIzquierda.setOpaque(false);
        
        // Indicador de estado del partido
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

        // Panel de equipos con etiquetas LOCAL/VISITANTE
        JPanel panelEquipos = new JPanel();
        panelEquipos.setLayout(new BoxLayout(panelEquipos, BoxLayout.Y_AXIS));
        panelEquipos.setOpaque(false);
        
        // Fila 1: EQUIPO LOCAL con icono
        JPanel filaLocal = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        filaLocal.setOpaque(false);
        
        JLabel lblIconoLocal = new JLabel("🏠");
        lblIconoLocal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filaLocal.add(lblIconoLocal);
        
        JLabel lblLocal = new JLabel(p.getEquipoLocal().getNombre());
        lblLocal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLocal.setForeground(new Color(100, 181, 246));
        filaLocal.add(lblLocal);
        
        panelEquipos.add(filaLocal);
        panelEquipos.add(Box.createVerticalStrut(5));
        
        // Fila 2: RESULTADO
        String resultado = p.isFinalizado() ? 
            p.getGolesLocal() + " - " + p.getGolesVisitante() : "vs";
        JLabel lblResultado = new JLabel(resultado);
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblResultado.setForeground(Color.WHITE);
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEquipos.add(lblResultado);
        panelEquipos.add(Box.createVerticalStrut(5));
        
        // Fila 3: EQUIPO VISITANTE con icono
        JPanel filaVisitante = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        filaVisitante.setOpaque(false);
        
        JLabel lblIconoVisitante = new JLabel("✈️");
        lblIconoVisitante.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filaVisitante.add(lblIconoVisitante);
        
        JLabel lblVisitante = new JLabel(p.getEquipoVisitante().getNombre());
        lblVisitante.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblVisitante.setForeground(new Color(255, 183, 77));
        filaVisitante.add(lblVisitante);
        
        panelEquipos.add(filaVisitante);
        
        panelIzquierda.add(panelEquipos, BorderLayout.CENTER);
        card.add(panelIzquierda, BorderLayout.CENTER);
        
        
    

        // Botón de resultado
        JButton btnGoles = new JButton(p.isFinalizado() ? "Editar Resultado" : "Anotar Goles");
        if (rolUsuario != Rol.ADMINISTRADOR && rolUsuario != Rol.ARBITRO) {
        	btnGoles.setVisible(false);}
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
                actualizarIndicadorEstadoPartidos();
                
                // ⭐ ACTUALIZAR CLASIFICACIÓN EN TIEMPO REAL
                if (panelClasificacionObjeto != null && t != null) {
                    panelClasificacionObjeto.actualizarClasificacion(t);
                }

            }
        });

        card.add(btnGoles, BorderLayout.EAST);
        return card;
        
    }
    /**
     * Actualiza el indicador visual del estado de la temporada en la sección de equipos.
     * 
     * <p>Muestra un label con código de colores según el estado:</p>
     * <ul>
     *   <li><b>FUTURA:</b> ● FUTURA (azul) - Permite agregar equipos</li>
     *   <li><b>EN_JUEGO:</b> ● EN CURSO (amarillo) - Bloquea agregar equipos</li>
     *   <li><b>TERMINADA:</b> ● FINALIZADA (rojo) - Bloquea todas las modificaciones</li>
     * </ul>
     * 
     * <p>Además controla el estado de los botones según la temporada:</p>
     * <ul>
     *   <li>Habilita/deshabilita btnAgregarEquipo</li>
     *   <li>Habilita/deshabilita btnFinalizarTemporada</li>
     *   <li>Habilita/deshabilita btnTxema</li>
     * </ul>
     * 
     * @see Temporada#getEstado()
     * @see #btnAgregarEquipo
     * @see #btnFinalizarTemporada
     */
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

        // Si no existe, crearlo al final del panel
        if (lblEstadoTemp == null) {
            lblEstadoTemp = new JLabel();
            lblEstadoTemp.setName("lblEstadoTemporada");
            lblEstadoTemp.setFont(new Font("Segoe UI", Font.BOLD, 14));
            panelSuperior.add(Box.createHorizontalStrut(15));
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
                    btnFinalizarTemporada.setEnabled(false);
                    btnTxema.setEnabled(false);
                    break;
                    
                case Temporada.EN_JUEGO:
                    lblEstadoTemp.setText("● EN CURSO");
                    lblEstadoTemp.setForeground(new Color(241, 196, 15));
                    btnAgregarEquipo.setEnabled(false);
                    btnAgregarEquipo.setToolTipText("No se pueden agregar equipos a temporadas en curso");
                    btnFinalizarTemporada.setEnabled(true);
                    btnTxema.setEnabled(true);
                    break;
                    
                case Temporada.TERMINADA:
                    lblEstadoTemp.setText("● FINALIZADA");
                    lblEstadoTemp.setForeground(new Color(231, 76, 60));
                    btnAgregarEquipo.setEnabled(false);
                    btnAgregarEquipo.setToolTipText("No se pueden agregar equipos a temporadas finalizadas");
                    btnFinalizarTemporada.setEnabled(false);
                    btnTxema.setEnabled(false);
                    break;
            }
        }

        panelSuperior.revalidate();
        panelSuperior.repaint();
    }

    /**
     * Actualiza los indicadores visuales de estado en la sección de partidos.
     * 
     * <p>Actualiza dos indicadores:</p>
     * <ul>
     *   <li><b>Estado de Temporada:</b> FUTURA / EN CURSO / FINALIZADA</li>
     *   <li><b>Estado de Jornada:</b> (Información adicional de la jornada actual)</li>
     * </ul>
     * 
     * <p>Controla el estado de múltiples botones según el estado de la temporada:</p>
     * <ul>
     *   <li>btnNuevaJor, btnNuevoPart (habilita solo en FUTURA y EN_JUEGO)</li>
     *   <li>btnInscribirEquipo (solo en FUTURA)</li>
     *   <li>btnFinalizarTemporada, btnTxema (solo en EN_JUEGO)</li>
     *   <li>btnEditarJugador, btnCambiarFoto (solo en FUTURA)</li>
     * </ul>
     * 
     * @see Temporada#getEstado()
     * @see #actualizarIndicadorEstadoTemporada()
     */
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
            // ⭐ CONTROL DE PERMISOS: Solo cambiar si el usuario tiene permisos
            boolean esAdministrador = (rolUsuario == Rol.ADMINISTRADOR);
            boolean esArbitro = (rolUsuario == Rol.ARBITRO);
            
            switch (t.getEstado()) {
                case Temporada.FUTURA:
                    lblEstadoTempPartidos.setText("  |  ● TEMPORADA FUTURA");
                    lblEstadoTempPartidos.setForeground(new Color(52, 152, 219));
                    
                    if (esAdministrador || esArbitro) {
                        btnNuevaJor.setEnabled(true);
                        btnNuevoPart.setEnabled(true);
                    }
                    if (esAdministrador) {
                        btnInscribirEquipo.setEnabled(true);
                        btnFinalizarTemporada.setEnabled(false);
                        btnTxema.setEnabled(false);
                    }
                    btnEditarJugador.setEnabled(true);
                    btnCambiarFoto.setEnabled(true);
                    break;
                    
                case Temporada.EN_JUEGO:
                    lblEstadoTempPartidos.setText("  |  ● EN CURSO");
                    lblEstadoTempPartidos.setForeground(new Color(241, 196, 15));
                    
                    if (esAdministrador || esArbitro) {
                        btnNuevaJor.setEnabled(true);
                        btnNuevoPart.setEnabled(true);
                    }
                    if (esAdministrador) {
                        btnInscribirEquipo.setEnabled(false);
                        btnFinalizarTemporada.setEnabled(true);  //  SOLO ADMIN
                        btnTxema.setEnabled(true);               //  SOLO ADMIN
                    }
                    btnEditarJugador.setEnabled(false);
                    btnCambiarFoto.setEnabled(false);
                    break;
                    
                case Temporada.TERMINADA:
                    lblEstadoTempPartidos.setText("  |  ● FINALIZADA");
                    lblEstadoTempPartidos.setForeground(new Color(231, 76, 60));
                    
                    if (esAdministrador || esArbitro) {
                        btnNuevaJor.setEnabled(false);
                        btnNuevoPart.setEnabled(false);
                    }
                    if (esAdministrador) {
                        btnInscribirEquipo.setEnabled(false);
                        btnFinalizarTemporada.setEnabled(false);
                        btnTxema.setEnabled(false);
                    }
                    btnEditarJugador.setEnabled(false);
                    btnCambiarFoto.setEnabled(false);
                    break;
            }
        }
        
        panelAdminPartidos_1.revalidate();
        panelAdminPartidos_1.repaint();
    }

    /**
     * Actualiza el ComboBox de equipos según la temporada seleccionada en la sección de jugadores.
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Limpia el ComboBox actual</li>
     *   <li>Agrega la opción "Todos"</li>
     *   <li>Agrega todos los equipos de la temporada seleccionada</li>
     * </ul>
     * 
     * @see #comboEquiposJugadores
     * @see Temporada#getEquiposParticipantes()
     */
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

    /**
     * Actualiza la vista de jugadores mostrando los jugadores filtrados por temporada y equipo.
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Limpia el panel de tarjetas de jugadores</li>
     *   <li>Obtiene la temporada seleccionada</li>
     *   <li>Filtra por equipo si no es "Todos"</li>
     *   <li>Crea tarjetas para cada jugador</li>
     *   <li>Actualiza la vista</li>
     * </ul>
     * 
     * @param nombreTemporada Nombre de la temporada a filtrar
     * @param nombreEquipo Nombre del equipo a filtrar, o "Todos" para mostrar todos
     * 
     * @see #crearTarjetaJugador(Jugador)
     * @see Equipo#getPlantilla()
     */
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
    }/**
     * Valida que todos los equipos de una temporada tengan
     * el número mínimo de jugadores requerido.
     * <p>
     * El método comprueba que la temporada y su lista de equipos
     * no sean nulas, que exista al menos un equipo registrado
     * y que cada equipo cumpla con el mínimo de jugadores definido
     * en {@link Equipo#JUGADORES_MINIMOS}.
     * </p>
     * <p>
     * En caso de detectar equipos que no cumplen el requisito,
     * se muestra un mensaje de advertencia detallado al usuario
     * y se registra el evento en el sistema de logs.
     * </p>
     *
     * @param temporada temporada a validar
     * @return {@code true} si todos los equipos tienen jugadores suficientes
     *         y la temporada puede inicializarse;
     *         {@code false} en caso contrario
     */
    /**
     * Valida que todos los equipos REALES de una temporada tengan
     * el número mínimo de jugadores requerido.
     * <p>
     * EXCLUYE el equipo fantasma "_SIN_EQUIPO_" de la validación.
     * </p>
     *
     * @param temporada temporada a validar
     * @return {@code true} si todos los equipos reales tienen jugadores suficientes
     */
    private boolean validarJugadoresMinimosPorTemporada(Temporada temporada) {
        if (temporada == null || temporada.getEquiposParticipantes() == null) {
            return false;
        }
        
        java.util.List<Equipo> equipos = temporada.getEquiposParticipantes();
        
        // Filtrar equipos reales (excluir fantasma)
        java.util.List<Equipo> equiposReales = new java.util.ArrayList<>();
        for (Equipo eq : equipos) {
            if (!eq.getNombre().equals("_SIN_EQUIPO_")) {
                equiposReales.add(eq);
            }
        }
        
        if (equiposReales.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay equipos registrados en esta temporada.",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        java.util.List<String> equiposInvalidos = new java.util.ArrayList<>();
        
        for (Equipo equipo : equiposReales) {
            if (!equipo.tieneJugadoresSuficientes()) {
                equiposInvalidos.add(equipo.obtenerDetalleValidacion());
            }
        }
        
        if (!equiposInvalidos.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("No se puede inicializar la temporada.\n\n");
            mensaje.append("Los siguientes equipos no tienen el mínimo de ");
            mensaje.append(Equipo.JUGADORES_MINIMOS);
            mensaje.append(" jugadores:\n\n");
            
            for (String detalle : equiposInvalidos) {
                mensaje.append("• ").append(detalle).append("\n");
            }
            
            mensaje.append("\nPor favor, completa las plantillas antes de continuar.");
            
            JOptionPane.showMessageDialog(this,
                mensaje.toString(),
                "Validación de plantillas",
                JOptionPane.WARNING_MESSAGE);
            
            GestorLog.advertencia("Validación fallida: " 
                    + equiposInvalidos.size() 
                    + " equipo(s) sin jugadores suficientes");
            return false;
        }
        
        return true;
    }

    
    
    

    /**
     * Crea un diálogo para añadir un nuevo partido manualmente.
     * 
     * <p>El diálogo permite:</p>
     * <ul>
     *   <li>Seleccionar equipo local</li>
     *   <li>Seleccionar equipo visitante</li>
     *   <li>Validar que no sea el mismo equipo</li>
     *   <li>Agregar el partido a la jornada seleccionada</li>
     * </ul>
     * 
     * @see Partido
     * @see Jornada#agregarPartido(Partido)
     * @see #actualizarVistaPartidos()
     */
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

    /**
     * Ejecuta el proceso de inscripción de un equipo existente en una temporada.
     * 
     * <p>El proceso incluye:</p>
     * <ul>
     *   <li>Seleccionar la temporada destino</li>
     *   <li>Filtrar equipos ya inscritos</li>
     *   <li>Mostrar equipos disponibles de otras temporadas</li>
     *   <li>Inscribir el equipo seleccionado</li>
     *   <li>Actualizar vistas y sincronizar combos</li>
     * </ul>
     * 
     * @see Temporada#inscribirEquipo(Equipo)
     * @see #sincronizarCombos()
     * @see #actualizarVistaEquipos()
     */
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

    /**
     * Obtiene la temporada actualmente seleccionada en el ComboBox de partidos.
     * 
     * @return La temporada seleccionada, o null si no hay selección
     * 
     * @see #comboTemporadasPartidos
     * @see DatosFederacion#buscarTemporadaPorNombre(String)
     */
    private Temporada obtenerTemporadaSeleccionada() {
        String nombreSeleccionado = (String) comboTemporadasPartidos.getSelectedItem();
        if (nombreSeleccionado == null) return null;

        return datosFederacion.buscarTemporadaPorNombre(nombreSeleccionado);
    }

    /**
     * Actualiza la tabla gráfica de clasificación con los datos actuales.
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Calcula la clasificación usando CalculadoraClasificacion</li>
     *   <li>Limpia la tabla actual</li>
     *   <li>Rellena con las filas de clasificación ordenadas</li>
     *   <li>Maneja errores si la temporada es nula</li>
     * </ul>
     * 
     * @see CalculadoraClasificacion#calcular(Temporada)
     * @see Clasificacion#getFilas()
     * @see FilaClasificacion
     */
    private void actualizarTablaClasificacionGrafica() {
        Temporada temp = obtenerTemporadaSeleccionada();
        
        if (temp == null || modeloTabla == null) return;

        modeloTabla.setRowCount(0);

        try {
            Clasificacion clasificacion = CalculadoraClasificacion.calcular(temp);
            java.util.List<FilaClasificacion> ranking = clasificacion.getFilas();

            for (FilaClasificacion fila : ranking) {
                Object[] datosFila = {
                    fila.getPosicion(),     
                    fila.getEquipo(),        
                    fila.getPuntos(),        
                    fila.getPj(),
                    fila.getPg(),
                    fila.getPe(),
                    fila.getPp(),
                    fila.getGf(),
                    fila.getGc(),
                    fila.getDifFormateada()
                };
                modeloTabla.addRow(datosFila);
            }
        } catch (IllegalArgumentException ex) {
            System.err.println("Aviso: " + ex.getMessage());
        } catch (Exception e) {
            System.err.println("Error al actualizar la tabla: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la vista de equipos mostrando los equipos de la temporada seleccionada.
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Limpia el panel de tarjetas</li>
     *   <li>Obtiene la temporada seleccionada</li>
     *   <li>Crea tarjetas para cada equipo</li>
     *   <li>Actualiza el indicador de estado</li>
     * </ul>
     * 
     * @see #crearTarjetaEquipo(String)
     * @see #actualizarIndicadorEstadoTemporada()
     */
    private void actualizarVistaEquipos() {
        panelTarjetasEquipo.removeAll();
        
        String tempNombre = (String) comboTemporadas.getSelectedItem();
        if (tempNombre == null) return;

        Temporada t = datosFederacion.buscarTemporadaPorNombre(tempNombre);
        
        if (t != null) {
        	for (Equipo eq : t.getEquiposParticipantes()) {
        	    // Saltar el equipo fantasma en la vista
        	    if (eq.getNombre().equals("_SIN_EQUIPO_")) {
        	        continue;
        	    }
        	    panelTarjetasEquipo.add(crearTarjetaEquipo(eq.getNombre()));
        	}}
        
        panelTarjetasEquipo.revalidate();
        panelTarjetasEquipo.repaint();
        
        actualizarIndicadorEstadoTemporada();
    }

    /**
     * Sincroniza todos los ComboBox de temporadas de la aplicación.
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Guarda las selecciones actuales de todos los combos</li>
     *   <li>Limpia y recarga los items de todos los ComboBox</li>
     *   <li>Restaura las selecciones previas si aún existen</li>
     *   <li>Actualiza el ComboBox del panel de clasificación</li>
     *   <li>Actualiza el ComboBox de equipos</li>
     * </ul>
     * 
     * @see #comboTemporadas
     * @see #comboTemporadasJugadores
     * @see #comboTemporadasPartidos
     * @see PanelClasificacion#getComboTemporadas()
     */
    void sincronizarCombos() {
        Object tempSelEquipos = comboTemporadas.getSelectedItem();
        Object tempSelJugadores = comboTemporadasJugadores.getSelectedItem();
        Object tempSelPartidos = comboTemporadasPartidos.getSelectedItem();
        
        Object tempSelClasificacion = null;
       
        
        JComboBox[] combosTemp = {
            comboTemporadas, 
            comboTemporadasJugadores, 
            comboTemporadasPartidos
        };
        
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

    /**
     * Actualiza el estado general de la interfaz según el estado de la temporada seleccionada.
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Habilita/deshabilita btnNuevaJor según el estado</li>
     *   <li>Actualiza el label de estado si existe</li>
     *   <li>Cambia el color del label según el estado</li>
     * </ul>
     * 
     * @see Temporada#getEstado()
     * @see #lblEstado
     */
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

    /**
     * Finaliza la temporada seleccionada si cumple todas las condiciones.
     * 
     * <p>Condiciones para finalizar:</p>
     * <ul>
     *   <li>La temporada debe estar EN_JUEGO</li>
     *   <li>Todos los partidos deben estar finalizados</li>
     *   <li>El usuario debe confirmar la acción</li>
     * </ul>
     * 
     * <p>Al finalizar:</p>
     * <ul>
     *   <li>Cambia el estado a TERMINADA</li>
     *   <li>Sincroniza todos los combos</li>
     *   <li>Actualiza todos los indicadores</li>
     *   <li>Registra la acción en el log</li>
     * </ul>
     * 
     * @see #todosLosPartidosFinalizados(Temporada)
     * @see Temporada#setEstado(String)
     * @see GestorLog#exito(String)-
     */
    private void finalizarTemporada() {
        Temporada temp = obtenerTemporadaSeleccionada();
        if (!temp.getEstado().equals(Temporada.EN_JUEGO)) { 
            JOptionPane.showMessageDialog(this, "Solo se pueden finalizar temporadas EN CURSO", "Operación no permitida", JOptionPane.WARNING_MESSAGE); 
            return; 
        } 
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
        
        sincronizarCombos();
        actualizarIndicadorEstadoPartidos();
        actualizarIndicadorEstadoTemporada();
        actualizarEstadoInterfaz();
        actualizarVistaPartidos();
        actualizarVistaEquipos();
        
        GestorLog.exito("Temporada finalizada: " + temp.getNombre() + 
                      " | Estado: " + estadoAnterior + " → " + Temporada.TERMINADA +
                      " | Equipos: " + temp.getEquiposParticipantes().size() +
                      " | Jornadas: " + temp.getListaJornadas().size());
        
        JOptionPane.showMessageDialog(this,
            "Temporada " + temp.getNombre() + " finalizada con éxito.\n" +
            "Puedes consultar la clasificación final en la pestaña Clasificación.",
            "Temporada finalizada",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Función de desarrollo "Txema" que simula resultados automáticos para todos los partidos pendientes.
     * 
     * <p><b>⚠️ SOLO PARA DESARROLLO/TESTING</b></p>
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Itera sobre todas las jornadas de la temporada seleccionada</li>
     *   <li>Busca partidos no finalizados</li>
     *   <li>Genera resultados aleatorios (0-49 goles)</li>
     *   <li>Marca los partidos como finalizados</li>
     *   <li>Actualiza todas las vistas</li>
     * </ul>
     * 
     * @see Partido#setGolesLocal(int)
     * @see Partido#setGolesVisitante(int)
     * @see Partido#setFinalizado(boolean)
     * @see #actualizarVistaPartidos()
     */
    private void funcionTxema() {
        Temporada temp = obtenerTemporadaSeleccionada();
        
        // 1. Verificación inicial de seguridad
        if (temp == null) {
            GestorLog.advertencia("No hay una temporada seleccionada para ejecutar la simulación.");
            return;
        }

        // 2. Validaciones previas
        if (!validarJugadoresMinimosPorTemporada(temp)) {
            return;
        }

        // 3. Simulación de resultados
        Random random = new Random();
        
        // Solo entramos si hay partidos pendientes
        if (!todosLosPartidosFinalizados(temp)) {
            for (Jornada j : temp.getListaJornadas()) {
                for (Partido p : j.getListaPartidos()) {
                    if (!p.isFinalizado()) {
                        // Genera goles entre 1 y 50
                        int golesLocal = random.nextInt(50) + 1;
                        int golesVisitante = random.nextInt(50) + 1;
                        
                        p.setGolesLocal(golesLocal);
                        p.setGolesVisitante(golesVisitante);
                        p.setFinalizado(true);
                    }
                }
            }
            
            // 4. Actualización de la interfaz (solo si hubo cambios)
   
            actualizarVistaPartidos();
            actualizarIndicadorEstadoPartidos();
            
            if (panelClasificacionObjeto != null) {
                panelClasificacionObjeto.actualizarClasificacion(temp);
            }
            
            GestorLog.exito("Función Txema ejecutada: Resultados aleatorios generados en " + temp.getNombre());
        } else {
            GestorLog.info("Todos los partidos ya estaban finalizados en " + temp.getNombre());
        }
    }

    /**
     * Verifica si todos los partidos de una temporada han sido finalizados.
     * 
     * @param temp La temporada a verificar
     * @return true si todos los partidos están finalizados, false en caso contrario
     * 
     * @see Partido#isFinalizado()
     */
    private boolean todosLosPartidosFinalizados(Temporada temp) {
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

    /**
     * Configura la interfaz después del login exitoso según el rol del usuario.
     * 
     * <p>Este método es llamado desde la ventana de Login tras autenticación exitosa.</p>
     * 
     * <p><b>Configuración por roles:</b></p>
     * <ul>
     *   <li><b>ADMINISTRADOR:</b> Acceso total a todas las funcionalidades</li>
     *   <li><b>MANAGER:</b> Gestión de jugadores, equipos y visualización</li>
     *   <li><b>ARBITRO:</b> Gestión de partidos y resultados</li>
     *   <li><b>INVITADO:</b> Solo visualización (lectura)</li>
     * </ul>
     * 
     * <p>Acciones realizadas:</p>
     * <ul>
     *   <li>Guarda el rol del usuario</li>
     *   <li>Actualiza las etiquetas de bienvenida</li>
     *   <li>Maximiza la ventana</li>
     *   <li>Oculta todos los controles</li>
     *   <li>Muestra solo los controles permitidos según el rol</li>
     *   <li>Registra la acción en el log</li>
     * </ul>
     * 
     * @param rol El rol del usuario autenticado
     * @param nombre El nombre del usuario autenticado
     * 
     * @see Rol
     * @see #ocultarTodosLosControles()
     * @see #mostrarTodo(boolean)
     * @see #habilitarNavegacionBasica()
     * @see GestorLog#info(String)
     */
    public void despuesDelLogin(Rol rol, String nombre) {
        this.rolUsuario = rol;
        this.lblBienvenido.setText("Bienvenido, " + nombre);
        this.lblUsuario.setText("Rol: " + rol.getNombreLegible());
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // ⭐ LIMPIEZA OBLIGATORIA
        ocultarTodosLosControles();

        // Aplicación de permisos específicos
        switch (rol) {
            case ADMINISTRADOR:
                habilitarNavegacionBasica();
                panelAdminPartidos_1.setVisible(true);
                btnNuevaTemp_1.setVisible(true);
                btnNuevaJor.setVisible(true);
                btnNuevoPart.setVisible(true);
                btnFinalizarTemporada.setVisible(true);  // ⭐ ADMIN SÍ LO VE
                btnTxema.setVisible(true);               // ⭐ ADMIN SÍ LO VE
                btnInscribirEquipo.setVisible(true);
                btnAgregarEquipo.setVisible(true);
                btnAgregarJugador.setVisible(true);
                btnCambiarEquipo.setVisible(true);
                btnCambiarFoto.setVisible(true);
                btnVerFoto.setVisible(true);
                btnEditarJugador.setVisible(true);
                btnExportar.setVisible(true);
                btnGestionUsuario.setVisible(true);
                btnTemporadas.setVisible(true);
                break;

            case ARBITRO:
                habilitarNavegacionBasica();
                panelAdminPartidos_1.setVisible(true);
                btnNuevaJor.setVisible(true);
                btnNuevoPart.setVisible(true);
                btnFinalizarTemporada.setVisible(false);  // ⭐ ÁRBITRO NO LO VE
                btnTxema.setVisible(false);               // ⭐ ÁRBITRO NO LO VE
                btnNuevaTemp_1.setVisible(false);
                btnInscribirEquipo.setVisible(false);
                btnCambiarFoto.setVisible(false);
                btnExportar.setVisible(false);
                btnGestionUsuario.setVisible(false);
                btnVerFoto.setVisible(true);
                btnTemporadas.setVisible(true);
                break;

            case MANAGER:
                habilitarNavegacionBasica();
                btnJugadores.setVisible(true);
                btnVerFoto.setVisible(true);
                btnCambiarFoto.setVisible(true);
                btnAgregarJugador.setVisible(true);
                btnCambiarEquipo.setVisible(true);
                btnEditarJugador.setVisible(true);
                btnInscribirEquipo.setVisible(true);
                panelAdminPartidos_1.setVisible(true);  //  Para ver partidos
                btnFinalizarTemporada.setVisible(false);  //  MANAGER NO LO VE
                btnTxema.setVisible(false);               //  MANAGER NO LO VE
                btnNuevaTemp_1.setVisible(false);
                btnNuevaJor.setVisible(false);
                btnNuevoPart.setVisible(false);
                btnExportar.setVisible(false);
                btnGestionUsuario.setVisible(false);
                btnTemporadas.setVisible(true);
                break;

            case INVITADO:
            default:
                habilitarNavegacionBasica();
                btnVerFoto.setVisible(true);
                panelAdminPartidos_1.setVisible(true);  //  Solo para ver
                btnFinalizarTemporada.setVisible(false);  //  INVITADO NO LO VE
                btnTxema.setVisible(false);               //  INVITADO NO LO VE
                btnNuevaTemp_1.setVisible(false);
                btnNuevaJor.setVisible(false);
                btnNuevoPart.setVisible(false);
                btnCambiarFoto.setVisible(false);
                btnInscribirEquipo.setVisible(false);
                btnExportar.setVisible(false);
                btnGestionUsuario.setVisible(false);
                btnTemporadas.setVisible(true);
                break;
        }
        
        // ⭐ FORZAR RECREACIÓN DE VISTAS
        SwingUtilities.invokeLater(() -> {
            actualizarVistaEquipos();
            
            if (comboTemporadasJugadores.getItemCount() > 0 && comboEquiposJugadores.getItemCount() > 0) {
                String tempSel = (String) comboTemporadasJugadores.getSelectedItem();
                String equipoSel = (String) comboEquiposJugadores.getSelectedItem();
                if (tempSel != null && equipoSel != null) {
                    actualizarJugadoresPorTemporada(tempSel, equipoSel);
                }
            }
            
            actualizarVistaPartidos();
            
            GestorLog.info(" Vistas actualizadas con permisos de: " + rol.getNombreLegible());
        });
        
        revalidate();
        repaint();
        
        GestorLog.info("Sesión iniciada - Usuario: " + nombre + " | Rol: " + rol.getNombreLegible());
    }
    
    /**
     * Cierra la sesión del usuario actual y vuelve a la ventana de login.
     * 
     * <p>Este método:</p>
     * <ul>
     *   <li>Solicita confirmación al usuario</li>
     *   <li>Detiene el auto-guardado si está activo</li>
     *   <li>Guarda todos los datos antes de cerrar</li>
     *   <li>Cierra la ventana actual</li>
     *   <li>Abre la ventana de Login</li>
     *   <li>Registra la acción en el log</li>
     * </ul>
     * 
     * <p><b>Nota:</b> No limpia el rolUsuario para preservar el estado.</p>
     * 
     * @see #autoSaveTimer
     * @see GestorArchivos#guardarTodo(DatosFederacion)
     * @see Login
     * @see GestorLog#info(String)
     */
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que quieres cerrar la sesión?", 
                "Cerrar Sesión", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            // Detener el auto-guardado
            if (autoSaveTimer != null && autoSaveTimer.isRunning()) {
                autoSaveTimer.stop();
                GestorLog.info("Auto-guardado detenido");
            }
            
            // Guardar los datos antes de cerrar sesión
            GestorLog.info("Guardando datos antes de cerrar sesión...");
            GestorArchivos.guardarTodo(datosFederacion);
            GestorLog.info("Datos guardados correctamente");
            
            // ⭐ LIMPIEZA CRÍTICA: Limpiar todas las vistas antes de cerrar
            limpiarTodasLasVistas();
            
            // ⭐ RESETEAR EL ROL
            this.rolUsuario = null;
            
            this.dispose();
            
            Login ventanaLogin = new Login();
            ventanaLogin.setVisible(true);
            
            GestorLog.info("Sesión cerrada correctamente");
        }
    }
    
    
    /**
     * Limpia todas las vistas de la interfaz para resetear el estado
     * antes de un cambio de sesión.
     * 
     * <p>Este método es crítico para evitar que componentes con permisos
     * del usuario anterior permanezcan visibles para el nuevo usuario.</p>
     */
    private void limpiarTodasLasVistas() {
        // Limpiar panel de equipos
        if (panelTarjetasEquipo != null) {
            panelTarjetasEquipo.removeAll();
            panelTarjetasEquipo.revalidate();
            panelTarjetasEquipo.repaint();
        }
        
        // Limpiar panel de jugadores
        if (panelTarjetasJugadores != null) {
            panelTarjetasJugadores.removeAll();
            panelTarjetasJugadores.revalidate();
            panelTarjetasJugadores.repaint();
        }
        
        // ⭐ CRÍTICO: Limpiar panel de partidos
        if (panelListaPartidos != null) {
            panelListaPartidos.removeAll();
            panelListaPartidos.revalidate();
            panelListaPartidos.repaint();
        }
        
        // Limpiar clasificación
      
        
        GestorLog.info("🧹 Todas las vistas limpiadas correctamente");
    }
    
    /**
     * Oculta todos los controles de edición y gestión de la interfaz.
     * 
     * <p>Este método se usa como paso inicial antes de aplicar permisos específicos por rol.</p>
     * 
     * <p>Controles ocultados:</p>
     * <ul>
     *   <li>Navegación principal (todos los botones del menú)</li>
     *   <li>Gestión de partidos y temporadas</li>
     *   <li>Gestión de equipos</li>
     *   <li>Gestión de jugadores</li>
     * </ul>
     * 
     * @see #despuesDelLogin(Rol, String)
     * @see #mostrarTodo(boolean)
     */
    /**
     * Oculta todos los controles de edición y gestión de la interfaz.
     * 
     * <p>Este método se usa como paso inicial antes de aplicar permisos específicos por rol.</p>
     */
    private void ocultarTodosLosControles() {
        // Navegación principal
        btnEquipos.setVisible(false);
        btnJugadores.setVisible(false);
        btnPartidos.setVisible(false);

        // ⭐ CRÍTICO: Ocultar TODOS los botones del panel de administración
        panelAdminPartidos_1.setVisible(false);
        btnNuevaTemp_1.setVisible(false);
        btnNuevaJor.setVisible(false);
        btnNuevoPart.setVisible(false);
        btnFinalizarTemporada.setVisible(false);  
        btnTxema.setVisible(false);               
        btnInscribirEquipo.setVisible(false);     
        
        // Gestión de Equipos
        btnAgregarEquipo.setVisible(false);
        
        // Gestión de Jugadores
        btnAgregarJugador.setVisible(false);
        btnCambiarEquipo.setVisible(false);
        btnCambiarFoto.setVisible(false);
        btnVerFoto.setVisible(false);
        btnEditarJugador.setVisible(false);  // ⭐ AGREGADO (por consistencia)
        
        // Botones del sistema
        btnExportar.setVisible(false);
        btnGestionUsuario.setVisible(false);
    }

    /**
     * Habilita los botones de navegación básica comunes para todos los usuarios.
     * 
     * <p>Botones habilitados:</p>
     * <ul>
     *   <li>btnEquipos - Visualización de equipos</li>
     *   <li>btnJugadores - Visualización de jugadores</li>
     *   <li>btnPartidos - Visualización de partidos</li>
     *   <li>btnClasificacin - Tabla de clasificación</li>
     * </ul>
     * 
     * @see #despuesDelLogin(Rol, String)
     */
    private void habilitarNavegacionBasica() {
  
        btnEquipos.setVisible(true);
        btnJugadores.setVisible(true);
        btnPartidos.setVisible(true);
    
    }

    /**
     * Activa o desactiva todos los componentes de administración de la interfaz.
     * 
     * <p><b>Uso exclusivo para ADMINISTRADOR.</b></p>
     * 
     * <p>Componentes controlados:</p>
     * <ul>
     *   <li>Navegación básica</li>
     *   <li>Paneles y botones de administración de partidos</li>
     *   <li>Botones de gestión de equipos</li>
     *   <li>Botones de gestión de jugadores</li>
     * </ul>
     * 
     * @param estado true para mostrar todos los componentes, false para ocultarlos
     * 
     * @see #despuesDelLogin(Rol, String)
     * @see Rol#ADMINISTRADOR
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
    }
    
    /**
     * Obtiene la temporada actualmente visible en la interfaz según el panel activo.
     * 
     * <p>Este método detecta qué panel está visible y devuelve su temporada seleccionada:</p>
     * <ul>
     *   <li>Si está visible panelEquipos → usa comboTemporadas</li>
     *   <li>Si está visible panelJugadores → usa comboTemporadasJugadores</li>
     *   <li>Si está visible panelPartidos → usa comboTemporadasPartidos</li>
     * </ul>
     * 
     * @return La temporada actualmente visible, o null si no hay ninguna seleccionada
     * 
     * @see DatosFederacion#buscarTemporadaPorNombre(String)
     */
    private Temporada obtenerTemporadaActualVisible() {
        String nombreTemporada = null;
        
        if (panelEquipos.isVisible() && comboTemporadas.getSelectedItem() != null) {
            nombreTemporada = (String) comboTemporadas.getSelectedItem();
        } else if (panelJugadores.isVisible() && comboTemporadasJugadores.getSelectedItem() != null) {
            nombreTemporada = (String) comboTemporadasJugadores.getSelectedItem();
        } else if (panelPartidos.isVisible() && comboTemporadasPartidos.getSelectedItem() != null) {
            nombreTemporada = (String) comboTemporadasPartidos.getSelectedItem();
        }
        
        if (nombreTemporada != null) {
            return datosFederacion.buscarTemporadaPorNombre(nombreTemporada);
        }
        
        return null;
    }

    /**
     * Muestra un diálogo para que el usuario seleccione una temporada específica.
     * 
     * <p>Este método es usado principalmente por la funcionalidad de exportación.</p>
     * 
     * <p>Características:</p>
     * <ul>
     *   <li>Muestra todas las temporadas disponibles</li>
     *   <li>Incluye el estado de cada temporada entre corchetes</li>
     *   <li>Permite cancelar la selección</li>
     *   <li>Devuelve el objeto Temporada seleccionado</li>
     * </ul>
     * 
     * @return La temporada seleccionada por el usuario, o null si canceló
     * 
     * @see ExportadorXML#exportarTemporada(Temporada)
     */
    private Temporada mostrarDialogoSeleccionTemporada() {
        java.util.List<Temporada> temporadas = datosFederacion.getListaTemporadas();
        
        if (temporadas.isEmpty()) {
            return null;
        }
        
        // Crear array con nombres de temporadas con información de estado
        String[] nombresTemporadas = new String[temporadas.size()];
        for (int i = 0; i < temporadas.size(); i++) {
            Temporada t = temporadas.get(i);
            nombresTemporadas[i] = t.getNombre() + " [" + t.getEstado() + "]";
        }
        
        String seleccion = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona la temporada a exportar:",
            "Seleccionar temporada",
            JOptionPane.QUESTION_MESSAGE,
            null,
            nombresTemporadas,
            nombresTemporadas[0]
        );
        
        if (seleccion == null) {
            return null; // Usuario canceló
        }
        
        // Extraer el nombre de la temporada (antes del corchete)
        String nombreTemp = seleccion.split(" \\[")[0];
        return datosFederacion.buscarTemporadaPorNombre(nombreTemp);
    }
    
    /**
     * Obtiene el nombre del equipo al que pertenece un jugador en la temporada actual.
     * 
     * <p>Busca en la temporada seleccionada en comboTemporadasJugadores.</p>
     * 
     * @param jugador El jugador del cual obtener el equipo
     * @return El nombre del equipo, o "SIN_EQUIPO" si no se encuentra
     * 
     * @see Equipo#getPlantilla()
     */
    private String obtenerEquipoDeJugador(Jugador jugador) {
        if (jugador == null) return "SIN_EQUIPO";
        
        String tempNom = (String) comboTemporadasJugadores.getSelectedItem();
        if (tempNom == null) return "SIN_EQUIPO";
        
        Temporada temp = datosFederacion.buscarTemporadaPorNombre(tempNom);
        if (temp == null) return "SIN_EQUIPO";
        
        for (Equipo eq : temp.getEquiposParticipantes()) {
            if (eq.getPlantilla().contains(jugador)) {
                return eq.getNombre();
            }
        }
        
        return "SIN_EQUIPO";
    }
    
    /**
     * Inicia el sistema de auto-guardado automático de datos.
     * 
     * <p>Configura un Timer que guarda automáticamente todos los datos cada 5 minutos (300000 ms).</p>
     * 
     * <p><b>Acciones del auto-guardado:</b></p>
     * <ul>
     *   <li>Se ejecuta cada 5 minutos</li>
     *   <li>Guarda todos los datos usando GestorArchivos</li>
     *   <li>Registra cada guardado en el log</li>
     *   <li>Se puede detener al cerrar sesión o ventana</li>
     * </ul>
     * 
     * @see #autoSaveTimer
     * @see GestorArchivos#guardarTodo(DatosFederacion)
     * @see GestorLog#info(String)
     * @see GestorLog#exito(String)
     */
    private void iniciarAutoGuardado() {
        // Autoguardado cada 5 minutos (300000 ms)
        autoSaveTimer = new javax.swing.Timer(300000, e -> {
            GestorLog.info("💾 Auto-guardado iniciado...");
            GestorArchivos.guardarTodo(datosFederacion);
            GestorLog.exito("✅ Auto-guardado completado");
        });
        autoSaveTimer.start();
        GestorLog.info("🔄 Sistema de auto-guardado activado (cada 5 minutos)");
    }

    // ==========================================
    // IMPLEMENTACIÓN DE WindowListener
    // ==========================================

    /**
     * Maneja el evento de cierre de ventana (clic en la X).
     * 
     * <p>Este método implementa un cierre seguro con las siguientes opciones:</p>
     * <ul>
     *   <li><b>YES:</b> Guarda los datos y cierra la aplicación</li>
     *   <li><b>NO:</b> Cierra sin guardar (con confirmación adicional)</li>
     *   <li><b>CANCEL:</b> Cancela el cierre y mantiene la ventana abierta</li>
     * </ul>
     * 
     * <p>Al guardar:</p>
     * <ul>
     *   <li>Detiene el auto-guardado</li>
     *   <li>Guarda todos los datos</li>
     *   <li>Muestra mensaje de confirmación</li>
     *   <li>Registra la acción en el log</li>
     *   <li>Cierra la aplicación con System.exit(0)</li>
     * </ul>
     * 
     * @param e El evento de ventana generado
     * 
     * @see WindowListener#windowClosing(WindowEvent)
     * @see #autoSaveTimer
     * @see GestorArchivos#guardarTodo(DatosFederacion)
     * @see GestorLog#info(String)
     * @see GestorLog#exito(String)
     * @see GestorLog#advertencia(String)
     */
    @Override
    public void windowClosing(WindowEvent e) {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Deseas guardar los cambios antes de salir?",
            "Confirmar Salida",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (respuesta == JOptionPane.YES_OPTION) {
            // Detener el auto-guardado
            if (autoSaveTimer != null && autoSaveTimer.isRunning()) {
                autoSaveTimer.stop();
            }
            
            // Guardar y salir
            GestorLog.info("💾 Guardando datos antes de cerrar aplicación...");
            GestorArchivos.guardarTodo(datosFederacion);
            GestorLog.exito("✅ Datos guardados correctamente");
            
            JOptionPane.showMessageDialog(
                this,
                "Datos guardados con éxito.\n¡Hasta pronto!",
                "Guardado exitoso",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            GestorLog.info("🚪 Aplicación cerrada por el usuario");
            System.exit(0);
            
        } else if (respuesta == JOptionPane.NO_OPTION) {
            // Salir sin guardar (con confirmación adicional)
            int confirmarSinGuardar = JOptionPane.showConfirmDialog(
                this,
                "⚠️ Se perderán todos los cambios no guardados.\n¿Estás seguro?",
                "Confirmar salida sin guardar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirmarSinGuardar == JOptionPane.YES_OPTION) {
                // Detener el auto-guardado
                if (autoSaveTimer != null && autoSaveTimer.isRunning()) {
                    autoSaveTimer.stop();
                }
                
                GestorLog.advertencia("⚠️ Aplicación cerrada sin guardar cambios");
                System.exit(0);
            }
        }
        
        // Si respuesta == CANCEL_OPTION, no hace nada (ventana permanece abierta)
    }
    
    /**
     * Maneja el evento de apertura de la ventana.
     * 
     * @param e El evento de ventana generado
     * @see WindowListener#windowOpened(WindowEvent)
     */
    @Override
    public void windowOpened(WindowEvent e) {
        GestorLog.info("🪟 Ventana principal abierta");
    }

    /**
     * Maneja el evento de ventana cerrada.
     * 
     * @param e El evento de ventana generado
     * @see WindowListener#windowClosed(WindowEvent)
     */
    @Override
    public void windowClosed(WindowEvent e) {
        GestorLog.info("🪟 Ventana principal cerrada");
    }

    /**
     * Maneja el evento de ventana minimizada.
     * 
     * @param e El evento de ventana generado
     * @see WindowListener#windowIconified(WindowEvent)
     */
    @Override
    public void windowIconified(WindowEvent e) {
        GestorLog.info("➖ Ventana minimizada");
    }

    /**
     * Maneja el evento de ventana restaurada desde minimización.
     * 
     * @param e El evento de ventana generado
     * @see WindowListener#windowDeiconified(WindowEvent)
     */
    @Override
    public void windowDeiconified(WindowEvent e) {
        GestorLog.info("⬆️ Ventana restaurada");
    }

    /**
     * Maneja el evento de ventana activada (recibe el foco).
     * 
     * <p>No realiza ninguna acción para evitar logs excesivos.</p>
     * 
     * @param e El evento de ventana generado
     * @see WindowListener#windowActivated(WindowEvent)
     */
    @Override
    public void windowActivated(WindowEvent e) {
        // No hacer nada para evitar logs excesivos
    }

    /**
     * Maneja el evento de ventana desactivada (pierde el foco).
     * 
     * <p>No realiza ninguna acción para evitar logs excesivos.</p>
     * 
     * @param e El evento de ventana generado
     * @see WindowListener#windowDeactivated(WindowEvent)
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
        // No hacer nada para evitar logs excesivos
    }
}
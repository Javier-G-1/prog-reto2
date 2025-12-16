package nuevoapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableModel; 

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class VentanaPrincipal extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel panelLateral;
    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    
    // Atributos de clase
    private JButton btnTemporada, btnEquipos, btnPartidos, btnResultados, btnCerrarSesion, btnInicio;
    
    private static final String CARD_INICIO = "inicio";
    private static final String CARD_TEMPORADAS = "temporadas";
    private static final String CARD_EQUIPOS = "equipos";
    private static final String CARD_PARTIDOS = "partidos";
    private static final String CARD_RESULTADOS = "resultado";
    private JTable tableClasificacion;
    
    // Colores usados en la aplicación
    private static final Color COLOR_FONDO_LATERAL = new Color(24, 24, 27);
    private static final Color COLOR_AZUL_SELECCION = new Color(37, 99, 235);
    private static final Color COLOR_HOVER = new Color(39, 39, 42);
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Color COLOR_BORDE = new Color(63, 63, 70);

    public VentanaPrincipal() {
        try {
            // Asumiendo que /assets/icono.png existe
            ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
            setIconImage(icono.getImage());
        } catch (Exception e) {
            System.err.println("Error al cargar el ícono de la ventana: " + e.getMessage());
        }
        
        initUI();
    }

    private void initUI() {
        setTitle("Federación de Balonmano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO_LATERAL);

        crearPanelLateral();
        crearPanelPrincipal();
        mostrarPanel(CARD_INICIO);
        // Inicializar la selección al inicio
        if (btnInicio != null) {
            actualizarSeleccion(btnInicio);
        }

        setVisible(true);
    }

    private void crearPanelLateral() {
        panelLateral = new JPanel();
        panelLateral.setPreferredSize(new Dimension(280, 0));
        panelLateral.setBackground(COLOR_FONDO_LATERAL);
        panelLateral.setLayout(new BorderLayout());
        panelLateral.setBorder(new EmptyBorder(30, 20, 30, 20));

        // Panel superior con logo y botones del menú
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setOpaque(false);

        // Logo y título
        JPanel panelLogo = new JPanel();
        panelLogo.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelLogo.setOpaque(false);
        panelLogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel lblIcono = new JLabel();
        lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        lblIcono.setForeground(COLOR_TEXTO);
        
        JLabel lblTitulo = new JLabel("Federación de Balonmano");
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        panelLogo.add(lblIcono);
        panelLogo.add(lblTitulo);
        panelSuperior.add(panelLogo);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 20)));

        // BOTONES DE MENÚ
        
        btnInicio = new JButton(" Inicio"); 
        configurarBotonMenu(btnInicio, " Inicio");
        btnInicio.addActionListener(this); 

        btnTemporada = new JButton(" Temporada");
        configurarBotonMenu(btnTemporada, " Temporadas");
        
        btnEquipos = new JButton(" Equipos");
        configurarBotonMenu(btnEquipos, " Equipos");

        btnPartidos = new JButton(" Partidos");
        configurarBotonMenu(btnPartidos, " Partidos");

        btnResultados = new JButton(" Resultados");
        configurarBotonMenu(btnResultados, " Resultados");
        
        // Agregar los Listeners de la clase a los botones
        btnTemporada.addActionListener(this);
        btnEquipos.addActionListener(this);
        btnPartidos.addActionListener(this);
        btnResultados.addActionListener(this);
        
        Component rigidArea_3 = Box.createRigidArea(new Dimension(0, 20));
        panelSuperior.add(rigidArea_3);
        
        // Agregar botones al panel superior
        panelSuperior.add(btnInicio);
        
        Component rigidArea = Box.createRigidArea(new Dimension(0, 20));
        panelSuperior.add(rigidArea);
        panelSuperior.add(btnTemporada);
        
        Component rigidArea_1 = Box.createRigidArea(new Dimension(0, 20));
        panelSuperior.add(rigidArea_1);
        panelSuperior.add(btnEquipos);
        
        Component rigidArea_2 = Box.createRigidArea(new Dimension(0, 20));
        panelSuperior.add(rigidArea_2);
        panelSuperior.add(btnPartidos);
        
        Component rigidArea_2_1 = Box.createRigidArea(new Dimension(0, 20));
        panelSuperior.add(rigidArea_2_1);
        panelSuperior.add(btnResultados);

        // Panel inferior con botón cerrar sesión
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setOpaque(false);

        btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnCerrarSesion.setBackground(new Color(0, 0, 0));
        btnCerrarSesion.setForeground(COLOR_TEXTO);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(this);

        panelInferior.add(btnCerrarSesion);

        // Añadir paneles al panel lateral
        panelLateral.add(panelSuperior, BorderLayout.NORTH);
        panelLateral.add(panelInferior, BorderLayout.SOUTH);

        getContentPane().add(panelLateral, BorderLayout.WEST);
    }
    
    /**
     * Configura el estilo y el MouseListener de un botón de menú.
     */
    private void configurarBotonMenu(JButton btn, String texto) {
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setText(texto);
        btn.setForeground(COLOR_TEXTO);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true); 
        btn.setContentAreaFilled(false); 

        // Añadir efecto hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Solo cambiar si no es el botón activo (COLOR_AZUL_SELECCION)
                if (!btn.getBackground().equals(COLOR_AZUL_SELECCION)) {
                    btn.setBackground(COLOR_HOVER);
                    btn.setContentAreaFilled(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Solo volver al fondo lateral si no es el botón activo
                if (!btn.getBackground().equals(COLOR_AZUL_SELECCION)) {
                    btn.setBackground(COLOR_FONDO_LATERAL);
                    btn.setContentAreaFilled(false);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnInicio) {
            mostrarPanel(CARD_INICIO);
            actualizarSeleccion(btnInicio);
        } else if (e.getSource() == btnTemporada) {
            mostrarPanel(CARD_TEMPORADAS);
            actualizarSeleccion(btnTemporada);
        } else if (e.getSource() == btnEquipos) {
            mostrarPanel(CARD_EQUIPOS);
            actualizarSeleccion(btnEquipos);
        } else if (e.getSource() == btnPartidos) {
            mostrarPanel(CARD_PARTIDOS);
            actualizarSeleccion(btnPartidos);
        } else if (e.getSource() == btnResultados) {
            mostrarPanel(CARD_RESULTADOS);
            actualizarSeleccion(btnResultados);
        } else if (e.getSource() == btnCerrarSesion) {
            // Lógica para cerrar sesión
            JOptionPane.showMessageDialog(this, "Sesión cerrada", "Adiós", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cierra la ventana principal
        }
    }

    /**
     * Actualiza el estado visual de los botones de menú.
     */
    private void actualizarSeleccion(JButton botonSeleccionado) {
        JButton[] botones = {btnInicio, btnTemporada, btnEquipos, btnPartidos, btnResultados};
        
        for (JButton btn : botones) {
            if (btn == botonSeleccionado) {
                btn.setBackground(COLOR_AZUL_SELECCION);
                btn.setContentAreaFilled(true);
            } else {
                btn.setBackground(COLOR_FONDO_LATERAL);
                btn.setContentAreaFilled(false);
            }
        }
    }
    
    // --- MÉTODOS DE PANELES PRINCIPALES ---

    private void crearPanelPrincipal() {
        panelPrincipal = new JPanel();
        cardLayout = new CardLayout();
        panelPrincipal.setLayout(cardLayout);
        panelPrincipal.setBackground(COLOR_FONDO_LATERAL);

        panelPrincipal.add(crearPanelInicio(), CARD_INICIO);
        panelPrincipal.add(crearPanelTemporadas(), CARD_TEMPORADAS);
        panelPrincipal.add(crearPanelEquipo(), CARD_EQUIPOS);
        panelPrincipal.add(crearPanelPartidos(), CARD_PARTIDOS);
        panelPrincipal.add(crearPanelResultados(), CARD_RESULTADOS);

        getContentPane().add(panelPrincipal, BorderLayout.CENTER);
    }

    private JPanel crearPanelInicio() {
        JPanel panelTituloInicio = new JPanel(new BorderLayout());
        panelTituloInicio.setBackground(COLOR_FONDO_LATERAL);
        panelTituloInicio.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lblTituloInicio = new JLabel("Inicio");
        lblTituloInicio.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloInicio.setForeground(COLOR_TEXTO);
        panelTituloInicio.add(lblTituloInicio, BorderLayout.NORTH);

        return panelTituloInicio;
    }

    private JPanel crearPanelTemporadas() {
        JPanel panelTemporada = new JPanel(new BorderLayout());
        panelTemporada.setBackground(COLOR_FONDO_LATERAL);
        panelTemporada.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lblTitulo = new JLabel("Temporadas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_TEXTO);
        panelTemporada.add(lblTitulo, BorderLayout.NORTH);
        
        JPanel panelBodyTemporada = new JPanel();
        panelTemporada.add(panelBodyTemporada, BorderLayout.SOUTH);
        
        JButton btnCrearTemporada = new JButton("New button");
        panelBodyTemporada.add(btnCrearTemporada);

        return panelTemporada;
    }
    
    /**
     * Helper: Carga un escudo de equipo desde los recursos.
     */
    private ImageIcon cargarEscudo(String nombreEquipo) {
        // Asume la ruta de recursos para WindowBuilder: /assets/escudos/[nombre].png
        String nombreArchivo = nombreEquipo.toLowerCase().replaceAll("\\s+", "");
        String ruta = "/assets/escudos/" + nombreArchivo + ".png";
        
        try {
            // Intentar cargar el recurso
            java.net.URL imgURL = getClass().getResource(ruta);
            if (imgURL == null) {
                // Si no se encuentra, usar una imagen de placeholder (ej. un círculo)
                System.err.println("Advertencia: No se pudo cargar el escudo en " + ruta + ". Usando placeholder.");
                // Retornar un placeholder visual, o un ImageIcon temporal
                return null; 
            }
            
            Image img = new ImageIcon(imgURL).getImage();
            // Escalar la imagen a un tamaño fijo (40x40) para la tarjeta
            Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH); 
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("Error al procesar el escudo para " + nombreEquipo + ": " + e.getMessage());
            return null;
        }
    }


    private JPanel crearPanelEquipo() {
        JPanel panelBodyEquipos = new JPanel(new BorderLayout());
        panelBodyEquipos.setBackground(COLOR_FONDO_LATERAL);
        panelBodyEquipos.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Header
        JPanel panelHeaderEquipos = new JPanel(new BorderLayout());
        panelHeaderEquipos.setOpaque(false);
        panelHeaderEquipos.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JPanel headerContentInicio = new JPanel();
        headerContentInicio.setLayout(new BoxLayout(headerContentInicio, BoxLayout.Y_AXIS));
        headerContentInicio.setOpaque(false);

        panelHeaderEquipos.add(headerContentInicio, BorderLayout.WEST);
        panelHeaderEquipos.add(headerContentInicio, BorderLayout.NORTH);
        panelBodyEquipos.add(panelHeaderEquipos, BorderLayout.NORTH);

        // Título sección
        JLabel lbltituloEquipos = new JLabel("Equipos");
        lbltituloEquipos.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbltituloEquipos.setForeground(COLOR_TEXTO);
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

        String[] equipos = {"Barcelona", "Athletic Club", "Granada", "Sevilla", "Zaragoza", "Valencia"};

        for (String nombre : equipos) {
            JPanel tarjeta = new JPanel(new BorderLayout(15, 0)); // Espacio entre elementos
            tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            tarjeta.setPreferredSize(new Dimension(800, 80));
            tarjeta.setBackground(COLOR_HOVER);
            tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(15, 20, 15, 20)
            ));
            
            // 1. ESCUDO (WEST)
            ImageIcon escudo = cargarEscudo(nombre);
            JLabel lblEscudo = new JLabel(escudo);
            lblEscudo.setPreferredSize(new Dimension(45, 45)); 
            tarjeta.add(lblEscudo, BorderLayout.WEST);

            // 2. NOMBRE (CENTER)
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblNombre.setForeground(COLOR_TEXTO);
            tarjeta.add(lblNombre, BorderLayout.CENTER);

            // 3. BOTONES (EAST)
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            panelBotones.setOpaque(false);

            // Botón Cambiar Escudo
            JButton btnCambiarEscudo = new JButton("Cambiar Escudo");
            btnCambiarEscudo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnCambiarEscudo.setForeground(new Color(161, 161, 170));
            btnCambiarEscudo.setBackground(COLOR_HOVER);
            btnCambiarEscudo.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            btnCambiarEscudo.setFocusPainted(false);
            btnCambiarEscudo.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Asignar Listener para abrir JFileChooser
            btnCambiarEscudo.addActionListener(e -> {
                manejarCambioEscudo(lblEscudo, nombre);
            });
            
            // Botón Gestionar
            JButton btnGestionar = new JButton("Gestionar");
            btnGestionar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnGestionar.setForeground(COLOR_AZUL_SELECCION); 
            btnGestionar.setBackground(COLOR_HOVER);
            btnGestionar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            btnGestionar.setFocusPainted(false);
            btnGestionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnGestionar.addActionListener(e -> {
                 JOptionPane.showMessageDialog(this, "Funcionalidad 'Gestionar' para " + nombre + " no implementada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            });
            
            panelBotones.add(btnCambiarEscudo);
            panelBotones.add(btnGestionar);
            
            tarjeta.add(panelBotones, BorderLayout.EAST);


            contenedor.add(tarjeta);
            contenedor.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JScrollPane scrollEquipos = new JScrollPane(contenedor);
        scrollEquipos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollEquipos.setViewportBorder(null);
        scrollEquipos.setEnabled(false);
        scrollEquipos.setOpaque(false);
        scrollEquipos.getViewport().setOpaque(false);
        scrollEquipos.getVerticalScrollBar().setUnitIncrement(10);
        scrollEquipos.setBorder(null);
        panelBodyEquipos.add(scrollEquipos, BorderLayout.CENTER);

        return panelBodyEquipos;
    }
    
    /**
     * Muestra un JFileChooser, permite al usuario seleccionar una imagen y actualiza el JLabel.
     * @param lblEscudo El JLabel donde se mostrará el nuevo escudo.
     * @param nombreEquipo El nombre del equipo (para mensajes).
     */
    private void manejarCambioEscudo(JLabel lblEscudo, String nombreEquipo) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Nuevo Escudo para " + nombreEquipo);
        
        // Filtro para mostrar solo archivos de imagen
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Archivos de Imagen (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                // Cargar la imagen del archivo seleccionado
                ImageIcon nuevaIcono = new ImageIcon(archivoSeleccionado.getAbsolutePath());
                Image img = nuevaIcono.getImage();
                
                // Escalar la nueva imagen al mismo tamaño (40x40)
                Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH); 
                lblEscudo.setIcon(new ImageIcon(scaledImg));
                
                JOptionPane.showMessageDialog(this, 
                    "Escudo de " + nombreEquipo + " actualizado con éxito desde:\n" + archivoSeleccionado.getName(), 
                    "Actualización Exitosa", JOptionPane.INFORMATION_MESSAGE);
                
                // En un entorno real, aquí guardarías la ruta o el binario de la imagen en tu base de datos o modelo.
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al cargar la imagen seleccionada.", 
                    "Error de Imagen", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    // El resto de los métodos se mantienen igual (Partidos, Resultados, Helpers)

    /**
     * Crea el panel de Partidos.
     */
    private JPanel crearPanelPartidos() {
        JPanel panelJornadas = new JPanel(new BorderLayout());
        panelJornadas.setBackground(COLOR_FONDO_LATERAL);
        panelJornadas.setBorder(new EmptyBorder(40, 40, 40, 40));

        final Color COLOR_FONDO_OSCURO_ELEM = COLOR_HOVER; 
        
        // 1. HEADER (Título y Filtros)
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Título
        JLabel lblTituloJornada = new JLabel("Partidos");
        lblTituloJornada.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloJornada.setForeground(COLOR_TEXTO);
        panelHeader.add(lblTituloJornada, BorderLayout.NORTH);

        // Contenedor de Filtros (alineado a la izquierda)
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelFiltros.setOpaque(false);
        
        // Filtro de Temporada
        JLabel lblTemporada = new JLabel("Temporada:");
        lblTemporada.setForeground(new Color(161, 161, 170));
        lblTemporada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JComboBox<String> comboTemporada = crearComboBox(COLOR_FONDO_OSCURO_ELEM);
        comboTemporada.addItem("2024/2025");
        comboTemporada.addItem("2023/2024");
        
        // Filtro de Jornada
        JLabel lblJornada = new JLabel("Jornada:");
        lblJornada.setForeground(new Color(161, 161, 170));
        lblJornada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JComboBox<String> comboJornada = crearComboBox(COLOR_FONDO_OSCURO_ELEM);
        for (int i = 1; i <= 30; i++) {
            comboJornada.addItem("Jornada " + i);
        }
        
        // Agregar filtros al panel de filtros
        panelFiltros.add(lblTemporada);
        panelFiltros.add(comboTemporada);
        panelFiltros.add(lblJornada);
        panelFiltros.add(comboJornada);
        
        panelHeader.add(panelFiltros, BorderLayout.CENTER);
        
        panelJornadas.add(panelHeader, BorderLayout.NORTH);

        // 2. CUERPO (Resumen y Lista de Partidos)
        JPanel panelBody = new JPanel();
        panelBody.setLayout(new BoxLayout(panelBody, BoxLayout.Y_AXIS));
        panelBody.setOpaque(false);

        // Resumen de la Jornada
        JPanel panelResumen = crearPanelResumenJornada(COLOR_FONDO_OSCURO_ELEM, COLOR_TEXTO);
        panelBody.add(panelResumen);
        panelBody.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Contenedor de Partidos (Simulación)
        JPanel contenedorPartidos = new JPanel();
        contenedorPartidos.setLayout(new BoxLayout(contenedorPartidos, BoxLayout.Y_AXIS));
        contenedorPartidos.setOpaque(false);
        
        contenedorPartidos.add(crearTarjetaPartido(COLOR_FONDO_OSCURO_ELEM, COLOR_TEXTO, "Barcelona", "Athletic Club", "TERMINADO", "30 - 25"));
        contenedorPartidos.add(Box.createRigidArea(new Dimension(0, 15)));
        contenedorPartidos.add(crearTarjetaPartido(COLOR_FONDO_OSCURO_ELEM, COLOR_TEXTO, "Granada", "Sevilla", "EN JUEGO", "15 - 12"));
        contenedorPartidos.add(Box.createRigidArea(new Dimension(0, 15)));
        contenedorPartidos.add(crearTarjetaPartido(COLOR_FONDO_OSCURO_ELEM, COLOR_TEXTO, "Zaragoza", "Valencia", "POR JUGAR", "18:00h"));

        JScrollPane scrollPartidos = new JScrollPane(contenedorPartidos);
        scrollPartidos.setOpaque(false);
        scrollPartidos.getViewport().setOpaque(false);
        scrollPartidos.setBorder(null);
        scrollPartidos.getVerticalScrollBar().setUnitIncrement(16);
        
        panelBody.add(scrollPartidos);
        
        panelJornadas.add(panelBody, BorderLayout.CENTER);

        return panelJornadas;
    }
    
    /**
     * Crea el panel de Resultados/Clasificación.
     */
    private JPanel crearPanelResultados() {
        JPanel panelClasificacion = new JPanel(new BorderLayout());
        panelClasificacion.setBackground(COLOR_FONDO_LATERAL);
        panelClasificacion.setBorder(new EmptyBorder(40, 40, 40, 40));

        final Color COLOR_FONDO_OSCURO_ELEM = COLOR_HOVER; 

        // 1. Cabecera (Título y ComboBox)
        JPanel panelHeaderClasificacion = new JPanel();
        panelHeaderClasificacion.setLayout(new BoxLayout(panelHeaderClasificacion, BoxLayout.Y_AXIS));
        panelHeaderClasificacion.setOpaque(false);
        panelHeaderClasificacion.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Título "Clasificación"
        JLabel lblTituloClasificacion = new JLabel("Clasificación");
        lblTituloClasificacion.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloClasificacion.setForeground(COLOR_TEXTO);
        
        lblTituloClasificacion.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ComboBox de Temporada
        JComboBox<String> comboBoxTemporada = new JComboBox<>();
        comboBoxTemporada.addItem("Temporada 2024/2025");
        comboBoxTemporada.addItem("Temporada 2023/2024");
        comboBoxTemporada.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBoxTemporada.setForeground(COLOR_TEXTO);
        comboBoxTemporada.setBackground(COLOR_FONDO_OSCURO_ELEM);
        
        comboBoxTemporada.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Renderer para el estilo del ComboBox
        comboBoxTemporada.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected || cellHasFocus ? COLOR_AZUL_SELECCION : COLOR_FONDO_OSCURO_ELEM);
                label.setForeground(COLOR_TEXTO);
                return label;
            }
        });

        // Añadir componentes al Header
        panelHeaderClasificacion.add(lblTituloClasificacion);
        panelHeaderClasificacion.add(Box.createRigidArea(new Dimension(0, 15)));
        panelHeaderClasificacion.add(comboBoxTemporada);

        panelClasificacion.add(panelHeaderClasificacion, BorderLayout.NORTH);

        // 2. Datos y Modelo de Tabla (No Editable)
        String[] columnNames = {"Pos", "Equipo", "PJ", "PG", "PE", "PP", "Puntos", "GF", "GC", "DF"};
        Object[][] data = {
            {"1", "Athletic Club", "14", "12", "1", "1", "37", "420", "300", "120"},
            {"2", "Barcelona", "14", "10", "1", "3", "31", "380", "310", "70"},
            {"3", "Granada", "14", "8", "2", "4", "26", "360", "350", "10"},
            {"4", "Sevilla", "14", "7", "2", "5", "23", "340", "345", "-5"},
            {"5", "Zaragoza", "14", "7", "1", "6", "22", "335", "360", "-25"},
            {"6", "Valencia", "14", "6", "3", "5", "21", "350", "340", "10"},
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tableClasificacion = new JTable(model);

        // 3. Estilo de la Tabla
        tableClasificacion.setBackground(COLOR_FONDO_OSCURO_ELEM);
        tableClasificacion.setForeground(COLOR_TEXTO);
        tableClasificacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableClasificacion.setGridColor(COLOR_BORDE);
        tableClasificacion.setRowHeight(30);
        tableClasificacion.setSelectionBackground(COLOR_AZUL_SELECCION.darker());
        tableClasificacion.setSelectionForeground(COLOR_TEXTO);
        tableClasificacion.setIntercellSpacing(new Dimension(0, 0));

        // Estilo de la Cabecera (Header)
        JTableHeader header = tableClasificacion.getTableHeader();
        header.setBackground(COLOR_FONDO_LATERAL);
        header.setForeground(new Color(161, 161, 170));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        header.setPreferredSize(new Dimension(header.getWidth(), 35)); 

        // Renderizadores (Centrado y Alineación a la izquierda)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(COLOR_FONDO_OSCURO_ELEM);
        centerRenderer.setForeground(COLOR_TEXTO);
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBackground(COLOR_FONDO_OSCURO_ELEM);
        leftRenderer.setForeground(COLOR_TEXTO);

        // Aplicar renderizadores
        for (int i = 0; i < tableClasificacion.getColumnCount(); i++) {
            tableClasificacion.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        tableClasificacion.getColumnModel().getColumn(1).setCellRenderer(leftRenderer); // Columna "Equipo" a la izquierda

        // 4. Panel de Scroll
        JScrollPane scrollPane = new JScrollPane(tableClasificacion);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE)); 
        scrollPane.getViewport().setBackground(COLOR_FONDO_OSCURO_ELEM); 
        scrollPane.setOpaque(false);

        panelClasificacion.add(scrollPane, BorderLayout.CENTER);

        return panelClasificacion;
    }

    private void mostrarPanel(String nombre) {
        cardLayout.show(panelPrincipal, nombre);
    }

	/**
	 * No hacemos nada con los niveles de privilegio por petición del usuario.
	 */
	public void despuesDelLogin(int nivel, String nombre) {
		// Lógica de privilegios (VACIADA intencionalmente)
	}

    /**
     * Helper: Crea un JComboBox con el estilo de la aplicación.
     */
    private JComboBox<String> crearComboBox(Color fondo) {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setForeground(COLOR_TEXTO);
        comboBox.setBackground(fondo);
        comboBox.setPreferredSize(new Dimension(150, 30));
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Renderer para el estilo del ComboBox
        comboBox.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected || cellHasFocus ? COLOR_AZUL_SELECCION : fondo);
                label.setForeground(COLOR_TEXTO);
                return label;
            }
        });
        return comboBox;
    }
    
    /**
     * Helper: Crea un panel que simula la etiqueta de estado sin usar HTML.
     */
    private JPanel crearIndicadorEstadoSinHtml(String label, int count, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);

        // 1. Etiqueta de texto fijo (en negrita)
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLabel.setForeground(COLOR_TEXTO);
        panel.add(lblLabel);

        // 2. Etiqueta para el número (con color)
        JLabel lblCount = new JLabel(String.valueOf(count));
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // El número no necesita negrita
        lblCount.setForeground(color);
        panel.add(lblCount);

        return panel;
    }
    
    /**
     * Helper: Crea el panel de resumen de la jornada. 
     */
    private JPanel crearPanelResumenJornada(Color fondo, Color texto) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 0));
        panel.setBackground(fondo);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Datos de simulación
        int totalPartidos = 5;
        
        // Etiqueta principal
        JLabel lblTotal = new JLabel("Partidos en Jornada 1: " + totalPartidos);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(texto);
        panel.add(lblTotal);
        
        JLabel lblSeparador = new JLabel("|");
        lblSeparador.setForeground(new Color(161, 161, 170));
        lblSeparador.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        panel.add(lblSeparador);

        // Indicadores de estado (SIN HTML)
        panel.add(crearIndicadorEstadoSinHtml("Terminados:", 3, new Color(74, 222, 128))); // Verde
        panel.add(crearIndicadorEstadoSinHtml("En Juego:", 1, new Color(253, 224, 71))); // Amarillo
        panel.add(crearIndicadorEstadoSinHtml("Por Jugar:", 1, new Color(129, 140, 248))); // Azul/Morado

        return panel;
    }

    /**
     * Helper: Crea una tarjeta de partido con su estado y resultado.
     */
    private JPanel crearTarjetaPartido(Color fondo, Color texto, String local, String visitante, String estado, String resultado) {
        JPanel tarjeta = new JPanel(new BorderLayout(20, 0));
        tarjeta.setBackground(fondo);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
            new EmptyBorder(20, 30, 20, 30)
        ));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // 1. Equipos (Izquierda) - Contenedor para manejar múltiples JLabels
        JPanel panelEquipos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelEquipos.setOpaque(false);
        
        // Etiqueta Equipo Local (Negrita)
        JLabel lblLocal = new JLabel(local);
        lblLocal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLocal.setForeground(texto);
        panelEquipos.add(lblLocal);
        
        // Etiqueta "vs"
        JLabel lblVS = new JLabel("vs");
        lblVS.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblVS.setForeground(new Color(161, 161, 170)); // Color más sutil para "vs"
        panelEquipos.add(lblVS);

        // Etiqueta Equipo Visitante (Negrita)
        JLabel lblVisitante = new JLabel(visitante);
        lblVisitante.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVisitante.setForeground(texto);
        panelEquipos.add(lblVisitante);

        tarjeta.add(panelEquipos, BorderLayout.WEST);

        // 2. Resultado y Estado (Derecha)
        JPanel panelResultado = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelResultado.setOpaque(false);

        // Estado (Terminado/En Juego/Por Jugar)
        Color estadoColor;
        if (estado.equals("TERMINADO")) {
            estadoColor = new Color(74, 222, 128); // Verde
        } else if (estado.equals("EN JUEGO")) {
            estadoColor = new Color(253, 224, 71); // Amarillo
        } else {
            estadoColor = new Color(129, 140, 248); // Azul
        }

        JLabel lblEstado = new JLabel(estado);
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstado.setForeground(estadoColor);
        panelResultado.add(lblEstado);

        // Resultado o Hora
        JLabel lblResultado = new JLabel(resultado);
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblResultado.setForeground(texto);
        panelResultado.add(lblResultado);

        // Botón
        JButton btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnVerDetalles.setBackground(COLOR_AZUL_SELECCION);
        btnVerDetalles.setForeground(Color.WHITE);
        btnVerDetalles.setFocusPainted(false);
        btnVerDetalles.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        panelResultado.add(btnVerDetalles);
        
        tarjeta.add(panelResultado, BorderLayout.EAST);
        
        return tarjeta;
    }
}
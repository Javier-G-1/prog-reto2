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
    // Colores adicionales para la columna de Estado
    private static final Color COLOR_TERMINADO = new Color(22, 163, 74); // Verde oscuro
    private static final Color COLOR_EN_CURSO = new Color(250, 204, 21);  // Amarillo oscuro
    private static final Color COLOR_FUTURO = new Color(99, 102, 241);   // Indigo

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

        // setVisible(true); // Se recomienda que el setVisible lo haga la clase Login
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
            // Idealmente, aquí se abriría de nuevo la ventana de Login
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

    private void mostrarPanel(String nombrePanel) {
        cardLayout.show(panelPrincipal, nombrePanel);
    }

    private JPanel crearPanelInicio() {
        // Panel contenedor principal del inicio
        JPanel panelInicio = new JPanel(new BorderLayout());
        panelInicio.setBackground(COLOR_FONDO_LATERAL);
        panelInicio.setBorder(new EmptyBorder(40, 40, 40, 40));

        // 1. TÍTULO EN LA PARTE SUPERIOR (NORTH)
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setOpaque(false);
        
        JLabel lblTituloInicio = new JLabel("REAL FEDERACION ESPAÑOLA DE BALONMANO");
        lblTituloInicio.setFont(new Font("Segoe UI", Font.BOLD, 48)); // Letra muy grande
        lblTituloInicio.setForeground(COLOR_TEXTO);
        panelTitulo.add(lblTituloInicio);
        
        panelInicio.add(panelTitulo, BorderLayout.NORTH);

        // 2. IMAGEN GRANDE EN EL CENTRO (CENTER)
        JPanel panelImagen = new JPanel(new GridBagLayout()); // Usamos GridBagLayout para centrar
        panelImagen.setOpaque(false);
        
        JLabel lblImagenBalon = new JLabel();
        try {
            // Carga la imagen BALON.jpg
            ImageIcon balonIcon = new ImageIcon(getClass().getResource("/assets/BALON.jpg"));
            if (balonIcon.getImage() != null) {
                // Escala la imagen para que sea grande (ejemplo: 400x400)
                Image scaledImg = balonIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                lblImagenBalon.setIcon(new ImageIcon(scaledImg));
            } else {
                lblImagenBalon.setText("IMAGEN BALON NO ENCONTRADA");
                lblImagenBalon.setForeground(Color.RED);
            }
        } catch (Exception e) {
            lblImagenBalon.setText("ERROR AL CARGAR IMAGEN DEL BALÓN");
            lblImagenBalon.setForeground(Color.RED);
        }
        
        panelImagen.add(lblImagenBalon); // La imagen queda centrada por el GridBagLayout
        
        panelInicio.add(panelImagen, BorderLayout.CENTER);

        return panelInicio;
    }
    
    // ***************************************************************
    // ************ MÉTODO MODIFICADO PARA EL REQUERIMIENTO **********
    // ***************************************************************
    private JPanel crearPanelTemporadas() {
        JPanel panelTemporada = new JPanel(new BorderLayout());
        panelTemporada.setBackground(COLOR_FONDO_LATERAL);
        panelTemporada.setBorder(new EmptyBorder(40, 40, 40, 40));

        // 1. Cabecera (Título y Botón Crear)
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel lblTitulo = new JLabel("Gestión de Temporadas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_TEXTO);
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        // Botón "Crear Nueva Temporada"
        JButton btnCrearTemporada = new JButton("Crear Nueva Temporada");
        btnCrearTemporada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCrearTemporada.setForeground(COLOR_TEXTO);
        btnCrearTemporada.setBackground(COLOR_AZUL_SELECCION);
        btnCrearTemporada.setFocusPainted(false);
        btnCrearTemporada.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCrearTemporada.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCrearTemporada.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad para crear una nueva temporada no implementada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        });
        
        panelHeader.add(btnCrearTemporada, BorderLayout.EAST);
        panelTemporada.add(panelHeader, BorderLayout.NORTH);

        // 2. Tabla de Temporadas
        String[] columnNames = {"ID", "Nombre de Temporada", "Inicio", "Fin", "Estado"};
        Object[][] data = {
            {"1", "Temporada 2025/2026", "01/09/2025", "30/06/2026", "Futuro"},
            {"2", "Temporada 2024/2025", "01/09/2024", "30/06/2025", "En Curso"},
            {"3", "Temporada 2023/2024", "01/09/2023", "30/06/2024", "Terminado"},
            {"4", "Temporada 2022/2023", "01/09/2022", "30/06/2023", "Terminado"},
            {"5", "Temporada 2021/2022", "01/09/2021", "30/06/2022", "Terminado"},
        };
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Esto es importante para el renderizador de JLabel
                return (columnIndex == 4) ? JLabel.class : super.getColumnClass(columnIndex);
            }
        };

        JTable tableTemporadas = new JTable(model);
        
        // Estilo de la Tabla
        tableTemporadas.setBackground(COLOR_HOVER);
        tableTemporadas.setForeground(COLOR_TEXTO);
        tableTemporadas.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tableTemporadas.setGridColor(COLOR_BORDE);
        tableTemporadas.setRowHeight(40);
        tableTemporadas.setSelectionBackground(COLOR_AZUL_SELECCION.darker());
        tableTemporadas.setSelectionForeground(COLOR_TEXTO);
        tableTemporadas.setIntercellSpacing(new Dimension(0, 0));

        // Estilo de la Cabecera (Header)
        JTableHeader header = tableTemporadas.getTableHeader();
        header.setBackground(COLOR_FONDO_LATERAL);
        header.setForeground(new Color(161, 161, 170));
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        header.setPreferredSize(new Dimension(header.getWidth(), 45)); 

        // Renderizador Personalizado para la Columna "Estado"
        class EstadoRenderer extends DefaultTableCellRenderer {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Centrar texto en todas las celdas
                label.setHorizontalAlignment(CENTER);
                
                // Aplicar estilo de fondo y selección
                label.setBackground(COLOR_HOVER);
                label.setForeground(COLOR_TEXTO);
                if (isSelected) {
                    label.setBackground(COLOR_AZUL_SELECCION.darker());
                } else {
                    label.setBackground(COLOR_HOVER);
                }
                
                // Estilo específico para la columna "Estado"
                if (column == 4 && value instanceof String) {
                    String estado = (String) value;
                    JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
                    panelEstado.setOpaque(true);
                    panelEstado.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding interno
                    
                    JLabel lblEstado = new JLabel(estado);
                    lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    lblEstado.setForeground(Color.WHITE); 
                    lblEstado.setHorizontalAlignment(JLabel.CENTER);

                    Color bgColor;
                    
                    switch (estado) {
                        case "Terminado":
                            bgColor = COLOR_TERMINADO;
                            break;
                        case "En Curso":
                            bgColor = COLOR_EN_CURSO;
                            break;
                        case "Futuro":
                            bgColor = COLOR_FUTURO;
                            break;
                        default:
                            bgColor = new Color(156, 163, 175); // Gris
                            break;
                    }
                    
                    lblEstado.setOpaque(true);
                    lblEstado.setBackground(bgColor);
                    lblEstado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    lblEstado.setForeground(Color.WHITE);
                    lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    
                    panelEstado.add(lblEstado);
                    panelEstado.setBackground(isSelected ? COLOR_AZUL_SELECCION.darker() : COLOR_HOVER);
                    
                    return panelEstado;
                }
                
                return label;
            }
        }
        
        tableTemporadas.getColumnModel().getColumn(4).setCellRenderer(new EstadoRenderer());
        
        // Renderizador para el resto de columnas
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        defaultRenderer.setHorizontalAlignment(JLabel.CENTER);
        defaultRenderer.setBackground(COLOR_HOVER);
        defaultRenderer.setForeground(COLOR_TEXTO);

        for (int i = 0; i < tableTemporadas.getColumnCount(); i++) {
            if (i != 4) { // No aplicar al Renderer de Estado
                 tableTemporadas.getColumnModel().getColumn(i).setCellRenderer(defaultRenderer);
            }
        }
        tableTemporadas.getColumnModel().getColumn(1).setPreferredWidth(250); // Columna Nombre más ancha

        // 3. Panel de Scroll
        JScrollPane scrollPane = new JScrollPane(tableTemporadas);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE)); 
        scrollPane.getViewport().setBackground(COLOR_HOVER); 
        scrollPane.setOpaque(false);
        
        panelTemporada.add(scrollPane, BorderLayout.CENTER);

        return panelTemporada;
    }
    // ***************************************************************
    // ***************************************************************
    
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
                // System.err.println("Advertencia: No se pudo cargar el escudo en " + ruta + ". Usando placeholder.");
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
        scrollEquipos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollEquipos.setViewportBorder(null);
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
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al cargar la imagen seleccionada.", 
                    "Error de Imagen", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    // El resto de los métodos se mantienen igual (Partidos, Resultados, Helpers)

    private JComboBox<String> crearComboBox(Color colorFondo) {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setForeground(COLOR_TEXTO);
        combo.setBackground(colorFondo);
        
        // Estilo del renderizador para que se vea bien en el fondo oscuro
        combo.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                // Color del desplegable
                label.setBackground(isSelected || cellHasFocus ? COLOR_AZUL_SELECCION : colorFondo.darker().darker()); 
                label.setForeground(COLOR_TEXTO);
                return label;
            }
        });
        
        return combo;
    }
    
    private JPanel crearPanelResumenJornada(Color colorFondo, Color colorTexto) {
        JPanel panelJornadasContenido = new JPanel(new GridLayout(1, 3, 20, 0)); // 3 columnas
        panelJornadasContenido.setOpaque(false);
        
        String[] titulos = {"Partidos Jugados", "Goles Totales", "Máximo Goleador"};
        String[] valores = {"12 de 30", "670", "Juan Pérez (35)"};

        for (int i = 0; i < 3; i++) {
            JPanel tarjeta = new JPanel(new BorderLayout(0, 5));
            tarjeta.setBackground(colorFondo);
            tarjeta.setBorder(new EmptyBorder(20, 20, 20, 20));
            tarjeta.setPreferredSize(new Dimension(250, 100)); 

            JLabel lblValor = new JLabel(valores[i]);
            lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
            lblValor.setForeground(colorTexto);
            lblValor.setHorizontalAlignment(SwingConstants.LEFT);

            JLabel lblTitulo = new JLabel(titulos[i]);
            lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblTitulo.setForeground(new Color(161, 161, 170));
            lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);

            tarjeta.add(lblTitulo, BorderLayout.NORTH);
            tarjeta.add(lblValor, BorderLayout.CENTER);
            panelJornadasContenido.add(tarjeta);
        }

        return panelJornadasContenido;
    }

    private JPanel crearTarjetaPartido(Color colorFondo, Color colorTexto, String equipoLocal, String equipoVisitante, String estado, String resultado) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        tarjeta.setBackground(colorFondo);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));

        // Panel Central para equipos y resultado
        JPanel panelPartido = new JPanel(new GridBagLayout());
        panelPartido.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // 1. Equipo Local
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        panelPartido.add(crearEtiquetaEquipo(equipoLocal, SwingConstants.RIGHT, colorTexto, cargarEscudo(equipoLocal)));

        // 2. Resultado
        JLabel lblResultado = new JLabel(resultado);
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblResultado.setForeground(COLOR_AZUL_SELECCION);
        lblResultado.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        panelPartido.add(lblResultado, gbc);

        // 3. Equipo Visitante
        gbc.gridx = 2;
        gbc.weightx = 0.4;
        panelPartido.add(crearEtiquetaEquipo(equipoVisitante, SwingConstants.LEFT, colorTexto, cargarEscudo(equipoVisitante)));
        
        tarjeta.add(panelPartido, BorderLayout.CENTER);

        // Panel Lateral Izquierdo para el Estado
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setPreferredSize(new Dimension(100, 0));
        panelEstado.setOpaque(false);

        JLabel lblEstado = new JLabel(estado);
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
        lblEstado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        lblEstado.setOpaque(true);

        Color estadoColor;
        switch (estado) {
            case "TERMINADO":
                estadoColor = COLOR_TERMINADO; // Verde
                break;
            case "EN JUEGO":
                estadoColor = COLOR_EN_CURSO; // Amarillo
                lblEstado.setForeground(Color.BLACK); // Texto negro para mejor contraste
                break;
            case "POR JUGAR":
                estadoColor = COLOR_FUTURO; // Indigo
                break;
            default:
                estadoColor = Color.GRAY;
                break;
        }
        
        if (!"EN JUEGO".equals(estado)) {
             lblEstado.setForeground(Color.WHITE);
        }

        lblEstado.setBackground(estadoColor);
        panelEstado.add(lblEstado, BorderLayout.NORTH); // Alineado arriba

        tarjeta.add(panelEstado, BorderLayout.WEST);

        return tarjeta;
    }

    private JPanel crearEtiquetaEquipo(String nombre, int alineacion, Color colorTexto, ImageIcon icono) {
        JPanel panel = new JPanel(new FlowLayout(alineacion == SwingConstants.RIGHT ? FlowLayout.RIGHT : FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(colorTexto);
        
        JLabel lblIcono = new JLabel(icono);
        
        if (alineacion == SwingConstants.RIGHT) {
            panel.add(lblNombre);
            panel.add(lblIcono);
        } else {
            panel.add(lblIcono);
            panel.add(lblNombre);
        }
        
        return panel;
    }


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
        JPanel panelBodyJornadas = new JPanel();
        panelBodyJornadas.setLayout(new BoxLayout(panelBodyJornadas, BoxLayout.Y_AXIS));
        panelBodyJornadas.setOpaque(false);

        // Resumen de la Jornada
        JPanel panelResumen = crearPanelResumenJornada(COLOR_FONDO_OSCURO_ELEM, COLOR_TEXTO);
        panelBodyJornadas.add(panelResumen);
        panelBodyJornadas.add(Box.createRigidArea(new Dimension(0, 30)));
        
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
        
        panelBodyJornadas.add(scrollPartidos);
        
        panelJornadas.add(panelBodyJornadas, BorderLayout.CENTER);

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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }

	public void despuesDelLogin(int nivel, String nombre) {
		// TODO Auto-generated method stub
		
	}

}
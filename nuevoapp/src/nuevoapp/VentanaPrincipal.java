package nuevoapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
  Clase principal que gestiona la interfaz del sistema de la Federación de Balonmano.
  Implementa ActionListener para capturar los clics en los botones.
 */
public class VentanaPrincipal extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    
    // --- COMPONENTES DE LA INTERFAZ ---
    private JPanel panelContenido; // Panel central que cambia (contenedor)
    private CardLayout layoutTarjetas; // Administrador para intercambiar paneles
    private JButton botonInicio;
    private JButton botonTemporada;
    private JButton botonEquipos;
    private JButton botonPartidos;
    private JButton botonResultados;
    private JButton botonCerrarSesion;
    
    
    // --- CONSTANTES PARA EL CardLayout ---
    // Estas etiquetas sirven para identificar qué pantalla queremos mostrar
    private static final String TARJETA_INICIO = "inicio";
    private static final String TARJETA_TEMPORADAS = "temporadas";
    private static final String TARJETA_EQUIPOS = "equipos";
    private static final String TARJETA_PARTIDOS = "partidos";
    private static final String TARJETA_RESULTADOS = "resultados";
    
    // --- PALETA DE COLORES (Estilo visual coherente) ---
    private static final Color COLOR_FONDO = new Color(240, 240, 240);
    private static final Color COLOR_MENU = new Color(45, 45, 45); // Gris oscuro
    private static final Color COLOR_AZUL = new Color(70, 130, 220); // Azul principal
    private static final Color COLOR_BLANCO = Color.WHITE;
    private static final Color COLOR_TEXTO = new Color(50, 50, 50);
    private static final Color COLOR_VERDE = new Color(60, 180, 75);
    private static final Color COLOR_AMARILLO = new Color(255, 200, 50);
    private static final Color COLOR_NARANJA = new Color(255, 150, 50);

    // CONSTRUCTOR: Configura la ventana y lanza la construcción de la UI.

    public VentanaPrincipal() {
        // Carga del icono de la aplicación
        ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
        setIconImage(icono.getImage());
        
        setTitle("Federación de Balonmano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null); // Centra la ventana en pantalla
        
        // Layout principal de la ventana
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);
        
        // Inicialización de componentes
        crearMenuLateral();
        crearPanelContenido();
        
        // Mostrar la pantalla de inicio por defecto
        mostrarTarjeta(TARJETA_INICIO);
        actualizarBotonSeleccionado(botonInicio);
    }

    //Crea la barra lateral de navegación con los botones del menú.
  
    private void crearMenuLateral() {
        JPanel panelLateral = new JPanel(new BorderLayout());
        panelLateral.setPreferredSize(new Dimension(250, 0));
        panelLateral.setBackground(COLOR_MENU);
        panelLateral.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Título del menú
        JLabel titulo = new JLabel("Balonmano");
        titulo.setForeground(COLOR_BLANCO);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(new EmptyBorder(10, 5, 30, 5));

        // Panel para los botones de navegación (GridLayout de 5 filas)
        JPanel panelMenu = new JPanel(new GridLayout(5, 1, 0, 15));
        panelMenu.setBackground(COLOR_MENU);

        // Crear instancias de los botones usando el método helper
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

        // Agrupar título y botones en la parte superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_MENU);
        panelSuperior.add(titulo, BorderLayout.NORTH);
        panelSuperior.add(panelMenu, BorderLayout.CENTER);

        // Configuración especial del botón cerrar sesión (en la parte inferior)
        botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.setPreferredSize(new Dimension(220, 40));
        botonCerrarSesion.setBackground(new Color(0, 0, 128)); // Azul oscuro
        botonCerrarSesion.setForeground(COLOR_BLANCO);
        botonCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 14));
        botonCerrarSesion.setFocusPainted(false);
        botonCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonCerrarSesion.addActionListener(this);

        panelLateral.add(panelSuperior, BorderLayout.NORTH);
        panelLateral.add(botonCerrarSesion, BorderLayout.SOUTH);
        getContentPane().add(panelLateral, BorderLayout.WEST);
    }

    // Método helper para crear botones de menú con un estilo uniforme.

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(220, 45));
        boton.setFont(new Font("Arial", Font.PLAIN, 15));
        boton.setForeground(COLOR_BLANCO);
        boton.setBackground(COLOR_MENU);
        boton.setFocusPainted(false);
        boton.setBorder(null); // Quita el borde por defecto
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addActionListener(this); // Escucha los clics
        return boton;
    }

    // Crea el contenedor central que utiliza CardLayout para intercambiar vistas.
    private void crearPanelContenido() {
        layoutTarjetas = new CardLayout();
        panelContenido = new JPanel(layoutTarjetas);
        panelContenido.setBackground(new Color(240, 240, 240));

        // Añadir cada panel funcional con su etiqueta correspondiente
        panelContenido.add(crearPanelInicio(), TARJETA_INICIO);
        panelContenido.add(crearPanelTemporadas(), TARJETA_TEMPORADAS);
        panelContenido.add(crearPanelEquipos(), TARJETA_EQUIPOS);
        panelContenido.add(crearPanelPartidos(), TARJETA_PARTIDOS);
        panelContenido.add(crearPanelResultados(), TARJETA_RESULTADOS);

        getContentPane().add(panelContenido, BorderLayout.CENTER);
    }

    // Función que cambia la vista actual en el panel central.
    private void mostrarTarjeta(String tarjeta) {
        layoutTarjetas.show(panelContenido, tarjeta);
    }

    // Cambia el color del botón pulsado para resaltar la sección activa.

    private void actualizarBotonSeleccionado(JButton activo) {
        JButton[] botones = {botonInicio, botonTemporada, botonEquipos, botonPartidos, botonResultados};
        for (JButton btn : botones) {
            // Si es el botón activo, se pone azul; si no, vuelve al color del menú
            btn.setBackground(btn == activo ? COLOR_AZUL : COLOR_MENU);
        }
    }

    // CONTROLADOR DE EVENTOS: Define qué pasa al pulsar cada botón.

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // Lógica de navegación entre paneles
        if (source == botonInicio) {
            mostrarTarjeta(TARJETA_INICIO);
            actualizarBotonSeleccionado(botonInicio);
        } else if (source == botonTemporada) {
            mostrarTarjeta(TARJETA_TEMPORADAS);
            actualizarBotonSeleccionado(botonTemporada);
        } else if (source == botonEquipos) {
            mostrarTarjeta(TARJETA_EQUIPOS);
            actualizarBotonSeleccionado(botonEquipos);
        } else if (source == botonPartidos) {
            mostrarTarjeta(TARJETA_PARTIDOS);
            actualizarBotonSeleccionado(botonPartidos);
        } else if (source == botonResultados) {
            mostrarTarjeta(TARJETA_RESULTADOS);
            actualizarBotonSeleccionado(botonResultados);
        } 
        // Lógica de Cierre de Sesión
        else if (source == botonCerrarSesion) {
            int respuesta = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que deseas cerrar sesión?", "Confirmar Salida", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                this.dispose(); // Cierra y libera la ventana actual
                
                // Abre la ventana de Login de nuevo
                SwingUtilities.invokeLater(() -> {
                    new Login().setVisible(true); 
                });
            }
        }
    }

    //Vista de Inicio: Muestra el título y la imagen principal.

    private JPanel crearPanelInicio() {
        JPanel panelTituloInicio = new JPanel(new BorderLayout());
        panelTituloInicio.setBackground(new Color(35, 31, 124)); // Color corporativo
        panelTituloInicio.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lbltituloInicio = new JLabel("FEDERACIÓN ESPAÑOLA DE BALONMANO", SwingConstants.CENTER);
        lbltituloInicio.setFont(new Font("Arial", Font.BOLD, 32));
        lbltituloInicio.setForeground(COLOR_TEXTO);
        lbltituloInicio.setBorder(new EmptyBorder(20, 0, 30, 0));

        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/assets/BALON.jpg"));
            Image img = icono.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImagen.setText("Imagen no disponible");
        }

        panelTituloInicio.add(lbltituloInicio, BorderLayout.NORTH);
        panelTituloInicio.add(lblImagen, BorderLayout.CENTER);
        return panelTituloInicio;
    }

    // Vista de Temporadas: Muestra una tabla con el histórico de temporadas.

    private JPanel crearPanelTemporadas() {
        JPanel panelTemporada = new JPanel(new BorderLayout());
        panelTemporada.setBackground(Color.BLACK);
        panelTemporada.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Cabecera con título y botón de acción
        JPanel panelCabeceraTemporada = new JPanel(new BorderLayout());
        panelCabeceraTemporada.setBackground(new Color(240, 240, 240));
        panelCabeceraTemporada.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTituloTemporada = new JLabel("Gestión de Temporadas");
        lblTituloTemporada.setFont(new Font("Arial", Font.BOLD, 24));
        
        JButton btnCrearTemporada = new JButton("+ Nueva Temporada");
        btnCrearTemporada.setBackground(COLOR_AZUL);
        btnCrearTemporada.setForeground(COLOR_BLANCO);

        panelCabeceraTemporada.add(lblTituloTemporada, BorderLayout.WEST);
        panelCabeceraTemporada.add(btnCrearTemporada, BorderLayout.EAST);

        // Configuración de la tabla de datos
        String[] columnas = {"ID", "Temporada", "Inicio", "Fin", "Estado"};
        Object[][] datos = {
            {"1", "2025/2026", "01/09/2025", "30/06/2026", "Futuro"},
            {"2", "2024/2025", "01/09/2024", "30/06/2025", "En Curso"}
        };

        JTable tablaTemporada = new JTable(new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        });
        
        panelTemporada.add(panelCabeceraTemporada, BorderLayout.NORTH);
        panelTemporada.add(new JScrollPane(tablaTemporada), BorderLayout.CENTER);
        return panelTemporada;
    }

    // Vista de Equipos: Crea una lista de tarjetas visuales para cada equipo.
  
    private JPanel crearPanelEquipos() {
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(COLOR_FONDO);
        panelTitulo.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblTituloEquipos = new JLabel("Equipos Registrados");
        lblTituloEquipos.setFont(new Font("Arial", Font.BOLD, 24));

        // Panel de scroll que contiene la lista de equipos
        JPanel contenedorEquipo = new JPanel(new GridLayout(0, 1, 0, 12));
        contenedorEquipo.setBackground(COLOR_FONDO);

        String[] equipos = {"Barcelona", "Athletic Club", "Granada", "Sevilla", "Zaragoza", "Valencia"};
        for (String eq : equipos) {
            contenedorEquipo.add(crearTarjetaEquipo(eq));
        }

        panelTitulo.add(lblTituloEquipos, BorderLayout.NORTH);
        panelTitulo.add(new JScrollPane(contenedorEquipo), BorderLayout.CENTER);
        return panelTitulo;
    }

    // Genera un panel individual (tarjeta) para representar a un equipo.
 
    private JPanel crearTarjetaEquipo(String nombre) {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 0));
        tarjeta.setBackground(COLOR_BLANCO);
        tarjeta.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel escudo = new JLabel(cargarEscudo(nombre));
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBotones.setBackground(COLOR_BLANCO);

        JButton btnEscudo = new JButton("Cambiar Escudo");
        btnEscudo.addActionListener(e -> cambiarEscudo(escudo, nombre));

        JButton btnGestionar = new JButton("Gestionar");
        btnGestionar.setBackground(COLOR_AZUL);
        btnGestionar.setForeground(COLOR_BLANCO);

        panelBotones.add(btnEscudo);
        panelBotones.add(btnGestionar);

        tarjeta.add(escudo, BorderLayout.WEST);
        tarjeta.add(lblNombre, BorderLayout.CENTER);
        tarjeta.add(panelBotones, BorderLayout.EAST);
        return tarjeta;
    }

    // Vista de Partidos: Incluye filtros por temporada y jornada.

    private JPanel crearPanelPartidos() {
        JPanel panelJornada = new JPanel(new BorderLayout());
        panelJornada.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Filtros superiores
        JPanel filtrosJornada = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filtrosJornada.add(new JLabel("Temporada:"));
        filtrosJornada.add(new JComboBox<>(new String[]{"2024/2025"}));
        filtrosJornada.add(new JLabel("Jornada:"));
        JComboBox<String> jornadas = new JComboBox<>();
        for (int i = 1; i <= 30; i++) jornadas.addItem("Jornada " + i);
        filtrosJornada.add(jornadas);

        // Lista de partidos estática (ejemplo)
        JPanel panelPartidos = new JPanel(new GridLayout(0, 1, 0, 12));
        panelPartidos.add(crearPartido("Barcelona", "Athletic Club", "TERMINADO", "30 - 25"));
        panelPartidos.add(crearPartido("Granada", "Sevilla", "EN JUEGO", "15 - 12"));

        panelJornada.add(filtrosJornada, BorderLayout.NORTH);
        panelJornada.add(new JScrollPane(panelPartidos), BorderLayout.CENTER);
        return panelJornada;
    }

    // Genera la fila visual de un partido con colores según su estado.
 
    private JPanel crearPartido(String local, String visitante, String estado, String resultado) {
        JPanel paneltarjeta = new JPanel(new BorderLayout());
        paneltarjeta.setBackground(COLOR_BLANCO);
        paneltarjeta.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblEstado = new JLabel(estado);
        lblEstado.setOpaque(true);
        lblEstado.setForeground(COLOR_BLANCO);
        
        // Lógica de colores según estado
        if (estado.equals("TERMINADO")) lblEstado.setBackground(COLOR_VERDE);
        else if (estado.equals("EN JUEGO")) lblEstado.setBackground(COLOR_AMARILLO);
        else lblEstado.setBackground(COLOR_NARANJA);

        JLabel info = new JLabel(local + "  " + resultado + "  " + visitante, SwingConstants.CENTER);
        info.setFont(new Font("Arial", Font.BOLD, 18));

        paneltarjeta.add(lblEstado, BorderLayout.WEST);
        paneltarjeta.add(info, BorderLayout.CENTER);
        return paneltarjeta;
    }

    // Vista de Clasificación: Muestra la tabla de puntos.
    
    private JPanel crearPanelResultados() {
        JPanel panelClasificaciongeneral = new JPanel(new BorderLayout());
        panelClasificaciongeneral.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblTituloClasificacion = new JLabel("Clasificación General");
        lblTituloClasificacion.setFont(new Font("Arial", Font.BOLD, 24));

        String[] columnas = {"Pos", "Equipo", "PJ", "Pts"};
        Object[][] datos = { {"1", "Athletic Club", "14", "37"}, {"2", "Barcelona", "14", "31"} };

        JTable tablaClasificacion = new JTable(new DefaultTableModel(datos, columnas));
        
        panelClasificaciongeneral.add(lblTituloClasificacion, BorderLayout.NORTH);
        panelClasificaciongeneral.add(new JScrollPane(tablaClasificacion), BorderLayout.CENTER);
        return panelClasificaciongeneral;
    }

    // Busca y escala el icono del escudo de un equipo.
 
    private ImageIcon cargarEscudo(String equipo) {
        String ruta = "/assets/escudos/" + equipo.toLowerCase().replaceAll("\\s+", "") + ".png";
        try {
            Image img = new ImageIcon(getClass().getResource(ruta)).getImage();
            return new ImageIcon(img.getScaledInstance(35, 35, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return new ImageIcon(); // Devuelve vacío si no existe
        }
    }

// Abre un selector de archivos para que el usuario suba una nueva imagen de escudo.
  
    private void cambiarEscudo(JLabel etiqueta, String equipo) {
        JFileChooser selector = new JFileChooser();
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png"));

        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = selector.getSelectedFile();
            try {
                Image img = new ImageIcon(archivo.getAbsolutePath()).getImage();
                etiqueta.setIcon(new ImageIcon(img.getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar imagen");
            }
        }
    }

    // Punto de entrada principal para ejecutar la ventana.
 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    		void despuesDelLogin(int nivel, String nombre) {
		// TODO Auto-generated method stub
		
	}
}
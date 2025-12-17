package nuevoapp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class ventanaPrincipalM extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableTemporada;
    private JTable tableClasificacion;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ventanaPrincipalM frame = new ventanaPrincipalM();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ventanaPrincipalM() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 450); // Aumentamos ancho para el menú lateral
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        
        // 1. CAMBIO: El panelLateral ahora va al WEST (Izquierda)
        contentPane.setLayout(new BorderLayout(0, 0));
        
        JPanel panelLateral = new JPanel();
        panelLateral.setBorder(new EmptyBorder(30, 20, 30, 20));
        panelLateral.setBackground(new Color(32, 30, 26));
        panelLateral.setPreferredSize(new Dimension(150, 0)); // Damos un ancho fijo al lateral
        contentPane.add(panelLateral, BorderLayout.WEST); 
        
        // 2. CAMBIO: Para que panelSuperior e inferior se apilen verticalmente
        panelLateral.setLayout(new BorderLayout(0, 0));
        
        // El panelSuperior ahora contendrá los botones uno abajo de otro
        JPanel panelSuperior = new JPanel();
        panelSuperior.setOpaque(false); // Para que se vea el fondo oscuro
        panelLateral.add(panelSuperior, BorderLayout.NORTH);
        
        // 3. CAMBIO: GridLayout en el panelSuperior para que los botones sean verticales
        panelSuperior.setLayout(new GridLayout(0, 1, 0, 10)); // 0 filas, 1 columna, 10px espacio
        
        JPanel panelLogo = new JPanel();
        panelSuperior.add(panelLogo);
        
        JLabel lblIcono = new JLabel("");
        panelLogo.add(lblIcono);
        
        JButton btnInicio = new JButton("Inicio");
        panelSuperior.add(btnInicio);
        
        JButton btnTemporada = new JButton("Temporada");
        panelSuperior.add(btnTemporada);
        
        JButton btnEquipos = new JButton("Equipos");
        panelSuperior.add(btnEquipos);
        
        JButton btnPartido = new JButton("Partido");
        panelSuperior.add(btnPartido);
        
        JButton btnResultado = new JButton("Resultado");
        panelSuperior.add(btnResultado);
        
        JPanel panelInferior = new JPanel();
        panelLateral.add(panelInferior, BorderLayout.SOUTH);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesion");
        panelInferior.add(btnCerrarSesion);
        
        // El panelPrincipal ahora debe ir en el CENTER para que no tape al lateral
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.DARK_GRAY);
        contentPane.add(panelPrincipal, BorderLayout.CENTER);
        panelPrincipal.setLayout(new CardLayout(0, 0));
        
        JPanel panelInicio = new JPanel();
        panelInicio.setBorder(new EmptyBorder(40, 40, 40, 40));
        panelInicio.setBackground(new Color(32, 30, 26));
        panelPrincipal.add(panelInicio, "name_15757962057300");
        panelInicio.setLayout(new BorderLayout(0, 0));
        
        JPanel panelTituloInicio = new JPanel();
        panelInicio.add(panelTituloInicio, BorderLayout.NORTH);
        
        JLabel lblTituloInicio = new JLabel("REAL FEDERACIÓN ESPAÑOLA DE BALONMANO");
        panelTituloInicio.add(lblTituloInicio);
        
        JPanel panelImagen = new JPanel();
        panelInicio.add(panelImagen, BorderLayout.SOUTH);
        
        JLabel lblImagenLogo = new JLabel("");
        panelImagen.add(lblImagenLogo);
        
        JPanel panelTemporada = new JPanel();
        panelTemporada.setBorder(new EmptyBorder(40, 40, 40, 40));
        panelTemporada.setBackground(new Color(32, 30, 26));
        panelPrincipal.add(panelTemporada, "name_15766188249200");
        panelTemporada.setLayout(new BorderLayout(0, 0));
        
        JPanel panelHeaderTemporada = new JPanel();
        panelHeaderTemporada.setBorder(new EmptyBorder(0, 0, 30, 0));
        panelTemporada.add(panelHeaderTemporada, BorderLayout.NORTH);
        panelHeaderTemporada.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTituloTemporada = new JLabel("Gestión de Temporada");
        panelHeaderTemporada.add(lblTituloTemporada, BorderLayout.WEST);
        
        JButton btnCrearTemporada = new JButton("Crear Temporada");
        panelHeaderTemporada.add(btnCrearTemporada, BorderLayout.SOUTH);
        
        JScrollPane scrollPaneTemporada = new JScrollPane();
        panelTemporada.add(scrollPaneTemporada, BorderLayout.SOUTH);
        
        tableTemporada = new JTable();
        panelTemporada.add(tableTemporada, BorderLayout.WEST);
        
        JPanel panelEquipos = new JPanel();
        panelEquipos.setBorder(new EmptyBorder(40, 40, 40, 40));
        panelEquipos.setBackground(new Color(32, 30, 26));
        panelPrincipal.add(panelEquipos, "name_15786419638200");
        panelEquipos.setLayout(new BorderLayout(0, 0));
        
        JPanel panelBodyEquipo = new JPanel();
        panelEquipos.add(panelBodyEquipo, BorderLayout.NORTH);
        
        JPanel panelContenidoEquipo = new JPanel();
        panelBodyEquipo.add(panelContenidoEquipo);
        
        JScrollPane scrollPaneEquipo = new JScrollPane();
        panelEquipos.add(scrollPaneEquipo, BorderLayout.SOUTH);
        
        JPanel panelContenedor = new JPanel();
        panelEquipos.add(panelContenedor, BorderLayout.WEST);
        
        JPanel panelJornadas = new JPanel();
        panelJornadas.setBorder(new EmptyBorder(40, 40, 40, 40));
        panelJornadas.setBackground(new Color(32, 30, 26));
        panelPrincipal.add(panelJornadas, "name_15788872179200");
        panelJornadas.setLayout(new BorderLayout(0, 0));
        
        JPanel panelHeader = new JPanel();
        panelJornadas.add(panelHeader, BorderLayout.NORTH);
        
        JLabel lblTituloJornada = new JLabel("Partidos");
        panelHeader.add(lblTituloJornada);
        
        JPanel panelFiltrosTemporada = new JPanel();
        panelHeader.add(panelFiltrosTemporada);
        
        JLabel lblTemporada = new JLabel("Temporada");
        panelFiltrosTemporada.add(lblTemporada);
        
        JComboBox comboBoxJornadaTemporada = new JComboBox();
        comboBoxJornadaTemporada.setForeground(new Color(255, 255, 255));
        comboBoxJornadaTemporada.setBackground(new Color(52, 52, 37));
        panelFiltrosTemporada.add(comboBoxJornadaTemporada);
        
        JLabel lblJornada = new JLabel("Jornada:");
        panelFiltrosTemporada.add(lblJornada);
        
        JPanel panelBodyJornada = new JPanel();
        panelJornadas.add(panelBodyJornada, BorderLayout.SOUTH);
        
        JPanel panelJornadaContenido = new JPanel();
        panelBodyJornada.add(panelJornadaContenido);
        panelJornadaContenido.setLayout(new GridLayout(1, 0, 0, 0));
        
        JPanel panelContenedorPartido = new JPanel();
        panelBodyJornada.add(panelContenedorPartido);
        
        JPanel panelTarjeta = new JPanel();
        panelTarjeta.setBorder(new CompoundBorder(new LineBorder(new Color(52, 52, 37), 1, true), new EmptyBorder(15, 20, 15, 20)));
        panelTarjeta.setBackground(new Color(52, 52, 37));
        panelContenedorPartido.add(panelTarjeta);
        
        JPanel panelPartido = new JPanel();
        panelTarjeta.add(panelPartido);
        panelPartido.setLayout(new GridLayout(1, 0, 0, 0));
        
        JPanel panel = new JPanel();
        panel.setForeground(new Color(0, 0, 0));
        panel.setBackground(new Color(255, 255, 255));
        panelPartido.add(panel);
        
        JLabel lblResultado = new JLabel("30 - 25");
        panelPartido.add(lblResultado);
        
        JPanel panelEstado = new JPanel();
        panelTarjeta.add(panelEstado);
        
        JLabel lblEstado = new JLabel("Terminado");
        panelEstado.add(lblEstado);
        
        JScrollPane scrollPanelPartido = new JScrollPane();
        panelBodyJornada.add(scrollPanelPartido);
        
        JPanel panelClasificacion = new JPanel();
        panelClasificacion.setBorder(new EmptyBorder(40, 40, 40, 40));
        panelClasificacion.setBackground(new Color(32, 30, 26));
        panelPrincipal.add(panelClasificacion, "name_15793208010200");
        
        JPanel panelHeaderClasificacion = new JPanel();
        panelHeaderClasificacion.setBackground(new Color(255, 255, 255));
        panelHeaderClasificacion.setForeground(new Color(0, 0, 0));
        panelHeaderClasificacion.setBorder(new EmptyBorder(0, 0, 30, 0));
        panelClasificacion.add(panelHeaderClasificacion);
        
        JLabel lblTituloClasificacion = new JLabel("Clasificacion");
        panelHeaderClasificacion.add(lblTituloClasificacion);
        
        JComboBox comboBoxTemporada = new JComboBox();
        comboBoxTemporada.setForeground(new Color(255, 255, 255));
        panelHeaderClasificacion.add(comboBoxTemporada);
        
        JScrollPane scrollPane = new JScrollPane();
        panelClasificacion.add(scrollPane);
        
        tableClasificacion = new JTable();
        panelClasificacion.add(tableClasificacion);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Acciones de los botones
    }
}
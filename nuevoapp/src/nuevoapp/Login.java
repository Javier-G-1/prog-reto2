package nuevoapp;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import gestion.Rol;
import gestion.DatosFederacion;
import gestion.Usuario;
import logica.GestorArchivos;
import logica.GestorLog;

/**
 * Clase Login.
 * 
 * Representa la ventana de inicio de sesión del sistema de la 
 * Real Federación Española de Balonmano. Permite al usuario autenticarse
 * con un usuario registrado o entrar como invitado.
 * 
 * Incluye validación de campos, interacción con la base de datos de usuarios,
 * inicialización de usuarios predeterminados y registro de eventos.
 * 
 * Implementa ActionListener para la gestión de eventos de botones y campos
 * y FocusListener para seleccionar automáticamente el contenido de los campos al enfocar.
 * 

 */
public class Login extends JFrame implements ActionListener, FocusListener {

    private static final long serialVersionUID = 1L;

    /** Panel principal del JFrame */
    private JPanel contentPane;

    /** Campo de texto para el nombre de usuario */
    private JTextField txtUsuario;

    /** Campo de contraseña */
    private JPasswordField pwdContra;

    /** Botón para iniciar sesión */
    private JButton btnIniciarSesion;

    /** Botón para entrar como invitado */
    private JButton btnInvitado;

    /** Etiqueta del logo */
    private JLabel lblLogo;

    /** Etiqueta de usuario */
    private JLabel lblUsuario;

    /** Etiqueta de contraseña */
    private JLabel lblContra;

    /** Panel del logo */
    private JPanel logoPanel;

    /** Panel principal de sesión */
    private JPanel panelSesion;

    /** Panel del campo usuario */
    private JPanel panelUsuario;

    /** Panel del campo contraseña */
    private JPanel panelContra;

    /** Panel de los botones de acción */
    private JPanel panelBotones;

    /** Ventana principal que se abre después del login */
    private static VentanaMain newVentanaPrincipal;

    /** Datos de la federación cargados desde archivos */
    private DatosFederacion datosFederacion;

    /**
     * Método principal para iniciar la aplicación.
     * Crea una instancia de Login y la hace visible.
     * 
     * @param args Argumentos de línea de comandos
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
     * Constructor de la clase Login.
     * Inicializa los componentes de la interfaz, carga los datos de la
     * federación y establece los usuarios predeterminados si es necesario.
     */
    public Login() {
        // Cargar datos del sistema
        this.datosFederacion = GestorArchivos.cargarTodo();
        inicializarUsuariosPredeterminados();
        
        // Configuración de la ventana
        setResizable(false);
        ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
        setIconImage(icono.getImage());
        setTitle("Real Federación Española de Balonmano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(800, 150, 616, 450); 
        
        // Configuración del panel principal
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Panel de logo
        logoPanel = new JPanel();
        logoPanel.setBackground(new Color(0, 0, 64)); 
        contentPane.add(logoPanel, BorderLayout.NORTH);
        
        lblLogo = new JLabel();
        try {
            lblLogo.setIcon(new ImageIcon(new ImageIcon(Login.class.getResource("/assets/icono.png"))
                    .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            lblLogo.setText("LOGO BALONMANO");
            lblLogo.setForeground(Color.WHITE);
        }
        logoPanel.add(lblLogo);

        // Panel de sesión
        panelSesion = new JPanel();
        panelSesion.setBackground(new Color(0, 0, 51)); 
        panelSesion.setLayout(new BoxLayout(panelSesion, BoxLayout.Y_AXIS));
        contentPane.add(panelSesion, BorderLayout.CENTER);

        panelSesion.add(Box.createVerticalStrut(20));

        JLabel lblIniciarSesion = new JLabel("Iniciar Sesión");
        lblIniciarSesion.setFont(new Font("Agency FB", Font.BOLD, 40));
        lblIniciarSesion.setForeground(Color.WHITE);
        lblIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblIniciarSesion.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE)); 
        panelSesion.add(lblIniciarSesion);

        panelSesion.add(Box.createVerticalStrut(30));

        // Panel usuario
        panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelUsuario.setOpaque(false);
        lblUsuario = new JLabel("Usuario: ");
        lblUsuario.setForeground(Color.WHITE);
        txtUsuario = new JTextField(15);
        txtUsuario.addFocusListener(this);
        panelUsuario.add(lblUsuario);
        panelUsuario.add(txtUsuario);
        panelSesion.add(panelUsuario);

        // Panel contraseña
        panelContra = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelContra.setOpaque(false);
        lblContra = new JLabel("Contraseña: ");
        lblContra.setForeground(Color.WHITE);
        pwdContra = new JPasswordField(15);
        pwdContra.addFocusListener(this);
        pwdContra.addActionListener(this); 
        panelContra.add(lblContra);
        panelContra.add(pwdContra);
        panelSesion.add(panelContra);

        // Panel botones
        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        
        btnIniciarSesion = new JButton("Entrar");
        btnIniciarSesion.addActionListener(this);
        btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnInvitado = new JButton("Invitado");
        btnInvitado.addActionListener(this);
        btnInvitado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panelBotones.add(btnIniciarSesion);
        panelBotones.add(btnInvitado);
        panelSesion.add(panelBotones);
    }
    
    /**
     * Inicializa los usuarios predeterminados si no existen en la base de datos.
     * Usuarios: admin, invitado, árbitro y manager.
     */
    private void inicializarUsuariosPredeterminados() {
        if (datosFederacion.buscarUsuario("admin") == null) {
            datosFederacion.getListaUsuarios().add(
                new Usuario("Administrador", "admin", "123", Rol.ADMINISTRADOR)
            );
        }
        if (datosFederacion.buscarUsuario("invitado") == null) {
            datosFederacion.getListaUsuarios().add(
                new Usuario("Usuario Invitado", "invitado", "123", Rol.INVITADO)
            );
        }
        if (datosFederacion.buscarUsuario("arbitro") == null) {
            datosFederacion.getListaUsuarios().add(
                new Usuario("Árbitro Principal", "arbitro", "123", Rol.ARBITRO)
            );
        }
        if (datosFederacion.buscarUsuario("manager") == null) {
            datosFederacion.getListaUsuarios().add(
                new Usuario("Manager Principal", "manager", "123", Rol.MANAGER)
            );
        }
        GestorArchivos.guardarTodo(datosFederacion);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object obj = ae.getSource();

        if (obj == btnInvitado) {
            Usuario usuarioInvitado = datosFederacion.buscarUsuario("invitado");
            if (usuarioInvitado != null) {
                ejecutarLogin(usuarioInvitado);
            }
        } else if (obj == btnIniciarSesion || obj == pwdContra) {
            validarAcceso(); 
        }
    }

    /**
     * Valida los datos ingresados por el usuario y realiza el login si son correctos.
     * Muestra mensajes de error en caso de campos vacíos o credenciales incorrectas.
     */
    private void validarAcceso() {
        String userText = txtUsuario.getText().trim();
        char[] passText = pwdContra.getPassword();
        
        if (userText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (passText.length == 0) {
            JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario usuario = datosFederacion.buscarUsuario(userText);
        if (usuario != null && usuario.getContrasena().equals(new String(passText))) {
            GestorLog.info("Login exitoso: " + usuario.getNombreUsuario() + " | Rol: " + usuario.getRol().getNombreLegible());
            ejecutarLogin(usuario);
            return;
        }
        
        GestorLog.advertencia("Intento de login fallido: " + userText);
        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrecta", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        pwdContra.setText("");
        txtUsuario.requestFocus();
    }

    /**
     * Ejecuta el login mostrando la ventana principal y pasando el rol y nombre del usuario.
     * 
     * @param usuario Usuario que ha iniciado sesión correctamente
     */
    private void ejecutarLogin(Usuario usuario) {
        this.dispose(); 
        
        if (newVentanaPrincipal == null) {
            newVentanaPrincipal = new VentanaMain(datosFederacion);
        }
        newVentanaPrincipal.despuesDelLogin(usuario.getRol(), usuario.getNombreReal());
        newVentanaPrincipal.setVisible(true);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            ((JTextField) e.getSource()).selectAll();
        } else if (e.getSource() instanceof JPasswordField) {
            ((JPasswordField) e.getSource()).selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {}
}

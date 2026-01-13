package nuevoapp;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.Arrays;
import gestion.Rol;
import gestion.DatosFederacion;
import gestion.Usuario;
import logica.GestorArchivos;
import logica.GestorLog;

public class Login extends JFrame implements ActionListener, FocusListener {

    private static final long serialVersionUID = 1L;
    
    private JPanel contentPane;
    private JTextField txtUsuario;
    private JPasswordField pwdContra;
    private JButton btnIniciarSesion;
    private JButton btnInvitado;
    private JLabel lblLogo, lblUsuario, lblContra;
    private JPanel logoPanel, panelSesion, panelUsuario, panelContra, panelBotones;
    
    private static newVentanaPrincipal newVentanaPrincipal;
    
    // ⭐ CAMBIO IMPORTANTE: Ahora cargamos los datos reales del sistema
    private DatosFederacion datosFederacion;

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

    public Login() {
        // ⭐ Cargar datos del sistema
        this.datosFederacion = GestorArchivos.cargarTodo();
        
        // Inicializar usuarios predeterminados si es primera ejecución
        inicializarUsuariosPredeterminados();
        
        setResizable(false);
        ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
        setIconImage(icono.getImage());
        setTitle("Real Federación Española de Balonmano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(800, 150, 616, 450); 
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

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

        panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelUsuario.setOpaque(false);
        lblUsuario = new JLabel("Usuario: ");
        lblUsuario.setForeground(Color.WHITE);
        txtUsuario = new JTextField(15);
        txtUsuario.addFocusListener(this);
        panelUsuario.add(lblUsuario);
        panelUsuario.add(txtUsuario);
        panelSesion.add(panelUsuario);

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
     * ⭐ NUEVO: Inicializa los 4 usuarios predeterminados si no existen
     */
    private void inicializarUsuariosPredeterminados() {
        // Admin
        if (datosFederacion.buscarUsuario("admin") == null) {
            datosFederacion.getListaUsuarios().add(
                new Usuario("Administrador", "admin", "123", Rol.ADMINISTRADOR)
            );
        }
        
        // Invitado
        if (datosFederacion.buscarUsuario("invitado") == null) {
            datosFederacion.getListaUsuarios().add(
                new Usuario("Usuario Invitado", "invitado", "123", Rol.INVITADO)
            );
        }
        
        // Árbitro
        if (datosFederacion.buscarUsuario("arbitro") == null) {
            datosFederacion.getListaUsuarios().add(
                new Usuario("Árbitro Principal", "arbitro", "123", Rol.ARBITRO)
            );
        }
        
        // Manager
        if (datosFederacion.buscarUsuario("manager") == null) {
            datosFederacion.getListaUsuarios().add(
                new Usuario("Manager Principal", "manager", "123", Rol.MANAGER)
            );
        }
        
        // Guardar los cambios
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
     * ⭐ ACTUALIZADO: Validación contra la base de datos real
     */
    private void validarAcceso() {
        String userText = txtUsuario.getText().trim();
        char[] passText = pwdContra.getPassword();
        
        if (userText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El nombre de usuario no puede estar vacío", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (passText.length == 0) {
            JOptionPane.showMessageDialog(this, 
                "La contraseña no puede estar vacía", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Buscar usuario en la base de datos
        Usuario usuario = datosFederacion.buscarUsuario(userText);
        
        if (usuario != null) {
            // Verificar contraseña
            String passwordIngresada = new String(passText);
            
            if (usuario.getContrasena().equals(passwordIngresada)) {
                // Login exitoso
                GestorLog.info("Login exitoso: " + usuario.getNombreUsuario() + " | Rol: " + usuario.getRol().getNombreLegible());
                ejecutarLogin(usuario);
                return;
            }
        }
        
        // Login fallido
        GestorLog.advertencia("Intento de login fallido: " + userText);
        JOptionPane.showMessageDialog(this, 
            "Usuario o contraseña incorrecta", 
            "Error de Autenticación", 
            JOptionPane.ERROR_MESSAGE);
        
        // Limpiar campos
        pwdContra.setText("");
        txtUsuario.requestFocus();
    }

    /**
     * ⭐ ACTUALIZADO: Recibe el objeto Usuario completo
     */
    private void ejecutarLogin(Usuario usuario) {
        this.dispose(); 
        
        if (newVentanaPrincipal == null) {
            newVentanaPrincipal = new newVentanaPrincipal(datosFederacion);
        }
        
        // Pasar los datos del usuario a la ventana principal
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
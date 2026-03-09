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
 * Clase Login con opción de registro de nuevos usuarios.
 */
public class Login extends JFrame implements ActionListener, FocusListener {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField txtUsuario;
    private JPasswordField pwdContra;
    private JButton btnIniciarSesion;
    private JButton btnInvitado;
    private JButton btnRegistrarse;
    private JLabel lblLogo;
    private JLabel lblUsuario;
    private JLabel lblContra;
    private JPanel logoPanel;
    private JPanel panelSesion;
    private JPanel panelUsuario;
    private JPanel panelContra;
    private JPanel panelBotones;
    private static VentanaMain newVentanaPrincipal;
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
        this.datosFederacion = GestorArchivos.cargarTodo();
        inicializarUsuariosPredeterminados();
        
        setResizable(false);
        ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
        setIconImage(icono.getImage());
        setTitle("Real Federación Española de Balonmano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(800, 150, 616, 480); 
        
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

        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setOpaque(false);
        
        btnIniciarSesion = new JButton("Entrar");
        btnIniciarSesion.addActionListener(this);
        btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.addActionListener(this);
        btnRegistrarse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrarse.setBackground(new Color(52, 152, 219));
        btnRegistrarse.setForeground(Color.WHITE);
        
        btnInvitado = new JButton("Invitado");
        btnInvitado.addActionListener(this);
        btnInvitado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panelBotones.add(btnIniciarSesion);
        panelBotones.add(btnRegistrarse);
        panelBotones.add(btnInvitado);
        panelSesion.add(panelBotones);
    }
    
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
        } else if (obj == btnRegistrarse) {
            mostrarDialogoRegistro();
        } else if (obj == btnIniciarSesion || obj == pwdContra) {
            validarAcceso(); 
        }
    }
    
    /**
     * Muestra el diálogo de registro de nuevo usuario
     */
    private void mostrarDialogoRegistro() {
        JDialog dialogoRegistro = new JDialog(this, "Registro de Usuario", true);
        dialogoRegistro.setSize(400, 300);
        dialogoRegistro.setLocationRelativeTo(this);
        dialogoRegistro.setLayout(new BorderLayout(10, 10));
        
        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 10, 10));
        panelCampos.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel lblNombreReal = new JLabel("Nombre completo:");
        JTextField txtNombreReal = new JTextField();
        
        JLabel lblNuevoUsuario = new JLabel("Nombre de usuario:");
        JTextField txtNuevoUsuario = new JTextField();
        
        JLabel lblNuevaContra = new JLabel("Contraseña:");
        JPasswordField txtNuevaContra = new JPasswordField();
        
        JLabel lblConfirmarContra = new JLabel("Confirmar contraseña:");
        JPasswordField txtConfirmarContra = new JPasswordField();
        
        panelCampos.add(lblNombreReal);
        panelCampos.add(txtNombreReal);
        panelCampos.add(lblNuevoUsuario);
        panelCampos.add(txtNuevoUsuario);
        panelCampos.add(lblNuevaContra);
        panelCampos.add(txtNuevaContra);
        panelCampos.add(lblConfirmarContra);
        panelCampos.add(txtConfirmarContra);
        
        dialogoRegistro.add(panelCampos, BorderLayout.CENTER);
        
        JPanel panelBotonesRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnConfirmar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnConfirmar.addActionListener(e -> {
            String nombreReal = txtNombreReal.getText().trim();
            String nuevoUsuario = txtNuevoUsuario.getText().trim();
            String nuevaContra = new String(txtNuevaContra.getPassword());
            String confirmarContra = new String(txtConfirmarContra.getPassword());
            
            // Validaciones
            if (nombreReal.isEmpty() || nuevoUsuario.isEmpty() || nuevaContra.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoRegistro, 
                    "Todos los campos son obligatorios", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!nuevaContra.equals(confirmarContra)) {
                JOptionPane.showMessageDialog(dialogoRegistro, 
                    "Las contraseñas no coinciden", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (datosFederacion.buscarUsuario(nuevoUsuario) != null) {
                JOptionPane.showMessageDialog(dialogoRegistro, 
                    "El nombre de usuario ya existe", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Crear usuario con rol INVITADO por defecto
            Usuario nuevoUser = new Usuario(nombreReal, nuevoUsuario, nuevaContra, Rol.INVITADO);
            datosFederacion.getListaUsuarios().add(nuevoUser);
            GestorArchivos.guardarTodo(datosFederacion);
            
            GestorLog.exito("Nuevo usuario registrado: " + nuevoUsuario + " (" + nombreReal + ")");
            
            JOptionPane.showMessageDialog(dialogoRegistro, 
                "¡Usuario registrado con éxito!\n\n" +
                "Usuario: " + nuevoUsuario + "\n" +
                "Rol inicial: Invitado\n\n" +
                "El administrador podrá cambiar tu rol si es necesario.", 
                "Registro exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dialogoRegistro.dispose();
        });
        
        btnCancelar.addActionListener(e -> dialogoRegistro.dispose());
        
        panelBotonesRegistro.add(btnConfirmar);
        panelBotonesRegistro.add(btnCancelar);
        dialogoRegistro.add(panelBotonesRegistro, BorderLayout.SOUTH);
        
        dialogoRegistro.setVisible(true);
    }

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
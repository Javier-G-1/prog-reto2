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
        setTitle("Real Federación Española de Waterpolo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(600, 100, 820, 520);

        // Colores del tema oscuro marino
        Color navyOscuro  = new Color(0x01, 0x1E, 0x38);
        Color navyMedio   = new Color(0x02, 0x2F, 0x58);
        Color azulCampo   = new Color(0x01, 0x48, 0x7A);
        Color textoSub    = new Color(0x8C, 0xC8, 0xF0);

        contentPane = new JPanel();
        contentPane.setBackground(navyOscuro);
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // ===== PANEL IZQUIERDO — MARCA =====
        logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setBackground(navyOscuro);
        logoPanel.setPreferredSize(new Dimension(280, 0));
        logoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0x02, 0x5A, 0x90)));
        contentPane.add(logoPanel, BorderLayout.WEST);

        JPanel panelMarca = new JPanel();
        panelMarca.setOpaque(false);
        panelMarca.setLayout(new BoxLayout(panelMarca, BoxLayout.Y_AXIS));

        lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            lblLogo.setIcon(new ImageIcon(new ImageIcon(Login.class.getResource("/assets/icono.png"))
                    .getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            lblLogo.setText("WATERPOLO");
            lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblLogo.setForeground(vista.Paleta.PRIMARIO);
        }
        panelMarca.add(lblLogo);
        panelMarca.add(Box.createVerticalStrut(18));

        JLabel lblNombreApp = new JLabel("Aquora");
        lblNombreApp.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblNombreApp.setForeground(vista.Paleta.PRIMARIO);
        lblNombreApp.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMarca.add(lblNombreApp);

        JLabel lblDeporte = new JLabel("Waterpolo");
        lblDeporte.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDeporte.setForeground(textoSub);
        lblDeporte.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMarca.add(lblDeporte);

        panelMarca.add(Box.createVerticalStrut(12));

        JLabel lblFed = new JLabel("Real Federación Española");
        lblFed.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblFed.setForeground(new Color(0x50, 0x90, 0xB8));
        lblFed.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMarca.add(lblFed);

        logoPanel.add(panelMarca);

        // ===== PANEL DERECHO — FORMULARIO =====
        panelSesion = new JPanel();
        panelSesion.setBackground(navyMedio);
        panelSesion.setLayout(new BoxLayout(panelSesion, BoxLayout.Y_AXIS));
        contentPane.add(panelSesion, BorderLayout.CENTER);

        panelSesion.add(Box.createVerticalStrut(60));

        JLabel lblIniciarSesion = new JLabel("Iniciar Sesión");
        lblIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblIniciarSesion.setForeground(Color.WHITE);
        lblIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSesion.add(lblIniciarSesion);

        panelSesion.add(Box.createVerticalStrut(6));

        JLabel lblSubtexto = new JLabel("Introduce tus credenciales de acceso");
        lblSubtexto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtexto.setForeground(textoSub);
        lblSubtexto.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSesion.add(lblSubtexto);

        panelSesion.add(Box.createVerticalStrut(36));

        panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        panelUsuario.setOpaque(false);
        lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsuario.setForeground(textoSub);
        lblUsuario.setPreferredSize(new Dimension(78, 22));
        txtUsuario = new JTextField(16);
        txtUsuario.setBackground(azulCampo);
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x02, 0x6A, 0xA0), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        txtUsuario.addFocusListener(this);
        panelUsuario.add(lblUsuario);
        panelUsuario.add(txtUsuario);
        panelSesion.add(panelUsuario);

        panelSesion.add(Box.createVerticalStrut(12));

        panelContra = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        panelContra.setOpaque(false);
        lblContra = new JLabel("Contraseña");
        lblContra.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblContra.setForeground(textoSub);
        lblContra.setPreferredSize(new Dimension(78, 22));
        pwdContra = new JPasswordField(16);
        pwdContra.setBackground(azulCampo);
        pwdContra.setForeground(Color.WHITE);
        pwdContra.setCaretColor(Color.WHITE);
        pwdContra.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x02, 0x6A, 0xA0), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        pwdContra.addFocusListener(this);
        pwdContra.addActionListener(this);
        panelContra.add(lblContra);
        panelContra.add(pwdContra);
        panelSesion.add(panelContra);

        panelSesion.add(Box.createVerticalStrut(28));

        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        panelBotones.setOpaque(false);

        btnIniciarSesion = new JButton("Entrar");
        btnIniciarSesion.addActionListener(this);
        btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIniciarSesion.setBackground(vista.Paleta.PRIMARIO);
        btnIniciarSesion.setForeground(new Color(0x01, 0x1A, 0x2E));
        btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));

        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.addActionListener(this);
        btnRegistrarse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrarse.setBackground(vista.Paleta.NEUTRO1);
        btnRegistrarse.setForeground(Color.WHITE);
        btnRegistrarse.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegistrarse.setFocusPainted(false);
        btnRegistrarse.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));

        btnInvitado = new JButton("Invitado");
        btnInvitado.addActionListener(this);
        btnInvitado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInvitado.setBackground(vista.Paleta.ACENTO);
        btnInvitado.setForeground(Color.WHITE);
        btnInvitado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnInvitado.setFocusPainted(false);
        btnInvitado.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));

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
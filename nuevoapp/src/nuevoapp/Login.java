package nuevoapp; 

import java.awt.*; 
import javax.swing.*; 
import javax.swing.border.*; 
import java.awt.event.*; 
import java.util.Arrays;
// IMPORTANTE: Asegúrate de importar tu Enum si está en otro paquete
import gestion.Rol; 

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

    // --- MATRIZ DE USUARIOS ---
    private String usuarios[][] = {
        {"admin", "123"},   
        {"arbitro", "123"}, 
        {"usuario", "123"}, 
        {"manager", "123"}  
    };  
    
    // CAMBIO AQUÍ: Ahora usamos los valores de tu Enum Rol en el mismo orden que la matriz
    private Rol roles[] = {
        Rol.ADMINISTRADOR, 
        Rol.ARBITRO, 
        Rol.INVITADO, 
        Rol.MANAGER
    }; 

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

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object obj = ae.getSource();

        if (obj == btnInvitado) {
            // CAMBIO AQUÍ: Usamos Rol.INVITADO directamente
            ejecutarLogin(Rol.INVITADO, "Invitado"); 
        } else if (obj == btnIniciarSesion || obj == pwdContra) {
            validarAcceso(); 
        }
    }

    private void validarAcceso() {
        String userText = txtUsuario.getText();
        char[] passText = pwdContra.getPassword(); 
        boolean loginCorrecto = false;

        for (int i = 0; i < usuarios.length; i++) {
            if (userText.equals(usuarios[i][0])) {
                if (Arrays.equals(passText, usuarios[i][1].toCharArray())) {
                    loginCorrecto = true;
                    // CAMBIO AQUÍ: Pasamos el objeto Rol de nuestro array de roles
                    ejecutarLogin(roles[i], userText); 
                    break;
                }
            }
        }

        if (!loginCorrecto) {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // CAMBIO AQUÍ: El primer parámetro ahora es de tipo Rol
    private void ejecutarLogin(Rol rol, String nombre) {
        this.dispose(); 
        if (newVentanaPrincipal == null) {
            newVentanaPrincipal = new newVentanaPrincipal();
        }
        // Pasamos el objeto Enum a la ventana principal
        newVentanaPrincipal.despuesDelLogin(rol, nombre);
        newVentanaPrincipal.setVisible(true);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            ((JTextField) e.getSource()).selectAll();
        }
    }

    @Override public void focusLost(FocusEvent e) {}
}
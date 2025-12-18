package nuevoapp; 

import java.awt.*; 
import javax.swing.*; 
import javax.swing.border.*; 
import java.awt.event.*; 
import java.util.Arrays; //comparar las contraseñas correctamente

public class Login extends JFrame implements ActionListener, FocusListener {

	private static final long serialVersionUID = 1L;
	
	// --- VARIABLES DE LA INTERFAZ ---
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField pwdContra;
	private JButton btnIniciarSesion;
	private JButton btnInvitado;
	private JLabel lblLogo, lblUsuario, lblContra;
	private JPanel logoPanel, panelSesion, panelUsuario, panelContra, panelBotones;
	
	// Variable para conectar con la siguiente ventana
	private static VentanaPrincipal ventanaPrincipal;

	// --- PRIVILEGIOS ---
	private String usuarios[][] = {
			 {"admin", "123"},    //es admin (Índice 0)
       {"arbitro", "123"},  //es arbitro (Índice 1)
       {"usuario", "123"},  //es usuario (Índice 2)
       {"manager", "123"}   //es manager (Índice 3)
	};	
	
	// Niveles de poder correspondientes a cada fila de arriba
	private int idPrivilegios[] = {1, 2, 0, 3}; 

	/**
	 * 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login(); // Crea la ventana
					frame.setVisible(true);    // La muestra
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * CONSTRUCTOR
	 */
	public Login() {
		// Configuración básica del marco (título, tamaño y cierre)
		setResizable(false);
		 ImageIcon icono = new ImageIcon(getClass().getResource("/assets/icono.png"));
         setIconImage(icono.getImage());
		setTitle("Real Federación Española de Balonmano");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(800, 150, 616, 450); 
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new BorderLayout(0, 0)); // Divide la ventana en Norte, Sur, Centro...
		setContentPane(contentPane);

		// --- PARTE SUPERIOR (NORTE): EL LOGO ---
		logoPanel = new JPanel();
		logoPanel.setBackground(new Color(0, 0, 64)); // Azul marino
		contentPane.add(logoPanel, BorderLayout.NORTH);
		
		lblLogo = new JLabel();
		try {
			// Intenta cargar la imagen. Si la ruta está mal, salta al 'catch'
			lblLogo.setIcon(new ImageIcon(new ImageIcon(Login.class.getResource("/assets/icono.png"))
					.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		} catch (Exception e) {
			// Si no hay imagen, pone un texto para que el programa no se rompa 
			lblLogo.setText("LOGO BALONMANO");
			lblLogo.setForeground(Color.WHITE);
		}
		logoPanel.add(lblLogo);

		// --- CUERPO CENTRAL: FORMULARIO ---
		panelSesion = new JPanel();
		panelSesion.setBackground(new Color(0, 0, 51)); // Azul más oscuro
		// BoxLayout apila los elementos de arriba a abajo
		panelSesion.setLayout(new BoxLayout(panelSesion, BoxLayout.Y_AXIS));
		contentPane.add(panelSesion, BorderLayout.CENTER);

		panelSesion.add(Box.createVerticalStrut(20)); // Espacio en blanco vertical

		// Título del formulario
		JLabel lblIniciarSesion = new JLabel("Iniciar Sesión");
		lblIniciarSesion.setFont(new Font("Agency FB", Font.BOLD, 40));
		lblIniciarSesion.setForeground(Color.WHITE);
		lblIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el texto
		lblIniciarSesion.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE)); // Subrraya
		panelSesion.add(lblIniciarSesion);

		panelSesion.add(Box.createVerticalStrut(30));

		// --- FILA DE USUARIO ---
		panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Alinea icono y caja de lado
		panelUsuario.setOpaque(false); // Para que se vea el azul de fondo
		lblUsuario = new JLabel("Usuario: ");
		lblUsuario.setForeground(Color.WHITE);												
		txtUsuario = new JTextField(15);
		txtUsuario.addFocusListener(this); // Detecta cuando entras con el ratón
		panelUsuario.add(lblUsuario);
		panelUsuario.add(txtUsuario);
		panelSesion.add(panelUsuario);

		// --- FILA DE CONTRASEÑA ---
		panelContra = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelContra.setOpaque(false);
		lblContra = new JLabel("Contraseña: ");
		lblContra.setForeground(Color.WHITE);
		pwdContra = new JPasswordField(15); // Oculta lo que escribes
		pwdContra.addFocusListener(this);
		pwdContra.addActionListener(this); // Detecta la tecla 'Enter'
		panelContra.add(lblContra);
		panelContra.add(pwdContra);
		panelSesion.add(panelContra);

		// --- FILA DE BOTONES ---
		panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panelBotones.setOpaque(false);
		
		btnIniciarSesion = new JButton("Entrar");
		btnIniciarSesion.addActionListener(this); // Detecta el clic
		btnIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cambia el cursor
		
		btnInvitado = new JButton("Invitado");
		btnInvitado.addActionListener(this);
		btnInvitado.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		panelBotones.add(btnIniciarSesion);
		panelBotones.add(btnInvitado);
		panelSesion.add(panelBotones);
	}

	/**
	 * Boton Invitado, Nivel 0 automatico 
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();

		if (obj == btnInvitado) {
			ejecutarLogin(0, "Invitado"); // Nivel 0 para invitados
		} else if (obj == btnIniciarSesion || obj == pwdContra) {
			validarAcceso(); // Lógica de comprobación
		}
	}

	/**
	 * Comprueba si los datos escritos están en la matriz
	 */
	private void validarAcceso() {
		String userText = txtUsuario.getText();
		char[] passText = pwdContra.getPassword(); // Obtiene la clave como array de caracteres 
		boolean loginCorrecto = false;

		// Bucle que recorre la matriz de usuarios
		for (int i = 0; i < usuarios.length; i++) {
			// ¿Coincide el nombre?
			if (userText.equals(usuarios[i][0])) {
				// ¿Coincide la contraseña?
				if (Arrays.equals(passText, usuarios[i][1].toCharArray())) {
					loginCorrecto = true;
					ejecutarLogin(idPrivilegios[i], userText); // Pasa nivel de poder y nombre
					break;
				}
			}
		}

		// Si el bucle termina y no hay éxito: error
		if (!loginCorrecto) {
			JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Cierra el login y abre la ventana principal
	 */
	private void ejecutarLogin(int nivel, String nombre) {
		this.dispose(); // Cierra la actual
		if (ventanaPrincipal == null) {
			ventanaPrincipal = new VentanaPrincipal();
		}
		// Envía los datos a la VentanaPrincipal para configurar los permisos
		ventanaPrincipal.despuesDelLogin(nivel, nombre);
		ventanaPrincipal.setVisible(true);
	}

	/**
	 * Cuando el usuario hace clic en una caja de texto, se selecciona todo automáticamente
	 */
	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() instanceof JTextField) {
			((JTextField) e.getSource()).selectAll();
		}
	}

	@Override public void focusLost(FocusEvent e) {}
}
package nuevoapp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.List;
import gestion.*;
import logica.GestorLog;

/**
 * PanelGestionUsuarios.
 * 
 * Panel de administración de usuarios del sistema.
 * Permite:
 * - Crear nuevos usuarios
 * - Cambiar roles de usuarios existentes
 * - Cambiar contraseña
 * - Eliminar usuarios
 * 
 * Algunos usuarios están protegidos y no se pueden eliminar ni modificar
 * su rol (ej: admin, invitado, árbitro, manager).
 * 
 * La información de usuarios se obtiene de un objeto DatosFederacion.
 * La tabla muestra todos los usuarios con sus roles y estado protegido.
 * 
 * Renderiza colores diferentes en la tabla según el rol.
 * Registra acciones de administración en GestorLog.
 * 
 */
public class PanelGestionUsuarios extends JPanel {
    
    private static final long serialVersionUID = 1L;
    /** Datos de la federación que contienen usuarios */
    private DatosFederacion datosFederacion;
    
    /** Tabla principal de usuarios */
    private JTable tablaUsuarios;
    
    /** Modelo de la tabla */
    private DefaultTableModel modeloTabla;
    
    /** Botones de acción */
    private JButton btnCrearUsuario;
    private JButton btnEditarRol;
    private JButton btnEliminarUsuario;
    private JButton btnCambiarPassword;
    
    /** Label que muestra total de usuarios */
    private JLabel lblTotalUsuarios;
    
    /** Lista de usuarios protegidos */
    private static final String[] USUARIOS_PROTEGIDOS = {
        "admin", "invitado", "arbitro", "manager"
    };
    
    /**
     * Constructor del panel de gestión de usuarios
     * @param datos DatosFederacion que contiene la lista de usuarios
     */
    public PanelGestionUsuarios(DatosFederacion datos) {
        this.datosFederacion = datos;
        
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(20, 24, 31));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        inicializarUsuariosPredeterminados();
        crearInterfaz();
        cargarUsuarios();
    }
    
    /**
     * Inicializa los usuarios predeterminados (admin, invitado, árbitro, manager)
     * si no existen en la lista de usuarios
     */
    private void inicializarUsuariosPredeterminados() {
        List<Usuario> usuarios = datosFederacion.getListaUsuarios();
        
        // Admin
        if (datosFederacion.buscarUsuario("admin") == null) {
            usuarios.add(new Usuario("Administrador", "admin", "123", Rol.ADMINISTRADOR));
        }
        
        // Invitado
        if (datosFederacion.buscarUsuario("invitado") == null) {
            usuarios.add(new Usuario("Usuario Invitado", "invitado", "123", Rol.INVITADO));
        }
        
        // Árbitro
        if (datosFederacion.buscarUsuario("arbitro") == null) {
            usuarios.add(new Usuario("Árbitro Principal", "arbitro", "123", Rol.ARBITRO));
        }
        
        // Manager
        if (datosFederacion.buscarUsuario("manager") == null) {
            usuarios.add(new Usuario("Manager Principal", "manager", "123", Rol.MANAGER));
        }
    }
    
    
    /**
     * Crea la interfaz visual del panel
     * - Panel superior con título y total de usuarios
     * - Tabla de usuarios con renderizado por rol
     * - Panel de botones de acción
     */
    private void crearInterfaz() {
        // ===== PANEL SUPERIOR =====
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Gestión de Usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        lblTotalUsuarios = new JLabel();
        lblTotalUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalUsuarios.setForeground(new Color(180, 180, 180));
        panelSuperior.add(lblTotalUsuarios, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // ===== TABLA DE USUARIOS =====
        String[] columnas = {"Usuario", "Nombre Real", "Rol", "Protegido"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable directamente
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setBackground(new Color(30, 34, 41));
        tablaUsuarios.setForeground(Color.WHITE);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(35);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.setGridColor(new Color(60, 60, 80));
        
        // Estilo del encabezado
        JTableHeader header = tablaUsuarios.getTableHeader();
        header.setBackground(new Color(45, 55, 140));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Renderizador personalizado para colores según rol
        tablaUsuarios.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String rol = (String) table.getValueAt(row, 2);
                    
                    if (rol.equals("Administrador")) {
                        c.setBackground(new Color(52, 73, 94));
                    } else if (rol.equals("Manager")) {
                        c.setBackground(new Color(41, 128, 185));
                    } else if (rol.equals("Árbitro")) {
                        c.setBackground(new Color(243, 156, 18));
                    } else if (rol.equals("Invitado")) {
                        c.setBackground(new Color(149, 165, 166));
                    } else {
                        c.setBackground(new Color(30, 34, 41));
                    }
                } else {
                    c.setBackground(new Color(45, 55, 140));
                }
                
                c.setForeground(Color.WHITE);
                setHorizontalAlignment(CENTER);
                
                return c;
            }
        });
        
        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);
        scrollTabla.setBackground(new Color(30, 34, 41));
        scrollTabla.getViewport().setBackground(new Color(30, 34, 41));
        add(scrollTabla, BorderLayout.CENTER);
        
        // ===== PANEL DE BOTONES =====
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setOpaque(false);
        
        btnCrearUsuario = crearBoton(" Crear Usuario", new Color(39, 174, 96));
        btnEditarRol = crearBoton(" Cambiar Rol", new Color(52, 152, 219));
        btnCambiarPassword = crearBoton(" Cambiar Contraseña", new Color(241, 196, 15));
        btnEliminarUsuario = crearBoton("🗑 Eliminar", new Color(231, 76, 60));
        
        btnCrearUsuario.addActionListener(e -> crearNuevoUsuario());
        btnEditarRol.addActionListener(e -> cambiarRolUsuario());
        btnCambiarPassword.addActionListener(e -> cambiarPasswordUsuario());
        btnEliminarUsuario.addActionListener(e -> eliminarUsuario());
        
        panelBotones.add(btnCrearUsuario);
        panelBotones.add(btnEditarRol);
        panelBotones.add(btnCambiarPassword);
        panelBotones.add(btnEliminarUsuario);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    /**
     * Crea un botón con estilo personalizado
     * @param texto Texto del botón
     * @param color Color de fondo principal
     * @return JButton con estilo aplicado
     */
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    /**
     * Carga todos los usuarios en la tabla
     */
    public void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        
        List<Usuario> usuarios = datosFederacion.getListaUsuarios();
        
        for (Usuario u : usuarios) {
            boolean esProtegido = esUsuarioProtegido(u.getNombreUsuario());
            
            modeloTabla.addRow(new Object[]{
                u.getNombreUsuario(),
                u.getNombreReal(),
                u.getRol().getNombreLegible(),
                esProtegido ? "✓ Sí" : "✗ No"
            });
        }
        
        lblTotalUsuarios.setText("Total: " + usuarios.size() + " usuarios");
        GestorLog.info("Usuarios cargados en tabla: " + usuarios.size());
    }
    
    /**
     * Comprueba si un usuario está protegido
     * @param nombreUsuario Nombre del usuario
     * @return true si está protegido
     */
    private boolean esUsuarioProtegido(String nombreUsuario) {
        for (String protegido : USUARIOS_PROTEGIDOS) {
            if (protegido.equalsIgnoreCase(nombreUsuario)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Muestra el diálogo para crear un nuevo usuario
     */
    private void crearNuevoUsuario() {
        DialogoCrearUsuario dialogo = new DialogoCrearUsuario((JFrame) SwingUtilities.getWindowAncestor(this));
        dialogo.setVisible(true);
        
        if (dialogo.isAceptado()) {
            String nombreUsuario = dialogo.getNombreUsuario();
            String nombreReal = dialogo.getNombreReal();
            String password = dialogo.getPassword();
            Rol rol = dialogo.getRol();
            
            // Validar que no existe ya
            if (datosFederacion.buscarUsuario(nombreUsuario) != null) {
                JOptionPane.showMessageDialog(this,
                    "Ya existe un usuario con ese nombre de usuario",
                    "Usuario duplicado",
                    JOptionPane.WARNING_MESSAGE);
                GestorLog.advertencia("Intento de crear usuario duplicado: " + nombreUsuario);
                return;
            }
            
            // Crear usuario
            Usuario nuevoUsuario = new Usuario(nombreReal, nombreUsuario, password, rol);
            datosFederacion.getListaUsuarios().add(nuevoUsuario);
            
            cargarUsuarios();
            GestorLog.exito("Usuario creado: " + nombreUsuario + " | Rol: " + rol.getNombreLegible());
            
            JOptionPane.showMessageDialog(this,
                "Usuario '" + nombreUsuario + "' creado con éxito",
                "Usuario creado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Cambia el rol del usuario seleccionado
     */
    private void cambiarRolUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un usuario de la tabla",
                "Sin selección",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Usuario usuario = datosFederacion.buscarUsuario(nombreUsuario);
        
        if (usuario == null) return;
        
        // Validar usuarios protegidos
        if (esUsuarioProtegido(nombreUsuario)) {
            JOptionPane.showMessageDialog(this,
                "No se puede cambiar el rol de usuarios protegidos del sistema",
                "Usuario protegido",
                JOptionPane.WARNING_MESSAGE);
            GestorLog.advertencia("Intento de cambiar rol de usuario protegido: " + nombreUsuario);
            return;
        }
        
        // Mostrar diálogo de selección de rol
        Rol[] rolesDisponibles = {Rol.INVITADO, Rol.ARBITRO, Rol.MANAGER};
        String[] nombresRoles = new String[rolesDisponibles.length];
        
        for (int i = 0; i < rolesDisponibles.length; i++) {
            nombresRoles[i] = rolesDisponibles[i].getNombreLegible();
        }
        
        String rolSeleccionado = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona el nuevo rol para: " + usuario.getNombreReal(),
            "Cambiar Rol",
            JOptionPane.QUESTION_MESSAGE,
            null,
            nombresRoles,
            usuario.getRol().getNombreLegible()
        );
        
        if (rolSeleccionado == null) return;
        
        // Buscar el Rol correspondiente
        Rol nuevoRol = null;
        for (Rol r : rolesDisponibles) {
            if (r.getNombreLegible().equals(rolSeleccionado)) {
                nuevoRol = r;
                break;
            }
        }
        
        if (nuevoRol != null && nuevoRol != usuario.getRol()) {
            Rol rolAnterior = usuario.getRol();
            usuario.setRol(nuevoRol);
            
            cargarUsuarios();
            GestorLog.exito("Rol actualizado: " + nombreUsuario + 
                          " | " + rolAnterior.getNombreLegible() + " → " + nuevoRol.getNombreLegible());
            
            JOptionPane.showMessageDialog(this,
                "Rol de '" + nombreUsuario + "' actualizado a: " + nuevoRol.getNombreLegible(),
                "Rol actualizado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Cambia la contraseña del usuario seleccionado
     */
    private void cambiarPasswordUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un usuario de la tabla",
                "Sin selección",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Usuario usuario = datosFederacion.buscarUsuario(nombreUsuario);
        
        if (usuario == null) return;
        
        JPasswordField passField = new JPasswordField(15);
        JPasswordField passConfirm = new JPasswordField(15);
        
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.add(new JLabel("Usuario: " + usuario.getNombreReal()));
        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(passField);
        panel.add(new JLabel("Confirmar contraseña:"));
        panel.add(passConfirm);
        
        int result = JOptionPane.showConfirmDialog(this, panel,
            "Cambiar Contraseña", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String pass1 = new String(passField.getPassword());
            String pass2 = new String(passConfirm.getPassword());
            
            if (pass1.isEmpty() || pass2.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "La contraseña no puede estar vacía",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(this,
                    "Las contraseñas no coinciden",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            usuario.setContrasena(pass1);
            GestorLog.exito("Contraseña actualizada para: " + nombreUsuario);
            
            JOptionPane.showMessageDialog(this,
                "Contraseña actualizada correctamente",
                "Contraseña cambiada",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Elimina el usuario seleccionado
     */
    private void eliminarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona un usuario de la tabla",
                "Sin selección",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        Usuario usuario = datosFederacion.buscarUsuario(nombreUsuario);
        
        if (usuario == null) return;
        
        // Validar usuarios protegidos
        if (esUsuarioProtegido(nombreUsuario)) {
            JOptionPane.showMessageDialog(this,
                "No se pueden eliminar los usuarios protegidos del sistema:\n" +
                "admin, invitado, arbitro, manager",
                "Usuario protegido",
                JOptionPane.WARNING_MESSAGE);
            GestorLog.advertencia("Intento de eliminar usuario protegido: " + nombreUsuario);
            return;
        }
        
        // Confirmación
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de eliminar al usuario?\n\n" +
            "Usuario: " + usuario.getNombreReal() + "\n" +
            "Login: " + nombreUsuario + "\n" +
            "Rol: " + usuario.getRol().getNombreLegible() + "\n\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            datosFederacion.getListaUsuarios().remove(usuario);
            
            cargarUsuarios();
            GestorLog.exito("Usuario eliminado: " + nombreUsuario + " | Rol: " + usuario.getRol().getNombreLegible());
            
            JOptionPane.showMessageDialog(this,
                "Usuario '" + nombreUsuario + "' eliminado correctamente",
                "Usuario eliminado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

/**
 * Diálogo modal para crear un nuevo usuario
 */
class DialogoCrearUsuario extends JDialog {
    
    private boolean aceptado = false;
    private JTextField txtNombreUsuario;
    private JTextField txtNombreReal;
    private JPasswordField txtPassword;
    private JPasswordField txtPasswordConfirm;
    private JComboBox<String> comboRol;
    
    
    /**
     * Constructor del diálogo
     * @param parent JFrame padre
     */
    public DialogoCrearUsuario(JFrame parent) {
        super(parent, "Crear Nuevo Usuario", true);
        
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Nombre de usuario (login):"));
        txtNombreUsuario = new JTextField();
        panel.add(txtNombreUsuario);
        
        panel.add(new JLabel("Nombre real:"));
        txtNombreReal = new JTextField();
        panel.add(txtNombreReal);
        
        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        
        panel.add(new JLabel("Confirmar contraseña:"));
        txtPasswordConfirm = new JPasswordField();
        panel.add(txtPasswordConfirm);
        
        panel.add(new JLabel("Rol:"));
        comboRol = new JComboBox<>(new String[]{
            "Invitado", "Árbitro", "Manager"
        });
        panel.add(comboRol);
        
        JButton btnAceptar = new JButton("Crear");
        btnAceptar.addActionListener(e -> {
            if (validar()) {
                aceptado = true;
                dispose();
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        
        panel.add(btnAceptar);
        panel.add(btnCancelar);
        
        add(panel);
    }
    
    
    /**
     * Valida los campos ingresados por el usuario
     * @return true si son válidos
     */
    private boolean validar() {
        if (txtNombreUsuario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario no puede estar vacío");
            return false;
        }
        
        if (txtNombreReal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre real no puede estar vacío");
            return false;
        }
        
        String pass1 = new String(txtPassword.getPassword());
        String pass2 = new String(txtPasswordConfirm.getPassword());
        
        if (pass1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía");
            return false;
        }
        
        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
            return false;
        }
        
        return true;
    }
    
    /** @return true si el usuario presionó Aceptar */
    public boolean isAceptado() { return aceptado; }
    
    /** @return Nombre de usuario ingresado */
    public String getNombreUsuario() { return txtNombreUsuario.getText().trim(); }
    
    /** @return Nombre real ingresado */
    public String getNombreReal() { return txtNombreReal.getText().trim(); }
    
    /** @return Contraseña ingresada */
    public String getPassword() { return new String(txtPassword.getPassword()); }
    
    /** @return Rol seleccionado */
    public Rol getRol() {
        String seleccion = (String) comboRol.getSelectedItem();
        switch (seleccion) {
            case "Árbitro": return Rol.ARBITRO;
            case "Manager": return Rol.MANAGER;
            default: return Rol.INVITADO;
        }
    }
}
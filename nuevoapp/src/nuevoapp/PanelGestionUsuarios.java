package nuevoapp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import java.util.List;
import gestion.*;
import logica.GestorLog;
import logica.GestorArchivos;

/**
 * PanelGestionUsuarios - Panel completo de administración de usuarios.
 * 
 * Permite al ADMINISTRADOR:
 * - Ver todos los usuarios registrados (incluyendo los que se registran desde el login)
 * - Cambiar roles de usuarios existentes
 * - Cambiar contraseñas
 * - Eliminar usuarios (excepto protegidos)
 * - Crear nuevos usuarios manualmente
 * 
 * Los usuarios protegidos (admin, invitado, árbitro, manager) no pueden ser eliminados
 * ni se puede cambiar su rol, pero sí su contraseña.
 */
public class PanelGestionUsuarios extends JPanel {
    
    private static final long serialVersionUID = 1L;
    
    private DatosFederacion datosFederacion;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    
    private JButton btnCrearUsuario;
    private JButton btnEditarRol;
    private JButton btnEliminarUsuario;
    private JButton btnCambiarPassword;
    private JButton btnRefrescar;
    
    private JLabel lblTotalUsuarios;
    
    private static final String[] USUARIOS_PROTEGIDOS = {
        "admin", "invitado", "arbitro", "manager"
    };
    
    public PanelGestionUsuarios(DatosFederacion datos) {
        this.datosFederacion = datos;
        
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(20, 24, 31));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        inicializarUsuariosPredeterminados();
        crearInterfaz();
        cargarUsuarios();
    }
    
    private void inicializarUsuariosPredeterminados() {
        List<Usuario> usuarios = datosFederacion.getListaUsuarios();
        
        if (datosFederacion.buscarUsuario("admin") == null) {
            usuarios.add(new Usuario("Administrador", "admin", "123", Rol.ADMINISTRADOR));
        }
        if (datosFederacion.buscarUsuario("invitado") == null) {
            usuarios.add(new Usuario("Usuario Invitado", "invitado", "123", Rol.INVITADO));
        }
        if (datosFederacion.buscarUsuario("arbitro") == null) {
            usuarios.add(new Usuario("Árbitro Principal", "arbitro", "123", Rol.ARBITRO));
        }
        if (datosFederacion.buscarUsuario("manager") == null) {
            usuarios.add(new Usuario("Manager Principal", "manager", "123", Rol.MANAGER));
        }
    }
    
    private void crearInterfaz() {
        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("👥 Gestión de Usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInfo.setOpaque(false);
        
        lblTotalUsuarios = new JLabel();
        lblTotalUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTotalUsuarios.setForeground(new Color(180, 180, 180));
        
        btnRefrescar = new JButton("🔄 Refrescar");
        btnRefrescar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefrescar.setBackground(new Color(52, 152, 219));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setBorderPainted(false);
        btnRefrescar.addActionListener(e -> cargarUsuarios());
        
        panelInfo.add(lblTotalUsuarios);
        panelInfo.add(Box.createHorizontalStrut(15));
        panelInfo.add(btnRefrescar);
        panelSuperior.add(panelInfo, BorderLayout.EAST);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de usuarios
        String[] columnas = {"Usuario", "Nombre Real", "Rol", "Protegido"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setBackground(new Color(30, 34, 41));
        tablaUsuarios.setForeground(Color.WHITE);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaUsuarios.setRowHeight(35);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.setGridColor(new Color(60, 60, 80));
        
        JTableHeader header = tablaUsuarios.getTableHeader();
        header.setBackground(new Color(45, 55, 140));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
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
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setOpaque(false);
        
        btnCrearUsuario = crearBoton("➕ Crear Usuario", new Color(39, 174, 96));
        btnEditarRol = crearBoton("🔄 Cambiar Rol", new Color(52, 152, 219));
        btnCambiarPassword = crearBoton("🔐 Cambiar Contraseña", new Color(241, 196, 15));
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
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
     * Carga todos los usuarios (incluyendo los registrados desde el login)
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
        
        lblTotalUsuarios.setText("📊 Total: " + usuarios.size() + " usuarios");
        GestorLog.info("Usuarios cargados en tabla: " + usuarios.size());
    }
    
    private boolean esUsuarioProtegido(String nombreUsuario) {
        for (String protegido : USUARIOS_PROTEGIDOS) {
            if (protegido.equalsIgnoreCase(nombreUsuario)) {
                return true;
            }
        }
        return false;
    }

    // ⭐ AGREGAR AQUÍ LA CONSTANTE Y EL MÉTODO
    /**
     * Lista de administradores fijos del sistema (no se puede cambiar su rol)
     */
    private static final String[] ADMINISTRADORES_SISTEMA = {
        "admin", "arbitro", "manager"
    };

    /**
     * Verifica si un usuario es uno de los 3 administradores del sistema.
     */
    private boolean esAdministradorSistema(String nombreUsuario) {
        for (String adminSistema : ADMINISTRADORES_SISTEMA) {
            if (adminSistema.equalsIgnoreCase(nombreUsuario)) {
                return true;
            }
        }
        return false;
    }


        // ... resto del código
    private void crearNuevoUsuario() {
        DialogoCrearUsuario dialogo = new DialogoCrearUsuario((JFrame) SwingUtilities.getWindowAncestor(this));
        dialogo.setVisible(true);
        
        if (dialogo.isAceptado()) {
            String nombreUsuario = dialogo.getNombreUsuario();
            String nombreReal = dialogo.getNombreReal();
            String password = dialogo.getPassword();
            Rol rol = dialogo.getRol();
            
            // ⭐ BLOQUEAR CREACIÓN DE ADMINISTRADORES
            if (rol == Rol.ADMINISTRADOR) {
                JOptionPane.showMessageDialog(this,
                    " No se pueden crear más administradores.\n\n" +
                    "El sistema ya tiene los 3 administradores predeterminados:\n" +
                    " admin\n" +
                    " arbitro\n" +
                    " manager\n\n" +
                    "Estos son los únicos usuarios con permisos de administrador.",
                    "Creación de administrador bloqueada",
                    JOptionPane.ERROR_MESSAGE);
                GestorLog.advertencia("Intento de crear usuario con rol ADMINISTRADOR: " + nombreUsuario);
                return;
            }
            
            if (datosFederacion.buscarUsuario(nombreUsuario) != null) {
                JOptionPane.showMessageDialog(this,
                    "Ya existe un usuario con ese nombre de usuario",
                    "Usuario duplicado",
                    JOptionPane.WARNING_MESSAGE);
                GestorLog.advertencia("Intento de crear usuario duplicado: " + nombreUsuario);
                return;
            }
            
            Usuario nuevoUsuario = new Usuario(nombreReal, nombreUsuario, password, rol);
            datosFederacion.getListaUsuarios().add(nuevoUsuario);
            GestorArchivos.guardarTodo(datosFederacion);
            
            cargarUsuarios();
            GestorLog.exito("Usuario creado: " + nombreUsuario + " | Rol: " + rol.getNombreLegible());
            
            JOptionPane.showMessageDialog(this,
                "Usuario '" + nombreUsuario + "' creado con éxito",
                "Usuario creado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
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
        
        // ⭐ BLOQUEAR CAMBIO DE ROL EN ADMINISTRADORES DEL SISTEMA
        if (esAdministradorSistema(nombreUsuario)) {
            JOptionPane.showMessageDialog(this,
                "No se puede cambiar el rol de los administradores del sistema.\n\n" +
                "Los usuarios 'admin', 'arbitro' y 'manager' mantienen\n" +
                "permanentemente el rol de ADMINISTRADOR.",
                "Usuario del sistema protegido",
                JOptionPane.WARNING_MESSAGE);
            GestorLog.advertencia("Intento de cambiar rol de administrador del sistema: " + nombreUsuario);
            return;
        }
        
        // ⭐ FILTRAR ROLES DISPONIBLES: NO permitir crear más ADMINISTRADORES
        Rol[] rolesDisponibles;
        String[] nombresRoles;
        
        if (usuario.getRol() == Rol.ADMINISTRADOR) {
            // Si ya es admin (creado manualmente), puede cambiar a otros roles
            rolesDisponibles = new Rol[]{Rol.INVITADO, Rol.ARBITRO, Rol.MANAGER};
            nombresRoles = new String[]{"Invitado", "Árbitro", "Manager"};
        } else {
            // Si NO es admin, NO puede convertirse en admin
            rolesDisponibles = new Rol[]{Rol.INVITADO, Rol.ARBITRO, Rol.MANAGER};
            nombresRoles = new String[]{"Invitado", "Árbitro", "Manager"};
        }
        
        String rolSeleccionado = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona el nuevo rol para: " + usuario.getNombreReal() + 
            "\n\n⚠️ No se pueden crear más administradores del sistema.",
            "Cambiar Rol",
            JOptionPane.QUESTION_MESSAGE,
            null,
            nombresRoles,
            usuario.getRol().getNombreLegible()
        );
        
        if (rolSeleccionado == null) return;
        
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
            GestorArchivos.guardarTodo(datosFederacion);
            
            cargarUsuarios();
            GestorLog.exito("Rol actualizado: " + nombreUsuario + 
                          " | " + rolAnterior.getNombreLegible() + " → " + nuevoRol.getNombreLegible());
            
            JOptionPane.showMessageDialog(this,
                "Rol de '" + nombreUsuario + "' actualizado a: " + nuevoRol.getNombreLegible(),
                "Rol actualizado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
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
            GestorArchivos.guardarTodo(datosFederacion);
            GestorLog.exito("Contraseña actualizada para: " + nombreUsuario);
            
            JOptionPane.showMessageDialog(this,
                "Contraseña actualizada correctamente",
                "Contraseña cambiada",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
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
        
        if (esUsuarioProtegido(nombreUsuario)) {
            JOptionPane.showMessageDialog(this,
                "No se pueden eliminar los usuarios protegidos del sistema:\n" +
                "admin, invitado, arbitro, manager",
                "Usuario protegido",
                JOptionPane.WARNING_MESSAGE);
            GestorLog.advertencia("Intento de eliminar usuario protegido: " + nombreUsuario);
            return;
        }
        
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
            GestorArchivos.guardarTodo(datosFederacion);
            
            cargarUsuarios();
            GestorLog.exito("Usuario eliminado: " + nombreUsuario + " | Rol: " + usuario.getRol().getNombreLegible());
            
            JOptionPane.showMessageDialog(this,
                "Usuario '" + nombreUsuario + "' eliminado correctamente",
                "Usuario eliminado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

class DialogoCrearUsuario extends JDialog {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean aceptado = false;
    private JTextField txtNombreUsuario;
    private JTextField txtNombreReal;
    private JPasswordField txtPassword;
    private JPasswordField txtPasswordConfirm;
    private JComboBox<String> comboRol;
    
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
        // ⭐ ELIMINAR "Administrador" de las opciones
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
    
    public boolean isAceptado() { return aceptado; }
    public String getNombreUsuario() { return txtNombreUsuario.getText().trim(); }
    public String getNombreReal() { return txtNombreReal.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    
    public Rol getRol() {
        String seleccion = (String) comboRol.getSelectedItem();
        switch (seleccion) {
            case "Árbitro": return Rol.ARBITRO;
            case "Manager": return Rol.MANAGER;
            default: return Rol.INVITADO;
            // ⭐ ADMINISTRADOR eliminado - ya no es opción
        }
    }

}
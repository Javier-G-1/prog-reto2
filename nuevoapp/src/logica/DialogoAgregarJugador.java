package logica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import gestion.Jugador;

public class DialogoAgregarJugador extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtNombre;
    private JComboBox<String> comboPosicion;
    private JSpinner spinnerEdad;
    private JTextField txtNacionalidad;
    private JSpinner spinnerAltura;
    private JSpinner spinnerPeso;
    private JSpinner spinnerDorsal;
    
    private boolean aceptado = false;
    private Jugador jugadorCreado = null;
    
    // Posiciones válidas de balonmano
    private static final String[] POSICIONES = {
        "Portero",
        "Extremo Izquierdo",
        "Extremo Derecho",
        "Lateral Izquierdo",
        "Lateral Derecho",
        "Central",
        "Pivote"
    };
    

     // ... resto del código ...
        public DialogoAgregarJugador(Frame owner) {
            // Pasamos 'owner' al constructor de JDialog
            super(owner, "Agregar Nuevo Jugador", true);
            
            setLayout(new BorderLayout(10, 10));
            setSize(450, 450);
            setLocationRelativeTo(owner); // Centrar respecto a la ventana principal
            
            // Panel con 7 filas (añadimos dorsal)
            JPanel panelCampos = new JPanel(new GridLayout(7, 2, 10, 15));
            panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
            panelCampos.setBackground(new Color(30, 34, 41));
            
            // --- NOMBRE ---
            panelCampos.add(crearLabel("Nombre:"));
            txtNombre = new JTextField();
            estilizarCampoTexto(txtNombre);
            panelCampos.add(txtNombre);
            
            // --- POSICIÓN ---
            panelCampos.add(crearLabel("Posición:"));
            comboPosicion = new JComboBox<>(POSICIONES);
            panelCampos.add(comboPosicion);
            
            // --- DORSAL (Corregido: valor fijo inicial) ---
            panelCampos.add(crearLabel("Dorsal:"));
            spinnerDorsal = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
            estilizarSpinner(spinnerDorsal);
            panelCampos.add(spinnerDorsal);

            // --- EDAD ---
            panelCampos.add(crearLabel("Edad:"));
            spinnerEdad = new JSpinner(new SpinnerNumberModel(25, 16, 50, 1));
            estilizarSpinner(spinnerEdad);
            panelCampos.add(spinnerEdad);
            
            // --- NACIONALIDAD ---
            panelCampos.add(crearLabel("Nacionalidad:"));
            txtNacionalidad = new JTextField("España");
            estilizarCampoTexto(txtNacionalidad);
            panelCampos.add(txtNacionalidad);
            
            // --- ALTURA ---
            panelCampos.add(crearLabel("Altura (cm):"));
            spinnerAltura = new JSpinner(new SpinnerNumberModel(180, 150, 220, 1));
            estilizarSpinner(spinnerAltura);
            panelCampos.add(spinnerAltura);
            
            // --- PESO ---
            panelCampos.add(crearLabel("Peso (kg):"));
            spinnerPeso = new JSpinner(new SpinnerNumberModel(80, 50, 150, 1));
            estilizarSpinner(spinnerPeso);
            panelCampos.add(spinnerPeso);
            
            add(panelCampos, BorderLayout.CENTER);
        // ===== PANEL DE BOTONES =====
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(new Color(30, 34, 41));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCancelar.setBackground(new Color(140, 45, 45));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.addActionListener(e -> {
            aceptado = false;
            dispose();
        });
        
        JButton btnAceptar = new JButton("Crear Jugador");
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAceptar.setBackground(new Color(45, 55, 140));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorderPainted(false);
        btnAceptar.setPreferredSize(new Dimension(130, 35));
        btnAceptar.addActionListener(e -> crearJugador());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        // Cerrar con ESC
        getRootPane().registerKeyboardAction(
            e -> {
                aceptado = false;
                dispose();
            },
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Aceptar con ENTER
        getRootPane().setDefaultButton(btnAceptar);
        
        getContentPane().setBackground(new Color(30, 34, 41));
    }
    
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }
    
    private void estilizarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setPreferredSize(new Dimension(200, 30));
    }
    
    private void estilizarSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
    }
    
    private void crearJugador() {
        // Validación del nombre
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre no puede estar vacío",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        
        if (nombre.length() < 3) {
            JOptionPane.showMessageDialog(this,
                "El nombre debe tener al menos 3 caracteres",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        
        // Validación de la nacionalidad
        String nacionalidad = txtNacionalidad.getText().trim();
        if (nacionalidad.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La nacionalidad no puede estar vacía",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            txtNacionalidad.requestFocus();
            return;
        }
        
        // Obtener valores (ya validados por los spinners)
        String posicion = (String) comboPosicion.getSelectedItem();
        int edad = (Integer) spinnerEdad.getValue();
        int altura = (Integer) spinnerAltura.getValue();
        int peso = (Integer) spinnerPeso.getValue();
        int dorsal = (Integer) spinnerDorsal.getValue();
        
        // Validación lógica adicional
        if (edad < 16) {
            JOptionPane.showMessageDialog(this,
                "La edad mínima permitida es 16 años",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (altura < 150 || altura > 220) {
            JOptionPane.showMessageDialog(this,
                "La altura debe estar entre 150 cm y 220 cm",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (peso < 50 || peso > 150) {
            JOptionPane.showMessageDialog(this,
                "El peso debe estar entre 50 kg y 150 kg",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Crear el jugador con todos los datos
        jugadorCreado = new Jugador(nombre, posicion, edad, null);
        jugadorCreado.setNacionalidad(nacionalidad);
        jugadorCreado.setAltura(altura + " cm");
        jugadorCreado.setPeso(peso + " kg");
        jugadorCreado.setDorsal(dorsal);
        aceptado = true;
        dispose();
    }
    
    public boolean isAceptado() {
        return aceptado;
    }
    
    public Jugador getJugadorCreado() {
        return jugadorCreado;
    }
}
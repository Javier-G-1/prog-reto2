package logica;

import gestion.Jugador;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para editar todos los datos de un jugador incluyendo
 * dorsal, nacionalidad, altura y peso para exportación completa.
 */
public class DialogoEditarJugador extends JDialog {
    
    private Jugador jugador;
    private boolean aceptado = false;
    
    private JTextField txtNombre;
    private JTextField txtPosicion;
    private JSpinner spnEdad;
    private JSpinner spnDorsal;
    private JTextField txtNacionalidad;
    private JTextField txtAltura;
    private JTextField txtPeso;
    private JTextField txtFotoURL;
    
    public DialogoEditarJugador(Frame parent, Jugador jugador) {
        super(parent, "Editar Jugador - " + jugador.getNombre(), true);
        this.jugador = jugador;
        
        setSize(450, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal con todos los campos
        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 10, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Nombre
        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField(jugador.getNombre());
        panelCampos.add(txtNombre);
        
        // Posición
        panelCampos.add(new JLabel("Posición:"));
        txtPosicion = new JTextField(jugador.getPosicion());
        panelCampos.add(txtPosicion);
        
        // Edad
        panelCampos.add(new JLabel("Edad:"));
        spnEdad = new JSpinner(new SpinnerNumberModel(jugador.getEdad(), 15, 50, 1));
        panelCampos.add(spnEdad);
        
        // Dorsal
        panelCampos.add(new JLabel("Dorsal:"));
        spnDorsal = new JSpinner(new SpinnerNumberModel(jugador.getDorsal(), 0, 99, 1));
        panelCampos.add(spnDorsal);
        
        // Nacionalidad
        panelCampos.add(new JLabel("Nacionalidad:"));
        txtNacionalidad = new JTextField(jugador.getNacionalidad());
        panelCampos.add(txtNacionalidad);
        
        // Altura
        panelCampos.add(new JLabel("Altura (ej: 175cm):"));
        txtAltura = new JTextField(jugador.getAltura());
        panelCampos.add(txtAltura);
        
        // Peso
        panelCampos.add(new JLabel("Peso (ej: 73kg):"));
        txtPeso = new JTextField(jugador.getPeso());
        panelCampos.add(txtPeso);
        
        // Foto URL
        panelCampos.add(new JLabel("URL Foto:"));
        JPanel panelFoto = new JPanel(new BorderLayout(5, 0));
        txtFotoURL = new JTextField(jugador.getFotoURL() != null ? jugador.getFotoURL() : "");
        JButton btnSeleccionar = new JButton("...");
        btnSeleccionar.setPreferredSize(new Dimension(30, 25));
        btnSeleccionar.addActionListener(e -> seleccionarFoto());
        panelFoto.add(txtFotoURL, BorderLayout.CENTER);
        panelFoto.add(btnSeleccionar, BorderLayout.EAST);
        panelCampos.add(panelFoto);
        
        add(panelCampos, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardar());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void seleccionarFoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imágenes", "jpg", "jpeg", "png", "gif"));
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtFotoURL.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void guardar() {
        try {
            // Validar campos obligatorios
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "El nombre no puede estar vacío", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (txtPosicion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "La posición no puede estar vacía", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Actualizar jugador
            jugador.setNombre(txtNombre.getText().trim());
            jugador.setPosicion(txtPosicion.getText().trim());
            jugador.setEdad((Integer) spnEdad.getValue());
            jugador.setDorsal((Integer) spnDorsal.getValue());
            jugador.setNacionalidad(txtNacionalidad.getText().trim());
            
            // Validar formato de altura
            String altura = txtAltura.getText().trim();
            if (!altura.isEmpty() && !altura.matches("\\d+cm")) {
                altura = altura.replaceAll("[^0-9]", "") + "cm";
            }
            jugador.setAltura(altura.isEmpty() ? "175cm" : altura);
            
            // Validar formato de peso
            String peso = txtPeso.getText().trim();
            if (!peso.isEmpty() && !peso.matches("\\d+kg")) {
                peso = peso.replaceAll("[^0-9]", "") + "kg";
            }
            jugador.setPeso(peso.isEmpty() ? "75kg" : peso);
            
            jugador.setFotoURL(txtFotoURL.getText().trim());
            
            aceptado = true;
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al guardar los datos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isAceptado() {
        return aceptado;
    }
}
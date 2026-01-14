package logica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import gestion.Jugador;

/**
 * DIÁLOGO MODAL para editar los datos de un jugador existente.
 * <p>
 * Permite modificar nombre, posición, dorsal, edad, nacionalidad, altura y peso.
 * Incluye validaciones de datos similares a {@link DialogoAgregarJugador}.
 * </p>
 */
public class DialogoEditarJugador extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtNombre;
    private JComboBox<String> comboPosicion;
    private JSpinner spinnerEdad;
    private JTextField txtNacionalidad;
    private JSpinner spinnerAltura;
    private JSpinner spinnerPeso;
    private JSpinner spinnerDorsal;

    private boolean aceptado = false;
    private Jugador jugador;

    private static final String[] POSICIONES = {
        "Portero", "Extremo Izquierdo", "Extremo Derecho",
        "Lateral Izquierdo", "Lateral Derecho", "Central", "Pivote"
    };

    /**
     * Constructor del diálogo de edición.
     *
     * @param parent ventana propietaria
     * @param jugador jugador a editar
     */
    public DialogoEditarJugador(Frame parent, Jugador jugador) {
        super(parent, "Editar Jugador - " + jugador.getNombre(), true);
        this.jugador = jugador;
        inicializarComponentes(parent);
    }

    /**
     * Indica si el usuario aceptó los cambios.
     *
     * @return {@code true} si se guardaron los cambios, {@code false} si se canceló
     */
    public boolean isAceptado() {
        return aceptado;
    }

    // ------------------- MÉTODOS PRIVADOS -------------------

    /** Inicializa los componentes gráficos y eventos del diálogo */
    private void inicializarComponentes(Frame parent) {
        setLayout(new BorderLayout(10, 10));
        setSize(450, 450);
        setLocationRelativeTo(parent);

        JPanel panelCampos = new JPanel(new GridLayout(7, 2, 10, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelCampos.setBackground(new Color(30, 34, 41));

        // Campos de texto y spinners
        txtNombre = crearCampoTexto("Nombre:", jugador.getNombre(), panelCampos);
        comboPosicion = crearComboPosicion("Posición:", jugador.getPosicion(), panelCampos);
        spinnerDorsal = crearSpinner("Dorsal:", jugador.getDorsal() > 0 ? jugador.getDorsal() : 1, 0, 99, 1, panelCampos);
        spinnerEdad = crearSpinner("Edad:", jugador.getEdad(), 16, 50, 1, panelCampos);
        txtNacionalidad = crearCampoTexto("Nacionalidad:", jugador.getNacionalidad() != null ? jugador.getNacionalidad() : "España", panelCampos);
        spinnerAltura = crearSpinner("Altura (cm):", extraerNumero(jugador.getAltura(), 180), 150, 220, 1, panelCampos);
        spinnerPeso = crearSpinner("Peso (kg):", extraerNumero(jugador.getPeso(), 80), 50, 150, 1, panelCampos);

        add(panelCampos, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(new Color(30, 34, 41));

        JButton btnCancelar = crearBotonCancelar();
        JButton btnGuardar = crearBotonGuardar();

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        add(panelBotones, BorderLayout.SOUTH);

        // Teclas rápidas
        getRootPane().registerKeyboardAction(
                e -> { aceptado = false; dispose(); },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        getRootPane().setDefaultButton(btnGuardar);

        getContentPane().setBackground(new Color(30, 34, 41));
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    private JTextField crearCampoTexto(String etiqueta, String valorInicial, JPanel panel) {
        JLabel lbl = crearLabel(etiqueta);
        JTextField campo = new JTextField(valorInicial);
        estilizarCampoTexto(campo);
        panel.add(lbl);
        panel.add(campo);
        return campo;
    }

    private JComboBox<String> crearComboPosicion(String etiqueta, String valorSeleccionado, JPanel panel) {
        JLabel lbl = crearLabel(etiqueta);
        JComboBox<String> combo = new JComboBox<>(POSICIONES);
        combo.setSelectedItem(valorSeleccionado);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        panel.add(lbl);
        panel.add(combo);
        return combo;
    }

    private JSpinner crearSpinner(String etiqueta, int valor, int min, int max, int paso, JPanel panel) {
        JLabel lbl = crearLabel(etiqueta);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(valor, min, max, paso));
        estilizarSpinner(spinner);
        panel.add(lbl);
        panel.add(spinner);
        return spinner;
    }

    private void estilizarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setPreferredSize(new Dimension(200, 30));
    }

    private void estilizarSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
    }

    private JButton crearBotonCancelar() {
        JButton btn = new JButton("Cancelar");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(new Color(140, 45, 45));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.addActionListener(e -> { aceptado = false; dispose(); });
        return btn;
    }

    private JButton crearBotonGuardar() {
        JButton btn = new JButton("Guardar Cambios");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(45, 55, 140));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 35));
        btn.addActionListener(e -> guardarCambios());
        return btn;
    }

    /**
     * Extrae el número de una cadena como "180 cm" o "80 kg".
     *
     * @param texto cadena de texto
     * @param valorPorDefecto valor a devolver si no se puede extraer número
     * @return número entero
     */
    private int extraerNumero(String texto, int valorPorDefecto) {
        if (texto == null || texto.trim().isEmpty()) return valorPorDefecto;
        try {
            String numeros = texto.replaceAll("[^0-9]", "");
            if (!numeros.isEmpty()) return Integer.parseInt(numeros);
        } catch (NumberFormatException ignored) {}
        return valorPorDefecto;
    }

    /**
     * Valida los datos y aplica los cambios al jugador.
     * Si ocurre algún error, muestra un mensaje y no cierra el diálogo.
     */
    private void guardarCambios() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty() || nombre.length() < 3) {
            JOptionPane.showMessageDialog(this, "El nombre debe tener al menos 3 caracteres",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        String nacionalidad = txtNacionalidad.getText().trim();
        if (nacionalidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La nacionalidad no puede estar vacía",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            txtNacionalidad.requestFocus();
            return;
        }

        String posicion = (String) comboPosicion.getSelectedItem();
        int dorsal = (Integer) spinnerDorsal.getValue();
        int edad = (Integer) spinnerEdad.getValue();
        int altura = (Integer) spinnerAltura.getValue();
        int peso = (Integer) spinnerPeso.getValue();

        if (edad < 16 || altura < 150 || altura > 220 || peso < 50 || peso > 150) {
            JOptionPane.showMessageDialog(this,
                    "Los datos ingresados no cumplen los requisitos de edad, altura o peso",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        jugador.setNombre(nombre);
        jugador.setPosicion(posicion);
        jugador.setDorsal(dorsal);
        jugador.setEdad(edad);
        jugador.setNacionalidad(nacionalidad);
        jugador.setAltura(altura + " cm");
        jugador.setPeso(peso + " kg");

        aceptado = true;
        dispose();
    }
}

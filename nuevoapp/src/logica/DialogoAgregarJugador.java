package logica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import gestion.Jugador;
import gestion.Equipo;

/**
 * DIÁLOGO MODAL para agregar un nuevo jugador a un equipo.
 * <p>
 * Permite ingresar nombre, posición, dorsal, edad, nacionalidad, altura y peso.
 * Realiza validaciones de los datos y asegura que el dorsal sea único dentro del equipo.
 * </p>
 */
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
    private Equipo equipoDestino;

    private static final String[] POSICIONES = {
        "Portero", "Extremo Izquierdo", "Extremo Derecho",
        "Lateral Izquierdo", "Lateral Derecho", "Central", "Pivote"
    };

    /**
     * Constructor del diálogo.
     *
     * @param owner ventana propietaria
     */
    public DialogoAgregarJugador(Window owner) {
        super(owner, "Agregar Nuevo Jugador", ModalityType.APPLICATION_MODAL);
        inicializarComponentes(owner);
    }

    /**
     * Establece el equipo al que se agregará el jugador.
     * <p>
     * Se utiliza para sugerir automáticamente un dorsal disponible.
     * </p>
     *
     * @param equipo objeto {@link Equipo} destino
     */
    public void setEquipoDestino(Equipo equipo) {
        this.equipoDestino = equipo;

        if (equipo != null && spinnerDorsal != null) {
            int dorsalSugerido = 1;
            boolean encontrado = false;

            while (!encontrado && dorsalSugerido <= 99) {
                boolean dorsalUsado = false;
                for (Jugador j : equipo.getPlantilla()) {
                    if (j.getDorsal() == dorsalSugerido) {
                        dorsalUsado = true;
                        break;
                    }
                }
                if (!dorsalUsado) encontrado = true;
                else dorsalSugerido++;
            }

            if (encontrado) spinnerDorsal.setValue(dorsalSugerido);
        }
    }

    /**
     * Indica si el usuario aceptó la creación del jugador.
     *
     * @return {@code true} si se creó un jugador, {@code false} si se canceló
     */
    public boolean isAceptado() {
        return aceptado;
    }

    /**
     * Devuelve el jugador creado después de cerrar el diálogo.
     *
     * @return objeto {@link Jugador} creado, o {@code null} si se canceló
     */
    public Jugador getJugadorCreado() {
        return jugadorCreado;
    }

    // ------------------- MÉTODOS PRIVADOS -------------------

    /** Inicializa los componentes gráficos y eventos del diálogo */
    private void inicializarComponentes(Window owner) {
        setLayout(new BorderLayout(10, 10));
        setSize(450, 450);
        setLocationRelativeTo(owner);

        JPanel panelCampos = new JPanel(new GridLayout(7, 2, 10, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panelCampos.setBackground(new Color(30, 34, 41));

        // Campos
        panelCampos.add(crearLabel("Nombre:"));
        txtNombre = new JTextField();
        estilizarCampoTexto(txtNombre);
        panelCampos.add(txtNombre);

        panelCampos.add(crearLabel("Posición:"));
        comboPosicion = new JComboBox<>(POSICIONES);
        panelCampos.add(comboPosicion);

        panelCampos.add(crearLabel("Dorsal:"));
        spinnerDorsal = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        estilizarSpinner(spinnerDorsal);
        panelCampos.add(spinnerDorsal);

        panelCampos.add(crearLabel("Edad:"));
        spinnerEdad = new JSpinner(new SpinnerNumberModel(25, 16, 50, 1));
        estilizarSpinner(spinnerEdad);
        panelCampos.add(spinnerEdad);

        panelCampos.add(crearLabel("Nacionalidad:"));
        txtNacionalidad = new JTextField("España");
        estilizarCampoTexto(txtNacionalidad);
        panelCampos.add(txtNacionalidad);

        panelCampos.add(crearLabel("Altura (cm):"));
        spinnerAltura = new JSpinner(new SpinnerNumberModel(180, 150, 220, 1));
        estilizarSpinner(spinnerAltura);
        panelCampos.add(spinnerAltura);

        panelCampos.add(crearLabel("Peso (kg):"));
        spinnerPeso = new JSpinner(new SpinnerNumberModel(80, 50, 150, 1));
        estilizarSpinner(spinnerPeso);
        panelCampos.add(spinnerPeso);

        add(panelCampos, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(new Color(30, 34, 41));

        JButton btnCancelar = new JButton("Cancelar");
        configurarBotonCancelar(btnCancelar);

        JButton btnAceptar = new JButton("Crear Jugador");
        configurarBotonAceptar(btnAceptar);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);

        add(panelBotones, BorderLayout.SOUTH);

        // Teclas rápidas
        getRootPane().registerKeyboardAction(e -> { aceptado = false; dispose(); },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
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

    private void configurarBotonCancelar(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(new Color(140, 45, 45));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.addActionListener(e -> { aceptado = false; dispose(); });
    }

    private void configurarBotonAceptar(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(45, 55, 140));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 35));
        btn.addActionListener(e -> crearJugador());
    }

    /** Valida los datos ingresados y crea el objeto {@link Jugador} */
    private void crearJugador() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty() || nombre.length() < 3) {
            JOptionPane.showMessageDialog(this,
                    "El nombre debe tener al menos 3 caracteres",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        String nacionalidad = txtNacionalidad.getText().trim();
        if (nacionalidad.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La nacionalidad no puede estar vacía",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            txtNacionalidad.requestFocus();
            return;
        }

        String posicion = (String) comboPosicion.getSelectedItem();
        int edad = (Integer) spinnerEdad.getValue();
        int altura = (Integer) spinnerAltura.getValue();
        int peso = (Integer) spinnerPeso.getValue();
        int dorsal = (Integer) spinnerDorsal.getValue();

        // Validar dorsal único
        if (equipoDestino != null) {
            for (Jugador j : equipoDestino.getPlantilla()) {
                if (j.getDorsal() == dorsal) {
                    JOptionPane.showMessageDialog(this,
                            "El dorsal " + dorsal + " ya está en uso por " + j.getNombre(),
                            "Dorsal duplicado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        // Validaciones lógicas
        if (edad < 16 || altura < 150 || altura > 220 || peso < 50 || peso > 150) {
            JOptionPane.showMessageDialog(this,
                    "Los datos ingresados no cumplen los requisitos de edad, altura o peso",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        jugadorCreado = new Jugador(nombre, posicion, edad, null);
        jugadorCreado.setNacionalidad(nacionalidad);
        jugadorCreado.setAltura(altura + " cm");
        jugadorCreado.setPeso(peso + " kg");
        jugadorCreado.setDorsal(dorsal);

        aceptado = true;
        dispose();
    }
}

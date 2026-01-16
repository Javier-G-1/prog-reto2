package nuevoapp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gestion.Partido;
import gestion.Usuario;
import logica.GestorLog;
import logica.PermisosApp;

/**
 * Clase DialogoResultado.
 * 
 * Esta clase representa un diálogo modal que permite introducir y guardar
 * el resultado de un partido específico. Incluye validaciones de entrada
 * y actualiza directamente el objeto Partido.
 * 
 */
public class DialogoResultado extends JDialog {
    
    private static final long serialVersionUID = 1L;

    /** Campo de texto para los goles del equipo local */
    private JTextField txtGolesLocal;

    /** Campo de texto para los goles del equipo visitante */
    private JTextField txtGolesVisitante;

    /** Botón para guardar el resultado y finalizar el partido */
    private JButton btnGuardar;

    /** Indica si se ha aceptado y guardado el resultado */
    private boolean aceptado = false;

    /** Objeto Partido cuyo resultado se va a modificar */
    private Partido partido;

    /** Usuario que intenta editar el resultado */
    private Usuario usuario;

    /** Goles introducidos para el equipo local */
    private int golesL;

    /** Goles introducidos para el equipo visitante */
    private int golesV;

    /**
     * Constructor del diálogo.
     * 
     * @param padre La ventana padre sobre la que se centra el diálogo
     * @param p El partido cuyo resultado se va a introducir
     * @param usuario El usuario que intenta editar el resultado
     */
    public DialogoResultado(Frame padre, Partido p, Usuario usuario) {
        super(padre, (p == null) ? "Resultado" : "Resultado: " 
                + p.getEquipoLocal().getNombre() + " vs " 
                + p.getEquipoVisitante().getNombre(), true);
        
        if (p == null) {
            dispose();
            return;
        }

        // Validar permisos del usuario
        if (!PermisosApp.puedeModificarResultados(usuario)) {
            JOptionPane.showMessageDialog(padre,
                "No tienes permisos para editar resultados de partidos.\nSolo los usuarios con rol Árbitro o Administrador pueden hacerlo.",
                "Acceso Denegado",
                JOptionPane.WARNING_MESSAGE);
          
            dispose();
            return;
        }

        this.partido = p;
        this.usuario = usuario;

        setLayout(new BorderLayout(10, 10));
        setSize(350, 200);
        setLocationRelativeTo(padre);

        // Panel de inputs
        JPanel panelInputs = new JPanel(new GridLayout(2, 2, 10, 10));
        panelInputs.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        panelInputs.add(new JLabel("Goles " + p.getEquipoLocal().getNombre() + ":"));
        txtGolesLocal = new JTextField(String.valueOf(p.getGolesLocal()));
        panelInputs.add(txtGolesLocal);

        panelInputs.add(new JLabel("Goles " + p.getEquipoVisitante().getNombre() + ":"));
        txtGolesVisitante = new JTextField(String.valueOf(p.getGolesVisitante()));
        panelInputs.add(txtGolesVisitante);

        add(panelInputs, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Finalizar Partido");
        btnGuardar.addActionListener(e -> guardarResultado());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Guarda el resultado introducido en los campos de texto.
     * 
     * Valida que los valores sean números enteros y estén dentro del rango
     * permitido (0-50). Actualiza el objeto Partido y registra el evento.
     */
    private void guardarResultado() {
        try {
            int gL = Integer.parseInt(txtGolesLocal.getText().trim());
            int gV = Integer.parseInt(txtGolesVisitante.getText().trim());

            if (gL < 0 || gV < 0 || gL > 50 || gV > 50) {
                JOptionPane.showMessageDialog(this, "Los goles deben estar entre 0 y 50.", 
                        "Error de Rango", JOptionPane.WARNING_MESSAGE);
                return;
            }

            this.golesL = gL;
            this.golesV = gV;
            
            partido.setGolesLocal(gL);
            partido.setGolesVisitante(gV);
            partido.setFinalizado(true);

            GestorLog.registrarEvento("PARTIDO FINALIZADO: " + partido.getEquipoLocal().getNombre() 
                    + " " + gL + " - " + gV + " " + partido.getEquipoVisitante().getNombre());

            aceptado = true;
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "¡Error! Introduce solo números enteros.", 
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Indica si se aceptó el resultado.
     * 
     * @return true si se ha guardado el resultado, false en caso contrario
     */
    public boolean isAceptado() { return aceptado; }

    /**
     * Devuelve los goles del equipo local introducidos.
     * 
     * @return Goles del equipo local
     */
    public int getGolesL() { return golesL; }

    /**
     * Devuelve los goles del equipo visitante introducidos.
     * 
     * @return Goles del equipo visitante
     */
    public int getGolesV() { return golesV; }
    
}

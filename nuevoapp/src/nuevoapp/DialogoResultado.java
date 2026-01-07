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
import logica.GestorLog;

public class DialogoResultado extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField txtGolesLocal, txtGolesVisitante;
    private JButton btnGuardar;
    private boolean aceptado = false; // Cambiado a 'aceptado' para coincidir con tu VentanaPrincipal
    private Partido partido;
    private int golesL;
    private int golesV;
    
    public DialogoResultado(Frame padre, Partido p) {
        // Usamos un operador ternario: si p es null ponemos un título genérico, si no, el real
        super(padre, (p == null) ? "Resultado" : "Resultado: " + p.getEquipoLocal().getNombre() + " vs " + p.getEquipoVisitante().getNombre(), true);
        
        // Ahora el check ya no es "dead code" porque el programa no ha petado antes
        if (p == null) {
            dispose();
            return;
        }
        this.partido = p;
        // ... resto del código
    

        // Configuración visual mejorada
        setLayout(new BorderLayout(10, 10));
        setSize(350, 200);
        setLocationRelativeTo(padre);

        // Panel Central para los inputs
        JPanel panelInputs = new JPanel(new GridLayout(2, 2, 10, 10));
        panelInputs.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        panelInputs.add(new JLabel("Goles " + p.getEquipoLocal().getNombre() + ":"));
        // Pre-cargamos los goles actuales para que no aparezca vacío (evita el 0-0 por error)
        txtGolesLocal = new JTextField(String.valueOf(p.getGolesLocal()));
        panelInputs.add(txtGolesLocal);

        panelInputs.add(new JLabel("Goles " + p.getEquipoVisitante().getNombre() + ":"));
        txtGolesVisitante = new JTextField(String.valueOf(p.getGolesVisitante()));
        panelInputs.add(txtGolesVisitante);

        add(panelInputs, BorderLayout.CENTER);

        // Panel Inferior para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Finalizar Partido");
        btnGuardar.addActionListener(e -> guardarResultado());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardarResultado() {
        try {
            // 1. Validación de caracteres (NumberFormatException)
            int gL = Integer.parseInt(txtGolesLocal.getText().trim());
            int gV = Integer.parseInt(txtGolesVisitante.getText().trim());

            // 2. Validación de "Goles Infinitos" o Negativos (Reglas de Federación)
            if (gL < 0 || gV < 0 || gL > 50 || gV > 50) {
                JOptionPane.showMessageDialog(this, "Los goles deben estar entre 0 y 50.", "Error de Rango", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Guardado de datos
            this.golesL = gL;
            this.golesV = gV;
            
            // Actualizamos el objeto directamente
            partido.setGolesLocal(gL);
            partido.setGolesVisitante(gV);
            partido.setFinalizado(true);

            GestorLog.registrarEvento("PARTIDO FINALIZADO: " + partido.getEquipoLocal().getNombre() + " " + gL + " - " + gV + " " + partido.getEquipoVisitante().getNombre());

            aceptado = true;
            dispose();
            
        } catch (NumberFormatException ex) {
            // Solución al error de caracteres
            JOptionPane.showMessageDialog(this, "¡Error! Introduce solo números enteros.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos Getter para que VentanaPrincipal pueda leer los resultados
 // 2. AÑADE ESTOS MÉTODOS AL FINAL (Los Getters)
    public boolean isAceptado() { return aceptado; }
    public int getGolesL() { return golesL; }
    public int getGolesV() { return golesV; }
    
}
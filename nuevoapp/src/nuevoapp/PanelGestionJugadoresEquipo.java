package nuevoapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import gestion.*;
import logica.*;

/**
 * Panel para gestionar jugadores del equipo y fichar nuevos jugadores.
 */
public class PanelGestionJugadoresEquipo extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private DatosFederacion datosFederacion;
    private Temporada temporada;
    private Equipo equipo;
    
    private JList<Jugador> listaJugadoresEquipo;
    private DefaultListModel<Jugador> modeloEquipo;
    
    private JList<Jugador> listaJugadoresDisponibles;
    private DefaultListModel<Jugador> modeloDisponibles;
    
    private JComboBox<String> comboFiltroEquipo;
    
    public PanelGestionJugadoresEquipo(Frame parent, DatosFederacion datos, 
                                       Temporada temp, Equipo eq) {
        super(parent, "Gestionar Jugadores - " + eq.getNombre(), true);
        this.datosFederacion = datos;
        this.temporada = temp;
        this.equipo = eq;
        
        setSize(1200, 700);
        setLocationRelativeTo(parent);
        
        // ⭐ VALIDACIÓN: Solo permitir si la temporada es FUTURA
        if (!temp.getEstado().equals(Temporada.FUTURA)) {
            mostrarMensajeRestringido();
            return;
        }
        
        inicializarComponentes();
        cargarJugadoresEquipo();
        cargarEquiposEnCombo();
        cargarJugadoresDisponibles();
    }
    
    private void mostrarMensajeRestringido() {
        // Crear panel informativo
        JPanel panelInfo = new JPanel(new BorderLayout(10, 10));
        panelInfo.setBackground(new Color(20, 24, 31));
        panelInfo.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Icono y mensaje principal
        JPanel panelMensaje = new JPanel();
        panelMensaje.setLayout(new BoxLayout(panelMensaje, BoxLayout.Y_AXIS));
        panelMensaje.setOpaque(false);
        
        JLabel lblIcono = new JLabel("");
        lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMensaje.add(lblIcono);
        
        panelMensaje.add(Box.createVerticalStrut(20));
        
        JLabel lblTitulo = new JLabel("Gestión de Plantilla Restringida");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(231, 76, 60));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMensaje.add(lblTitulo);
        
        panelMensaje.add(Box.createVerticalStrut(15));
        
        String estado = temporada.getEstado().equals(Temporada.EN_JUEGO) ? "EN CURSO" : "FINALIZADA";
        JLabel lblMensaje = new JLabel("<html><center>Solo se pueden gestionar plantillas en temporadas <b>FUTURAS</b>.<br><br>" +
                                       "Estado actual: <b style='color: #F39C12;'>" + estado + "</b><br><br>" +
                                       "Temporada: <b>" + temporada.getNombre() + "</b></center></html>");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMensaje.setForeground(new Color(220, 220, 220));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMensaje.add(lblMensaje);
        
        panelInfo.add(panelMensaje, BorderLayout.CENTER);
        
        // Botón cerrar
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setOpaque(false);
        
        JButton btnCerrar = new JButton("Entendido");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setBackground(new Color(128, 128, 128));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setPreferredSize(new Dimension(150, 40));
        btnCerrar.addActionListener(e -> dispose());
        panelBoton.add(btnCerrar);
        
        panelInfo.add(panelBoton, BorderLayout.SOUTH);
        
        setContentPane(panelInfo);
        
        GestorLog.advertencia("Intento de gestionar plantilla en temporada " + estado + ": " + 
                            temporada.getNombre() + " | Equipo: " + equipo.getNombre());
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(20, 24, 31));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // ===== PANEL PRINCIPAL CON DOS COLUMNAS =====
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2, 20, 0));
        panelPrincipal.setOpaque(false);
        
        // ===== COLUMNA IZQUIERDA: JUGADORES DEL EQUIPO =====
        JPanel panelIzquierda = new JPanel(new BorderLayout(10, 10));
        panelIzquierda.setOpaque(false);
        
        JLabel lblTituloEquipo = new JLabel("👥 Plantilla Actual (" + equipo.getNombre() + ")");
        lblTituloEquipo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloEquipo.setForeground(new Color(100, 181, 246));
        lblTituloEquipo.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierda.add(lblTituloEquipo, BorderLayout.NORTH);
        
        modeloEquipo = new DefaultListModel<>();
        listaJugadoresEquipo = new JList<>(modeloEquipo);
        listaJugadoresEquipo.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaJugadoresEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        listaJugadoresEquipo.setBackground(new Color(40, 44, 52));
        listaJugadoresEquipo.setForeground(new Color(255, 255, 255));
        listaJugadoresEquipo.setCellRenderer(new JugadorRenderer());
        
        JScrollPane scrollEquipo = new JScrollPane(listaJugadoresEquipo);
        scrollEquipo.setBorder(BorderFactory.createLineBorder(new Color(100, 181, 246), 2));
        panelIzquierda.add(scrollEquipo, BorderLayout.CENTER);
        
        // Botón eliminar jugadores
        JButton btnEliminar = new JButton("❌ Dar de Baja Seleccionados");
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setPreferredSize(new Dimension(0, 40));
        btnEliminar.addActionListener(e -> darDeBajaJugadores());
        panelIzquierda.add(btnEliminar, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelIzquierda);
        
        // ===== COLUMNA DERECHA: JUGADORES DISPONIBLES =====
        JPanel panelDerecha = new JPanel(new BorderLayout(10, 10));
        panelDerecha.setOpaque(false);
        
        JLabel lblTituloDisponibles = new JLabel("🔍 Jugadores Disponibles");
        lblTituloDisponibles.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloDisponibles.setForeground(new Color(255, 183, 77));
        lblTituloDisponibles.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel superior derecho (título + filtro)
        JPanel panelSuperiorDerecho = new JPanel(new BorderLayout(5, 5));
        panelSuperiorDerecho.setOpaque(false);
        panelSuperiorDerecho.add(lblTituloDisponibles, BorderLayout.NORTH);
        
        // Panel de filtro
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltro.setOpaque(false);
        
        JLabel lblFiltro = new JLabel("Filtrar por equipo:");
        lblFiltro.setForeground(Color.WHITE);
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelFiltro.add(lblFiltro);
        
        comboFiltroEquipo = new JComboBox<>();
        comboFiltroEquipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboFiltroEquipo.setPreferredSize(new Dimension(250, 30));
        comboFiltroEquipo.addActionListener(e -> cargarJugadoresDisponibles());
        panelFiltro.add(comboFiltroEquipo);
        
        panelSuperiorDerecho.add(panelFiltro, BorderLayout.SOUTH);
        panelDerecha.add(panelSuperiorDerecho, BorderLayout.NORTH);
        
        modeloDisponibles = new DefaultListModel<>();
        listaJugadoresDisponibles = new JList<>(modeloDisponibles);
        listaJugadoresDisponibles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaJugadoresDisponibles.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        listaJugadoresDisponibles.setBackground(new Color(40, 44, 52));
        listaJugadoresDisponibles.setForeground(new Color(255, 255, 255));
        listaJugadoresDisponibles.setCellRenderer(new JugadorRenderer());
        
        JScrollPane scrollDisponibles = new JScrollPane(listaJugadoresDisponibles);
        scrollDisponibles.setBorder(BorderFactory.createLineBorder(new Color(255, 183, 77), 2));
        panelDerecha.add(scrollDisponibles, BorderLayout.CENTER);
        
        // Botón fichar jugadores
        JButton btnFichar = new JButton("✅ Fichar Seleccionados");
        btnFichar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFichar.setBackground(new Color(46, 204, 113));
        btnFichar.setForeground(Color.WHITE);
        btnFichar.setFocusPainted(false);
        btnFichar.setBorderPainted(false);
        btnFichar.setPreferredSize(new Dimension(0, 40));
        btnFichar.addActionListener(e -> ficharJugadores());
        panelDerecha.add(btnFichar, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelDerecha);
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        // ===== PANEL INFERIOR: BOTÓN CERRAR =====
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelInferior.setOpaque(false);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCerrar.setBackground(new Color(128, 128, 128));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setPreferredSize(new Dimension(100, 40));
        btnCerrar.addActionListener(e -> dispose());
        panelInferior.add(btnCerrar);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void cargarJugadoresEquipo() {
        modeloEquipo.clear();
        for (Jugador j : equipo.getPlantilla()) {
            modeloEquipo.addElement(j);
        }
    }
    
    private void cargarEquiposEnCombo() {
        comboFiltroEquipo.removeAllItems();
        comboFiltroEquipo.addItem("_SIN_EQUIPO_");
        comboFiltroEquipo.addItem("--- Todos los equipos ---");
        
        for (Equipo eq : temporada.getEquiposParticipantes()) {
            if (!eq.getNombre().equals("_SIN_EQUIPO_") && !eq.equals(equipo)) {
                comboFiltroEquipo.addItem(eq.getNombre());
            }
        }
        
        comboFiltroEquipo.setSelectedIndex(0); // Por defecto "_SIN_EQUIPO_"
    }
    
    private void cargarJugadoresDisponibles() {
        modeloDisponibles.clear();
        
        String filtro = (String) comboFiltroEquipo.getSelectedItem();
        if (filtro == null) return;
        
        if (filtro.equals("--- Todos los equipos ---")) {
            // Mostrar jugadores de TODOS los equipos excepto el actual
            for (Equipo eq : temporada.getEquiposParticipantes()) {
                if (!eq.equals(equipo)) {
                    for (Jugador j : eq.getPlantilla()) {
                        modeloDisponibles.addElement(j);
                    }
                }
            }
        } else {
            // Mostrar jugadores del equipo específico
            for (Equipo eq : temporada.getEquiposParticipantes()) {
                if (eq.getNombre().equals(filtro)) {
                    for (Jugador j : eq.getPlantilla()) {
                        modeloDisponibles.addElement(j);
                    }
                    break;
                }
            }
        }
    }
    
    private void ficharJugadores() {
        List<Jugador> seleccionados = listaJugadoresDisponibles.getSelectedValuesList();
        
        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Selecciona al menos un jugador para fichar",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Fichar " + seleccionados.size() + " jugador(es) en " + equipo.getNombre() + "?",
            "Confirmar fichajes",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmar != JOptionPane.YES_OPTION) return;
        
        int fichados = 0;
        StringBuilder errores = new StringBuilder();
        
        for (Jugador j : seleccionados) {
            try {
                // Buscar equipo origen y eliminarlo
                Equipo equipoOrigen = null;
                for (Equipo eq : temporada.getEquiposParticipantes()) {
                    if (eq.getPlantilla().contains(j)) {
                        equipoOrigen = eq;
                        break;
                    }
                }
                
                if (equipoOrigen != null) {
                    equipoOrigen.getPlantilla().remove(j);
                }
                
                // Fichar en equipo destino
                equipo.ficharJugador(j);
                fichados++;
                
            } catch (IllegalArgumentException ex) {
                errores.append("• ").append(j.getNombre()).append(": ").append(ex.getMessage()).append("\n");
            }
        }
        
        // Actualizar vistas
        cargarJugadoresEquipo();
        cargarJugadoresDisponibles();
        
        // Mensaje final
        if (fichados > 0) {
            GestorLog.exito(fichados + " jugador(es) fichados en " + equipo.getNombre());
        }
        
        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this,
                "Algunos fichajes fallaron:\n\n" + errores.toString(),
                "Errores en fichajes",
                JOptionPane.WARNING_MESSAGE);
        } else if (fichados > 0) {
            JOptionPane.showMessageDialog(this,
                fichados + " jugador(es) fichados correctamente",
                "Fichajes completados",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void darDeBajaJugadores() {
        List<Jugador> seleccionados = listaJugadoresEquipo.getSelectedValuesList();
        
        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Selecciona al menos un jugador para dar de baja",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Dar de baja a " + seleccionados.size() + " jugador(es) de " + equipo.getNombre() + "?\n" +
            "Serán movidos a '_SIN_EQUIPO_'",
            "Confirmar bajas",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmar != JOptionPane.YES_OPTION) return;
        
        // Obtener o crear equipo fantasma
        Equipo equipoFantasma = obtenerOCrearEquipoFantasma();
        
        int bajas = 0;
        for (Jugador j : seleccionados) {
            equipo.getPlantilla().remove(j);
            equipoFantasma.getPlantilla().add(j);
            bajas++;
        }
        
        // Actualizar vistas
        cargarJugadoresEquipo();
        cargarJugadoresDisponibles();
        
        GestorLog.exito(bajas + " jugador(es) dados de baja de " + equipo.getNombre());
        
        JOptionPane.showMessageDialog(this,
            bajas + " jugador(es) dados de baja correctamente.\n" +
            "Ahora están en '_SIN_EQUIPO_'",
            "Bajas completadas",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private Equipo obtenerOCrearEquipoFantasma() {
        // Buscar equipo fantasma en la temporada
        for (Equipo eq : temporada.getEquiposParticipantes()) {
            if (eq.getNombre().equals("_SIN_EQUIPO_")) {
                return eq;
            }
        }
        
        // Si no existe, crearlo
        Equipo fantasma = new Equipo("_SIN_EQUIPO_");
        temporada.getEquiposParticipantes().add(fantasma);
        GestorLog.info("Equipo fantasma creado en " + temporada.getNombre());
        return fantasma;
    }
    
    /**
     * Renderer personalizado para mostrar jugadores con más información
     */
    private class JugadorRenderer extends DefaultListCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Jugador) {
                Jugador j = (Jugador) value;
                label.setText(String.format("  %s  |  %s  |  %d años  |  Dorsal: %d",
                    j.getNombre(), j.getPosicion(), j.getEdad(), j.getDorsal()));
                label.setBorder(new EmptyBorder(8, 10, 8, 10));
            }
            
            if (isSelected) {
                label.setBackground(new Color(52, 152, 219));
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(new Color(40, 44, 52));
                label.setForeground(new Color(220, 220, 220));
            }
            
            return label;
        }
    }
}
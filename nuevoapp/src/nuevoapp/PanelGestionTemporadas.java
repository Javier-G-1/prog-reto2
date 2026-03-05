package nuevoapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import gestion.*;
import logica.*;

/**
 * Panel de gestión completa de temporadas.
 * Muestra todas las temporadas con sus estados y permite crear nuevas.
 * ⭐ ACTUALIZADO: Usa TemaColores para consistencia visual
 */
public class PanelGestionTemporadas extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DatosFederacion datosFederacion;
    private VentanaMain ventanaPrincipal;
    
    private JPanel panelTarjetas;
    private JScrollPane scrollPane;
    private JButton btnNuevaTemporada;
    
    /**
     * Constructor del panel
     */
    public PanelGestionTemporadas(DatosFederacion datos, VentanaMain ventana) {
        this.datosFederacion = datos;
        this.ventanaPrincipal = ventana;
        
        setLayout(new BorderLayout(10, 10));
        setBackground(TemaColores.FONDO_PRINCIPAL);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        inicializarComponentes();
        cargarTemporadas();
    }
    
    /**
     * Inicializa los componentes visuales
     */
    private void inicializarComponentes() {
        // Panel superior con título y botón
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 0));
        panelSuperior.setBackground(TemaColores.FONDO_PRINCIPAL);
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblTitulo = new JLabel("GESTIÓN DE TEMPORADAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(TemaColores.TEXTO_PRIMARIO);
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        btnNuevaTemporada = new JButton("+ Nueva Temporada");
        btnNuevaTemporada.setBackground(TemaColores.ACENTO_VERDE);
        btnNuevaTemporada.setForeground(Color.WHITE);
        btnNuevaTemporada.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNuevaTemporada.setFocusPainted(false);
        btnNuevaTemporada.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnNuevaTemporada.addActionListener(e -> crearNuevaTemporada());
        panelSuperior.add(btnNuevaTemporada, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con tarjetas de temporadas
        panelTarjetas = new JPanel();
        panelTarjetas.setLayout(new BoxLayout(panelTarjetas, BoxLayout.Y_AXIS));
        panelTarjetas.setBackground(TemaColores.FONDO_SECUNDARIO);
        
        scrollPane = new JScrollPane(panelTarjetas);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        
        add(scrollPane, BorderLayout.CENTER);
    }
   
    /**
     * Carga y muestra todas las temporadas
     */
    public void cargarTemporadas() {
        panelTarjetas.removeAll();
        
        java.util.List<Temporada> temporadas = datosFederacion.getListaTemporadas();
        
        if (temporadas.isEmpty()) {
            JLabel lblVacio = new JLabel("No hay temporadas creadas. Crea una nueva temporada para comenzar.");
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblVacio.setForeground(TemaColores.TEXTO_TERCIARIO);
            lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblVacio.setBorder(new EmptyBorder(50, 20, 50, 20));
            panelTarjetas.add(lblVacio);
        } else {
            for (Temporada temp : temporadas) {
                panelTarjetas.add(crearTarjetaTemporada(temp));
                panelTarjetas.add(Box.createVerticalStrut(10));
            }
        }
        
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }
    
    /**
     * Crea una tarjeta visual para una temporada
     * ⭐ CORREGIDO: Excluye equipo fantasma del conteo + Usa TemaColores
     */
    private JPanel crearTarjetaTemporada(Temporada temp) {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 10));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        tarjeta.setBackground(TemaColores.FONDO_TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TemaColores.BORDE_NORMAL, 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Panel izquierdo: Información básica
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        
        // Nombre de la temporada
        JLabel lblNombre = new JLabel(temp.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNombre.setForeground(TemaColores.TEXTO_PRIMARIO);
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(5));
        
        // Estado con colores del tema
        JLabel lblEstado = new JLabel("● " + temp.getEstado());
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        switch (temp.getEstado()) {
            case Temporada.FUTURA:
                lblEstado.setForeground(TemaColores.ESTADO_FUTURA);
                break;
            case Temporada.EN_JUEGO:
                lblEstado.setForeground(TemaColores.ESTADO_EN_CURSO);
                break;
            case Temporada.TERMINADA:
                lblEstado.setForeground(TemaColores.ESTADO_FINALIZADA);
                break;
        }
        panelInfo.add(lblEstado);
        panelInfo.add(Box.createVerticalStrut(10));
        
        // ⭐ ESTADÍSTICAS CORREGIDAS: Excluir equipo fantasma
        int numEquipos = 0;
        int numJugadores = 0;
        
        for (Equipo eq : temp.getEquiposParticipantes()) {
            // Saltar el equipo fantasma
            if (eq.getNombre().equals("_SIN_EQUIPO_")) {
                continue;
            }
            numEquipos++;
            numJugadores += eq.getPlantilla().size();
        }
        
        int numJornadas = temp.getListaJornadas().size();
        
        JLabel lblStats = new JLabel(String.format(" %d equipos  |   %d jornadas  |   %d jugadores", 
            numEquipos, numJornadas, numJugadores));
        lblStats.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStats.setForeground(TemaColores.TEXTO_TERCIARIO);
        panelInfo.add(lblStats);
        
        tarjeta.add(panelInfo, BorderLayout.CENTER);
        
        // Panel derecho: Botones de acción
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        
        JButton btnVerClasificacion = new JButton("📊 Ver Clasificación");
        btnVerClasificacion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVerClasificacion.setBackground(TemaColores.ACENTO_CYAN);
        btnVerClasificacion.setForeground(TemaColores.TEXTO_PRIMARIO);
        btnVerClasificacion.setFocusPainted(false);
        btnVerClasificacion.setBorderPainted(false);
        btnVerClasificacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerClasificacion.setMaximumSize(new Dimension(200, 35));
        btnVerClasificacion.addActionListener(e -> verClasificacion(temp));
        
        JButton btnDetalles = new JButton("🔍 Ver Detalles");
        btnDetalles.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDetalles.setBackground(TemaColores.BOTON_NEUTRAL);
        btnDetalles.setForeground(TemaColores.TEXTO_PRIMARIO);
        btnDetalles.setFocusPainted(false);
        btnDetalles.setBorderPainted(false);
        btnDetalles.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDetalles.setMaximumSize(new Dimension(200, 35));
        btnDetalles.addActionListener(e -> verDetallesTemporada(temp));
        
        panelBotones.add(btnVerClasificacion);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(btnDetalles);
        
        tarjeta.add(panelBotones, BorderLayout.EAST);
        
        return tarjeta;
    }
    
    /**
     * ⭐ CORRECCIÓN: Validar orden de temporadas y evitar múltiples EN_JUEGO
     */
    private void crearNuevaTemporada() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la Temporada (ej: 2026/27):");
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }
        
        // Validar que no exista ya
        if (datosFederacion.buscarTemporadaPorNombre(nombre) != null) {
            JOptionPane.showMessageDialog(this,
                "Ya existe una temporada con el nombre: " + nombre,
                "Temporada duplicada",
                JOptionPane.WARNING_MESSAGE);
            GestorLog.advertencia("Intento de crear temporada duplicada: " + nombre);
            return;
        }
        
        GestorTemporadas gestor = new GestorTemporadas();
        
        // ⭐ VALIDACIÓN 1: No puede haber temporada EN_JUEGO
        if (gestor.existeTemporadaEnCurso(datosFederacion)) {
            Temporada enCurso = gestor.obtenerTemporadaEnCurso(datosFederacion);
            
            JOptionPane.showMessageDialog(this,
                " No se puede crear una nueva temporada.\n\n" +
                "Temporada en curso: " + enCurso.getNombre() + "\n\n" +
                "Debes finalizarla antes de crear una nueva temporada.",
                "Temporada en curso activa",
                JOptionPane.WARNING_MESSAGE);
            
            GestorLog.advertencia("Intento de crear temporada con " + enCurso.getNombre() + " EN_JUEGO");
            return;
        }
        
        // ⭐ VALIDACIÓN 2: Solo puede haber una temporada FUTURA a la vez
        int temporadasFuturas = 0;
        Temporada tempFuturaExistente = null;
        for (Temporada t : datosFederacion.getListaTemporadas()) {
            if (t.getEstado().equals(Temporada.FUTURA)) {
                temporadasFuturas++;
                tempFuturaExistente = t;
            }
        }
        
        if (temporadasFuturas > 0) {
            JOptionPane.showMessageDialog(this,
                " Ya existe una temporada FUTURA pendiente de iniciar.\n\n" +
                "Temporada futura: " + tempFuturaExistente.getNombre() + "\n\n" +
                "Debes iniciarla (crear jornadas) antes de crear una nueva temporada futura.",
                "Temporada futura existente",
                JOptionPane.WARNING_MESSAGE);
            
            GestorLog.advertencia("Intento de crear segunda temporada FUTURA con " + tempFuturaExistente.getNombre() + " pendiente");
            return;
        }
        
        // Obtener temporadas finalizadas para copiar
        java.util.List<Temporada> temporadasFinalizadas = gestor.obtenerTemporadasFinalizadas(datosFederacion);
        Temporada temporadaOrigen = null;
        
        if (!temporadasFinalizadas.isEmpty()) {
            String[] opciones = new String[temporadasFinalizadas.size() + 1];
            opciones[0] = "--- Crear temporada vacía ---";
            
            for (int i = 0; i < temporadasFinalizadas.size(); i++) {
                Temporada t = temporadasFinalizadas.get(i);
                
                // ⭐ Contar equipos reales (sin fantasma)
                int equipos = 0;
                int jugadores = 0;
                for (Equipo eq : t.getEquiposParticipantes()) {
                    if (!eq.getNombre().equals("_SIN_EQUIPO_")) {
                        equipos++;
                        jugadores += eq.getPlantilla().size();
                    }
                }
                
                opciones[i + 1] = t.getNombre() + " (" + equipos + " equipos, " + jugadores + " jugadores)";
            }
            
            String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Selecciona de qué temporada copiar los datos:\n(Equipos, jugadores y configuraciones)",
                "Origen de datos para " + nombre,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[opciones.length - 1]
            );
            
            if (seleccion == null) {
                GestorLog.info("Creación de temporada cancelada por el usuario");
                return;
            }
            
            if (!seleccion.equals(opciones[0])) {
                String nombreTemp = seleccion.split(" \\(")[0];
                temporadaOrigen = datosFederacion.buscarTemporadaPorNombre(nombreTemp);
                
                // ⭐ Contar equipos reales para el mensaje de confirmación
                int equipos = 0;
                int jugadores = 0;
                for (Equipo eq : temporadaOrigen.getEquiposParticipantes()) {
                    if (!eq.getNombre().equals("_SIN_EQUIPO_")) {
                        equipos++;
                        jugadores += eq.getPlantilla().size();
                    }
                }
                
                int confirmar = JOptionPane.showConfirmDialog(this,
                    "Se copiarán automáticamente:\n\n" +
                    " Equipos: " + equipos + "\n" +
                    " Jugadores: " + jugadores + "\n\n" +
                    "Desde: " + temporadaOrigen.getNombre() + "\n" +
                    "Hacia: " + nombre + "\n\n" +
                    "¿Continuar?",
                    "Confirmar creación de temporada",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                
                if (confirmar != JOptionPane.YES_OPTION) {
                    GestorLog.info("Creación de temporada cancelada por el usuario");
                    return;
                }
            }
        }
        
        // Crear la temporada
        boolean creada = gestor.crearTemporadaFutura(nombre, datosFederacion, temporadaOrigen);
        
        if (creada) {
            Temporada nuevaCreada = datosFederacion.buscarTemporadaPorNombre(nombre);
            if (nuevaCreada != null) {
                // ⭐ Contar equipos reales (sin fantasma)
                int equiposFinales = 0;
                int jugadoresFinales = 0;
                for (Equipo eq : nuevaCreada.getEquiposParticipantes()) {
                    if (!eq.getNombre().equals("_SIN_EQUIPO_")) {
                        equiposFinales++;
                        jugadoresFinales += eq.getPlantilla().size();
                    }
                }
                
                String mensaje;
                if (temporadaOrigen != null) {
                    mensaje = " Temporada " + nombre + " creada con éxito.\n\n" +
                              " " + equiposFinales + " equipos inscritos\n" +
                              " " + jugadoresFinales + " jugadores fichados\n\n" +
                              " Copiados desde: " + temporadaOrigen.getNombre();
                } else {
                    mensaje = equiposFinales > 0 
                        ? " Temporada " + nombre + " creada con éxito.\n\n" +
                          " " + equiposFinales + " equipos inscritos\n" +
                          " " + jugadoresFinales + " jugadores fichados"
                        : " Temporada " + nombre + " creada con éxito.\n\n" +
                          " Temporada vacía (sin equipos)";
                }
                
                JOptionPane.showMessageDialog(this, mensaje, 
                    "Temporada creada", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Recargar la vista
            cargarTemporadas();
            
            // Sincronizar combos de la ventana principal
            ventanaPrincipal.sincronizarCombos();
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo crear la temporada. Revisa los logs.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Muestra la clasificación de una temporada
     */
    private void verClasificacion(Temporada temp) {
        if (temp.getListaJornadas().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La temporada '" + temp.getNombre() + "' aún no tiene jornadas jugadas.",
                "Sin clasificación",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            Clasificacion clasificacion = CalculadoraClasificacion.calcular(temp);
            
            // Crear diálogo de clasificación
            JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                "Clasificación - " + temp.getNombre(), true);
            dialogo.setSize(800, 600);
            dialogo.setLocationRelativeTo(this);
            
            PanelClasificacion panelClasif = new PanelClasificacion(datosFederacion);
            panelClasif.actualizarClasificacion(temp);
            
            dialogo.add(panelClasif);
            dialogo.setVisible(true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al calcular la clasificación: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            GestorLog.error("Error al mostrar clasificación de " + temp.getNombre() + ": " + e.getMessage());
        }
    }
    
    /**
     * ⭐ CORRECCIÓN: Mostrar detalles sin contar equipo fantasma
     */
    private void verDetallesTemporada(Temporada temp) {
        StringBuilder detalles = new StringBuilder();
        detalles.append(" TEMPORADA: ").append(temp.getNombre()).append("\n\n");
        detalles.append(" Estado: ").append(temp.getEstado()).append("\n\n");
        
        detalles.append("═══════════════════════════════════\n");
        
        // ⭐ Contar equipos reales
        int equiposReales = 0;
        for (Equipo eq : temp.getEquiposParticipantes()) {
            if (!eq.getNombre().equals("_SIN_EQUIPO_")) {
                equiposReales++;
            }
        }
        
        detalles.append(" EQUIPOS PARTICIPANTES (").append(equiposReales).append(")\n");
        detalles.append("═══════════════════════════════════\n");
        
        for (Equipo eq : temp.getEquiposParticipantes()) {
            if (!eq.getNombre().equals("_SIN_EQUIPO_")) {
                detalles.append("  • ").append(eq.getNombre())
                       .append(" (").append(eq.getPlantilla().size()).append(" jugadores)\n");
            }
        }
        
        detalles.append("\n═══════════════════════════════════\n");
        detalles.append(" JORNADAS (").append(temp.getListaJornadas().size()).append(")\n");
        detalles.append("═══════════════════════════════════\n");
        
        int partidosJugados = 0;
        int partidosPendientes = 0;
        
        for (Jornada jor : temp.getListaJornadas()) {
            int jugados = 0;
            int pendientes = 0;
            for (Partido p : jor.getListaPartidos()) {
                if (p.isFinalizado()) {
                    jugados++;
                    partidosJugados++;
                } else {
                    pendientes++;
                    partidosPendientes++;
                }
            }
            detalles.append("  • ").append(jor.getNombre())
                   .append(" - Jugados: ").append(jugados)
                   .append(" | Pendientes: ").append(pendientes).append("\n");
        }
        
        detalles.append("\n══════════════════════════════════\n");
        detalles.append(" RESUMEN\n");
        detalles.append("═══════════════════════════════════\n");
        detalles.append(" Partidos jugados: ").append(partidosJugados).append("\n");
        detalles.append(" Partidos pendientes: ").append(partidosPendientes).append("\n");
        
        JTextArea textArea = new JTextArea(detalles.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(TemaColores.FONDO_SECUNDARIO);
        textArea.setForeground(TemaColores.TEXTO_PRIMARIO);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scroll, 
            "Detalles - " + temp.getNombre(), 
            JOptionPane.INFORMATION_MESSAGE);
        
        GestorLog.info("Detalles visualizados: " + temp.getNombre());
    }

    /**
     * Controla la visibilidad del botón de nueva temporada
     * (usado por VentanaMain según el rol del usuario)
     */
    public void setBotonNuevaTemporadaVisible(boolean visible) {
        btnNuevaTemporada.setVisible(visible);
    }
}
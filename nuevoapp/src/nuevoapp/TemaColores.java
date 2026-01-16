package nuevoapp;

import java.awt.Color;

/**
 * Clase que centraliza todos los colores de la aplicación para mejorar la legibilidad.
 */
public class TemaColores {
    
    // === FONDOS ===
    public static final Color FONDO_PRINCIPAL = new Color(20, 24, 31);
    public static final Color FONDO_SECUNDARIO = new Color(30, 34, 41);
    public static final Color FONDO_TARJETA = new Color(40, 44, 52);
    public static final Color FONDO_MENU = new Color(25, 28, 35);
    
    // === TEXTOS (MEJORADOS PARA MEJOR CONTRASTE) ===
    public static final Color TEXTO_PRIMARIO = new Color(245, 245, 245);      // Blanco casi puro
    public static final Color TEXTO_SECUNDARIO = new Color(220, 220, 220);    // Gris muy claro
    public static final Color TEXTO_TERCIARIO = new Color(180, 185, 195);     // Gris medio claro
    public static final Color TEXTO_DESHABILITADO = new Color(120, 125, 135); // Gris medio
    
    // === ACENTOS ===
    public static final Color ACENTO_AZUL = new Color(52, 152, 219);
    public static final Color ACENTO_AZUL_HOVER = new Color(72, 172, 239);
    public static final Color ACENTO_VERDE = new Color(46, 204, 113);
    public static final Color ACENTO_ROJO = new Color(231, 76, 60);
    public static final Color ACENTO_AMARILLO = new Color(241, 196, 15);
    public static final Color ACENTO_NARANJA = new Color(255, 183, 77);
    
    // === BOTONES ===
    public static final Color BOTON_PRIMARIO = new Color(45, 55, 140);
    public static final Color BOTON_EXITO = new Color(0, 128, 0);
    public static final Color BOTON_PELIGRO = new Color(231, 76, 60);
    public static final Color BOTON_NEUTRAL = new Color(100, 100, 110);
    
    // === ESTADOS ===
    public static final Color ESTADO_FUTURA = new Color(52, 152, 219);      // Azul
    public static final Color ESTADO_EN_CURSO = new Color(241, 196, 15);    // Amarillo
    public static final Color ESTADO_FINALIZADA = new Color(231, 76, 60);   // Rojo
    
    // === BORDES ===
    public static final Color BORDE_NORMAL = new Color(70, 74, 82);
    public static final Color BORDE_SELECCIONADO = new Color(100, 150, 255);
    public static final Color BORDE_ERROR = new Color(231, 76, 60);
    
    // === EQUIPOS (LOCAL/VISITANTE) ===
    public static final Color EQUIPO_LOCAL = new Color(100, 181, 246);
    public static final Color EQUIPO_VISITANTE = new Color(255, 183, 77);
    
    // === PARTIDOS ===
    public static final Color PARTIDO_PENDIENTE = new Color(52, 152, 219);
    public static final Color PARTIDO_FINALIZADO = new Color(231, 76, 60);
}
package nuevoapp;

import java.awt.Color;
import vista.Paleta;

/**
 * Clase que centraliza todos los colores de la aplicación.
 * Basada en la paleta oficial ORIGO Waterpolo ({@link Paleta}).
 */
public class TemaColores {

    // === FONDOS ===
    public static final Color FONDO_PRINCIPAL  = Paleta.PRIMARIO;   // azul agua — fondos principales, headers
    public static final Color FONDO_SECUNDARIO = Paleta.NEUTRO3;    // gris cálido — paneles secundarios
    public static final Color FONDO_TARJETA    = Paleta.NEUTRO3;    // gris cálido — tarjetas
    public static final Color FONDO_MENU       = Paleta.NEUTRO3;    // gris cálido — menú lateral

    // === TEXTOS ===
    public static final Color TEXTO_PRIMARIO      = new Color(245, 245, 245); // blanco casi puro
    public static final Color TEXTO_SECUNDARIO    = Paleta.NEUTRO2;           // gris azulado
    public static final Color TEXTO_TERCIARIO     = new Color(180, 185, 195);
    public static final Color TEXTO_DESHABILITADO = new Color(120, 125, 135);

    // === ACENTOS ===
    public static final Color ACENTO_AZUL       = Paleta.PRIMARIO;             // azul agua
    public static final Color ACENTO_AZUL_HOVER = Paleta.PRIMARIO.brighter();
    public static final Color ACENTO_VERDE      = new Color(46, 204, 113);
    public static final Color ACENTO_ROJO       = Paleta.ACENTO;               // rosa
    public static final Color ACENTO_AMARILLO   = Paleta.SECUNDARIO;           // dorado
    public static final Color ACENTO_NARANJA    = Paleta.SECUNDARIO;           // dorado

    // === BOTONES ===
    public static final Color BOTON_PRIMARIO = Paleta.SECUNDARIO;  // dorado — acción principal
    public static final Color BOTON_EXITO    = new Color(0, 128, 0);
    public static final Color BOTON_PELIGRO  = Paleta.ACENTO;      // rosa — alerta/peligro
    public static final Color BOTON_NEUTRAL  = Paleta.NEUTRO2;     // gris azulado

    // === ESTADOS ===
    public static final Color ESTADO_FUTURA     = Paleta.PRIMARIO;   // azul agua
    public static final Color ESTADO_EN_CURSO   = Paleta.SECUNDARIO; // dorado
    public static final Color ESTADO_FINALIZADA = Paleta.ACENTO;     // rosa

    // === BORDES ===
    public static final Color BORDE_NORMAL      = Paleta.NEUTRO1;   // malva
    public static final Color BORDE_SELECCIONADO = Paleta.PRIMARIO; // azul agua
    public static final Color BORDE_ERROR       = Paleta.ACENTO;    // rosa

    // === EQUIPOS (LOCAL/VISITANTE) ===
    public static final Color EQUIPO_LOCAL     = Paleta.PRIMARIO;   // azul agua
    public static final Color EQUIPO_VISITANTE = Paleta.SECUNDARIO; // dorado

    // === PARTIDOS ===
    public static final Color PARTIDO_PENDIENTE  = Paleta.PRIMARIO; // azul agua
    public static final Color PARTIDO_FINALIZADO = Paleta.ACENTO;   // rosa
}
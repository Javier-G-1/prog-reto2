package nuevoapp;

import java.awt.Color;
import vista.Paleta;

/**
 * Clase que centraliza todos los colores semánticos de la aplicación.
 * Basada en la paleta oficial ORIGO Waterpolo ({@link Paleta}).
 */
public class TemaColores {

    // === FONDOS ===
    public static final Color FONDO_PRINCIPAL  = Paleta.OCEANO;     // azul océano — fondos principales, headers
    public static final Color FONDO_SECUNDARIO = Paleta.NEUTRO2;    // gris azulado — paneles secundarios
    public static final Color FONDO_TARJETA    = Paleta.NEUTRO2;    // gris azulado — tarjetas
    public static final Color FONDO_MENU       = Paleta.OCEANO;     // azul océano — menú lateral

    // === TEXTOS ===
    public static final Color TEXTO_PRIMARIO      = new Color(245, 245, 245);        // blanco casi puro
    public static final Color TEXTO_SECUNDARIO    = new Color(0xD0, 0xF4, 0xF8);    // cyan muy pálido — texto secundario
    public static final Color TEXTO_TERCIARIO     = new Color(0xA8, 0xE8, 0xF0);    // cyan suave — texto terciario
    public static final Color TEXTO_DESHABILITADO = new Color(0x98, 0x71, 0x89);    // malva — deshabilitado

    // === ACENTOS ===
    public static final Color ACENTO_CYAN       = Paleta.PRIMARIO;                  // cyan brillante — acento principal
    public static final Color ACENTO_CYAN_HOVER = Paleta.SECUNDARIO;                // cyan medio — hover
    public static final Color ACENTO_VERDE      = new Color(0x00, 0xC8, 0x53);      // verde éxito (#00C853)
    public static final Color ACENTO_ROJO       = new Color(0xE6, 0x39, 0x46);      // rojo alerta (#E63946)
    public static final Color ACENTO_AMARILLO   = Paleta.DORADO;                    // dorado ámbar — stats
    public static final Color ACENTO_NARANJA    = Paleta.DORADO;                    // dorado ámbar — alias

    // === BOTONES ===
    // · Primario: cyan brillante con texto oscuro
    public static final Color BOTON_PRIMARIO        = Paleta.PRIMARIO;              // #2DE5EF — fondo botón primario
    public static final Color BOTON_PRIMARIO_TEXTO  = new Color(0x0A, 0x0F, 0x1A); // #0a0f1a — texto sobre botón primario

    // · Outline: fondo transparente, borde y texto cyan brillante
    public static final Color BOTON_OUTLINE_BORDE   = Paleta.PRIMARIO;              // #2DE5EF — borde y texto outline
    public static final Color BOTON_OUTLINE_TEXTO   = Paleta.PRIMARIO;              // #2DE5EF — igual que borde

    // · Acento: fucsia con texto blanco
    public static final Color BOTON_ACENTO          = Paleta.ACENTO;               // #F06ECC — fondo botón acento
    public static final Color BOTON_ACENTO_TEXTO    = Color.WHITE;                  // blanco — texto sobre botón acento

    // · Otros
    public static final Color BOTON_EXITO           = new Color(0x00, 0xC8, 0x53); // verde — éxito/confirmar
    public static final Color BOTON_PELIGRO         = new Color(0xE6, 0x39, 0x46); // rojo — alerta/cancelar
    public static final Color BOTON_NEUTRAL         = Paleta.NEUTRO1;              // malva — neutral

    // === ESTADOS ===
    public static final Color ESTADO_FUTURA     = Paleta.PRIMARIO;                  // cyan — futura
    public static final Color ESTADO_EN_CURSO   = Paleta.DORADO;                    // dorado — en juego
    public static final Color ESTADO_FINALIZADA = new Color(0xE6, 0x39, 0x46);      // rojo — terminada

    // === BORDES ===
    public static final Color BORDE_NORMAL       = Paleta.NEUTRO1;                  // malva
    public static final Color BORDE_SELECCIONADO = Paleta.PRIMARIO;                 // cyan — selección activa
    public static final Color BORDE_ERROR        = new Color(0xE6, 0x39, 0x46);     // rojo — error

    // === EQUIPOS (LOCAL/VISITANTE) ===
    public static final Color EQUIPO_LOCAL     = Paleta.PRIMARIO;                   // cyan — local
    public static final Color EQUIPO_VISITANTE = Paleta.DORADO;                     // dorado — visitante

    // === PARTIDOS ===
    public static final Color PARTIDO_PENDIENTE  = Paleta.SECUNDARIO;               // cyan medio
    public static final Color PARTIDO_FINALIZADO = new Color(0x00, 0xC8, 0x53);     // verde
}

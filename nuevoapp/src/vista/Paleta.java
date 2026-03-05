package vista;

import java.awt.Color;

/**
 * Paleta de colores oficial de la aplicación ORIGO Waterpolo.
 * <p>
 * Basada en la identidad visual de la Liga de Waterpolo (LM_Grupo5).
 * Centraliza los colores corporativos para su uso uniforme en toda la UI.
 * </p>
 *
 * <ul>
 *   <li><b>PRIMARIO</b>   #2DE5EF → cyan brillante        (color principal de marca)</li>
 *   <li><b>SECUNDARIO</b> #00B8D4 → cyan medio            (variante fría y profunda)</li>
 *   <li><b>OCEANO</b>     #0288D1 → azul océano intenso   (profundidad, fondos oscuros)</li>
 *   <li><b>ACENTO</b>     #F06ECC → fucsia vibrante       (botones de alerta, destacados)</li>
 *   <li><b>DORADO</b>     #EDAC61 → dorado ámbar          (estadísticas, cálidos)</li>
 *   <li><b>NEUTRO1</b>    #987189 → malva                 (bordes, separadores)</li>
 *   <li><b>NEUTRO2</b>    #626D6E → gris azulado          (texto secundario, fondos tabla)</li>
 *   <li><b>NEUTRO3</b>    #6E6C62 → gris cálido           (fondo paneles laterales)</li>
 * </ul>
 */
public class Paleta {

    /** Cyan brillante — color principal de marca (#2DE5EF) */
    public static final Color PRIMARIO   = new Color(0x2D, 0xE5, 0xEF);

    /** Cyan medio, más frío y profundo (#00B8D4) */
    public static final Color SECUNDARIO = new Color(0x00, 0xB8, 0xD4);

    /** Azul océano intenso — profundidad (#0288D1) */
    public static final Color OCEANO     = new Color(0x02, 0x88, 0xD1);

    /** Fucsia vibrante — acento llamativo, alertas (#F06ECC) */
    public static final Color ACENTO     = new Color(0xF0, 0x6E, 0xCC);

    /** Dorado ámbar — destacados cálidos, estadísticas (#EDAC61) */
    public static final Color DORADO     = new Color(0xED, 0xAC, 0x61);

    /** Malva — bordes, separadores (#987189) */
    public static final Color NEUTRO1    = new Color(0x98, 0x71, 0x89);

    /** Gris azulado — texto secundario, fondos tabla (#626D6E) */
    public static final Color NEUTRO2    = new Color(0x62, 0x6D, 0x6E);

    /** Gris cálido — fondo paneles laterales (#6E6C62) */
    public static final Color NEUTRO3    = new Color(0x6E, 0x6C, 0x62);
}

package vista;

import java.awt.Color;

/**
 * Paleta de colores oficial de la aplicación ORIGO Waterpolo.
 * <p>
 * Centraliza los colores corporativos para su uso uniforme en toda la UI.
 * </p>
 *
 * <ul>
 *   <li><b>PRIMARIO</b>   #2DE5F0 → azul agua   (fondos principales, headers)</li>
 *   <li><b>SECUNDARIO</b> #EDAC61 → dorado       (botones de acción)</li>
 *   <li><b>ACENTO</b>     #F06ECC → rosa         (botones de alerta, destacados)</li>
 *   <li><b>NEUTRO1</b>    #987189 → malva        (bordes, separadores)</li>
 *   <li><b>NEUTRO2</b>    #626D6E → gris azulado (texto secundario, fondos de tabla)</li>
 *   <li><b>NEUTRO3</b>    #6E6C62 → gris cálido  (fondo de paneles laterales)</li>
 * </ul>
 */
public class Paleta {

    /** Azul agua — fondos principales, headers (#2DE5F0) */
    public static final Color PRIMARIO   = new Color(0x2D, 0xE5, 0xF0);

    /** Dorado — botones de acción (#EDAC61) */
    public static final Color SECUNDARIO = new Color(0xED, 0xAC, 0x61);

    /** Rosa — botones de alerta, destacados (#F06ECC) */
    public static final Color ACENTO     = new Color(0xF0, 0x6E, 0xCC);

    /** Malva — bordes, separadores (#987189) */
    public static final Color NEUTRO1    = new Color(0x98, 0x71, 0x89);

    /** Gris azulado — texto secundario, fondos de tabla (#626D6E) */
    public static final Color NEUTRO2    = new Color(0x62, 0x6D, 0x6E);

    /** Gris cálido — fondo de paneles laterales (#6E6C62) */
    public static final Color NEUTRO3    = new Color(0x6E, 0x6C, 0x62);
}

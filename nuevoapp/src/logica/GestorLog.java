package logica;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <h2>Gestor centralizado de logging</h2>
 * <p>
 * Sistema de registro de eventos del sistema con archivo único por ejecución.
 * Crea automáticamente un archivo de log en la carpeta {@code logs} con
 * timestamp en el nombre.
 * </p>
 * <p>
 * Permite registrar información general, errores, advertencias, depuración,
 * éxitos, inicio de sesión de usuarios y stack traces de excepciones.
 * </p>
 */
public class GestorLog {

    /** Carpeta donde se almacenan los logs */
    private static final String CARPETA_LOG = "logs";

    /** Archivo de log actual de la ejecución */
    private static String archivoLog;

    // Bloque estático para inicializar al cargar la clase
    static {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        archivoLog = CARPETA_LOG + "/log_" + timestamp + ".txt";

        // Crear carpeta si no existe
        File carpeta = new File(CARPETA_LOG);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Cabecera inicial del log
        escribirLog("SISTEMA", "========================================");
        escribirLog("SISTEMA", "INICIO DE SESIÓN DEL SISTEMA");
        escribirLog("SISTEMA", "Archivo de log: " + archivoLog);
        escribirLog("SISTEMA", "========================================");
    }

    /**
     * Registra un evento genérico.
     * Mantiene compatibilidad con código antiguo.
     *
     * @param evento Descripción del evento
     */
    public static void registrarEvento(String evento) {
        escribirLog("INFO", evento);
    }

    /**
     * Registra información general del sistema.
     *
     * @param mensaje Mensaje a registrar
     */
    public static void info(String mensaje) {
        escribirLog("INFO", mensaje);
    }

    /**
     * Registra un mensaje de éxito.
     *
     * @param mensaje Mensaje a registrar
     */
    public static void exito(String mensaje) {
        escribirLog("✓ ÉXITO", mensaje);
    }

    /**
     * Registra una advertencia.
     *
     * @param mensaje Mensaje de advertencia
     */
    public static void advertencia(String mensaje) {
        escribirLog("⚠ ADVERTENCIA", mensaje);
    }

    /**
     * Registra un error simple sin excepción.
     *
     * @param mensaje Mensaje de error
     */
    public static void error(String mensaje) {
        escribirLog("✗ ERROR", mensaje);
    }

    /**
     * Registra un error junto con una excepción y su stack trace.
     *
     * @param mensaje Mensaje de error
     * @param e Excepción que se desea registrar
     */
    public static void error(String mensaje, Exception e) {
        escribirLog("✗ ERROR", mensaje + " | Excepción: " + e.getMessage());

        // Escribir stack trace completo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoLog, true))) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            bw.write(sw.toString());
            bw.newLine();
        } catch (IOException ex) {
            System.err.println("Error al escribir stack trace: " + ex.getMessage());
        }
    }

    /**
     * Registra un mensaje de depuración (debug).
     *
     * @param mensaje Mensaje de depuración
     */
    public static void debug(String mensaje) {
        escribirLog("DEBUG", mensaje);
    }

    /**
     * Registra el inicio de sesión de un usuario.
     *
     * @param usuario Nombre de usuario que inició sesión
     */
    public static void iniciarSesion(String usuario) {
        escribirLog("LOGIN", "Sesión iniciada - Usuario: " + usuario);
    }

    /**
     * Escribe un mensaje en el archivo de log con tipo y timestamp.
     *
     * @param tipo Tipo de mensaje (INFO, ERROR, DEBUG, etc.)
     * @param mensaje Mensaje a escribir
     */
    private static void escribirLog(String tipo, String mensaje) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoLog, true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);
            bw.write("[" + timestamp + "] [" + tipo + "] " + mensaje);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en el log: " + e.getMessage());
        }
    }

    /**
     * Obtiene la ruta del archivo de log actual.
     *
     * @return Ruta completa del log
     */
    public static String getArchivoLog() {
        return archivoLog;
    }

    /**
     * Escribe un separador visual en el log.
     */
    public static void separador() {
        escribirLog("", "----------------------------------------");
    }
}

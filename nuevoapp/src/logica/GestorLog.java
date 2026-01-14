package logica;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Sistema centralizado de logging con archivo único por ejecución
 */
public class GestorLog {
    private static final String CARPETA_LOG = "logs";
    private static String archivoLog; // Variable, no constante
    
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
        
        // Escribir cabecera del log
        escribirLog("SISTEMA", "========================================");
        escribirLog("SISTEMA", "INICIO DE SESIÓN DEL SISTEMA");
        escribirLog("SISTEMA", "Archivo de log: " + archivoLog);
        escribirLog("SISTEMA", "========================================");
    }
    
    /**
     * Registra un evento genérico (compatibilidad con código antiguo)
     */
    public static void registrarEvento(String evento) {
        escribirLog("INFO", evento);
    }
    
    /**
     * Registra información general
     */
    public static void info(String mensaje) {
        escribirLog("INFO", mensaje);
    }
    
    /**
     * Registra una operación exitosa
     */
    public static void exito(String mensaje) {
        escribirLog("✓ ÉXITO", mensaje);
    }
    
    /**
     * Registra una advertencia
     */
    public static void advertencia(String mensaje) {
        escribirLog("⚠ ADVERTENCIA", mensaje);
    }
    
    /**
     * Registra un error
     */
    public static void error(String mensaje) {
        escribirLog("✗ ERROR", mensaje);
    }
    
    /**
     * Registra un error con excepción
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
     * Registra información de depuración
     */
    public static void debug(String mensaje) {
        escribirLog("DEBUG", mensaje);
    }
    
    /**
     * Registra inicio de sesión de usuario
     */
    public static void iniciarSesion(String usuario) {
        escribirLog("LOGIN", "Sesión iniciada - Usuario: " + usuario);
    }
    
    /**
     * Método privado que escribe en el archivo de log
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
     * Obtiene la ruta del archivo de log actual
     */
    public static String getArchivoLog() {
        return archivoLog;
    }
    
    /**
     * Escribe un separador visual en el log
     */
    public static void separador() {
        escribirLog("", "----------------------------------------");
    }
}
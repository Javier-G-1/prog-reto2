package logica;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Sistema mejorado de logging con niveles de severidad y rotación de archivos
 */
public class GestorLog {
    private static final String DIRECTORIO_LOGS = "logs";
    private static final String ARCHIVO_LOG = DIRECTORIO_LOGS + "/federacion.log";
    private static final String FORMATO_FECHA = "yyyy-MM-dd HH:mm:ss";
    private static final long TAMANIO_MAXIMO_MB = 5; // 5 MB
    
    // Niveles de log
    public enum Nivel {
        INFO("INFO"),
        ADVERTENCIA("WARN"),
        ERROR("ERROR"),
        EXITO("SUCCESS"),
        DEBUG("DEBUG");
        
        private final String etiqueta;
        
        Nivel(String etiqueta) {
            this.etiqueta = etiqueta;
        }
        
        public String getEtiqueta() {
            return etiqueta;
        }
    }
    
    static {
        // Crear el directorio de logs si no existe
        try {
            Files.createDirectories(Paths.get(DIRECTORIO_LOGS));
        } catch (IOException e) {
            System.err.println("No se pudo crear el directorio de logs: " + e.getMessage());
        }
    }
    
    /**
     * MÉTODO DE COMPATIBILIDAD - Para mantener código antiguo funcionando
     * Registra un evento con nivel INFO por defecto
     */
    public static void registrarEvento(String mensaje) {
        info(mensaje);
    }
    
    /**
     * Registra un evento con nivel INFO
     */
    public static void info(String mensaje) {
        registrar(Nivel.INFO, mensaje);
    }
    
    /**
     * Registra un evento exitoso
     */
    public static void exito(String mensaje) {
        registrar(Nivel.EXITO, mensaje);
    }
    
    /**
     * Registra una advertencia
     */
    public static void advertencia(String mensaje) {
        registrar(Nivel.ADVERTENCIA, mensaje);
    }
    
    /**
     * Registra un error
     */
    public static void error(String mensaje) {
        registrar(Nivel.ERROR, mensaje);
    }
    
    /**
     * Registra un error con excepción
     */
    public static void error(String mensaje, Exception e) {
        registrar(Nivel.ERROR, mensaje + " - Excepción: " + e.getMessage());
    }
    
    /**
     * Registra información de debug
     */
    public static void debug(String mensaje) {
        registrar(Nivel.DEBUG, mensaje);
    }
    
    /**
     * Método principal de registro con nivel personalizado
     */
    private static void registrar(Nivel nivel, String mensaje) {
        if (mensaje == null || mensaje.isBlank()) return;
        
        try {
            // Verificar rotación de archivo
            verificarRotacion();
            
            // Escribir el log
            try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_LOG, true))) {
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMATO_FECHA));
                String linea = String.format("[%s] [%s] %s", fecha, nivel.getEtiqueta(), mensaje);
                out.println(linea);
                
                // También imprimir en consola para desarrollo
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el log: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si el archivo de log excede el tamaño máximo y lo rota
     */
    private static void verificarRotacion() {
        try {
            File archivo = new File(ARCHIVO_LOG);
            if (!archivo.exists()) return;
            
            long tamanioBytes = archivo.length();
            long tamanioMB = tamanioBytes / (1024 * 1024);
            
            if (tamanioMB >= TAMANIO_MAXIMO_MB) {
                // Crear backup con timestamp
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String archivoBackup = DIRECTORIO_LOGS + "/federacion_" + timestamp + ".log";
                Files.move(Paths.get(ARCHIVO_LOG), Paths.get(archivoBackup), StandardCopyOption.REPLACE_EXISTING);
                
                info("Log rotado. Archivo anterior guardado como: " + archivoBackup);
            }
        } catch (IOException e) {
            System.err.println("Error al rotar el archivo de log: " + e.getMessage());
        }
    }
    
    /**
     * Registra el inicio de sesión de la aplicación
     */
    public static void iniciarSesion(String usuario) {
        registrar(Nivel.INFO, "=".repeat(80));
        registrar(Nivel.INFO, "INICIO DE SESIÓN - Usuario: " + usuario);
        registrar(Nivel.INFO, "Sistema: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        registrar(Nivel.INFO, "Java: " + System.getProperty("java.version"));
        registrar(Nivel.INFO, "=".repeat(80));
    }
    
    /**
     * Registra el cierre de sesión
     */
    public static void cerrarSesion(String usuario) {
        registrar(Nivel.INFO, "CIERRE DE SESIÓN - Usuario: " + usuario);
        registrar(Nivel.INFO, "=".repeat(80));
    }
    
    /**
     * Lee las últimas N líneas del log
     */
    public static String leerUltimasLineas(int cantidad) {
        StringBuilder resultado = new StringBuilder();
        try {
            File archivo = new File(ARCHIVO_LOG);
            if (!archivo.exists()) return "No hay logs disponibles";
            
            // Leer todas las líneas
            java.util.List<String> lineas = Files.readAllLines(Paths.get(ARCHIVO_LOG));
            
            // Tomar las últimas N líneas
            int inicio = Math.max(0, lineas.size() - cantidad);
            for (int i = inicio; i < lineas.size(); i++) {
                resultado.append(lineas.get(i)).append("\n");
            }
        } catch (IOException e) {
            return "Error al leer el log: " + e.getMessage();
        }
        
        return resultado.toString();
    }
    
    /**
     * Borra todos los logs (usar con precaución)
     */
    public static void limpiarLogs() {
        try {
            File directorio = new File(DIRECTORIO_LOGS);
            if (directorio.exists() && directorio.isDirectory()) {
                for (File archivo : directorio.listFiles()) {
                    if (archivo.getName().endsWith(".log")) {
                        archivo.delete();
                    }
                }
                info("Logs limpiados correctamente");
            }
        } catch (Exception e) {
            error("Error al limpiar logs", e);
        }
    }
}
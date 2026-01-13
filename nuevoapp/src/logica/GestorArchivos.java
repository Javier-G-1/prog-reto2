package logica;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import gestion.*;

/**
 * GESTOR CENTRALIZADO DE PERSISTENCIA Y ARCHIVOS
 * 
 * Funcionalidades:
 * - Guardado/carga del archivo principal .dat
 * - Sistema de backups automáticos
 * - Exportación a XML (general.xml)
 * - Exportación de clasificaciones a PDF
 * - Gestión de imágenes (escudos y jugadores)
 * - Validación de integridad de datos
 */
public class GestorArchivos {
    
    // ==================== CONSTANTES DE CONFIGURACIÓN ====================
    
    // Archivo principal
    private static final String ARCHIVO_DATOS = "datos_federacion.dat";
    
    // Carpetas del sistema
    private static final String CARPETA_BACKUPS = "backups";
    private static final String CARPETA_EXPORTACIONES = "exportaciones";
    private static final String CARPETA_LOGS = "logs";
    private static final String CARPETA_IMAGENES = "imagenes";
    private static final String CARPETA_LOGOS = "imagenes/imagenes_Logos";
    private static final String CARPETA_JUGADORES = "imagenes/imagenes_Jugadores";
    
    // Archivos de exportación
    private static final String ARCHIVO_XML_GENERAL = "exportaciones/general.xml";
    
    // ==================== INICIALIZACIÓN DEL SISTEMA ====================
    
    static {
        // Crear todas las carpetas necesarias al cargar la clase
        crearEstructuraCarpetas();
    }
    
    /**
     * Crea la estructura completa de carpetas del sistema
     */
    private static void crearEstructuraCarpetas() {
        try {
            Files.createDirectories(Paths.get(CARPETA_BACKUPS));
            Files.createDirectories(Paths.get(CARPETA_EXPORTACIONES));
            Files.createDirectories(Paths.get(CARPETA_LOGS));
            Files.createDirectories(Paths.get(CARPETA_IMAGENES));
            Files.createDirectories(Paths.get(CARPETA_LOGOS));
            Files.createDirectories(Paths.get(CARPETA_JUGADORES));
            
            System.out.println("✓ Estructura de carpetas verificada/creada");
        } catch (IOException e) {
            System.err.println(" Error al crear estructura de carpetas: " + e.getMessage());
        }
    }
    
    // ==================== PERSISTENCIA PRINCIPAL (.DAT) ====================
    
    /**
     * Guarda todos los datos del sistema en el archivo .dat principal.
     * Crea un backup automático antes de sobrescribir.
     * 
     * @param datos Los datos de la federación a guardar
     * @return true si se guardó correctamente, false en caso contrario
     */
    public static boolean guardarTodo(DatosFederacion datos) {
        if (datos == null) {
            System.err.println("ERROR: No se pueden guardar datos nulos.");
            return false;
        }
        
        try {
            // Crear backup del archivo actual si existe
            crearBackupAutomatico();
            
            // Guardar datos
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(ARCHIVO_DATOS))) {
                oos.writeObject(datos);
                System.out.println(" SISTEMA: Datos guardados correctamente en " + ARCHIVO_DATOS);
                GestorLog.info("Datos guardados en " + ARCHIVO_DATOS);
                return true;
            }
            
        } catch (IOException e) {
            System.err.println(" ERROR al guardar los datos: " + e.getMessage());
            GestorLog.error("Error al guardar datos", e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Carga todos los datos desde el archivo .dat.
     * Si no existe o está corrupto, retorna una instancia nueva.
     * 
     * @return Los datos de la federación cargados
     */
    public static DatosFederacion cargarTodo() {
        File archivo = new File(ARCHIVO_DATOS);
        
        if (!archivo.exists()) {
            System.out.println("→ No se encontró " + ARCHIVO_DATOS + ". Creando sistema nuevo...");
            GestorLog.info("Primera ejecución - Creando sistema nuevo");
            return inicializarDatosPorDefecto();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARCHIVO_DATOS))) {
            
            DatosFederacion datos = (DatosFederacion) ois.readObject();
            System.out.println("✓ Datos cargados correctamente desde " + ARCHIVO_DATOS);
            GestorLog.info("Datos cargados desde " + ARCHIVO_DATOS);
            return validarYCorregirDatos(datos);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ ERROR al cargar datos: " + e.getMessage());
            GestorLog.error("Error al cargar " + ARCHIVO_DATOS, e);
            System.out.println("→ Intentando recuperar desde backup...");
            
            DatosFederacion backup = restaurarUltimoBackup();
            if (backup != null) {
                return backup;
            }
            
            System.out.println(" No hay backups disponibles. Iniciando sistema limpio.");
            GestorLog.advertencia("Sistema iniciado sin datos (archivo corrupto y sin backups)");
            return inicializarDatosPorDefecto();
        }
    }
    
    // ==================== SISTEMA DE BACKUPS ====================
    
    /**
     * Crea un backup automático del archivo actual con timestamp
     */
    private static void crearBackupAutomatico() {
        File archivoActual = new File(ARCHIVO_DATOS);
        if (!archivoActual.exists()) return;
        
        try {
            // Nombre del backup con timestamp
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new java.util.Date());
            String nombreBackup = "backup_" + timestamp + ".dat";
            Path rutaBackup = Paths.get(CARPETA_BACKUPS, nombreBackup);
            
            // Copiar archivo
            Files.copy(archivoActual.toPath(), rutaBackup, 
                    StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("   Backup creado: " + nombreBackup);
            GestorLog.info("Backup automático creado: " + nombreBackup);
            
            // Limpiar backups antiguos (mantener solo los últimos 5)
            limpiarBackupsAntiguos();
            
        } catch (IOException e) {
            System.err.println(" Advertencia: No se pudo crear backup: " + e.getMessage());
            GestorLog.advertencia("Fallo al crear backup: " + e.getMessage());
        }
    }
    
    /**
     * Restaura los datos desde el backup más reciente
     */
    private static DatosFederacion restaurarUltimoBackup() {
        try {
            File carpeta = new File(CARPETA_BACKUPS);
            if (!carpeta.exists()) return null;
            
            // Buscar el backup más reciente
            File[] backups = carpeta.listFiles(
                    (dir, name) -> name.startsWith("backup_") && name.endsWith(".dat"));
            
            if (backups == null || backups.length == 0) return null;
            
            // Ordenar por fecha (más reciente primero)
            java.util.Arrays.sort(backups, (a, b) -> 
                    Long.compare(b.lastModified(), a.lastModified()));
            
            File backupMasReciente = backups[0];
            System.out.println("  → Restaurando desde: " + backupMasReciente.getName());
            GestorLog.info("Restaurando desde backup: " + backupMasReciente.getName());
            
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(backupMasReciente))) {
                return (DatosFederacion) ois.readObject();
            }
            
        } catch (Exception e) {
            System.err.println(" Error al restaurar backup: " + e.getMessage());
            GestorLog.error("Error al restaurar backup", e);
            return null;
        }
    }
    
    /**
     * Mantiene solo los 5 backups más recientes
     */
    private static void limpiarBackupsAntiguos() {
        try {
            File carpeta = new File(CARPETA_BACKUPS);
            File[] backups = carpeta.listFiles(
                    (dir, name) -> name.startsWith("backup_") && name.endsWith(".dat"));
            
            if (backups == null || backups.length <= 5) return;
            
            // Ordenar por fecha
            java.util.Arrays.sort(backups, (a, b) -> 
                    Long.compare(b.lastModified(), a.lastModified()));
            
            // Eliminar los más antiguos
            for (int i = 5; i < backups.length; i++) {
                if (backups[i].delete()) {
                    System.out.println("  → Backup antiguo eliminado: " + backups[i].getName());
                    GestorLog.debug("Backup antiguo eliminado: " + backups[i].getName());
                }
            }
            
        } catch (Exception e) {
            System.err.println("⚠ Error al limpiar backups: " + e.getMessage());
        }
    }
    
    // ==================== EXPORTACIÓN A XML (general.xml) ====================
    
    /**
     * Exporta una temporada al archivo general.xml
     * Compatible con ExportadorXML existente
     * 
     * @param datos Los datos de la federación
     * @param temporada La temporada a exportar
     * @return true si se exportó correctamente
     */
    public static boolean exportarTemporadaAXML(DatosFederacion datos, Temporada temporada) {
        if (datos == null || temporada == null) {
            System.err.println("ERROR: Datos o temporada nulos");
            GestorLog.error("Intento de exportar con datos/temporada nulos");
            return false;
        }
        
        try {
            ExportadorXML exportador = new ExportadorXML(datos);
            boolean exito = exportador.exportarTemporada(temporada);
            
            if (exito) {
                GestorLog.exito("Temporada exportada a general.xml: " + temporada.getNombre());
            } else {
                GestorLog.error("Fallo al exportar temporada: " + temporada.getNombre());
            }
            
            return exito;
            
        } catch (Exception e) {
            System.err.println("✗ ERROR en exportación XML: " + e.getMessage());
            GestorLog.error("Error al exportar temporada a XML", e);
            return false;
        }
    }
    
    /**
     * Exporta todas las temporadas al archivo general.xml
     */
    public static boolean exportarTodasLasTemporadas(DatosFederacion datos) {
        if (datos == null) return false;
        
        int exitosas = 0;
        int fallidas = 0;
        
        GestorLog.info("Iniciando exportación masiva de temporadas");
        
        for (Temporada temp : datos.getListaTemporadas()) {
            boolean exito = exportarTemporadaAXML(datos, temp);
            if (exito) exitosas++;
            else fallidas++;
        }
        
        GestorLog.exito("Exportación masiva completada: " + exitosas + " exitosas, " + fallidas + " fallidas");
        
        return fallidas == 0;
    }
    
    // ==================== EXPORTACIÓN DE CLASIFICACIONES A PDF ====================
    
    /**
     * Exporta la clasificación de una temporada a PDF
     * 
     * @param temporada La temporada
     * @param nombreArchivo Nombre del archivo (sin ruta)
     * @return true si se exportó correctamente
     */
    public static boolean exportarClasificacionPDF(Temporada temporada, String nombreArchivo) {
        if (temporada == null) {
            System.err.println("ERROR: Temporada nula");
            return false;
        }
        
        try {
            // Calcular clasificación
            Clasificacion clasificacion = CalculadoraClasificacion.calcular(temporada);
            
            // Asegurar que el nombre tenga extensión .pdf
            if (!nombreArchivo.toLowerCase().endsWith(".pdf")) {
                nombreArchivo += ".pdf";
            }
            
            // Ruta completa
            String rutaCompleta = CARPETA_EXPORTACIONES + File.separator + nombreArchivo;
            
            // Exportar
            boolean exito = ExportarPDF.exportarClasificacion(
                temporada, 
                clasificacion.getFilas(), 
                rutaCompleta
            );
            
            if (exito) {
                System.out.println(" EXPORTACIÓN PDF: " + nombreArchivo);
                GestorLog.exito("Clasificación exportada a PDF: " + nombreArchivo);
            } else {
                System.err.println(" Error al generar PDF");
                GestorLog.error("Fallo al generar PDF: " + nombreArchivo);
            }
            
            return exito;
            
        } catch (Exception e) {
            System.err.println("✗ ERROR en exportación PDF: " + e.getMessage());
            GestorLog.error("Error al exportar clasificación a PDF", e);
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== GESTIÓN DE IMÁGENES ====================
    
    /**
     * Copia un escudo a la carpeta de logos
     * 
     * @param rutaOrigen Ruta del archivo original
     * @param nombreEquipo Nombre del equipo
     * @return Ruta relativa del escudo copiado, o null si falla
     */
    public static String copiarEscudo(String rutaOrigen, String nombreEquipo) {
        if (rutaOrigen == null || rutaOrigen.isEmpty() || nombreEquipo == null) {
            return null;
        }
        
        try {
            File archivoOrigen = new File(rutaOrigen);
            
            if (!archivoOrigen.exists()) {
                GestorLog.advertencia("Escudo no encontrado: " + rutaOrigen);
                return null;
            }
            
            // Generar nombre normalizado
            String extension = obtenerExtension(archivoOrigen.getName());
            String nombreNormalizado = normalizarNombre(nombreEquipo) + extension;
            
            // Ruta de destino
            Path rutaDestino = Paths.get(CARPETA_LOGOS, nombreNormalizado);
            
            // Copiar archivo
            Files.copy(archivoOrigen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            
            GestorLog.info("Escudo copiado: " + nombreEquipo + " → " + nombreNormalizado);
            
            // Retornar ruta relativa
            return CARPETA_LOGOS + "/" + nombreNormalizado;
            
        } catch (IOException e) {
            System.err.println("✗ Error al copiar escudo: " + e.getMessage());
            GestorLog.error("Error al copiar escudo de " + nombreEquipo, e);
            return null;
        }
    }
    
    /**
     * Copia una foto de jugador a la carpeta correspondiente
     * 
     * @param rutaOrigen Ruta del archivo original
     * @param nombreJugador Nombre del jugador
     * @param nombreEquipo Nombre del equipo
     * @return Ruta relativa de la foto copiada, o null si falla
     */
    public static String copiarFotoJugador(String rutaOrigen, String nombreJugador, String nombreEquipo) {
        if (rutaOrigen == null || rutaOrigen.isEmpty()) {
            return null;
        }
        
        try {
            File archivoOrigen = new File(rutaOrigen);
            
            if (!archivoOrigen.exists()) {
                GestorLog.advertencia("Foto no encontrada: " + rutaOrigen);
                return null;
            }
            
            // Generar nombre normalizado
            String extension = obtenerExtension(archivoOrigen.getName());
            String nombreNormalizado = normalizarNombre(nombreJugador) + "_" + 
                                       normalizarNombre(nombreEquipo) + extension;
            
            // Ruta de destino
            Path rutaDestino = Paths.get(CARPETA_JUGADORES, nombreNormalizado);
            
            // Copiar archivo
            Files.copy(archivoOrigen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            
            GestorLog.info("Foto copiada: " + nombreJugador + " → " + nombreNormalizado);
            
            // Retornar ruta relativa
            return CARPETA_JUGADORES + "/" + nombreNormalizado;
            
        } catch (IOException e) {
            System.err.println("✗ Error al copiar foto: " + e.getMessage());
            GestorLog.error("Error al copiar foto de " + nombreJugador, e);
            return null;
        }
    }
    
    /**
     * Normaliza un nombre para usar como nombre de archivo
     */
    private static String normalizarNombre(String nombre) {
        if (nombre == null) return "sin_nombre";
        
        return nombre.replaceAll("[^a-zA-Z0-9]", "_")
                    .replaceAll("_+", "_")
                    .toUpperCase();
    }
    
    /**
     * Obtiene la extensión de un archivo
     */
    private static String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null) return ".png";
        
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto > 0) {
            return nombreArchivo.substring(ultimoPunto);
        }
        return ".png";
    }
    
    // ==================== VALIDACIÓN Y RECUPERACIÓN ====================
    
    /**
     * Valida y corrige posibles inconsistencias en los datos cargados
     */
    private static DatosFederacion validarYCorregirDatos(DatosFederacion datos) {
        if (datos == null) {
            GestorLog.error("Datos nulos recibidos para validación");
            return inicializarDatosPorDefecto();
        }
        
        boolean huboCorrecciones = false;
        
        // Asegurar que las listas no sean null
        if (datos.getListaUsuarios() == null) {
            System.out.println(" Corrigiendo: lista de usuarios nula");
            GestorLog.advertencia("Lista de usuarios nula - corregida");
            huboCorrecciones = true;
        }
        if (datos.getTodosLosJugadores() == null) {
            System.out.println(" Corrigiendo: lista de jugadores nula");
            GestorLog.advertencia("Lista de jugadores nula - corregida");
            huboCorrecciones = true;
        }
        if (datos.getListaEquipos() == null) {
            System.out.println(" Corrigiendo: lista de equipos nula");
            GestorLog.advertencia("Lista de equipos nula - corregida");
            huboCorrecciones = true;
        }
        if (datos.getListaTemporadas() == null) {
            System.out.println(" Corrigiendo: lista de temporadas nula");
            GestorLog.advertencia("Lista de temporadas nula - corregida");
            huboCorrecciones = true;
        }
        
        if (huboCorrecciones) {
            GestorLog.info("Datos corregidos y validados");
        } else {
            GestorLog.info("Datos validados correctamente");
        }
        
        return datos;
    }
    
    /**
     * Crea una instancia inicial con datos por defecto
     */
    private static DatosFederacion inicializarDatosPorDefecto() {
        DatosFederacion datos = new DatosFederacion();
        
        // Crear los 4 usuarios predeterminados
        Usuario admin = new Usuario(
            "Administrador del Sistema",
            "admin",
            "123",
            Rol.ADMINISTRADOR
        );
        Usuario invitado = new Usuario(
            "Usuario Invitado",
            "invitado",
            "123",
            Rol.INVITADO
        );
        Usuario arbitro = new Usuario(
            "Árbitro Principal",
            "arbitro",
            "123",
            Rol.ARBITRO
        );
        Usuario manager = new Usuario(
            "Manager Principal",
            "manager",
            "123",
            Rol.MANAGER
        );
        
        datos.getListaUsuarios().add(admin);
        datos.getListaUsuarios().add(invitado);
        datos.getListaUsuarios().add(arbitro);
        datos.getListaUsuarios().add(manager);
        
        System.out.println("→ Usuarios por defecto creados:");
        System.out.println("   • admin / 123 (Administrador)");
        System.out.println("   • invitado / 123 (Invitado)");
        System.out.println("   • arbitro / 123 (Árbitro)");
        System.out.println("   • manager / 123 (Manager)");
        
        GestorLog.exito("Usuarios predeterminados creados");
        
        return datos;
    }
    
    // ==================== UTILIDADES ====================
    
    /**
     * Obtiene información del estado del sistema de archivos
     */
    public static String obtenerEstadoSistema() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════════════════════╗\n");
        sb.append("║         ESTADO DEL SISTEMA DE ARCHIVOS                        ║\n");
        sb.append("╠═══════════════════════════════════════════════════════════════╣\n");
        
        // Archivo principal
        File archivo = new File(ARCHIVO_DATOS);
        if (archivo.exists()) {
            double tamanoMB = archivo.length() / (1024.0 * 1024.0);
            sb.append(String.format("║  Archivo principal: %s (%.2f MB)\n", 
                    ARCHIVO_DATOS, tamanoMB));
            sb.append(String.format("║  Última modificación: %s\n",
                    new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        .format(new java.util.Date(archivo.lastModified()))));
        } else {
            sb.append("║ 📄 Archivo principal: NO EXISTE\n");
        }
        
        sb.append("╠═══════════════════════════════════════════════════════════════╣\n");
        
        // Backups
        File carpetaBackups = new File(CARPETA_BACKUPS);
        if (carpetaBackups.exists() && carpetaBackups.isDirectory()) {
            File[] backups = carpetaBackups.listFiles(
                    (dir, name) -> name.endsWith(".dat"));
            int numBackups = backups != null ? backups.length : 0;
            sb.append(String.format("║ 💾 Backups disponibles: %d\n", numBackups));
        } else {
            sb.append("║  Backups disponibles: 0\n");
        }
        
        // Exportaciones
        File carpetaExport = new File(CARPETA_EXPORTACIONES);
        if (carpetaExport.exists() && carpetaExport.isDirectory()) {
            File[] exports = carpetaExport.listFiles();
            int numExports = exports != null ? exports.length : 0;
            sb.append(String.format("║  Exportaciones: %d archivo(s)\n", numExports));
        } else {
            sb.append("║  Exportaciones: 0 archivo(s)\n");
        }
        
        // Imágenes
        File carpetaLogos = new File(CARPETA_LOGOS);
        File carpetaJugadores = new File(CARPETA_JUGADORES);
        
        int numLogos = 0;
        int numFotos = 0;
        
        if (carpetaLogos.exists()) {
            File[] logos = carpetaLogos.listFiles();
            numLogos = logos != null ? logos.length : 0;
        }
        
        if (carpetaJugadores.exists()) {
            File[] fotos = carpetaJugadores.listFiles();
            numFotos = fotos != null ? fotos.length : 0;
        }
        
        sb.append(String.format("║   Escudos: %d | Fotos jugadores: %d\n", numLogos, numFotos));
        
        sb.append("╚═══════════════════════════════════════════════════════════════╝\n");
        
        return sb.toString();
    }
    
    /**
     * Resetea completamente el sistema (USAR CON PRECAUCIÓN)
     */
    public static boolean resetearSistema() {
        try {
            File archivo = new File(ARCHIVO_DATOS);
            if (archivo.exists() && archivo.delete()) {
                System.out.println(" Sistema reseteado correctamente.");
                GestorLog.advertencia(" SISTEMA RESETEADO");
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println(" Error al resetear: " + e.getMessage());
            GestorLog.error("Error al resetear sistema", e);
            return false;
        }
    }
    
    /**
     * Crea un backup manual con nombre personalizado
     */
    public static boolean crearBackupManual(String nombreBackup) {
        File archivoActual = new File(ARCHIVO_DATOS);
        if (!archivoActual.exists()) {
            System.err.println("No hay datos para hacer backup");
            return false;
        }
        
        try {
            if (!nombreBackup.endsWith(".dat")) {
                nombreBackup += ".dat";
            }
            
            Path rutaBackup = Paths.get(CARPETA_BACKUPS, nombreBackup);
            Files.copy(archivoActual.toPath(), rutaBackup, 
                    StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("✓ Backup manual creado: " + nombreBackup);
            GestorLog.exito("Backup manual creado: " + nombreBackup);
            return true;
            
        } catch (IOException e) {
            System.err.println("✗ Error al crear backup manual: " + e.getMessage());
            GestorLog.error("Error al crear backup manual", e);
            return false;
        }
    }
}
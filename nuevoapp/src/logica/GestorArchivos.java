package logica;

import java.io.*;
import java.nio.file.*;
import gestion.*;

/**
 * GESTOR CENTRALIZADO DE PERSISTENCIA Y ARCHIVOS
 * 
 * <p>Responsable de:</p>
 * <ul>
 *     <li>Guardar y cargar datos de la federación (DatosFederacion).</li>
 *     <li>Crear y mantener backups automáticos.</li>
 *     <li>Normalizar rutas de imágenes de escudos y fotos de jugadores.</li>
 *     <li>Inicializar datos por defecto si no existen o están corruptos.</li>
 *     <li>⭐ NUEVO: Sincronizar contadores de IDs únicos al cargar datos.</li>
 * </ul>
 */
public class GestorArchivos {
    
    private static final String ARCHIVO_DATOS = "datos_federacion.dat";
    private static final String CARPETA_BACKUPS = "backups";
    private static final String CARPETA_EXPORTACIONES = "exportaciones";
    private static final String CARPETA_LOGS = "logs";
    private static final String CARPETA_IMAGENES = "imagenes";
    
    // ⭐ RUTAS RELATIVAS UNIFICADAS (con ./ al inicio)
    private static final String CARPETA_LOGOS = "./imagenes/imagenes_Logos";
    private static final String CARPETA_JUGADORES = "./imagenes/imagenes_Jugadores";
    private static final String ARCHIVO_XML_GENERAL = "exportaciones/ligaBalonmano.xml";
    
    static {
        crearEstructuraCarpetas();
    }
    
    private static void crearEstructuraCarpetas() {
        try {
            Files.createDirectories(Paths.get(CARPETA_BACKUPS));
            Files.createDirectories(Paths.get(CARPETA_EXPORTACIONES));
            Files.createDirectories(Paths.get(CARPETA_LOGS));
            Files.createDirectories(Paths.get(CARPETA_IMAGENES));
            Files.createDirectories(Paths.get("imagenes/imagenes_Logos"));
            Files.createDirectories(Paths.get("imagenes/imagenes_Jugadores"));
            
            System.out.println("✓ Estructura de carpetas verificada/creada");
        } catch (IOException e) {
            System.err.println("❌ Error al crear estructura de carpetas: " + e.getMessage());
        }
    }
    
    /**
     * Guarda todos los datos de la federación en disco.
     * Normaliza las URLs de imágenes antes de guardar.
     * Crea backup automático si el archivo ya existe.
     * 
     * @param datos Datos de la federación a guardar
     * @return true si se guardaron correctamente, false si hubo error
     */
    public static boolean guardarTodo(DatosFederacion datos) {
        if (datos == null) {
            System.err.println("ERROR: No se pueden guardar datos nulos.");
            return false;
        }
        
        try {
            // ⭐ PASO 1: Normalizar todas las URLs de imágenes ANTES de guardar
            normalizarURLsImagenes(datos);
            
            // PASO 2: Crear backup del archivo actual si existe
            crearBackupAutomatico();
            
            // PASO 3: Guardar datos
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(ARCHIVO_DATOS))) {
                oos.writeObject(datos);
                System.out.println("💾 SISTEMA: Datos guardados correctamente en " + ARCHIVO_DATOS);
                GestorLog.info("Datos guardados en " + ARCHIVO_DATOS);
                return true;
            }
            
        } catch (IOException e) {
            System.err.println("❌ ERROR al guardar los datos: " + e.getMessage());
            GestorLog.error("Error al guardar datos", e);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * ⭐ MEJORADO: Normaliza todas las URLs con formato "./imagenes/..."
     */
    private static void normalizarURLsImagenes(DatosFederacion datos) {
        if (datos == null) return;
        
        int escudosNormalizados = 0;
        int fotosNormalizadas = 0;
        
        // Normalizar escudos de equipos
        for (Temporada temp : datos.getListaTemporadas()) {
            for (Equipo equipo : temp.getEquiposParticipantes()) {
                String rutaOriginal = equipo.getRutaEscudo();
                
                if (rutaOriginal != null && !rutaOriginal.isEmpty()) {
                    String rutaNormalizada = normalizarRutaImagen(rutaOriginal, true);
                    
                    if (!rutaNormalizada.equals(rutaOriginal)) {
                        equipo.setRutaEscudo(rutaNormalizada);
                        escudosNormalizados++;
                    }
                }
            }
        }
        
        // Normalizar fotos de jugadores
        for (Temporada temp : datos.getListaTemporadas()) {
            for (Equipo equipo : temp.getEquiposParticipantes()) {
                for (Jugador jugador : equipo.getPlantilla()) {
                    String rutaOriginal = jugador.getFotoURL();
                    
                    if (rutaOriginal != null && !rutaOriginal.isEmpty()) {
                        String rutaNormalizada = normalizarRutaImagen(rutaOriginal, false);
                        
                        if (!rutaNormalizada.equals(rutaOriginal)) {
                            jugador.setFotoURL(rutaNormalizada);
                            fotosNormalizadas++;
                        }
                    }
                }
            }
        }
        
        if (escudosNormalizados > 0 || fotosNormalizadas > 0) {
            System.out.println("🔧 URLs normalizadas: " + escudosNormalizados + 
                             " escudos, " + fotosNormalizadas + " fotos");
        }
    }
    
    /**
     * ⭐ NUEVO: Normaliza una ruta de imagen al formato estándar "./imagenes/..."
     */
    private static String normalizarRutaImagen(String rutaOriginal, boolean esEscudo) {
        if (rutaOriginal == null || rutaOriginal.isEmpty()) {
            return "";
        }
        
        // Si ya está en el formato correcto, devolverla sin cambios
        if (rutaOriginal.startsWith("./imagenes/")) {
            return rutaOriginal;
        }
        
        File archivo = new File(rutaOriginal);
        
        // Verificar que el archivo existe
        if (!archivo.exists()) {
            archivo = new File("imagenes/imagenes_Logos/" + archivo.getName());
            if (!archivo.exists()) {
                archivo = new File("imagenes/imagenes_Jugadores/" + archivo.getName());
                if (!archivo.exists()) {
                    System.err.println("⚠️ Archivo no encontrado: " + rutaOriginal);
                    return "";
                }
            }
        }
        
        String nombreArchivo = archivo.getName();
        
        // Construir ruta normalizada
        if (esEscudo) {
            return "./imagenes/imagenes_Logos/" + nombreArchivo;
        } else {
            return "./imagenes/imagenes_Jugadores/" + nombreArchivo;
        }
    }
    
    /**
     * Carga los datos de la federación desde disco.
     * ⭐ ACTUALIZADO: Sincroniza TODOS los contadores de IDs únicos.
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
            
            // ⭐ NORMALIZAR RUTAS AL CARGAR
            normalizarURLsImagenes(datos);
            
            // ⭐ SINCRONIZAR TODOS LOS CONTADORES DE IDs ÚNICOS
            sincronizarContadores(datos);
            
            return validarYCorregirDatos(datos);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("✗ ERROR al cargar datos: " + e.getMessage());
            GestorLog.error("Error al cargar " + ARCHIVO_DATOS, e);
            System.out.println("→ Intentando recuperar desde backup...");
            
            DatosFederacion backup = restaurarUltimoBackup();
            if (backup != null) {
                normalizarURLsImagenes(backup);
                sincronizarContadores(backup);
                return backup;
            }
            
            System.out.println("⚠ No hay backups disponibles. Iniciando sistema limpio.");
            GestorLog.advertencia("Sistema iniciado sin datos (archivo corrupto y sin backups)");
            return inicializarDatosPorDefecto();
        }
    }
    
    /**
     * ⭐ NUEVO: Sincroniza TODOS los contadores globales de IDs únicos.
     * DEBE llamarse después de cargar datos desde archivos.
     */
    public static void sincronizarContadores(DatosFederacion datos) {
        if (datos == null) return;
        
        System.out.println("\n🔄 Sincronizando contadores de IDs únicos...");
        
        // Recolectar todas las entidades
        java.util.List<Jugador> todosJugadores = new java.util.ArrayList<>();
        java.util.List<Equipo> todosEquipos = new java.util.ArrayList<>();
        java.util.List<Jornada> todasJornadas = new java.util.ArrayList<>();
        java.util.List<Partido> todosPartidos = new java.util.ArrayList<>();
        
        for (Temporada temp : datos.getListaTemporadas()) {
            // Equipos
            todosEquipos.addAll(temp.getEquiposParticipantes());
            
            // Jugadores de cada equipo
            for (Equipo eq : temp.getEquiposParticipantes()) {
                todosJugadores.addAll(eq.getPlantilla());
            }
            
            // Jornadas y partidos
            todasJornadas.addAll(temp.getListaJornadas());
            for (Jornada jor : temp.getListaJornadas()) {
                todosPartidos.addAll(jor.getListaPartidos());
            }
        }
        
        // Sincronizar cada contador
        Jugador.sincronizarContadorGlobal(todosJugadores);
        Equipo.sincronizarContadorGlobal(todosEquipos);
        Jornada.sincronizarContadorGlobal(todasJornadas);
        Partido.sincronizarContadorGlobal(todosPartidos);
        
        System.out.println("✅ Todos los contadores sincronizados correctamente");
        System.out.println("   • Jugadores: " + todosJugadores.size());
        System.out.println("   • Equipos: " + todosEquipos.size());
        System.out.println("   • Jornadas: " + todasJornadas.size());
        System.out.println("   • Partidos: " + todosPartidos.size() + "\n");
        
        GestorLog.info("Contadores sincronizados - J:" + todosJugadores.size() + 
                      " E:" + todosEquipos.size() + 
                      " Jor:" + todasJornadas.size() + 
                      " P:" + todosPartidos.size());
    }
    
    private static void crearBackupAutomatico() {
        File archivoActual = new File(ARCHIVO_DATOS);
        if (!archivoActual.exists()) return;
        
        try {
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new java.util.Date());
            String nombreBackup = "backup_" + timestamp + ".dat";
            Path rutaBackup = Paths.get(CARPETA_BACKUPS, nombreBackup);
            
            Files.copy(archivoActual.toPath(), rutaBackup, 
                    StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("💾 Backup creado: " + nombreBackup);
            GestorLog.info("Backup automático creado: " + nombreBackup);
            
            limpiarBackupsAntiguos();
            
        } catch (IOException e) {
            System.err.println("Advertencia: No se pudo crear backup: " + e.getMessage());
            GestorLog.advertencia("Fallo al crear backup: " + e.getMessage());
        }
    }
    
    private static DatosFederacion restaurarUltimoBackup() {
        try {
            File carpeta = new File(CARPETA_BACKUPS);
            if (!carpeta.exists()) return null;
            
            File[] backups = carpeta.listFiles(
                    (dir, name) -> name.startsWith("backup_") && name.endsWith(".dat"));
            
            if (backups == null || backups.length == 0) return null;
            
            java.util.Arrays.sort(backups, (a, b) -> 
                    Long.compare(b.lastModified(), a.lastModified()));
            
            File backupMasReciente = backups[0];
            System.out.println("📂 Restaurando desde: " + backupMasReciente.getName());
            GestorLog.info("Restaurando desde backup: " + backupMasReciente.getName());
            
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(backupMasReciente))) {
                return (DatosFederacion) ois.readObject();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al restaurar backup: " + e.getMessage());
            GestorLog.error("Error al restaurar backup", e);
            return null;
        }
    }
    
    private static void limpiarBackupsAntiguos() {
        try {
            File carpeta = new File(CARPETA_BACKUPS);
            File[] backups = carpeta.listFiles(
                    (dir, name) -> name.startsWith("backup_") && name.endsWith(".dat"));
            
            if (backups == null || backups.length <= 5) return;
            
            java.util.Arrays.sort(backups, (a, b) -> 
                    Long.compare(b.lastModified(), a.lastModified()));
            
            for (int i = 5; i < backups.length; i++) {
                if (backups[i].delete()) {
                    System.out.println("🗑️ Backup antiguo eliminado: " + backups[i].getName());
                    GestorLog.debug("Backup antiguo eliminado: " + backups[i].getName());
                }
            }
            
        } catch (Exception e) {
            System.err.println("⚠️ Error al limpiar backups: " + e.getMessage());
        }
    }
    
    /**
     * Copia un escudo de equipo al directorio estándar.
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
            
            String extension = obtenerExtension(archivoOrigen.getName());
            String nombreNormalizado = normalizarNombre(nombreEquipo) + extension;
            
            Path rutaDestino = Paths.get("imagenes/imagenes_Logos", nombreNormalizado);
            
            Files.copy(archivoOrigen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            
            GestorLog.info("Escudo copiado: " + nombreEquipo + " → " + nombreNormalizado);
            
            return "./imagenes/imagenes_Logos/" + nombreNormalizado;
            
        } catch (IOException e) {
            System.err.println("❌ Error al copiar escudo: " + e.getMessage());
            GestorLog.error("Error al copiar escudo de " + nombreEquipo, e);
            return null;
        }
    }
    
    /**
     * Copia una foto de jugador al directorio estándar.
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
            
            String extension = obtenerExtension(archivoOrigen.getName());
            String nombreNormalizado = normalizarNombre(nombreJugador) + "_" + 
                                       normalizarNombre(nombreEquipo) + extension;
            
            Path rutaDestino = Paths.get("imagenes/imagenes_Jugadores", nombreNormalizado);
            
            Files.copy(archivoOrigen.toPath(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            
            GestorLog.info("Foto copiada: " + nombreJugador + " → " + nombreNormalizado);
            
            return "./imagenes/imagenes_Jugadores/" + nombreNormalizado;
            
        } catch (IOException e) {
            System.err.println("❌ Error al copiar foto: " + e.getMessage());
            GestorLog.error("Error al copiar foto de " + nombreJugador, e);
            return null;
        }
    }
    
    private static String normalizarNombre(String nombre) {
        if (nombre == null) return "sin_nombre";
        
        return nombre.replaceAll("[^a-zA-Z0-9]", "_")
                    .replaceAll("_+", "_")
                    .toUpperCase();
    }
    
    private static String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null) return ".png";
        
        int ultimoPunto = nombreArchivo.lastIndexOf('.');
        if (ultimoPunto > 0) {
            return nombreArchivo.substring(ultimoPunto);
        }
        return ".png";
    }
    
    private static DatosFederacion validarYCorregirDatos(DatosFederacion datos) {
        if (datos == null) {
            GestorLog.error("Datos nulos recibidos para validación");
            return inicializarDatosPorDefecto();
        }
        
        boolean huboCorrecciones = false;
        
        if (datos.getListaUsuarios() == null) {
            System.out.println("⚠ Corrigiendo: lista de usuarios nula");
            GestorLog.advertencia("Lista de usuarios nula - corregida");
            huboCorrecciones = true;
        }
        if (datos.getTodosLosJugadores() == null) {
            System.out.println("⚠ Corrigiendo: lista de jugadores nula");
            GestorLog.advertencia("Lista de jugadores nula - corregida");
            huboCorrecciones = true;
        }
        if (datos.getListaEquipos() == null) {
            System.out.println("⚠ Corrigiendo: lista de equipos nula");
            GestorLog.advertencia("Lista de equipos nula - corregida");
            huboCorrecciones = true;
        }
        if (datos.getListaTemporadas() == null) {
            System.out.println("⚠ Corrigiendo: lista de temporadas nula");
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
    
    private static DatosFederacion inicializarDatosPorDefecto() {
        DatosFederacion datos = new DatosFederacion();
        
        Usuario admin = new Usuario("Administrador del Sistema", "admin", "123", Rol.ADMINISTRADOR);
        Usuario invitado = new Usuario("Usuario Invitado", "invitado", "123", Rol.INVITADO);
        Usuario arbitro = new Usuario("Árbitro Principal", "arbitro", "123", Rol.ARBITRO);
        Usuario manager = new Usuario("Manager Principal", "manager", "123", Rol.MANAGER);
        
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
}
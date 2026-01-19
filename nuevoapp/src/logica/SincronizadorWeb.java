package logica;

import gestion.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;

/**
 * Clase para sincronizar datos con el servidor web.
 * Copia el XML y todas las imágenes al directorio del sitio web.
 */
public class SincronizadorWeb {
    
    // ⭐ RUTAS DE SINCRONIZACIÓN
    private static final String RUTA_WEB_BASE = "../LM_Grupo2/HTML/";
    private static final String RUTA_WEB_XML = RUTA_WEB_BASE + "xml/data/";
    private static final String RUTA_WEB_IMAGENES = RUTA_WEB_BASE + "imagenes/";
    
    private static final String ARCHIVO_XML_ORIGEN = "ligaBalonmano.xml";
    private static final String CARPETA_IMAGENES_ORIGEN = "imagenes/";
    
    /**
     * Sincroniza el XML y todas las imágenes con el sitio web.
     * 
     * @param datos Datos de la federación
     * @param nombreTemporada Nombre de la temporada que se acaba de exportar
     */
    public static void sincronizarConWeb(DatosFederacion datos, String nombreTemporada) {
        if (datos == null || nombreTemporada == null) {
            GestorLog.error("❌ Parámetros nulos en sincronizarConWeb");
            return;
        }
        
        GestorLog.info("🌐 Iniciando sincronización con sitio web...");
        
        boolean exito = true;
        
        // 1️⃣ COPIAR XML
        if (!copiarXMLAlWeb()) {
            exito = false;
        }
        
        // 2️⃣ COPIAR IMÁGENES
        if (!copiarImagenesAlWeb()) {
            exito = false;
        }
        
        // 3️⃣ MOSTRAR RESULTADO
        if (exito) {
            GestorLog.exito("✅ Sincronización completada con éxito");
            
            JOptionPane.showMessageDialog(null,
                "✅ Sincronización web exitosa\n\n" +
                "📄 XML actualizado en: " + RUTA_WEB_XML + "\n" +
                "🖼️ Imágenes copiadas a: " + RUTA_WEB_IMAGENES + "\n\n" +
                "🌐 El sitio web ya tiene los datos actualizados.",
                "Sincronización completada",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            GestorLog.advertencia("⚠️ Sincronización completada con errores (ver logs)");
            
            JOptionPane.showMessageDialog(null,
                "⚠️ Sincronización completada con algunos errores\n\n" +
                "Revisa los logs para más detalles.",
                "Sincronización parcial",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Copia el archivo XML al directorio del sitio web.
     * 
     * @return true si la copia fue exitosa, false en caso contrario
     */
    private static boolean copiarXMLAlWeb() {
        try {
            File archivoOrigen = new File(ARCHIVO_XML_ORIGEN);
            
            if (!archivoOrigen.exists()) {
                GestorLog.error("❌ No se encontró el archivo XML: " + ARCHIVO_XML_ORIGEN);
                return false;
            }
            
            // Crear directorio destino si no existe
            Path directorioDestino = Paths.get(RUTA_WEB_XML);
            Files.createDirectories(directorioDestino);
            
            // Copiar XML
            Path archivoDestino = directorioDestino.resolve(ARCHIVO_XML_ORIGEN);
            Files.copy(
                archivoOrigen.toPath(),
                archivoDestino,
                StandardCopyOption.REPLACE_EXISTING
            );
            
            GestorLog.exito("✅ XML copiado al sitio web: " + archivoDestino);
            return true;
            
        } catch (IOException e) {
            GestorLog.error("❌ Error al copiar XML: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Copia todas las imágenes (logos y jugadores) al sitio web.
     * 
     * @return true si la copia fue exitosa, false en caso contrario
     */
    private static boolean copiarImagenesAlWeb() {
        try {
            File carpetaImagenesOrigen = new File(CARPETA_IMAGENES_ORIGEN);
            
            if (!carpetaImagenesOrigen.exists()) {
                GestorLog.advertencia("⚠️ No se encontró la carpeta de imágenes: " + CARPETA_IMAGENES_ORIGEN);
                return false;
            }
            
            // Crear directorio destino si no existe
            Path directorioDestino = Paths.get(RUTA_WEB_IMAGENES);
            Files.createDirectories(directorioDestino);
            
            // Copiar logos
            boolean logosOk = copiarCarpeta(
                new File(carpetaImagenesOrigen, "imagenes_Logos"),
                directorioDestino.resolve("imagenes_Logos").toFile()
            );
            
            // Copiar fotos de jugadores
            boolean jugadoresOk = copiarCarpeta(
                new File(carpetaImagenesOrigen, "imagenes_Jugadores"),
                directorioDestino.resolve("imagenes_Jugadores").toFile()
            );
            
            if (logosOk && jugadoresOk) {
                GestorLog.exito("✅ Todas las imágenes copiadas al sitio web");
                return true;
            } else {
                GestorLog.advertencia("⚠️ Algunas imágenes no se pudieron copiar");
                return false;
            }
            
        } catch (IOException e) {
            GestorLog.error("❌ Error al copiar imágenes: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Copia recursivamente una carpeta completa.
     * 
     * @param origen Carpeta origen
     * @param destino Carpeta destino
     * @return true si la copia fue exitosa, false en caso contrario
     */
    private static boolean copiarCarpeta(File origen, File destino) {
        if (!origen.exists()) {
            GestorLog.advertencia("⚠️ Carpeta no encontrada: " + origen.getAbsolutePath());
            return false;
        }
        
        try {
            // Crear carpeta destino si no existe
            if (!destino.exists()) {
                destino.mkdirs();
            }
            
            File[] archivos = origen.listFiles();
            
            if (archivos == null || archivos.length == 0) {
                GestorLog.advertencia("⚠️ Carpeta vacía: " + origen.getName());
                return true;
            }
            
            int archivosCopiadosExitosos = 0;
            int archivosTotales = archivos.length;
            
            for (File archivo : archivos) {
                if (archivo.isFile()) {
                    try {
                        Path archivoDestino = destino.toPath().resolve(archivo.getName());
                        Files.copy(
                            archivo.toPath(),
                            archivoDestino,
                            StandardCopyOption.REPLACE_EXISTING
                        );
                        archivosCopiadosExitosos++;
                    } catch (IOException e) {
                        GestorLog.error("❌ Error al copiar archivo: " + archivo.getName() + " | " + e.getMessage());
                    }
                }
            }
            
            GestorLog.info("📊 " + origen.getName() + ": " + archivosCopiadosExitosos + "/" + archivosTotales + " archivos copiados");
            
            return archivosCopiadosExitosos == archivosTotales;
            
        } catch (Exception e) {
            GestorLog.error("❌ Error al copiar carpeta " + origen.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Actualiza la información de la temporada activa en el servidor.
     * (Método heredado - ahora usa sincronizarConWeb)
     */
    public static void actualizarActivosServidor(DatosFederacion datos, String nombreTemporada) {
        if (datos == null || nombreTemporada == null) {
            GestorLog.error("Parámetros nulos en actualizarActivosServidor");
            return;
        }
        
        GestorLog.info("Sincronizando con servidor web - Temporada activa: " + nombreTemporada);
        
        // Buscar la temporada
        Temporada temporada = datos.buscarTemporadaPorNombre(nombreTemporada);
        
        if (temporada == null) {
            GestorLog.error("Temporada no encontrada para sincronización: " + nombreTemporada);
            return;
        }
        
        // Contar recursos
        int equipos = temporada.getEquiposParticipantes().size();
        int jugadores = 0;
        for (Equipo e : temporada.getEquiposParticipantes()) {
            jugadores += e.getPlantilla().size();
        }
        int jornadas = temporada.getListaJornadas().size();
        int partidos = 0;
        for (Jornada j : temporada.getListaJornadas()) {
            partidos += j.getListaPartidos().size();
        }
        
        GestorLog.info("Datos a sincronizar - Equipos: " + equipos +
                      " | Jugadores: " + jugadores +
                      " | Jornadas: " + jornadas +
                      " | Partidos: " + partidos);
        
        // Llamar a la sincronización completa
        sincronizarConWeb(datos, nombreTemporada);
    }
    
    /**
     * Notifica al servidor sobre el cambio de estado de una temporada.
     */
    public static void notificarCambioEstado(String nombreTemporada, String estadoAnterior, String estadoNuevo) {
        GestorLog.info("Notificación de cambio de estado - Temporada: " + nombreTemporada +
                      " | " + estadoAnterior + " → " + estadoNuevo);
    }
    
    /**
     * Sube las imágenes de una exportación al servidor.
     * (Método heredado - ahora integrado en sincronizarConWeb)
     */
    public static void subirImagenesAlServidor(String rutaCarpetaImagenes) {
        if (rutaCarpetaImagenes == null || rutaCarpetaImagenes.isEmpty()) {
            GestorLog.advertencia("Ruta de imágenes vacía para subir al servidor");
            return;
        }
        
        GestorLog.info("Preparando subida de imágenes al servidor: " + rutaCarpetaImagenes);
        
        // La sincronización de imágenes ahora se maneja automáticamente
        // en sincronizarConWeb()
        
        GestorLog.info("Las imágenes se sincronizarán automáticamente con la próxima exportación");
    }
    
    /**
     * Verifica si el directorio del sitio web existe y es accesible.
     * 
     * @return true si el directorio web existe, false en caso contrario
     */
    public static boolean verificarDirectorioWeb() {
        File directorioWeb = new File(RUTA_WEB_BASE);
        
        if (!directorioWeb.exists()) {
            GestorLog.error("❌ No se encontró el directorio del sitio web: " + RUTA_WEB_BASE);
            GestorLog.info("💡 Asegúrate de que la estructura LM_Grupo2/HTML/ existe en el directorio padre");
            return false;
        }
        
        if (!directorioWeb.canWrite()) {
            GestorLog.error("❌ No hay permisos de escritura en: " + RUTA_WEB_BASE);
            return false;
        }
        
        GestorLog.info("✅ Directorio web verificado: " + directorioWeb.getAbsolutePath());
        return true;
    }
}
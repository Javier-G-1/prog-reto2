package logica;

import java.io.*;
import gestion.DatosFederacion;

/**
  CLASE: GestorArchivos

  Es la herramienta encargada de LEER y ESCRIBIR archivos en el ordenador.

   Guardado Automático: Salva todo el estado de la app en un archivo binario (.dat).
   Carga Inicial: Recupera los datos cuando abres la aplicación.
   Exportación XML: Crea un archivo legible por otras aplicaciones (como Excel o navegadores).
 */
public class GestorArchivos {

    // Nombre del archivo donde se guardará toda la "mochila" de datos de la federación. 
    private static final String ARCHIVO_DATOS = "datos_federacion.dat";

    // BLOQUE DE PERSISTENCIA (GUARDAR / CARGAR)

    /**
      MÉTODO: guardarTodo
      Toma el objeto maestro 'DatosFederacion' y lo escribe en el disco duro.
      datos: El contenedor con todos los usuarios, equipos y partidos.
     */
    public static void guardarTodo(DatosFederacion datos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(datos);
            System.out.println("SISTEMA: Todos los datos se han guardado correctamente.");
        } catch (IOException e) {
            System.err.println("ERROR al guardar los datos: " + e.getMessage());
        }
    }

    /**
      MÉTODO: cargarTodo
      Busca el archivo en el ordenador y lo convierte de nuevo en objetos de Java.
      El objeto DatosFederacion recuperado, o un objeto vacío si no hay archivo.
     */
    public static DatosFederacion cargarTodo() {
        File archivo = new File(ARCHIVO_DATOS);
        
        // Si el archivo no existe, devolvemos un contenedor vacío para que la app empiece de cero.
        if (!archivo.exists()) {
            return new DatosFederacion();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_DATOS))) {
            return (DatosFederacion) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ERROR al cargar los datos: " + e.getMessage());
            return new DatosFederacion();
        }
    }

    //BLOQUE DE EXPORTACIÓN (XML)

    /**
      MÉTODO: exportarAXML
      Toma la información y la escribe en un formato de texto estructurado (XML).
      datos El contenedor de la federación.
      rutaDestino Dónde queremos guardar el archivo XML (elegido por el usuario).
     */
    public static void exportarAXML(DatosFederacion datos, String rutaDestino) {
        // Para este ejemplo, simulamos la creación del archivo de texto plano con formato XML
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaDestino))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<Federacion>");
            
            // Ejemplo: Exportar lista de equipos
            writer.println("  <Equipos>");
            datos.getListaEquipos().forEach(e -> {
                writer.println("    <Equipo nombre=\"" + e.getNombre() + "\">");
                writer.println("      <JugadoresTotales>" + e.getPlantilla().size() + "</JugadoresTotales>");
                writer.println("    </Equipo>");
            });
            writer.println("  </Equipos>");
            
            writer.println("</Federacion>");
            System.out.println("EXPORTACIÓN: Archivo XML creado en: " + rutaDestino);
        } catch (IOException e) {
            System.err.println("ERROR en la exportación: " + e.getMessage());
        }
    }
}
package logica;

import java.io.*;
import gestion.DatosFederacion;

public class GestorArchivos {

    private static final String ARCHIVO_DATOS = "datos_federacion.dat";

    // Guardado de datos
    public static void guardarTodo(DatosFederacion datos) {
        if (datos == null) return;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(datos);
            System.out.println("SISTEMA: Todos los datos se han guardado correctamente.");
        } catch (IOException e) {
            System.err.println("ERROR al guardar los datos: " + e.getMessage());
        }
    }

    // Carga de datos
    public static DatosFederacion cargarTodo() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) return new DatosFederacion();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_DATOS))) {
            return (DatosFederacion) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ERROR al cargar los datos: " + e.getMessage());
            return new DatosFederacion();
        }
    }

    // Exportación XML simple
    public static void exportarAXML(DatosFederacion datos, String rutaDestino) {
        if (datos == null || rutaDestino == null || rutaDestino.isBlank()) return;
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaDestino))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<Federacion>");
            
            writer.println("  <Equipos>");
            for (var e : datos.getListaEquipos()) {
                writer.println("    <Equipo nombre=\"" + e.getNombre() + "\">");
                writer.println("      <JugadoresTotales>" + e.getPlantilla().size() + "</JugadoresTotales>");
                writer.println("    </Equipo>");
            }
            writer.println("  </Equipos>");
            writer.println("</Federacion>");
            System.out.println("EXPORTACIÓN: Archivo XML creado en: " + rutaDestino);
        } catch (IOException e) {
            System.err.println("ERROR en la exportación: " + e.getMessage());
        }
    }
}

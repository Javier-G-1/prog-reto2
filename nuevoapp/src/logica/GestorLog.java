package logica;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestorLog {
    private static final String ARCHIVO_LOG = "seguimiento_federacion.log";

    public static void registrarEvento(String mensaje) {
        if (mensaje == null || mensaje.isBlank()) return;

        try (PrintWriter out = new PrintWriter(new FileWriter(ARCHIVO_LOG, true))) {
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.println("[" + fecha + "] " + mensaje);
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el log: " + e.getMessage());
        }
    }
}

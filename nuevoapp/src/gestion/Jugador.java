package gestion;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CLASE: Jugador
 * <p>
 * Representa un jugador dentro del sistema deportivo.
 * Cada jugador tiene un ID único e incremental que nunca se repite,
 * incluso si se eliminan jugadores.
 * </p>
 * <p>
 * La clase implementa {@link Serializable} para permitir su persistencia.
 * </p>
 */
public class Jugador implements Serializable {

    /** Identificador de versión para la serialización */
    private static final long serialVersionUID = 1L;

    /** Contador global de IDs, compartido entre todos los jugadores */
    private static AtomicInteger contadorGlobal = new AtomicInteger(1);

    /** Identificador único e incremental del jugador */
    private String id;

    /** Nombre del jugador */
    private String nombre;

    /** Posición en el campo */
    private String posicion;

    /** Edad del jugador */
    private int edad;

    /** URL de la foto del jugador */
    private String fotoURL;

    /** Nacionalidad del jugador */
    private String nacionalidad;

    /** Altura del jugador */
    private String altura;

    /** Peso del jugador */
    private String peso;

    /** Número de dorsal */
    private int dorsal;

    // --- CONSTRUCTORES ---

    /**
     * Constructor simplificado.
     * <p>
     * Genera automáticamente un ID único incremental y asigna
     * valores por defecto a nacionalidad, altura, peso y dorsal.
     * </p>
     *
     * @param nombre nombre del jugador
     * @param posicion posición en el campo
     * @param edad edad del jugador
     * @param fotoURL URL de la foto del jugador
     */
    public Jugador(String nombre, String posicion, int edad, String fotoURL) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.edad = edad;
        this.fotoURL = fotoURL;
        this.id = generarIdUnico();
        this.nacionalidad = "España";
        this.altura = "175cm";
        this.peso = "75kg";
        this.dorsal = 0;
    }

    /**
     * Constructor completo para carga desde archivo o base de datos.
     * <p>
     * Genera un ID único si el proporcionado es nulo o vacío.
     * Además, actualiza el contador global si el ID cargado es mayor
     * para evitar duplicados en futuras inserciones.
     * </p>
     *
     * @param id ID del jugador (puede ser nulo)
     * @param nombre nombre del jugador
     * @param posicion posición en el campo
     * @param edad edad del jugador
     * @param fotoURL URL de la foto
     * @param nacionalidad nacionalidad
     * @param altura altura
     * @param peso peso
     * @param dorsal número de dorsal
     */
    public Jugador(String id, String nombre, String posicion, int edad, 
                   String fotoURL, String nacionalidad, String altura, 
                   String peso, int dorsal) {
        this.id = (id == null || id.isBlank()) ? generarIdUnico() : id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.edad = edad;
        this.fotoURL = fotoURL;
        this.nacionalidad = nacionalidad;
        this.altura = altura;
        this.peso = peso;
        this.dorsal = dorsal;

        // Actualizar contador si el ID cargado es mayor
        actualizarContadorSiNecesario(this.id);
    }

    // --- MÉTODOS PRIVADOS ---

    /**
     * Genera un ID único incremental.
     * <p>
     * Formato: JU00001, JU00002, ...
     * </p>
     *
     * @return ID único generado
     */
    private String generarIdUnico() {
        int numero = contadorGlobal.getAndIncrement();
        return String.format("JU%05d", numero);
    }

    /**
     * Actualiza el contador global si el ID proporcionado es mayor.
     * <p>
     * Esto asegura que los IDs futuros no se dupliquen.
     * </p>
     *
     * @param id ID del jugador a evaluar
     */
    private void actualizarContadorSiNecesario(String id) {
        if (id == null || !id.startsWith("JU")) return;

        try {
            int numeroId = Integer.parseInt(id.substring(2));
            int valorActual;
            do {
                valorActual = contadorGlobal.get();
                if (numeroId < valorActual) break;
            } while (!contadorGlobal.compareAndSet(valorActual, numeroId + 1));
        } catch (NumberFormatException e) {
            // ID mal formado, ignorar
        }
    }

    // --- MÉTODOS ESTÁTICOS ---

    /**
     * Sincroniza el contador global al cargar jugadores desde almacenamiento.
     * <p>
     * Debe llamarse después de cargar todos los jugadores para asegurar
     * que los nuevos IDs generados no se dupliquen.
     * </p>
     *
     * @param todosLosJugadores lista de todos los jugadores cargados
     */
    public static void sincronizarContadorGlobal(java.util.List<Jugador> todosLosJugadores) {
        if (todosLosJugadores == null || todosLosJugadores.isEmpty()) {
            contadorGlobal.set(1);
            return;
        }

        int maxId = 0;

        for (Jugador j : todosLosJugadores) {
            if (j.id != null && j.id.startsWith("JU")) {
                try {
                    int numeroId = Integer.parseInt(j.id.substring(2));
                    if (numeroId > maxId) {
                        maxId = numeroId;
                    }
                } catch (NumberFormatException e) {
                    // Ignorar IDs mal formados
                }
            }
        }

        contadorGlobal.set(maxId + 1);
        System.out.println("✓ Contador de jugadores sincronizado en: JU" + 
                         String.format("%05d", maxId + 1));
    }

    /**
     * Obtiene el siguiente ID que se generará.
     *
     * @return próximo ID como cadena
     */
    public static String obtenerProximoId() {
        return String.format("JU%05d", contadorGlobal.get());
    }

    // --- GETTERS Y SETTERS ---

    public String getId() { return id; }
    public void setId(String id) { 
        this.id = id;
        actualizarContadorSiNecesario(id);
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getFotoURL() { return fotoURL; }
    public void setFotoURL(String fotoURL) { this.fotoURL = fotoURL; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getAltura() { return altura; }
    public void setAltura(String altura) { this.altura = altura; }

    public String getPeso() { return peso; }
    public void setPeso(String peso) { this.peso = peso; }

    public int getDorsal() { return dorsal; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }

    // --- OVERRIDES ---

    /**
     * Representación en texto del jugador.
     *
     * @return nombre, dorsal y posición
     */
    @Override
    public String toString() {
        return nombre + " (#" + dorsal + " - " + posicion + ")";
    }

    /**
     * Compara jugadores por su ID único.
     *
     * @param o objeto a comparar
     * @return {@code true} si los IDs coinciden, {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(id, jugador.id);
    }

    /**
     * Calcula el hash basado en el ID del jugador.
     *
     * @return valor hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

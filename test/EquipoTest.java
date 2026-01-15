

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gestion.Equipo;
import gestion.Jugador;

import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para la clase Equipo.
 * Valida el correcto funcionamiento de todas las operaciones
 * relacionadas con la gestión de equipos y jugadores.
 */
class EquipoTest {

    private Equipo equipo;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugador3;

    @BeforeEach
    void setUp() {
        // Inicializar equipo y jugadores de prueba antes de cada test
        equipo = new Equipo("Real Madrid", "img/realmadrid.png");
        
        jugador1 = new Jugador("Karim Benzema", "Delantero", 35, "foto1.jpg");
        jugador1.setDorsal(9);
        
        jugador2 = new Jugador("Luka Modric", "Centrocampista", 37, "foto2.jpg");
        jugador2.setDorsal(10);
        
        jugador3 = new Jugador("Thibaut Courtois", "Portero", 31, "foto3.jpg");
        jugador3.setDorsal(1);
    }

    // ==================== TESTS DE CONSTRUCTORES ====================

    @Test
    @DisplayName("Constructor con ruta de escudo válida")
    void testConstructorConRutaEscudo() {
        Equipo eq = new Equipo("Barcelona", "img/barcelona.png");
        
        assertEquals("Barcelona", eq.getNombre());
        assertEquals("img/barcelona.png", eq.getRutaEscudo());
        assertNotNull(eq.getPlantilla());
        assertTrue(eq.getPlantilla().isEmpty());
    }

    @Test
    @DisplayName("Constructor con ruta de escudo nula asigna escudo por defecto")
    void testConstructorConRutaEscudoNula() {
        Equipo eq = new Equipo("Atlético", null);
        
        assertEquals("img/default_escudo.png", eq.getRutaEscudo());
    }

    @Test
    @DisplayName("Constructor con ruta de escudo vacía asigna escudo por defecto")
    void testConstructorConRutaEscudoVacia() {
        Equipo eq = new Equipo("Sevilla", "   ");
        
        assertEquals("img/default_escudo.png", eq.getRutaEscudo());
    }

    @Test
    @DisplayName("Constructor simplificado asigna escudo por defecto")
    void testConstructorSimplificado() {
        Equipo eq = new Equipo("Valencia");
        
        assertEquals("Valencia", eq.getNombre());
        assertEquals("img/default_escudo.png", eq.getRutaEscudo());
        assertNotNull(eq.getPlantilla());
    }

    // ==================== TESTS DE FICHAR JUGADOR ====================

    @Test
    @DisplayName("Fichar jugador correctamente")
    void testFicharJugadorCorrecto() {
        equipo.ficharJugador(jugador1);
        
        assertEquals(1, equipo.getPlantilla().size());
        assertTrue(equipo.getPlantilla().contains(jugador1));
    }

    @Test
    @DisplayName("Fichar múltiples jugadores")
    void testFicharMultiplesJugadores() {
        equipo.ficharJugador(jugador1);
        equipo.ficharJugador(jugador2);
        equipo.ficharJugador(jugador3);
        
        assertEquals(3, equipo.getPlantilla().size());
    }

    @Test
    @DisplayName("No se puede fichar jugador nulo")
    void testFicharJugadorNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            equipo.ficharJugador(null);
        });
        
        assertEquals("El jugador no puede ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("No se puede fichar el mismo jugador dos veces")
    void testFicharJugadorDuplicado() {
        equipo.ficharJugador(jugador1);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            equipo.ficharJugador(jugador1);
        });
        
        assertEquals("El jugador ya está en la plantilla", exception.getMessage());
    }

    @Test
    @DisplayName("No se puede fichar jugador con dorsal ocupado")
    void testFicharJugadorConDorsalOcupado() {
        equipo.ficharJugador(jugador1); // Dorsal 9
        
        Jugador jugador4 = new Jugador("Nuevo Jugador", "Defensa", 25, "foto4.jpg");
        jugador4.setDorsal(9); // Mismo dorsal
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            equipo.ficharJugador(jugador4);
        });
        
        assertTrue(exception.getMessage().contains("El dorsal 9 ya está ocupado"));
    }

    @Test
    @DisplayName("Se puede fichar jugador con dorsal 0 (sin asignar)")
    void testFicharJugadorSinDorsal() {
        Jugador jugadorSinDorsal = new Jugador("Sin Dorsal", "Defensa", 22, "foto.jpg");
        jugadorSinDorsal.setDorsal(0);
        
        assertDoesNotThrow(() -> equipo.ficharJugador(jugadorSinDorsal));
        assertEquals(1, equipo.getPlantilla().size());
    }

    // ==================== TESTS DE VALIDACIÓN DE DORSAL ====================

    @Test
    @DisplayName("Validar dorsal único correcto")
    void testValidarDorsalUnicoCorrecto() {
        equipo.ficharJugador(jugador1); // Dorsal 9
        
        assertDoesNotThrow(() -> equipo.validarDorsalUnico(7, "JU00001"));
    }

    @Test
    @DisplayName("Validar dorsal ocupado lanza excepción")
    void testValidarDorsalOcupado() {
        equipo.ficharJugador(jugador1); // Dorsal 9
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            equipo.validarDorsalUnico(9, "JU00002");
        });
        
        assertTrue(exception.getMessage().contains("El dorsal 9 ya está ocupado"));
    }

    @Test
    @DisplayName("Validar dorsal menor a 1 lanza excepción")
    void testValidarDorsalMenorA1() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            equipo.validarDorsalUnico(0, "JU00001");
        });
        
        assertEquals("El dorsal debe estar entre 1 y 99", exception.getMessage());
    }

    @Test
    @DisplayName("Validar dorsal mayor a 99 lanza excepción")
    void testValidarDorsalMayorA99() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            equipo.validarDorsalUnico(100, "JU00001");
        });
        
        assertEquals("El dorsal debe estar entre 1 y 99", exception.getMessage());
    }

    @Test
    @DisplayName("Jugador puede mantener su propio dorsal")
    void testJugadorMantieneSuPropioDorsal() {
        equipo.ficharJugador(jugador1); // Dorsal 9
        
        // El jugador1 puede validar su propio dorsal 9
        assertDoesNotThrow(() -> equipo.validarDorsalUnico(9, jugador1.getId()));
    }

    // ==================== TESTS DE DORSAL DISPONIBLE ====================

    @Test
    @DisplayName("Dorsal disponible cuando no está ocupado")
    void testDorsalDisponible() {
        equipo.ficharJugador(jugador1); // Dorsal 9
        
        assertTrue(equipo.dorsalDisponible(7));
        assertFalse(equipo.dorsalDisponible(9));
    }

    @Test
    @DisplayName("Dorsal 0 no está disponible")
    void testDorsal0NoDisponible() {
        assertFalse(equipo.dorsalDisponible(0));
    }

    @Test
    @DisplayName("Dorsal 100 no está disponible")
    void testDorsal100NoDisponible() {
        assertFalse(equipo.dorsalDisponible(100));
    }

    @Test
    @DisplayName("Dorsal disponible excluyendo un jugador")
    void testDorsalDisponibleExcluyendoJugador() {
        equipo.ficharJugador(jugador1); // Dorsal 9
        
        // El dorsal 9 está ocupado normalmente
        assertFalse(equipo.dorsalDisponible(9));
        
        // Pero si excluimos al jugador1, está disponible
        assertTrue(equipo.dorsalDisponible(9, jugador1.getId()));
    }

    // ==================== TESTS DE SIGUIENTE DORSAL DISPONIBLE ====================

    @Test
    @DisplayName("Obtener siguiente dorsal disponible en equipo vacío")
    void testObtenerSiguienteDorsalDisponibleEquipoVacio() {
        assertEquals(1, equipo.obtenerSiguienteDorsalDisponible());
    }

    @Test
    @DisplayName("Obtener siguiente dorsal disponible con jugadores")
    void testObtenerSiguienteDorsalDisponibleConJugadores() {
        jugador1.setDorsal(1);
        jugador2.setDorsal(2);
        jugador3.setDorsal(3);
        
        equipo.ficharJugador(jugador1);
        equipo.ficharJugador(jugador2);
        equipo.ficharJugador(jugador3);
        
        assertEquals(4, equipo.obtenerSiguienteDorsalDisponible());
    }

    @Test
    @DisplayName("Obtener siguiente dorsal disponible con huecos")
    void testObtenerSiguienteDorsalDisponibleConHuecos() {
        jugador1.setDorsal(1);
        jugador2.setDorsal(3);
        jugador3.setDorsal(5);
        
        equipo.ficharJugador(jugador1);
        equipo.ficharJugador(jugador2);
        equipo.ficharJugador(jugador3);
        
        assertEquals(2, equipo.obtenerSiguienteDorsalDisponible());
    }

    // ==================== TESTS DE LISTA DE DORSALES DISPONIBLES ====================

    @Test
    @DisplayName("Obtener todos los dorsales disponibles")
    void testObtenerDorsalesDisponibles() {
        jugador1.setDorsal(1);
        jugador2.setDorsal(2);
        
        equipo.ficharJugador(jugador1);
        equipo.ficharJugador(jugador2);
        
        List<Integer> disponibles = equipo.obtenerDorsalesDisponibles();
        
        assertEquals(97, disponibles.size()); // 99 - 2 ocupados
        assertFalse(disponibles.contains(1));
        assertFalse(disponibles.contains(2));
        assertTrue(disponibles.contains(3));
        assertTrue(disponibles.contains(99));
    }

    // ==================== TESTS DE BAJA JUGADOR ====================

    @Test
    @DisplayName("Dar de baja jugador correctamente")
    void testBajaJugador() {
        equipo.ficharJugador(jugador1);
        equipo.ficharJugador(jugador2);
        
        assertEquals(2, equipo.getPlantilla().size());
        
        equipo.bajaJugador(jugador1);
        
        assertEquals(1, equipo.getPlantilla().size());
        assertFalse(equipo.getPlantilla().contains(jugador1));
        assertTrue(equipo.getPlantilla().contains(jugador2));
    }

    // ==================== TESTS DE JUGADORES MÍNIMOS ====================

    @Test
    @DisplayName("Equipo sin jugadores suficientes")
    void testEquipoSinJugadoresSuficientes() {
        assertFalse(equipo.tieneJugadoresSuficientes());
        assertEquals(9, equipo.jugadoresFaltantes());
    }

    @Test
    @DisplayName("Equipo con jugadores suficientes")
    void testEquipoConJugadoresSuficientes() {
        // Añadir 9 jugadores
        for (int i = 1; i <= 9; i++) {
            Jugador j = new Jugador("Jugador " + i, "Defensa", 25, "foto.jpg");
            j.setDorsal(i);
            equipo.ficharJugador(j);
        }
        
        assertTrue(equipo.tieneJugadoresSuficientes());
        assertEquals(0, equipo.jugadoresFaltantes());
    }

    @Test
    @DisplayName("Número de jugadores correcto")
    void testNumeroDeJugadores() {
        assertEquals(0, equipo.numeroDeJugadores());
        
        equipo.ficharJugador(jugador1);
        assertEquals(1, equipo.numeroDeJugadores());
        
        equipo.ficharJugador(jugador2);
        assertEquals(2, equipo.numeroDeJugadores());
    }

    // ==================== TESTS DE MENSAJES DE ESTADO ====================

    @Test
    @DisplayName("Mensaje de estado con plantilla incompleta")
    void testMensajeEstadoPlantillaIncompleta() {
        equipo.ficharJugador(jugador1);
        
        String mensaje = equipo.obtenerMensajeEstadoPlantilla();
        
        assertTrue(mensaje.contains("Faltan"));
        assertTrue(mensaje.contains("8"));
    }

    @Test
    @DisplayName("Mensaje de estado con mínimo alcanzado")
    void testMensajeEstadoMinimoAlcanzado() {
        for (int i = 1; i <= 10; i++) {
            Jugador j = new Jugador("Jugador " + i, "Defensa", 25, "foto.jpg");
            j.setDorsal(i);
            equipo.ficharJugador(j);
        }
        
        String mensaje = equipo.obtenerMensajeEstadoPlantilla();
        
        assertTrue(mensaje.contains("Mínimo alcanzado"));
    }

    @Test
    @DisplayName("Mensaje de estado con plantilla completa")
    void testMensajeEstadoPlantillaCompleta() {
        for (int i = 1; i <= 18; i++) {
            Jugador j = new Jugador("Jugador " + i, "Defensa", 25, "foto.jpg");
            j.setDorsal(i);
            equipo.ficharJugador(j);
        }
        
        String mensaje = equipo.obtenerMensajeEstadoPlantilla();
        
        assertTrue(mensaje.contains("Plantilla completa"));
    }

    @Test
    @DisplayName("Detalle de validación con plantilla insuficiente")
    void testDetalleValidacionInsuficiente() {
        String detalle = equipo.obtenerDetalleValidacion();
        
        assertTrue(detalle.contains("Real Madrid"));
        assertTrue(detalle.contains("Faltan"));
        assertTrue(detalle.contains("✗"));
    }

    @Test
    @DisplayName("Detalle de validación con plantilla suficiente")
    void testDetalleValidacionSuficiente() {
        for (int i = 1; i <= 9; i++) {
            Jugador j = new Jugador("Jugador " + i, "Defensa", 25, "foto.jpg");
            j.setDorsal(i);
            equipo.ficharJugador(j);
        }
        
        String detalle = equipo.obtenerDetalleValidacion();
        
        assertTrue(detalle.contains("Real Madrid"));
        assertTrue(detalle.contains("9"));
        assertTrue(detalle.contains("✓"));
    }

    // ==================== TESTS DE EQUALS Y HASHCODE ====================

    @Test
    @DisplayName("Equipos con mismo nombre son iguales")
    void testEqualsConMismoNombre() {
        Equipo equipo2 = new Equipo("Real Madrid", "otra_ruta.png");
        
        assertEquals(equipo, equipo2);
    }

    @Test
    @DisplayName("Equipos con mismo nombre ignoran mayúsculas")
    void testEqualsIgnoraMayusculas() {
        Equipo equipo2 = new Equipo("REAL MADRID");
        
        assertEquals(equipo, equipo2);
    }

    @Test
    @DisplayName("Equipos con diferente nombre no son iguales")
    void testEqualsConDiferenteNombre() {
        Equipo equipo2 = new Equipo("Barcelona");
        
        assertNotEquals(equipo, equipo2);
    }

    @Test
    @DisplayName("HashCode consistente con equals")
    void testHashCodeConsistente() {
        Equipo equipo2 = new Equipo("Real Madrid");
        
        assertEquals(equipo.hashCode(), equipo2.hashCode());
    }

    // ==================== TESTS DE GETTERS Y SETTERS ====================

    @Test
    @DisplayName("Modificar nombre del equipo")
    void testSetNombre() {
        equipo.setNombre("Nuevo Nombre");
        
        assertEquals("Nuevo Nombre", equipo.getNombre());
    }

    @Test
    @DisplayName("Modificar ruta del escudo")
    void testSetRutaEscudo() {
        equipo.setRutaEscudo("nueva_ruta.png");
        
        assertEquals("nueva_ruta.png", equipo.getRutaEscudo());
    }

    // ==================== TESTS DE TOSTRING ====================

    @Test
    @DisplayName("ToString devuelve el nombre del equipo")
    void testToString() {
        assertEquals("Real Madrid", equipo.toString());
    }
}
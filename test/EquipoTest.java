import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import gestion.Equipo;
import gestion.Jugador;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class EquipoTest {

    private Equipo equipo;
    private Jugador jugador1, jugador2, jugador3;

    @BeforeEach
    void setUp() {
        Equipo.sincronizarContadorGlobal(new ArrayList<>());
        equipo = new Equipo("Real Madrid", "img/realmadrid.png");
        jugador1 = new Jugador("Benzema", "Delantero", 35, "foto1.jpg");
        jugador1.setDorsal(9);
        jugador2 = new Jugador("Modric", "Centrocampista", 37, "foto2.jpg");
        jugador2.setDorsal(10);
        jugador3 = new Jugador("Courtois", "Portero", 31, "foto3.jpg");
        jugador3.setDorsal(1);
    }

    @Test
    @DisplayName("Constructor con ruta válida")
    void testConstructor() {
        assertEquals("Real Madrid", equipo.getNombre());
        assertEquals("img/realmadrid.png", equipo.getRutaEscudo());
        assertTrue(equipo.getPlantilla().isEmpty());
        assertTrue(equipo.getId().startsWith("E"));
    }

    @Test
    @DisplayName("Constructor con ruta nula/vacía asigna default")
    void testConstructorRutaDefault() {
        assertEquals("img/default_escudo.png", new Equipo("A", null).getRutaEscudo());
        assertEquals("img/default_escudo.png", new Equipo("B", "   ").getRutaEscudo());
        assertEquals("img/default_escudo.png", new Equipo("C").getRutaEscudo());
    }

    @Test
    @DisplayName("IDs únicos e incrementales")
    void testIdsUnicos() {
        Equipo e1 = new Equipo("E1"), e2 = new Equipo("E2");
        assertNotEquals(e1.getId(), e2.getId());
    }

    @Test
    @DisplayName("Fichar jugador correctamente")
    void testFichar() {
        equipo.ficharJugador(jugador1);
        assertEquals(1, equipo.getPlantilla().size());
        assertTrue(equipo.getPlantilla().contains(jugador1));
    }

    @Test
    @DisplayName("No fichar jugador nulo o duplicado")
    void testFicharInvalido() {
        assertThrows(IllegalArgumentException.class, () -> equipo.ficharJugador(null));
        equipo.ficharJugador(jugador1);
        assertThrows(IllegalArgumentException.class, () -> equipo.ficharJugador(jugador1));
    }

    @Test
    @DisplayName("No fichar con dorsal ocupado")
    void testDorsalOcupado() {
        equipo.ficharJugador(jugador1);
        Jugador j = new Jugador("Otro", "Defensa", 25, "foto.jpg");
        j.setDorsal(9);
        assertThrows(IllegalArgumentException.class, () -> equipo.ficharJugador(j));
    }

    @Test
    @DisplayName("Validar dorsal único")
    void testValidarDorsal() {
        equipo.ficharJugador(jugador1);
        assertDoesNotThrow(() -> equipo.validarDorsalUnico(7, "ID"));
        assertThrows(IllegalArgumentException.class, () -> equipo.validarDorsalUnico(9, "ID2"));
        assertThrows(IllegalArgumentException.class, () -> equipo.validarDorsalUnico(0, "ID"));
        assertThrows(IllegalArgumentException.class, () -> equipo.validarDorsalUnico(100, "ID"));
        assertDoesNotThrow(() -> equipo.validarDorsalUnico(9, jugador1.getId()));
    }

    @Test
    @DisplayName("Dorsal disponible")
    void testDorsalDisponible() {
        equipo.ficharJugador(jugador1);
        assertTrue(equipo.dorsalDisponible(7));
        assertFalse(equipo.dorsalDisponible(9));
        assertFalse(equipo.dorsalDisponible(0));
        assertFalse(equipo.dorsalDisponible(100));
        assertTrue(equipo.dorsalDisponible(9, jugador1.getId()));
    }

    @Test
    @DisplayName("Siguiente dorsal disponible")
    void testSiguienteDorsal() {
        assertEquals(1, equipo.obtenerSiguienteDorsalDisponible());
        jugador1.setDorsal(1);
        jugador2.setDorsal(3);
        equipo.ficharJugador(jugador1);
        equipo.ficharJugador(jugador2);
        assertEquals(2, equipo.obtenerSiguienteDorsalDisponible());
    }

    @Test
    @DisplayName("Lista dorsales disponibles")
    void testListaDorsales() {
        jugador1.setDorsal(1);
        jugador2.setDorsal(2);
        equipo.ficharJugador(jugador1);
        equipo.ficharJugador(jugador2);
        List<Integer> disp = equipo.obtenerDorsalesDisponibles();
        assertEquals(97, disp.size());
        assertFalse(disp.contains(1));
        assertTrue(disp.contains(3));
    }

    @Test
    @DisplayName("Baja jugador")
    void testBaja() {
        equipo.ficharJugador(jugador1);
        equipo.ficharJugador(jugador2);
        equipo.bajaJugador(jugador1);
        assertEquals(1, equipo.getPlantilla().size());
        assertFalse(equipo.getPlantilla().contains(jugador1));
    }

    @Test
    @DisplayName("Jugadores suficientes")
    void testJugadoresSuficientes() {
        assertFalse(equipo.tieneJugadoresSuficientes());
        assertEquals(9, equipo.jugadoresFaltantes());
        for (int i = 1; i <= 9; i++) {
            Jugador j = new Jugador("J" + i, "D", 25, "f.jpg");
            j.setDorsal(i);
            equipo.ficharJugador(j);
        }
        assertTrue(equipo.tieneJugadoresSuficientes());
        assertEquals(0, equipo.jugadoresFaltantes());
    }

    @Test
    @DisplayName("Mensajes de estado")
    void testMensajes() {
        assertTrue(equipo.obtenerMensajeEstadoPlantilla().contains("Faltan"));
        assertTrue(equipo.obtenerDetalleValidacion().contains("✗"));
        for (int i = 1; i <= 18; i++) {
            Jugador j = new Jugador("J" + i, "D", 25, "f.jpg");
            j.setDorsal(i);
            equipo.ficharJugador(j);
        }
        assertTrue(equipo.obtenerMensajeEstadoPlantilla().contains("completa"));
    }

    @Test
    @DisplayName("Equals y hashCode por ID")
    void testEquals() {
        Equipo e2 = new Equipo(equipo.getId(), "Otro", "r.png");
        assertEquals(equipo, e2);
        assertEquals(equipo.hashCode(), e2.hashCode());
        assertNotEquals(equipo, new Equipo("Barcelona"));
    }

    @Test
    @DisplayName("ToString con ID")
    void testToString() {
        String s = equipo.toString();
        assertTrue(s.contains("Real Madrid"));
        assertTrue(s.contains(equipo.getId()));
    }

    @Test
    @DisplayName("Setters funcionan")
    void testSetters() {
        equipo.setNombre("Nuevo");
        assertEquals("Nuevo", equipo.getNombre());
        equipo.setRutaEscudo("r.png");
        assertEquals("r.png", equipo.getRutaEscudo());
    }

    @Test
    @DisplayName("Sincronización contador")
    void testSincronizar() {
        List<Equipo> equipos = new ArrayList<>();
        equipos.add(new Equipo("E100", "E1", "r.png"));
        Equipo.sincronizarContadorGlobal(equipos);
        assertEquals("E101", new Equipo("Test").getId());
    }
}
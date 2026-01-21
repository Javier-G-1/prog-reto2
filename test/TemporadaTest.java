import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import gestion.Equipo;
import gestion.Jornada;
import gestion.Jugador;
import gestion.Partido;
import gestion.Temporada;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class TemporadaJornadaPartidoTest {

    @Nested
    @DisplayName("Tests Partido")
    class PartidoTest {
        private Equipo local, visitante;
        private Partido partido;

        @BeforeEach
        void setUp() {
            Partido.reiniciarContador();
            local = new Equipo("Real Madrid");
            visitante = new Equipo("Barcelona");
            partido = new Partido(local, visitante);
        }

        @Test
        @DisplayName("Constructor válido con ID")
        void testConstructor() {
            assertEquals(local, partido.getEquipoLocal());
            assertEquals(visitante, partido.getEquipoVisitante());
            assertEquals(-1, partido.getGolesLocal());
            assertFalse(partido.isFinalizado());
            assertNull(partido.getFecha());
            assertNotNull(partido.getId());
            assertTrue(partido.getId().startsWith("P"));
        }

        @Test
        @DisplayName("Constructor con equipos inválidos")
        void testConstructorInvalido() {
            assertThrows(IllegalArgumentException.class, () -> new Partido(null, visitante));
            assertThrows(IllegalArgumentException.class, () -> new Partido(local, null));
            assertThrows(IllegalArgumentException.class, () -> new Partido(local, local));
        }

        @Test
        @DisplayName("Constructor completo con ID específico")
        void testConstructorConId() {
            Partido p = new Partido("P99", local, visitante);
            assertEquals("P99", p.getId());
        }

        @Test
        @DisplayName("IDs únicos e incrementales")
        void testIdsUnicos() {
            Partido p1 = new Partido(local, visitante);
            Partido p2 = new Partido(visitante, local);
            assertNotEquals(p1.getId(), p2.getId());
        }

        @Test
        @DisplayName("Ganador según resultado")
        void testGanador() {
            assertEquals("Pendiente", partido.obtenerGanador());
            
            partido.setGolesLocal(3);
            partido.setGolesVisitante(1);
            partido.setFinalizado(true);
            assertEquals("Real Madrid", partido.obtenerGanador());
            
            partido.setGolesLocal(1);
            partido.setGolesVisitante(3);
            assertEquals("Barcelona", partido.obtenerGanador());
            
            partido.setGolesLocal(2);
            partido.setGolesVisitante(2);
            assertEquals("Empate", partido.obtenerGanador());
        }

        @Test
        @DisplayName("Puntos local")
        void testPuntosLocal() {
            assertEquals(0, partido.getPuntosLocal());
            
            partido.setFinalizado(true);
            partido.setGolesLocal(3);
            partido.setGolesVisitante(1);
            assertEquals(2, partido.getPuntosLocal());
            
            partido.setGolesVisitante(3);
            assertEquals(1, partido.getPuntosLocal());
            
            partido.setGolesVisitante(5);
            assertEquals(0, partido.getPuntosLocal());
        }

        @Test
        @DisplayName("Puntos visitante")
        void testPuntosVisitante() {
            partido.setFinalizado(true);
            partido.setGolesLocal(1);
            partido.setGolesVisitante(3);
            assertEquals(2, partido.getPuntosVisitante());
            
            partido.setGolesLocal(3);
            assertEquals(1, partido.getPuntosVisitante());
            
            partido.setGolesLocal(5);
            assertEquals(0, partido.getPuntosVisitante());
        }

        @Test
        @DisplayName("Setters funcionan")
        void testSetters() {
            partido.setGolesLocal(5);
            assertEquals(5, partido.getGolesLocal());
            
            partido.setFecha("15/01/2026");
            assertEquals("15/01/2026", partido.getFecha());
            
            Equipo nuevo = new Equipo("Atlético");
            partido.setEquipoLocal(nuevo);
            assertEquals(nuevo, partido.getEquipoLocal());
        }

        @Test
        @DisplayName("ToString con ID")
        void testToString() {
            partido.setGolesLocal(2);
            partido.setGolesVisitante(1);
            String s = partido.toString();
            assertTrue(s.contains(partido.getId()));
            assertTrue(s.contains("Real Madrid"));
            assertTrue(s.contains("Barcelona"));
        }

        @Test
        @DisplayName("Sincronización contador")
        void testSincronizar() {
            java.util.List<Partido> partidos = new ArrayList<>();
            partidos.add(new Partido("P50", local, visitante));
            Partido.sincronizarContadorGlobal(partidos);
            assertEquals("P51", Partido.obtenerProximoId());
        }
    }

    @Nested
    @DisplayName("Tests Jornada")
    class JornadaTest {
        private Jornada jornada;
        private Equipo e1, e2, e3, e4;

        @BeforeEach
        void setUp() {
            Jornada.reiniciarContador();
            jornada = new Jornada("Jornada 1");
            e1 = new Equipo("RM");
            e2 = new Equipo("FCB");
            e3 = new Equipo("ATM");
            e4 = new Equipo("SEV");
        }

        @Test
        @DisplayName("Constructor válido con ID")
        void testConstructor() {
            assertEquals("Jornada 1", jornada.getNombre());
            assertTrue(jornada.getListaPartidos().isEmpty());
            assertNotNull(jornada.getId());
            assertTrue(jornada.getId().startsWith("J"));
        }

        @Test
        @DisplayName("Constructor completo con ID específico")
        void testConstructorConId() {
            Jornada j = new Jornada("J99", "Jornada Final");
            assertEquals("J99", j.getId());
            assertEquals("Jornada Final", j.getNombre());
        }

        @Test
        @DisplayName("IDs únicos e incrementales")
        void testIdsUnicos() {
            Jornada j1 = new Jornada("J1");
            Jornada j2 = new Jornada("J2");
            assertNotEquals(j1.getId(), j2.getId());
        }

        @Test
        @DisplayName("Agregar partidos")
        void testAgregarPartidos() {
            Partido p1 = new Partido(e1, e2);
            jornada.agregarPartido(p1);
            assertEquals(1, jornada.getListaPartidos().size());
            
            jornada.agregarPartido(null);
            assertEquals(1, jornada.getListaPartidos().size());
            
            Partido p2 = new Partido(e3, e4);
            jornada.agregarPartido(p2);
            assertEquals(2, jornada.getListaPartidos().size());
        }

        @Test
        @DisplayName("Equals y hashCode por ID")
        void testEquals() {
            Jornada j2 = new Jornada(jornada.getId(), "Otro nombre");
            assertEquals(jornada, j2);
            assertEquals(jornada.hashCode(), j2.hashCode());
            assertNotEquals(jornada, new Jornada("Nueva"));
        }

        @Test
        @DisplayName("ToString con ID")
        void testToString() {
            String s = jornada.toString();
            assertTrue(s.contains("Jornada 1"));
            assertTrue(s.contains(jornada.getId()));
        }

        @Test
        @DisplayName("Setters funcionan")
        void testSetters() {
            jornada.setNombre("Nueva Jornada");
            assertEquals("Nueva Jornada", jornada.getNombre());
        }

        @Test
        @DisplayName("Sincronización contador")
        void testSincronizar() {
            java.util.List<Jornada> jornadas = new ArrayList<>();
            jornadas.add(new Jornada("J50", "J1"));
            Jornada.sincronizarContadorGlobal(jornadas);
            assertEquals("J51", Jornada.obtenerProximoId());
        }
    }

    @Nested
    @DisplayName("Tests Temporada")
    class TemporadaTest {
        private Temporada temp;
        private Equipo e1, e2, e3, e4, e5, e6;

        @BeforeEach
        void setUp() {
            temp = new Temporada("2025/2026", Temporada.FUTURA);
            e1 = new Equipo("E1");
            e2 = new Equipo("E2");
            e3 = new Equipo("E3");
            e4 = new Equipo("E4");
            e5 = new Equipo("E5");
            e6 = new Equipo("E6");
        }

        @Test
        @DisplayName("Constructor y estados")
        void testConstructor() {
            assertEquals("2025/2026", temp.getNombre());
            assertEquals(Temporada.FUTURA, temp.getEstado());
            assertTrue(temp.getEquiposParticipantes().isEmpty());
            assertNotNull(temp.getId());
            
            assertEquals(Temporada.EN_JUEGO, new Temporada("T1", Temporada.EN_JUEGO).getEstado());
            assertEquals(Temporada.TERMINADA, new Temporada("T2", Temporada.TERMINADA).getEstado());
        }

        @Test
        @DisplayName("Constructor completo con ID")
        void testConstructorConId() {
            Temporada t = new Temporada("TEMP_2024", "Liga 2024", Temporada.FUTURA);
            assertEquals("TEMP_2024", t.getId());
        }

        @Test
        @DisplayName("Inscribir equipos")
        void testInscribir() {
            temp.inscribirEquipo(e1);
            assertEquals(1, temp.getEquiposParticipantes().size());
            
            temp.inscribirEquipo(null);
            assertEquals(1, temp.getEquiposParticipantes().size());
            
            temp.inscribirEquipo(e1);
            assertEquals(1, temp.getEquiposParticipantes().size());
        }

        @Test
        @DisplayName("No inscribir si no es FUTURA")
        void testNoInscribirSiNoEsFutura() {
            temp.setEstado(Temporada.EN_JUEGO);
            temp.inscribirEquipo(e1);
            assertTrue(temp.getEquiposParticipantes().isEmpty());
            
            temp.setEstado(Temporada.TERMINADA);
            temp.inscribirEquipo(e2);
            assertTrue(temp.getEquiposParticipantes().isEmpty());
        }

        @Test
        @DisplayName("Equipos suficientes")
        void testEquiposSuficientes() {
            assertFalse(temp.tieneEquiposSuficientes());
            
            temp.inscribirEquipo(e1);
            temp.inscribirEquipo(e2);
            temp.inscribirEquipo(e3);
            assertFalse(temp.tieneEquiposSuficientes());
            
            temp.inscribirEquipo(e4);
            temp.inscribirEquipo(e5);
            temp.inscribirEquipo(e6);
            assertTrue(temp.tieneEquiposSuficientes());
        }

        @Test
        @DisplayName("Buscar equipo")
        void testBuscar() {
            temp.inscribirEquipo(e1);
            temp.inscribirEquipo(e2);
            
            assertNotNull(temp.buscarEquipoPorNombre("E1"));
            assertNull(temp.buscarEquipoPorNombre("NoExiste"));
            assertNull(temp.buscarEquipoPorNombre(null));
        }

        @Test
        @DisplayName("Jugador inscrito")
        void testJugadorInscrito() {
            Jugador j = new Jugador("Messi", "Del", 36, "f.jpg");
            j.setDorsal(10);
            
            assertFalse(temp.jugadorYaInscrito(j));
            
            e1.ficharJugador(j);
            temp.inscribirEquipo(e1);
            assertTrue(temp.jugadorYaInscrito(j));
        }

        @Test
        @DisplayName("Agregar jornadas")
        void testJornadas() {
            Jornada j1 = new Jornada("J1");
            temp.agregarJornada(j1);
            assertEquals(1, temp.getListaJornadas().size());
            
            temp.agregarJornada(null);
            assertEquals(1, temp.getListaJornadas().size());
        }

        @Test
        @DisplayName("Cambiar estado")
        void testCambiarEstado() {
            temp.setEstado(Temporada.EN_JUEGO);
            assertEquals(Temporada.EN_JUEGO, temp.getEstado());
            
            temp.setEstado("INVALIDO");
            assertEquals(Temporada.EN_JUEGO, temp.getEstado());
        }

        @Test
        @DisplayName("ToString con ID")
        void testToString() {
            String s = temp.toString();
            assertTrue(s.contains("2025/2026"));
            assertTrue(s.contains("FUTURA"));
            assertTrue(s.contains(temp.getId()));
        }

        @Test
        @DisplayName("Setters funcionan")
        void testSetters() {
            temp.setNombre("Nueva Temp");
            assertEquals("Nueva Temp", temp.getNombre());
            
            temp.setId("CUSTOM_ID");
            assertEquals("CUSTOM_ID", temp.getId());
        }
    }

    @Nested
    @DisplayName("Tests Integración")
    class IntegracionTest {
        @Test
        @DisplayName("Flujo completo temporada-jornada-partido")
        void testFlujoCompleto() {
            Temporada t = new Temporada("Liga 2026", Temporada.FUTURA);
            
            for (int i = 1; i <= 6; i++) {
                t.inscribirEquipo(new Equipo("E" + i));
            }
            assertTrue(t.tieneEquiposSuficientes());
            
            Jornada j = new Jornada("J1");
            Partido p1 = new Partido(t.buscarEquipoPorNombre("E1"), t.buscarEquipoPorNombre("E2"));
            Partido p2 = new Partido(t.buscarEquipoPorNombre("E3"), t.buscarEquipoPorNombre("E4"));
            
            j.agregarPartido(p1);
            j.agregarPartido(p2);
            t.agregarJornada(j);
            
            assertEquals(1, t.getListaJornadas().size());
            assertEquals(2, j.getListaPartidos().size());
            
            p1.setGolesLocal(3);
            p1.setGolesVisitante(1);
            p1.setFinalizado(true);
            p2.setGolesLocal(2);
            p2.setGolesVisitante(2);
            p2.setFinalizado(true);
            
            assertEquals("E1", p1.obtenerGanador());
            assertEquals("Empate", p2.obtenerGanador());
            assertEquals(2, p1.getPuntosLocal());
            assertEquals(1, p2.getPuntosLocal());
        }

        @Test
        @DisplayName("Progresión estados temporada")
        void testProgresion() {
            Temporada t = new Temporada("T", Temporada.FUTURA);
            
            for (int i = 1; i <= 6; i++) {
                t.inscribirEquipo(new Equipo("E" + i));
            }
            
            t.setEstado(Temporada.EN_JUEGO);
            Equipo nuevo = new Equipo("Tarde");
            t.inscribirEquipo(nuevo);
            assertFalse(t.getEquiposParticipantes().contains(nuevo));
            
            t.setEstado(Temporada.TERMINADA);
            assertEquals(Temporada.TERMINADA, t.getEstado());
        }

        @Test
        @DisplayName("Jugador en dos equipos mismo torneo")
        void testJugadorDosEquipos() {
            Temporada t = new Temporada("Liga", Temporada.FUTURA);
            Equipo eq1 = new Equipo("E1");
            Equipo eq2 = new Equipo("E2");
            
            Jugador j = new Jugador("Messi", "Del", 36, "f.jpg");
            j.setDorsal(10);
            eq1.ficharJugador(j);
            
            t.inscribirEquipo(eq1);
            t.inscribirEquipo(eq2);
            
            assertTrue(t.jugadorYaInscrito(j));
        }
    }
}
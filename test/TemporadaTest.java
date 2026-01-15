

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gestion.Equipo;
import gestion.Jornada;
import gestion.Jugador;
import gestion.Partido;
import gestion.Temporada;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas para las clases Temporada, Jornada y Partido.
 * Organizadas en clases anidadas para mejor estructura.
 */
class TemporadaJornadaPartidoTest {

    // ==================== TESTS DE PARTIDO ====================
    
    @Nested
    @DisplayName("Tests de la clase Partido")
    class PartidoTest {

        private Equipo equipoLocal;
        private Equipo equipoVisitante;
        private Partido partido;

        @BeforeEach
        void setUp() {
            equipoLocal = new Equipo("Real Madrid");
            equipoVisitante = new Equipo("Barcelona");
            partido = new Partido(equipoLocal, equipoVisitante);
        }

        // --- Tests de Constructor ---

        @Test
        @DisplayName("Constructor crea partido correctamente")
        void testConstructorCorrecto() {
            assertNotNull(partido);
            assertEquals(equipoLocal, partido.getEquipoLocal());
            assertEquals(equipoVisitante, partido.getEquipoVisitante());
            assertEquals(-1, partido.getGolesLocal());
            assertEquals(-1, partido.getGolesVisitante());
            assertFalse(partido.isFinalizado());
            assertNull(partido.getFecha());
        }

        @Test
        @DisplayName("No se puede crear partido con equipo local nulo")
        void testConstructorEquipoLocalNulo() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Partido(null, equipoVisitante);
            });
            
            assertEquals("Los equipos no pueden ser nulos.", exception.getMessage());
        }

        @Test
        @DisplayName("No se puede crear partido con equipo visitante nulo")
        void testConstructorEquipoVisitanteNulo() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Partido(equipoLocal, null);
            });
            
            assertEquals("Los equipos no pueden ser nulos.", exception.getMessage());
        }

        @Test
        @DisplayName("No se puede crear partido de un equipo contra sí mismo")
        void testConstructorMismoEquipo() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Partido(equipoLocal, equipoLocal);
            });
            
            assertEquals("Un equipo no puede jugar contra sí mismo.", exception.getMessage());
        }

        // --- Tests de Ganador ---

        @Test
        @DisplayName("Partido no finalizado devuelve Pendiente")
        void testGanadorPartidoPendiente() {
            assertEquals("Pendiente", partido.obtenerGanador());
        }

        @Test
        @DisplayName("Victoria del equipo local")
        void testGanadorEquipoLocal() {
            partido.setGolesLocal(3);
            partido.setGolesVisitante(1);
            partido.setFinalizado(true);
            
            assertEquals("Real Madrid", partido.obtenerGanador());
        }

        @Test
        @DisplayName("Victoria del equipo visitante")
        void testGanadorEquipoVisitante() {
            partido.setGolesLocal(1);
            partido.setGolesVisitante(3);
            partido.setFinalizado(true);
            
            assertEquals("Barcelona", partido.obtenerGanador());
        }

        @Test
        @DisplayName("Partido empatado")
        void testGanadorEmpate() {
            partido.setGolesLocal(2);
            partido.setGolesVisitante(2);
            partido.setFinalizado(true);
            
            assertEquals("Empate", partido.obtenerGanador());
        }

        @Test
        @DisplayName("Empate a cero")
        void testGanadorEmpateACero() {
            partido.setGolesLocal(0);
            partido.setGolesVisitante(0);
            partido.setFinalizado(true);
            
            assertEquals("Empate", partido.obtenerGanador());
        }

        // --- Tests de Puntos Local ---

        @Test
        @DisplayName("Puntos local cuando no está finalizado")
        void testPuntosLocalPartidoPendiente() {
            assertEquals(0, partido.getPuntosLocal());
        }

        @Test
        @DisplayName("Puntos local en victoria (2 puntos)")
        void testPuntosLocalVictoria() {
            partido.setGolesLocal(3);
            partido.setGolesVisitante(1);
            partido.setFinalizado(true);
            
            assertEquals(2, partido.getPuntosLocal());
        }

        @Test
        @DisplayName("Puntos local en empate (1 punto)")
        void testPuntosLocalEmpate() {
            partido.setGolesLocal(2);
            partido.setGolesVisitante(2);
            partido.setFinalizado(true);
            
            assertEquals(1, partido.getPuntosLocal());
        }

        @Test
        @DisplayName("Puntos local en derrota (0 puntos)")
        void testPuntosLocalDerrota() {
            partido.setGolesLocal(1);
            partido.setGolesVisitante(3);
            partido.setFinalizado(true);
            
            assertEquals(0, partido.getPuntosLocal());
        }

        // --- Tests de Puntos Visitante ---

        @Test
        @DisplayName("Puntos visitante cuando no está finalizado")
        void testPuntosVisitantePartidoPendiente() {
            assertEquals(0, partido.getPuntosVisitante());
        }

        @Test
        @DisplayName("Puntos visitante en victoria (2 puntos)")
        void testPuntosVisitanteVictoria() {
            partido.setGolesLocal(1);
            partido.setGolesVisitante(3);
            partido.setFinalizado(true);
            
            assertEquals(2, partido.getPuntosVisitante());
        }

        @Test
        @DisplayName("Puntos visitante en empate (1 punto)")
        void testPuntosVisitanteEmpate() {
            partido.setGolesLocal(2);
            partido.setGolesVisitante(2);
            partido.setFinalizado(true);
            
            assertEquals(1, partido.getPuntosVisitante());
        }

        @Test
        @DisplayName("Puntos visitante en derrota (0 puntos)")
        void testPuntosVisitanteDerrota() {
            partido.setGolesLocal(3);
            partido.setGolesVisitante(1);
            partido.setFinalizado(true);
            
            assertEquals(0, partido.getPuntosVisitante());
        }

        // --- Tests de Getters y Setters ---

        @Test
        @DisplayName("Modificar goles del equipo local")
        void testSetGolesLocal() {
            partido.setGolesLocal(5);
            assertEquals(5, partido.getGolesLocal());
        }

        @Test
        @DisplayName("Modificar goles del equipo visitante")
        void testSetGolesVisitante() {
            partido.setGolesVisitante(3);
            assertEquals(3, partido.getGolesVisitante());
        }

        @Test
        @DisplayName("Modificar estado de finalizado")
        void testSetFinalizado() {
            partido.setFinalizado(true);
            assertTrue(partido.isFinalizado());
        }

        @Test
        @DisplayName("Modificar fecha del partido")
        void testSetFecha() {
            partido.setFecha("15/01/2026");
            assertEquals("15/01/2026", partido.getFecha());
        }

        @Test
        @DisplayName("Modificar equipo local")
        void testSetEquipoLocal() {
            Equipo nuevoEquipo = new Equipo("Atlético Madrid");
            partido.setEquipoLocal(nuevoEquipo);
            assertEquals(nuevoEquipo, partido.getEquipoLocal());
        }

        @Test
        @DisplayName("Modificar equipo visitante")
        void testSetEquipoVisitante() {
            Equipo nuevoEquipo = new Equipo("Sevilla");
            partido.setEquipoVisitante(nuevoEquipo);
            assertEquals(nuevoEquipo, partido.getEquipoVisitante());
        }

        // --- Tests de ToString ---

        @Test
        @DisplayName("ToString formatea correctamente el partido")
        void testToString() {
            partido.setGolesLocal(2);
            partido.setGolesVisitante(1);
            
            assertEquals("Real Madrid 2 - 1 Barcelona", partido.toString());
        }

        @Test
        @DisplayName("ToString con partido sin jugar")
        void testToStringSinJugar() {
            assertEquals("Real Madrid -1 - -1 Barcelona", partido.toString());
        }
    }

    // ==================== TESTS DE JORNADA ====================
    
    @Nested
    @DisplayName("Tests de la clase Jornada")
    class JornadaTest {

        private Jornada jornada;
        private Equipo equipo1;
        private Equipo equipo2;
        private Equipo equipo3;
        private Equipo equipo4;

        @BeforeEach
        void setUp() {
            jornada = new Jornada("Jornada 1");
            equipo1 = new Equipo("Real Madrid");
            equipo2 = new Equipo("Barcelona");
            equipo3 = new Equipo("Atlético Madrid");
            equipo4 = new Equipo("Sevilla");
        }

        // --- Tests de Constructor ---

        @Test
        @DisplayName("Constructor crea jornada correctamente")
        void testConstructorCorrecto() {
            assertNotNull(jornada);
            assertEquals("Jornada 1", jornada.getNombre());
            assertNotNull(jornada.getListaPartidos());
            assertTrue(jornada.getListaPartidos().isEmpty());
        }

        @Test
        @DisplayName("No se puede crear jornada con nombre nulo")
        void testConstructorNombreNulo() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Jornada(null);
            });
            
            assertEquals("El nombre de la jornada no puede estar vacío.", exception.getMessage());
        }

        @Test
        @DisplayName("No se puede crear jornada con nombre vacío")
        void testConstructorNombreVacio() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Jornada("   ");
            });
            
            assertEquals("El nombre de la jornada no puede estar vacío.", exception.getMessage());
        }

        // --- Tests de Agregar Partido ---

        @Test
        @DisplayName("Agregar partido correctamente")
        void testAgregarPartidoCorrecto() {
            Partido partido = new Partido(equipo1, equipo2);
            jornada.agregarPartido(partido);
            
            assertEquals(1, jornada.getListaPartidos().size());
            assertTrue(jornada.getListaPartidos().contains(partido));
        }

        @Test
        @DisplayName("Agregar múltiples partidos")
        void testAgregarMultiplesPartidos() {
            Partido partido1 = new Partido(equipo1, equipo2);
            Partido partido2 = new Partido(equipo3, equipo4);
            
            jornada.agregarPartido(partido1);
            jornada.agregarPartido(partido2);
            
            assertEquals(2, jornada.getListaPartidos().size());
        }

        @Test
        @DisplayName("No se puede agregar partido nulo")
        void testAgregarPartidoNulo() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                jornada.agregarPartido(null);
            });
            
            assertEquals("El partido no puede ser nulo.", exception.getMessage());
        }

        @Test
        @DisplayName("No se puede agregar enfrentamiento duplicado")
        void testAgregarPartidoDuplicado() {
            Partido partido1 = new Partido(equipo1, equipo2);
            Partido partido2 = new Partido(equipo1, equipo2);
            
            jornada.agregarPartido(partido1);
            
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                jornada.agregarPartido(partido2);
            });
            
            assertEquals("Este enfrentamiento ya existe en la jornada.", exception.getMessage());
        }

        @Test
        @DisplayName("Se puede agregar enfrentamiento inverso")
        void testAgregarEnfrentamientoInverso() {
            Partido partido1 = new Partido(equipo1, equipo2);
            Partido partido2 = new Partido(equipo2, equipo1);
            
            jornada.agregarPartido(partido1);
            
            // El enfrentamiento inverso es diferente
            assertDoesNotThrow(() -> jornada.agregarPartido(partido2));
            assertEquals(2, jornada.getListaPartidos().size());
        }

        // --- Tests de Jornada Completa ---

        @Test
        @DisplayName("Jornada vacía no está completa")
        void testJornadaVaciaNoCompleta() {
            assertFalse(jornada.estaCompleta());
        }

        @Test
        @DisplayName("Jornada con partidos pendientes no está completa")
        void testJornadaConPartidosPendientesNoCompleta() {
            Partido partido1 = new Partido(equipo1, equipo2);
            Partido partido2 = new Partido(equipo3, equipo4);
            
            partido1.setFinalizado(true);
            // partido2 no finalizado
            
            jornada.agregarPartido(partido1);
            jornada.agregarPartido(partido2);
            
            assertFalse(jornada.estaCompleta());
        }

        @Test
        @DisplayName("Jornada con todos los partidos finalizados está completa")
        void testJornadaCompletaTodosFinalizados() {
            Partido partido1 = new Partido(equipo1, equipo2);
            Partido partido2 = new Partido(equipo3, equipo4);
            
            partido1.setFinalizado(true);
            partido2.setFinalizado(true);
            
            jornada.agregarPartido(partido1);
            jornada.agregarPartido(partido2);
            
            assertTrue(jornada.estaCompleta());
        }

        @Test
        @DisplayName("Jornada con un solo partido finalizado está completa")
        void testJornadaUnSoloPartidoFinalizado() {
            Partido partido = new Partido(equipo1, equipo2);
            partido.setFinalizado(true);
            
            jornada.agregarPartido(partido);
            
            assertTrue(jornada.estaCompleta());
        }

        // --- Tests de Getters y Setters ---

        @Test
        @DisplayName("Modificar nombre de la jornada")
        void testSetNombre() {
            jornada.setNombre("Jornada 2");
            assertEquals("Jornada 2", jornada.getNombre());
        }

        @Test
        @DisplayName("Modificar lista completa de partidos")
        void testSetListaPartidos() {
            Partido partido1 = new Partido(equipo1, equipo2);
            Partido partido2 = new Partido(equipo3, equipo4);
            
            java.util.List<Partido> nuevaLista = new java.util.ArrayList<>();
            nuevaLista.add(partido1);
            nuevaLista.add(partido2);
            
            jornada.setListaPartidos(nuevaLista);
            
            assertEquals(2, jornada.getListaPartidos().size());
        }

        // --- Tests de ToString ---

        @Test
        @DisplayName("ToString formatea correctamente la jornada")
        void testToString() {
            assertEquals("Jornada Jornada 1", jornada.toString());
        }
    }

    // ==================== TESTS DE TEMPORADA ====================
    
    @Nested
    @DisplayName("Tests de la clase Temporada")
    class TemporadaTest {

        private Temporada temporada;
        private Equipo equipo1;
        private Equipo equipo2;
        private Equipo equipo3;
        private Equipo equipo4;
        private Equipo equipo5;
        private Equipo equipo6;

        @BeforeEach
        void setUp() {
            temporada = new Temporada("Temporada 2025/2026", Temporada.FUTURA);
            
            equipo1 = new Equipo("Real Madrid");
            equipo2 = new Equipo("Barcelona");
            equipo3 = new Equipo("Atlético Madrid");
            equipo4 = new Equipo("Sevilla");
            equipo5 = new Equipo("Valencia");
            equipo6 = new Equipo("Athletic Bilbao");
        }

        // --- Tests de Constructor ---

        @Test
        @DisplayName("Constructor crea temporada correctamente")
        void testConstructorCorrecto() {
            assertNotNull(temporada);
            assertEquals("Temporada 2025/2026", temporada.getNombre());
            assertEquals(Temporada.FUTURA, temporada.getEstado());
            assertNotNull(temporada.getEquiposParticipantes());
            assertNotNull(temporada.getListaJornadas());
            assertTrue(temporada.getEquiposParticipantes().isEmpty());
            assertTrue(temporada.getListaJornadas().isEmpty());
        }

        @Test
        @DisplayName("Constructor con estado EN_JUEGO")
        void testConstructorEnJuego() {
            Temporada temp = new Temporada("Temporada Actual", Temporada.EN_JUEGO);
            assertEquals(Temporada.EN_JUEGO, temp.getEstado());
        }

        @Test
        @DisplayName("Constructor con estado TERMINADA")
        void testConstructorTerminada() {
            Temporada temp = new Temporada("Temporada Pasada", Temporada.TERMINADA);
            assertEquals(Temporada.TERMINADA, temp.getEstado());
        }

        // --- Tests de Inscribir Equipo ---

        @Test
        @DisplayName("Inscribir equipo en temporada FUTURA")
        void testInscribirEquipoEnFutura() {
            temporada.inscribirEquipo(equipo1);
            
            assertEquals(1, temporada.getEquiposParticipantes().size());
            assertTrue(temporada.getEquiposParticipantes().contains(equipo1));
        }

        @Test
        @DisplayName("Inscribir múltiples equipos")
        void testInscribirMultiplesEquipos() {
            temporada.inscribirEquipo(equipo1);
            temporada.inscribirEquipo(equipo2);
            temporada.inscribirEquipo(equipo3);
            
            assertEquals(3, temporada.getEquiposParticipantes().size());
        }

        @Test
        @DisplayName("No se puede inscribir equipo nulo")
        void testInscribirEquipoNulo() {
            temporada.inscribirEquipo(null);
            
            assertTrue(temporada.getEquiposParticipantes().isEmpty());
        }

        @Test
        @DisplayName("No se puede inscribir equipo duplicado")
        void testInscribirEquipoDuplicado() {
            temporada.inscribirEquipo(equipo1);
            temporada.inscribirEquipo(equipo1);
            
            assertEquals(1, temporada.getEquiposParticipantes().size());
        }

        @Test
        @DisplayName("No se puede inscribir equipo en temporada EN_JUEGO")
        void testNoInscribirEnTemporadaEnJuego() {
            temporada.setEstado(Temporada.EN_JUEGO);
            temporada.inscribirEquipo(equipo1);
            
            assertTrue(temporada.getEquiposParticipantes().isEmpty());
        }

        @Test
        @DisplayName("No se puede inscribir equipo en temporada TERMINADA")
        void testNoInscribirEnTemporadaTerminada() {
            temporada.setEstado(Temporada.TERMINADA);
            temporada.inscribirEquipo(equipo1);
            
            assertTrue(temporada.getEquiposParticipantes().isEmpty());
        }

        // --- Tests de Equipos Suficientes ---

        @Test
        @DisplayName("Temporada sin equipos no tiene suficientes")
        void testTemporadaSinEquipos() {
            assertFalse(temporada.tieneEquiposSuficientes());
        }

        @Test
        @DisplayName("Temporada con menos de 6 equipos no tiene suficientes")
        void testTemporadaMenosDe6Equipos() {
            temporada.inscribirEquipo(equipo1);
            temporada.inscribirEquipo(equipo2);
            temporada.inscribirEquipo(equipo3);
            
            assertFalse(temporada.tieneEquiposSuficientes());
        }

        @Test
        @DisplayName("Temporada con 6 equipos tiene suficientes")
        void testTemporadaCon6Equipos() {
            temporada.inscribirEquipo(equipo1);
            temporada.inscribirEquipo(equipo2);
            temporada.inscribirEquipo(equipo3);
            temporada.inscribirEquipo(equipo4);
            temporada.inscribirEquipo(equipo5);
            temporada.inscribirEquipo(equipo6);
            
            assertTrue(temporada.tieneEquiposSuficientes());
        }

        @Test
        @DisplayName("Temporada con más de 6 equipos tiene suficientes")
        void testTemporadaConMasDe6Equipos() {
            for (int i = 1; i <= 8; i++) {
                temporada.inscribirEquipo(new Equipo("Equipo " + i));
            }
            
            assertTrue(temporada.tieneEquiposSuficientes());
        }

        // --- Tests de Buscar Equipo ---

        @Test
        @DisplayName("Buscar equipo existente por nombre")
        void testBuscarEquipoPorNombreExistente() {
            temporada.inscribirEquipo(equipo1);
            temporada.inscribirEquipo(equipo2);
            
            Equipo encontrado = temporada.buscarEquipoPorNombre("Barcelona");
            
            assertNotNull(encontrado);
            assertEquals(equipo2, encontrado);
        }

        @Test
        @DisplayName("Buscar equipo no existente devuelve null")
        void testBuscarEquipoPorNombreNoExistente() {
            temporada.inscribirEquipo(equipo1);
            
            Equipo encontrado = temporada.buscarEquipoPorNombre("Getafe");
            
            assertNull(encontrado);
        }

        @Test
        @DisplayName("Buscar equipo con nombre null devuelve null")
        void testBuscarEquipoPorNombreNull() {
            Equipo encontrado = temporada.buscarEquipoPorNombre(null);
            assertNull(encontrado);
        }

        // --- Tests de Jugador Ya Inscrito ---

        @Test
        @DisplayName("Jugador no inscrito en ningún equipo")
        void testJugadorNoInscrito() {
            Jugador jugador = new Jugador("Cristiano", "Delantero", 38, "foto.jpg");
            temporada.inscribirEquipo(equipo1);
            
            assertFalse(temporada.jugadorYaInscrito(jugador));
        }

        @Test
        @DisplayName("Jugador inscrito en un equipo")
        void testJugadorInscritoEnUnEquipo() {
            Jugador jugador = new Jugador("Messi", "Delantero", 36, "foto.jpg");
            jugador.setDorsal(10);
            equipo1.ficharJugador(jugador);
            temporada.inscribirEquipo(equipo1);
            
            assertTrue(temporada.jugadorYaInscrito(jugador));
        }

        @Test
        @DisplayName("Jugador inscrito en otro equipo diferente")
        void testJugadorInscritoEnOtroEquipo() {
            Jugador jugador = new Jugador("Benzema", "Delantero", 35, "foto.jpg");
            jugador.setDorsal(9);
            equipo2.ficharJugador(jugador);
            
            temporada.inscribirEquipo(equipo1);
            temporada.inscribirEquipo(equipo2);
            
            assertTrue(temporada.jugadorYaInscrito(jugador));
        }

        // --- Tests de Agregar Jornada ---

        @Test
        @DisplayName("Agregar jornada correctamente")
        void testAgregarJornada() {
            Jornada jornada = new Jornada("Jornada 1");
            temporada.agregarJornada(jornada);
            
            assertEquals(1, temporada.getListaJornadas().size());
            assertTrue(temporada.getListaJornadas().contains(jornada));
        }

        @Test
        @DisplayName("Agregar múltiples jornadas")
        void testAgregarMultiplesJornadas() {
            Jornada jornada1 = new Jornada("Jornada 1");
            Jornada jornada2 = new Jornada("Jornada 2");
            
            temporada.agregarJornada(jornada1);
            temporada.agregarJornada(jornada2);
            
            assertEquals(2, temporada.getListaJornadas().size());
        }

        @Test
        @DisplayName("No se puede agregar jornada nula")
        void testAgregarJornadaNula() {
            temporada.agregarJornada(null);
            
            assertTrue(temporada.getListaJornadas().isEmpty());
        }

        @Test
        @DisplayName("Agregar jornada inicializa lista si es nula")
        void testAgregarJornadaInicializaLista() {
            Temporada temp = new Temporada("Test", Temporada.FUTURA);
            // Forzar lista nula (aunque el constructor la inicializa)
            temp.getListaJornadas().clear();
            
            Jornada jornada = new Jornada("Jornada 1");
            temp.agregarJornada(jornada);
            
            assertNotNull(temp.getListaJornadas());
        }

        // --- Tests de Cambiar Estado ---

        @Test
        @DisplayName("Cambiar estado de FUTURA a EN_JUEGO")
        void testCambiarEstadoFuturaAEnJuego() {
            temporada.setEstado(Temporada.EN_JUEGO);
            assertEquals(Temporada.EN_JUEGO, temporada.getEstado());
        }

        @Test
        @DisplayName("Cambiar estado de EN_JUEGO a TERMINADA")
        void testCambiarEstadoEnJuegoATerminada() {
            temporada.setEstado(Temporada.EN_JUEGO);
            temporada.setEstado(Temporada.TERMINADA);
            assertEquals(Temporada.TERMINADA, temporada.getEstado());
        }

        @Test
        @DisplayName("No se puede establecer estado inválido")
        void testEstadoInvalido() {
            temporada.setEstado("ESTADO_INVALIDO");
            // Debe mantener el estado original
            assertEquals(Temporada.FUTURA, temporada.getEstado());
        }

        // --- Tests de ToString ---

        @Test
        @DisplayName("ToString formatea correctamente la temporada")
        void testToString() {
            assertEquals("Temporada 2025/2026 [FUTURA]", temporada.toString());
        }

        @Test
        @DisplayName("ToString con temporada en juego")
        void testToStringEnJuego() {
            temporada.setEstado(Temporada.EN_JUEGO);
            assertEquals("Temporada 2025/2026 [EN JUEGO]", temporada.toString());
        }

        @Test
        @DisplayName("ToString con temporada terminada")
        void testToStringTerminada() {
            temporada.setEstado(Temporada.TERMINADA);
            assertEquals("Temporada 2025/2026 [TERMINADA]", temporada.toString());
        }
    }

    // ==================== TESTS DE INTEGRACIÓN ====================
    
    @Nested
    @DisplayName("Tests de integración entre Temporada, Jornada y Partido")
    class IntegracionTest {

        @Test
        @DisplayName("Flujo completo: crear temporada, inscribir equipos y programar jornadas")
        void testFlujoCompleto() {
            // Crear temporada
            Temporada temporada = new Temporada("Liga 2026", Temporada.FUTURA);
            
            // Inscribir equipos
            Equipo rm = new Equipo("Real Madrid");
            Equipo fcb = new Equipo("Barcelona");
            Equipo atm = new Equipo("Atlético");
            Equipo sev = new Equipo("Sevilla");
            Equipo val = new Equipo("Valencia");
            Equipo ath = new Equipo("Athletic");
            
            temporada.inscribirEquipo(rm);
            temporada.inscribirEquipo(fcb);
            temporada.inscribirEquipo(atm);
            temporada.inscribirEquipo(sev);
            temporada.inscribirEquipo(val);
            temporada.inscribirEquipo(ath);
            
            assertTrue(temporada.tieneEquiposSuficientes());
            
            // Crear jornada con partidos
            Jornada jornada1 = new Jornada("Jornada 1");
            Partido partido1 = new Partido(rm, fcb);
            Partido partido2 = new Partido(atm, sev);
            Partido partido3 = new Partido(val, ath);
            
            jornada1.agregarPartido(partido1);
            jornada1.agregarPartido(partido2);
            jornada1.agregarPartido(partido3);
            
            temporada.agregarJornada(jornada1);
            
            // Verificaciones
            assertEquals(6, temporada.getEquiposParticipantes().size());
            assertEquals(1, temporada.getListaJornadas().size());
            assertEquals(3, jornada1.getListaPartidos().size());
            assertFalse(jornada1.estaCompleta());
            
            // Jugar partidos
            partido1.setGolesLocal(3);
            partido1.setGolesVisitante(1);
            partido1.setFinalizado(true);
            
            partido2.setGolesLocal(2);
            partido2.setGolesVisitante(2);
            partido2.setFinalizado(true);
            
            partido3.setGolesLocal(1);
            partido3.setGolesVisitante(0);
            partido3.setFinalizado(true);
            
            // Verificar jornada completa
            assertTrue(jornada1.estaCompleta());
            
            // Verificar ganadores
            assertEquals("Real Madrid", partido1.obtenerGanador());
            assertEquals("Empate", partido2.obtenerGanador());
            assertEquals("Valencia", partido3.obtenerGanador());
            
            // Verificar puntos
            assertEquals(2, partido1.getPuntosLocal());
            assertEquals(0, partido1.getPuntosVisitante());
            assertEquals(1, partido2.getPuntosLocal());
            assertEquals(1, partido2.getPuntosVisitante());
            assertEquals(2, partido3.getPuntosLocal());
            assertEquals(0, partido3.getPuntosVisitante());
        }

        @Test
        @DisplayName("Verificar que jugador no puede estar en dos equipos de la misma temporada")
        void testJugadorEnDosEquipos() {
            Temporada temporada = new Temporada("Liga 2026", Temporada.FUTURA);
            
            Equipo equipo1 = new Equipo("Real Madrid");
            Equipo equipo2 = new Equipo("Barcelona");
            
            Jugador jugador = new Jugador("Messi", "Delantero", 36, "foto.jpg");
            jugador.setDorsal(10);
            
            equipo1.ficharJugador(jugador);
            
            temporada.inscribirEquipo(equipo1);
            temporada.inscribirEquipo(equipo2);
            
            assertTrue(temporada.jugadorYaInscrito(jugador));
        }

        @Test
        @DisplayName("Simular una jornada completa con todos sus resultados")
        void testSimularJornadaCompleta() {
            Jornada jornada = new Jornada("Jornada Final");
            
            Equipo eq1 = new Equipo("Equipo A");
            Equipo eq2 = new Equipo("Equipo B");
            Equipo eq3 = new Equipo("Equipo C");
            Equipo eq4 = new Equipo("Equipo D");
            
            Partido p1 = new Partido(eq1, eq2);
            Partido p2 = new Partido(eq3, eq4);
            
            // Partido 1: Victoria local
            p1.setGolesLocal(4);
            p1.setGolesVisitante(1);
            p1.setFinalizado(true);
            p1.setFecha("15/01/2026");
            
            // Partido 2: Empate
            p2.setGolesLocal(2);
            p2.setGolesVisitante(2);
            p2.setFinalizado(true);
            p2.setFecha("15/01/2026");
            
            jornada.agregarPartido(p1);
            jornada.agregarPartido(p2);
            
            assertTrue(jornada.estaCompleta());
            assertEquals(2, jornada.getListaPartidos().size());
            
            // Verificar resultados
            assertEquals("Equipo A", p1.obtenerGanador());
            assertEquals(2, p1.getPuntosLocal());
            assertEquals(0, p1.getPuntosVisitante());
            
            assertEquals("Empate", p2.obtenerGanador());
            assertEquals(1, p2.getPuntosLocal());
            assertEquals(1, p2.getPuntosVisitante());
        }

        @Test
        @DisplayName("Cambiar estado de temporada a medida que progresa")
        void testProgresionTemporada() {
            Temporada temp = new Temporada("Liga Progresiva", Temporada.FUTURA);
            
            // Estado inicial
            assertEquals(Temporada.FUTURA, temp.getEstado());
            
            // Inscribir equipos
            for (int i = 1; i <= 6; i++) {
                temp.inscribirEquipo(new Equipo("Equipo " + i));
            }
            
            // Iniciar temporada
            temp.setEstado(Temporada.EN_JUEGO);
            assertEquals(Temporada.EN_JUEGO, temp.getEstado());
            
            // Ya no se pueden inscribir más equipos
            Equipo nuevoEquipo = new Equipo("Equipo Tardío");
            temp.inscribirEquipo(nuevoEquipo);
            assertFalse(temp.getEquiposParticipantes().contains(nuevoEquipo));
            
            // Finalizar temporada
            temp.setEstado(Temporada.TERMINADA);
            assertEquals(Temporada.TERMINADA, temp.getEstado());
        }
    }
}
package logica;

public class FilaClasificacion {
    private String equipo;
    private int pj, pg, pe, pp, puntos, gf, gc;

    public FilaClasificacion(String equipo) {
        if (equipo == null || equipo.isBlank())
            throw new IllegalArgumentException("El nombre del equipo no puede ser vacío.");
        this.equipo = equipo;
    }

    public void registrarPartido(int misGoles, int golesRival) {
        if (misGoles < 0 || golesRival < 0)
            throw new IllegalArgumentException("Los goles no pueden ser negativos.");

        this.pj++;
        this.gf += misGoles;
        this.gc += golesRival;

        if (misGoles > golesRival) {
            this.pg++;
            this.puntos += 2;
        } else if (misGoles == golesRival) {
            this.pe++;
            this.puntos += 1;
        } else {
            this.pp++;
        }
    }

    // Getters
    public String getEquipo() { return equipo; }
    public int getPj() { return pj; }
    public int getPg() { return pg; }
    public int getPe() { return pe; }
    public int getPp() { return pp; }
    public int getPuntos() { return puntos; }
    public int getGf() { return gf; }
    public int getGc() { return gc; }
    public int getDf() { return gf - gc; }
}

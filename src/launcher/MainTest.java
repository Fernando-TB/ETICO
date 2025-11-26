package launcher;

import modelo.TXTtoCalendario;

public class MainTest {

    public static void main(String[] args) {
        TXTtoCalendario cep = new TXTtoCalendario();

        cep.lineToEvento("Reunion Equipo | 2025-11-25T21:10:00.000-03:00 | 2025-11-25T22:10:00.000-03:00");

    }

}

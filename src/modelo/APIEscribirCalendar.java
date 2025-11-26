package modelo;

public class APIEscribirCalendar {
    public void crearEvento(String userEmail, String titulo, String horario) {
        System.out.println("Simulación: Creando evento en el calendario de " + userEmail);
        System.out.println("Detalles: " + titulo + " a las " + horario);
    }
}
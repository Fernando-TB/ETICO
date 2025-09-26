import java.util.List;

public class Main {

    public static void main(String[] args) {

        ManejadorConsola manejadorConsola = new ManejadorConsola();

        APILeerCalendar apiLeerCalendar = new APILeerCalendar();
        APIGemini apiGemini = new APIGemini();
        APIEscribirCalendar apiEscribirCalendar = new APIEscribirCalendar();

        Logica coordinadorDeLogica = new Logica(apiLeerCalendar, apiGemini, apiEscribirCalendar, manejadorConsola);


        //Definimos los datos de la reunion
        List<String> participantes = List.of("jefe@ejemplo.com", "trabajador1@ejemplo.com", "trabajador2@ejemplo.com");
        String tituloReunion = "Reunion de Sincronizacion del Proyecto";
        int duracionMinutos = 30;



        coordinadorDeLogica.agendarReunion(participantes, tituloReunion, duracionMinutos);

    }


}

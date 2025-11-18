package modelo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class APIConsultarDisponibilidad {

    /**
     * Consulta las franjas de tiempo ocupado de los calendarios dados en la próxima semana.
     */
    public FreeBusyResponse consultarDisponibilidad(Calendar service, List<String> workerEmails) throws IOException {
        System.out.println("\n[DISPONIBILIDAD] Consultando horarios libres para los trabajadores...");

        // Definir rango de tiempo (ej. Próxima semana)
        long currentTimeMillis = System.currentTimeMillis();
        long oneWeekLaterMillis = currentTimeMillis + (7 * 24 * 60 * 60 * 1000);

        DateTime timeMin = new DateTime(currentTimeMillis);
        DateTime timeMax = new DateTime(oneWeekLaterMillis);

        // Crear la lista de calendarios a consultar
        List<FreeBusyRequestItem> items = workerEmails.stream()
                .map(email -> new FreeBusyRequestItem().setId(email))
                .collect(Collectors.toList());

        // Crear el cuerpo de la solicitud
        FreeBusyRequest requestBody = new FreeBusyRequest()
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setItems(items);

        // Ejecutar la consulta Free/Busy
        FreeBusyResponse response = service.freebusy().query(requestBody).execute();

        System.out.println("[DISPONIBILIDAD] Consulta a la API finalizada.");

        return response;
    }
}
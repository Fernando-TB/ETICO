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


    public FreeBusyResponse consultarDisponibilidad(Calendar service, List<String> workerEmails) throws IOException {
        System.out.println("\nConsultando horarios libres para los trabajadores...");

        long currentTimeMillis = System.currentTimeMillis();
        long oneWeekLaterMillis = currentTimeMillis + (7 * 24 * 60 * 60 * 1000);

        DateTime timeMin = new DateTime(currentTimeMillis);
        DateTime timeMax = new DateTime(oneWeekLaterMillis);

        List<FreeBusyRequestItem> items = workerEmails.stream()
                .map(email -> new FreeBusyRequestItem().setId(email))
                .collect(Collectors.toList());

        FreeBusyRequest requestBody = new FreeBusyRequest()
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setItems(items);

        FreeBusyResponse response = service.freebusy().query(requestBody).execute();

        System.out.println("Consulta a la API finalizada.");

        return response;
    }
}
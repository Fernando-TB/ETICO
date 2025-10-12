import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class APILeerCalendar {

    private static final String APPLICATION_NAME = "Extractor a TXT (Solo Lectura)";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

/// hola///
    static {
        Logger.getLogger("com.google").setLevel(Level.SEVERE);
    }

    private static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = APILeerCalendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("¡Error! Archivo de credenciales no encontrado: " + CREDENTIALS_FILE_PATH +
                    ". Asegúrate de que esté en src/main/resources.");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        List<String> scopes = Collections.singletonList("https://www.googleapis.com/auth/calendar.events");

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public static Calendar getCalendarService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void readAndWriteEvents() {
        try {
            Calendar service = getCalendarService();

            java.util.Calendar calendar = java.util.Calendar.getInstance(TimeZone.getDefault());
            calendar.setFirstDayOfWeek(java.util.Calendar.MONDAY);

            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.clear(java.util.Calendar.MINUTE);
            calendar.clear(java.util.Calendar.SECOND);
            calendar.clear(java.util.Calendar.MILLISECOND);
            calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY);
            DateTime timeMin = new DateTime(calendar.getTime());

            calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SUNDAY);
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
            calendar.set(java.util.Calendar.MINUTE, 59);
            calendar.set(java.util.Calendar.SECOND, 59);
            DateTime timeMax = new DateTime(calendar.getTime());
            // ----------------------------------------------------------------------

            System.out.printf("Rango de Extracción: Desde %s hasta %s\n",
                    timeMin.toStringRfc3339().split("T")[0],
                    timeMax.toStringRfc3339().split("T")[0]);

            Events events = service.events().list("primary")
                    .setMaxResults(100)
                    .setTimeMin(timeMin)
                    .setTimeMax(timeMax)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            if (items == null || items.isEmpty()) {
                System.out.println("No se encontraron eventos en este rango.");
                return;
            }

            System.out.println("Eventos encontrados: " + items.size());

            String fileName = "eventos_calendar.txt";
            try (PrintWriter writer = new PrintWriter(fileName, "UTF-8")) {

                for (Event event : items) {
                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }

                    writer.println("\n--------------------------------------------------");
                    writer.printf("Título:      %s\n", event.getSummary());
                    writer.printf("Fecha/Hora:  %s\n", start.toStringRfc3339());
                    writer.printf("Ubicación:   %s\n",
                            event.getLocation() != null ? event.getLocation() : "N/A");
                    writer.printf("ID Evento:   %s\n", event.getId());
                }

                System.out.println("\n ¡Éxito! Eventos guardados en: " + new File(fileName).getAbsolutePath());

            } catch (IOException e) {
                System.err.println(" Error al escribir en el archivo TXT: " + e.getMessage());
            }

        } catch (FileNotFoundException e) {
            System.err.println("\n ERROR de Credenciales (FileNotFound):");
            System.err.println("  " + e.getMessage());
            System.err.println("  Asegúrate de que 'credentials.json' esté en src/main/resources.");
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("\n ERROR de Conexión/Autenticación:");
            System.err.println("  Asegúrate de haber habilitado la API de Calendar en Google Cloud.");
            System.err.println("  Si es la primera vez, borra la carpeta 'tokens' y vuelve a ejecutar para autorizar.");
            System.err.println("  Detalles: " + e.getMessage());
        }
    }
}

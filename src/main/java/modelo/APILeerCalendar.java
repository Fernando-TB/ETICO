package Modelo; // Paquete simple

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Gestiona la conexión y lectura de eventos de Google Calendar.
 * NOTA: Esta clase ahora SÓLO se encarga de la lectura.
 */
public class APILeerCalendar {

    private static final String APPLICATION_NAME = "ETICO - Google Calendar Connector";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // Directorio para almacenar credenciales de usuario (tokens)
    private static final java.io.File DATA_STORE_DIR = new java.io.File("tokens");

    private HttpTransport httpTransport;
    private Calendar client;

    public APILeerCalendar() throws IOException, GeneralSecurityException {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // Carga y autoriza las credenciales
        Credential credential = authorize();

        // Inicializa el cliente de la API de Google Calendar
        this.client = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        System.out.println("Conexión con Google Calendar establecida correctamente.");
    }

    /**
     * Autoriza al usuario a acceder a Google Calendar.
     * @return Credential objeto de credencial autorizado.
     * @throws IOException si hay problemas leyendo el archivo credentials.json.
     * @throws GeneralSecurityException si hay problemas de seguridad con el transporte HTTP.
     */
    private Credential authorize() throws IOException, GeneralSecurityException {
        // Carga client secrets desde credentials.json (asume que está en src/main/resources)
        InputStream in = APILeerCalendar.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new IOException("Archivo de credenciales 'credentials.json' no encontrado en src/main/resources.");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Define el flujo de autorización (solo lectura)
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, Collections.singletonList(CalendarScopes.CALENDAR_READONLY))
                .setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR))
                .setAccessType("offline")
                .build();

        // Realiza la autorización a través del navegador
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Obtiene una lista de los próximos 10 eventos del calendario principal.
     * @return Una lista de objetos Event.
     * @throws IOException si la llamada a la API falla.
     */
    public List<Event> obtenerProximosEventos() throws IOException {
        // Obtiene la hora actual
        DateTime now = new DateTime(System.currentTimeMillis());

        // Llama a la API para obtener los eventos
        Events events = client.events().list("primary")
                .setMaxResults(10) // Limita a los 10 próximos eventos
                .setTimeMin(now) // Eventos que comienzan a partir de ahora
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        return events.getItems();
    }
}

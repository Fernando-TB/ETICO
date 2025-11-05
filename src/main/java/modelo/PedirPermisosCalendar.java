package modelo;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;


public class PedirPermisosCalendar {

    private static final String APPLICATION_NAME = "ETICO - Google Calendar Connector";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    // La carpeta base es 'tokens'. Se agregará un subdirectorio por cada usuario.
    private static final java.io.File BASE_DATA_STORE_DIR = new java.io.File("tokens");

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);


    /**
     * Obtiene el servicio de Calendar para un usuario específico.
     * @param userId El identificador único del usuario (usado para almacenar el token).
     */
    public static Calendar getCalendarService(String userId) throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        System.out.println("--- Autenticando usuario: " + userId + " ---");

        // Autoriza al usuario, guardando su token en una carpeta con su nombre.
        Credential credential = authorize(httpTransport, userId);

        System.out.println("Autenticación exitosa para " + userId + ". Construyendo cliente de la API.");

        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    /**
     * Realiza el proceso de autorización de OAuth para un usuario,
     * almacenando su token en un directorio específico.
     */
    private static Credential authorize(HttpTransport httpTransport, String userId) throws IOException {
        InputStream in = PedirPermisosCalendar.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new IOException("Archivo de credenciales 'credentials.json' no encontrado en src/main/resources.");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Crea una carpeta de almacenamiento de tokens única para cada usuario
        File userDataStoreDir = new File(BASE_DATA_STORE_DIR, userId);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(userDataStoreDir)) // Usa el directorio específico
                .setAccessType("offline")
                .build();

        // Usa un 'user' genérico como clave, ya que el almacenamiento ya está segmentado por la carpeta del userId.
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
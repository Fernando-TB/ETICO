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

    private static final String APPLICATION_NAME = "ETICO";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final java.io.File BASE_DATA_STORE_DIR = new java.io.File("tokens");

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);



    public static Calendar getCalendarService(String userId) throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        System.out.println("Autenticando usuario: " + userId + " ---");

        Credential credential = authorize(httpTransport, userId);

        System.out.println("Autenticación exitosa para " + userId + ". Construyendo cliente de la API.");

        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    private static Credential authorize(HttpTransport httpTransport, String userId) throws IOException {
        InputStream in = PedirPermisosCalendar.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new IOException("Archivo de credenciales 'credentials.json' no encontrado en src/main/resources.");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        File userDataStoreDir = new File(BASE_DATA_STORE_DIR, userId);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES) // SCOPES ACTUALIZADOS AQUÍ
                .setDataStoreFactory(new FileDataStoreFactory(userDataStoreDir))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
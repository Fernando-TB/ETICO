package com.etico;

import com.google.api.services.calendar.model.Event;
// Importamos las clases directamente, ya que están en el mismo paquete.
import APILeerCalendar;
import ConversorCSV;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class Main {
    /**
     * Punto de entrada principal para el programa.
     * Actúa como el orquestador que:
     * 1. Lee los eventos de Google Calendar (usando APILeerCalendar).
     * 2. Convierte y guarda los eventos en un archivo .txt (usando ConversorCSV).
     */
    public static void main(String[] args) {
        System.out.println("--- INICIO DE ORQUESTACIÓN ETICO ---");

        // 1. Inicializar la ruta del archivo de salida
        String nombreArchivoSalida = "eventos_calendar.txt";

        try {
            // 2. Obtener la instancia de la API y leer los eventos del calendario
            APILeerCalendar apiCalendar = new APILeerCalendar();
            List<Event> eventos = apiCalendar.obtenerProximosEventos();

            if (eventos.isEmpty()) {
                System.out.println("No se encontraron eventos próximos para exportar.");
                return;
            }

            // 3. Inicializar el conversor y escribir los datos en el archivo
            ConversorCSV conversor = new ConversorCSV();
            conversor.escribirEventosAArchivo(eventos, nombreArchivoSalida);

            System.out.println("\n✅ Éxito: Se han exportado " + eventos.size() +
                    " eventos al archivo: " + nombreArchivoSalida);

        } catch (IOException e) {
            System.err.println("Error de I/O (lectura o escritura): " + e.getMessage());
        } catch (GeneralSecurityException e) {
            System.err.println("Error de seguridad o autorización de la API: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado: " + e.getMessage());
        } finally {
            System.out.println("\n--- FIN DE ORQUESTACIÓN ETICO ---");
        }
    }
}

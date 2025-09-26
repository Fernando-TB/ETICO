package org.example;

import apis.APIGemini;

public class Main {
    public static void main(String[] args) {
        String apiKey = "AIzaSyANXSQSgr86OdGMV4c1H7P1GkUKSllC910";

        APIGemini gemini = new APIGemini(apiKey);

        String schedulesCSV = "Trabajador,Inicio,Fin\n" +
                "Juan,09:00,11:00\n" +
                "Ana,10:00,12:00\n" +
                "Pedro,15:00,17:00\n";

        String prompt = "Encuentra un bloque de tiempo libre de al menos 1 hora en el que todos estén disponibles.";

        try {
            String response = gemini.getOptimalSchedule(schedulesCSV, prompt);
            System.out.println("Respuesta de Gemini:");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

import com.google.cloud.gemini.v1beta.GeminiClient;
import com.google.cloud.gemini.v1beta.GenerateContentResponse;
import com.google.cloud.gemini.v1beta.Content;
import com.google.cloud.gemini.v1beta.Part;
import java.io.IOException;

public class APIGemini {

    private final String apiKey;
    private GeminiClient client;

    public APIGemini(String apiKey) {
        this.apiKey = "AIzaSyCbKVka07ge-NtmX9hg2ETRVgi1z5JpMq8";
    }

    public String getOptimalTime(String fullPrompt) {
        try {

            String simulatedResponse = "startTime,endTime\n2025-10-01T14:00:00,2025-10-01T15:00:00\n";
            return simulatedResponse;
        } catch (Exception e) {
            System.err.println("Error al comunicarse con la API de Gemini: " + e.getMessage());
            return null;
        }
    }
}
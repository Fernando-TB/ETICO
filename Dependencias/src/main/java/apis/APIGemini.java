package apis;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class APIGemini {
    private final String apiKey;
    private final OkHttpClient client;

    public APIGemini(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
    }

    public String getOptimalSchedule(String schedulesCSV, String prompt) throws IOException {
        JSONObject promptObj = new JSONObject();
        promptObj.put("text", prompt + "\n\n" + schedulesCSV);

        JSONObject body = new JSONObject();
        body.put("model", "text-bison-001");
        body.put("prompt", promptObj);
        body.put("temperature", 0.0);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/text-bison-001:generateText?key=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en Gemini: " + response);
            }
            return response.body().string();
        }
    }
}

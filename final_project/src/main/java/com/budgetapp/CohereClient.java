package com.budgetapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

// Client class to interact with Cohere's text generation API
public class CohereClient {

    private final String apiKey;

    // Contructor retrieves API key from environment variables
    // Throws exception if key is not set
    public CohereClient() {
        this.apiKey = System.getenv("COHERE_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("COHERE_API_KEY not set in environment variables");
        }
    }

    // Sends a prompt to Cohere's generate endpoint and retrieves AI generated advice
    public String generateAdvice(String prompt) {
        try {
            // Set up HTTP connection to Cohere's generate endpoint
            URL url = new URL("https://api.cohere.ai/v1/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Configure request headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Prepare request body with model parameters
            String body = new JSONObject()
                .put("model", "command-xlarge")
                .put("prompt", prompt)
                .put("max_tokens", 400)
                .put("temperature", 0.7)
                .toString();

            // Send request body
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Handle successful response
            if (conn.getResponseCode() == 200) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }

                // Parse JSON and extract generated text
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray generations = jsonResponse.getJSONArray("generations");
                String text = generations.getJSONObject(0).getString("text");
                return text;

            } else {
                // Handle error response from Cohere API
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    errorResponse.append(line);
                }
                return "Error from Cohere: " + errorResponse.toString();
            }

        } catch (Exception e) {
            // Catch-all for unexpected failures
            e.printStackTrace();
            return "Failed to generate advice.";
        }
    }
}
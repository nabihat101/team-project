package use_case.professor_pages;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles retrieving information from professors through API
 */
public class ProfessDataObject implements ProfessorDataAccessInterface{
    private final OkHttpClient client;
    private final String apiKey;

    /**
     * Inititalizes fields by creating a new client and using method defined in function for key
     */
    public ProfessDataObject() {
        this.client = new OkHttpClient();
        this.apiKey = loadAPIKey();
    }

    //Source for API Keys: https://vonagedev.medium.com/working-with-environment-variables-in-java-26b11f1344a5

    /**
     * Retrieves API key from file and returns it as a string to be used in calls
     * @return String that is key
     */
    private String loadAPIKey() {
        Properties properties = new Properties();
        Path envFile = Paths.get("api_key.properties");

        try {
            properties.load(Files.newInputStream(envFile));
        } catch (IOException e) {
            throw new RuntimeException("Could not load API key", e);
        }

        return properties.getProperty("OPENALEX_API_KEY");
    }

    /**
     * Method that handles making a request to api
     * @param url - the url we want to request
     * @return response from api
     */
    private String makeAPIRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code())
            }

            return response.body().string();
        }
    }

    private JSONObject getProfessorJSON (String profID) {
        String url = "https://api.openalex.org/authors/" + profID + "?api_key=" + apiKey;

        String response = makeAPIRequest(url);return new JSONObject(response);


}

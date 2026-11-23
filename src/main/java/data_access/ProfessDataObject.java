package data_access;
import java.io.IOException;

import entities.Professor;
import entities.ResearchPaper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.professor_pages.ProfessorDataAccessInterface;
import use_case.RankedProfessorCase.RankedProfessorDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles retrieving information from professors through API
 */
public class ProfessDataObject implements ProfessorDataAccessInterface, RankedProfessorDataAccessInterface {
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
    private String makeAPIRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            return response.body().string();
        }
    }

    /**
     * Creates the JSON object for the professor that's to be passed on to another method
     * @param profID - Professor ID given my openAlex api
     * @return JSON object of the professor
     */
    private JSONObject getProfessorJSON (String profID) throws IOException {
        String url = "https://api.openalex.org/authors/" + profID + "?api_key=" + apiKey;

        String response = makeAPIRequest(url);
        return new JSONObject(response);
    }

    /**
     * Creates entity based on id
     * @param id - ID of professor
     * @return Professor - The professor object
     */

    public Professor getProfessorById(String id) {
        JSONObject professorJSON;

        try {
            professorJSON = getProfessorJSON(id);
        } catch (IOException e) {
            throw new RuntimeException("Could not retrieve professor", e);
        }

        String name = professorJSON.getString("display_name");

        String institution = "";
        JSONArray institutions = professorJSON.optJSONArray("last_known_institutions");

        if (institutions != null && institutions.length() > 0) {
            institution = institutions.getJSONObject(0).getString("display_name");
        }

        List<String> researchAreas = new ArrayList<>();
        JSONArray topics = professorJSON.optJSONArray("topics");

        if (topics != null) {
            for (int i = 0; i < topics.length(); i++) {
                String topic = topics.getJSONObject(i).getString("display_name");
                researchAreas.add(topic);
            }
        }

        List<ResearchPaper> publications = getPublications(id);

        return new Professor(id, name, institution, researchAreas, publications);
    }

    /**
     * Gets all publications (first 100) publications of a author
     * @param id - professor id
     * @return publications - list of ResearchPaper
     */

    public List<ResearchPaper> getPublications(String id) {
        String url = "https://api.openalex.org/works?filter=author.id:"
                + id + "&per_page=100&api_key=" + apiKey;

        String responseBody;

        try {
            responseBody = makeAPIRequest(url);
        } catch (IOException e) {
            throw new RuntimeException("Could not retrieve publications", e);
        }

        JSONObject responseJSON = new JSONObject(responseBody);
        JSONArray results = responseJSON.getJSONArray("results");

        List<ResearchPaper> publications = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject paperJSON = results.getJSONObject(i);

            String paperId = paperJSON.getString("id");
            String title = paperJSON.optString("title", "");
            int year = paperJSON.optInt("publication_year", 0);

            List<String> authors = new ArrayList<>();
            JSONArray authorships = paperJSON.optJSONArray("authorships");

            if (authorships != null) {
                for (int j = 0; j < authorships.length(); j++) {
                    JSONObject authorship = authorships.getJSONObject(j);
                    String authorName = authorship.optString("raw_author_name", "");

                    authors.add(authorName);
                }
            }

            // Get paper URL
            String paperURL = "";

            JSONObject bestOaLocation = paperJSON.optJSONObject("best_oa_location");
            if (bestOaLocation != null) {
                paperURL = bestOaLocation.optString("pdf_url", "");

                if (paperURL.isEmpty()) {
                    paperURL = bestOaLocation.optString("landing_page_url", "");
                }
            }

            if (paperURL.isEmpty()) {
                JSONObject primaryLocation = paperJSON.optJSONObject("primary_location");
                if (primaryLocation != null) {
                    paperURL = primaryLocation.optString("landing_page_url", "");
                }
            }

            if (paperURL.isEmpty()) {
                paperURL = paperJSON.optString("doi", "");
            }

            ResearchPaper paper = new ResearchPaper(paperId, title, authors, year, "", paperURL);

            publications.add(paper);
        }
        return publications;
    }




    public List<Professor> findByAreasAndLocations(List<String> areas, List<String> locations){
        return List.of();
    }

}

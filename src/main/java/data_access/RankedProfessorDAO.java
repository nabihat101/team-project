package data_access;

import entities.Professor;
import entities.ResearchPaper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import use_case.FilterOptionsDataAccessInterface;
import use_case.RankedProfessorCase.RankedProfessorDataAccessInterface;

import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Handles retrieving information from professors through API
 */
public class RankedProfessorDAO implements
        RankedProfessorDataAccessInterface, FilterOptionsDataAccessInterface {
    private final OkHttpClient client;
    private final String apiKey;

    // FOr ranked prof + filter options:
    private static final String BASE_URL = "https://api.openalex.org";
    private static final int COMPUTER_SCIENCE_FIELD_ID = 17;
    private static final int MAX_INSTITUTIONS = 50;

    // Display name -> OpenAlex id, filled the first time the options are needed.
    // Linked hashmap to preserve order
    private final Map<String, String> subfieldIds = new LinkedHashMap<>();
    private final Map<String, String> institutionIds = new LinkedHashMap<>();

    /**
     * Inititalizes fields by creating a new client and using method defined in function for key
     */
    public RankedProfessorDAO() {
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


    // FOR FILTER DATA OPTIONS, USED IN BOTH RANKED PROFESSOR USE CASE & SIGNUP USE CASE
    /**
     * Fetches a list of all topics available in API.
     * @see loadOptionsIfNeeded for helper. Loads and saves all options first time so program
     * doesn't have to repeat it every time.
     * @return list of all topics
     */
    @Override
    public List<String> getTopicOptions() {
        loadOptionsIfNeeded();
        return new ArrayList<>(subfieldIds.keySet());
    }


    /**
     * Fetches a list of all institutions available in API (only Canada).
     * @see loadOptionsIfNeeded for helper. Loads and saves all options first time so program
     * doesn't have to repeat it every time.
     * @return list of all institutions in Canada.
     */
    @Override
    public List<String> getLocationOptions() {
        loadOptionsIfNeeded();
        return new ArrayList<>(institutionIds.keySet());
    }

    /**
     * Loads ids and display names from API, depending on subfield or institution. Gets called by implementations of
     * FilterOptionsDA Interface. Uses two helpers (in order to follow SRP from SOLID):
     * @see fillMap, which populates the respective instance attribute with info from API
     * @see getResults, which makes the request given the API
     */
    private void loadOptionsIfNeeded() {
        // https://openalex.org/fields/17
        if (subfieldIds.isEmpty()) {
            String url = BASE_URL + "/subfields?filter=field.id:" + COMPUTER_SCIENCE_FIELD_ID
                    + "&per_page=200&select=id,display_name&api_key=" + apiKey;
            fillMap(subfieldIds, getResults(url));
        }
        // https://help.openalex.org/data/institutions/
        if (institutionIds.isEmpty()) {
            String url = BASE_URL + "/institutions?filter=country_code:CA,type:education"
                    + "&sort=cited_by_count:desc&per_page=" + MAX_INSTITUTIONS
                    + "&select=id,display_name&api_key=" + apiKey;
            fillMap(institutionIds, getResults(url));
        }
    }

    /** Populates the linked hashmap with JSON info from API: (display name, id).
     * For id, the API gives it in the format of {"id": "openalex.org/subfields/id"}, so the helper calls
     * @see lastPart to only keep id.
     * @param map, the linked hashmap to populate
     * @param results, the JSONArray to parse from
     * */
    private void fillMap(Map<String, String> map, JSONArray results) {
        for (int i = 0; i < results.length(); i++) {
            JSONObject item = results.getJSONObject(i);
            String name = item.optString("display_name", "");
            if (!name.isEmpty()) {
                map.put(name, lastPart(item.optString("id", "")));
            }
        }
    }

    /** Makes a request on API given and returns the "results" array of the response. Helper for
     * @see loadOptionsIfNeeded
     * @param url to make request to
     *  */
    private JSONArray getResults(String url) {
        try {
            return new JSONObject(makeAPIRequest(url)).getJSONArray("results");
        } catch (IOException e) {
            throw new RuntimeException("OpenAlex request failed", e);
        }
    }

    /** Only keeps last part of a url. Used by
     * @see fillMap to only keep id from id url: eg. "https://openalex.org/A123" to "A123".
     * @param url, to take id out of
     * @return the id from url. */
    private String lastPart(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }


    // FOR RANKED PROFESSOR USE CASE

    /** Finds professors that match the categories provided (see param).
     * Gets URL ids from the {@link subfieldIds} and {@link institutionIds} populated by
     * @see loadOptionsIfNeeded.
     * Uses ids to make new URL for author filtering, then uses
     * {@code getResults} to get info from URL, and {@code professorFromJson} to parse information
     * from URL.
     *
     * @param areas, the subfields to match with professors
     * @param locations, the institutions to match with professors
     * @return list of professors that match
     */
    @Override
    public List<Professor> findByAreasAndLocations(List<String> areas, List<String> locations) {
        loadOptionsIfNeeded();
        final List<Professor> professors = new ArrayList<>();

        // Turn the selected subfield names into ids, then make a subfield api link
        List<String> subfieldFilter = new ArrayList<>();
        for (String area : areas) {
            String id = subfieldIds.get(area);
            if (id != null) {
                subfieldFilter.add(id);
            }
        }
        if (subfieldFilter.isEmpty()) {
            return professors;
        }
        // Make an API string for subfields, 'or'ing all subfields together. To be used for prof filter
        String filter = "topics.subfield.id:" + String.join("|", subfieldFilter);


        // Turn the selected locations names into ids, then make a subfield API link
        if (locations.isEmpty()) {
            filter += ",last_known_institutions.country_code:CA";
        } else {
            // Turn selected institutions into ids
            List<String> institutionFilter = new ArrayList<>();
            for (String location : locations) {
                String id = institutionIds.get(location);
                if (id != null) {
                    institutionFilter.add(id);
                }
            }
            if (institutionFilter.isEmpty()) {
                return professors;
            }
            // Make an API string for institutions, 'or'ing all subfields together. To be used for prof filter
            filter += ",last_known_institutions.id:" + String.join("|", institutionFilter);
        }

        // Make the full URL
        String url = BASE_URL + "/authors?filter=" + filter
                + "&sort=cited_by_count:desc&per_page=100"
                + "&select=id,display_name,last_known_institutions,topics"
                + "&api_key=" + apiKey;

        // Add professors that match into the
        JSONArray results = getResults(url);
        for (int i = 0; i < results.length(); i++) {
            professors.add(professorFromJson(results.getJSONObject(i)));
        }
        return professors;
    }

    /** Parses JSONObject received to build a Professor Object. Doesn't populate Research Area,
     * that is dealt with later on.
     * NOTE: USES optJSON___() instead of getJSON___() so no error if object missing
     * Publications stay empty: the page loads them later.
     * @param json, a JSONObject received from function that called the API
     * @return Professor Entity
     * */
    private Professor professorFromJson(JSONObject json) {
        // id
        String id = lastPart(json.optString("id", ""));

        // name
        String name = json.optString("display_name", "");

        // institution
        String institution = "";
        JSONArray institutions = json.optJSONArray("last_known_institutions");
        if (institutions != null && !institutions.isEmpty()) {
            // USING optJSONobject() instead of getJSONObject() so no error if object missing
            institution = institutions.getJSONObject(0).optString("display_name", "");
        }

        // research areas
        List<String> researchAreas = new ArrayList<>();
        JSONArray topics = json.optJSONArray("topics");
        if (topics != null) {
            for (int i = 0; i < topics.length(); i++) {
                JSONObject subfield = topics.getJSONObject(i).optJSONObject("subfield");
                if (subfield != null) {
                    String subfieldName = subfield.optString("display_name", "");
                    if (!subfieldName.isEmpty() && !researchAreas.contains(subfieldName)) {
                        researchAreas.add(subfieldName);
                    }
                }
            }
        }

        return new Professor(id, name, institution, researchAreas, new ArrayList<ResearchPaper>());
    }
}

package data_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import entities.ResearchPaper;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.PaperOfTheDayUseCase.PaperSearchInterface;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

/**
 * Finds recent research papers using the OpenAlex API.
 */
public class PaperSearchDataObject implements PaperSearchInterface {
    private final OkHttpClient client;
    private final String apiKey;


    /**
     * Inititalizes fields by creating a new client and using method defined in function for key
     */
    public PaperSearchDataObject() {
        this.client = new OkHttpClient();
        this.apiKey = loadAPIKey();
    }

    // Source for API Keys: https://vonagedev.medium.com/working-with-environment-variables-in-java-26b11f1344a5

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
     * Calls OpenAlex with the given URL and turns the results into ResearchPapers.
     * @param url the full OpenAlex request URL
     * @return the papers in the response
     * @throws RuntimeException if the request fails
     */
    private List<ResearchPaper> fetchPapers(String url) {
        String responseBody;
        try {
            responseBody = makeAPIRequest(url);
        } catch (IOException e) {
            throw new RuntimeException("Could not retrieve publications", e);
        }

        JSONObject json = new JSONObject(responseBody);
        JSONArray resultsArray = json.getJSONArray("results");

        List<ResearchPaper> papers = new ArrayList<>();

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject paperJson = resultsArray.getJSONObject(i);
            papers.add(parsePaper(paperJson));
        }

        return papers;

    }

    /**
     * Gets recent papers for each of the user's interests, with no duplicates.
     * If the user has no interests, gets recent highly cited papers instead.
     * @param interests the user's research interests
     * @return a list of unique recent papers
     */
    @Override
    public List<ResearchPaper> searchRecent(List<String> interests) {
        List<ResearchPaper> results = new ArrayList<>();
        TreeSet<String> ids = new TreeSet<>();
        String fromDate = LocalDate.now().minusYears(2).toString();

        if (!interests.isEmpty()) {
            for (String interest : interests) {
                String url = "https://api.openalex.org/works"
                        + "?search=" + URLEncoder.encode(interest, StandardCharsets.UTF_8)
                        + "&filter=from_publication_date:" + fromDate
                        + "&sort=cited_by_count:desc"
                        + "&per_page=25"
                        + "&api_key=" + apiKey;

                // add new papers found for this interest
                for (ResearchPaper paper : fetchPapers(url)) {
                    if (ids.add(paper.getId())) {
                        results.add(paper);
                    }
                }
            }
        }

        // no interests
        else {
            String url = "https://api.openalex.org/works"
                    + "?"
                    + "&filter=from_publication_date:" + fromDate
                    + "&sort=cited_by_count:desc"
                    + "&per_page=25"
                    + "&api_key=" + apiKey;
            for (ResearchPaper paper : fetchPapers(url)) {
                if (ids.add(paper.getId())) {
                    results.add(paper);
                }
            }
        }

        return results;
    }

    /**
     * Turns one OpenAlex result into a ResearchPaper.
     * @param paperJson a single paper from OpenAlex's "results" array
     * @return the paper as a ResearchPaper
     */
    private ResearchPaper parsePaper(JSONObject paperJson) {

        String paperId = paperJson.getString("id");
        String title = paperJson.optString("title", "");
        int year = paperJson.optInt("publication_year", 0);

        List<String> authors = new ArrayList<>();
        JSONArray authorships = paperJson.optJSONArray("authorships");

        if (authorships != null) {
            for (int j = 0; j < authorships.length(); j++) {
                JSONObject authorship = authorships.getJSONObject(j);
                String authorName = authorship.optString("raw_author_name", "");

                authors.add(authorName);
            }
        }

        // Get paper URL
        String paperURL = "";

        JSONObject bestOaLocation = paperJson.optJSONObject("best_oa_location");
        if (bestOaLocation != null) {
            paperURL = bestOaLocation.optString("pdf_url", "");

            if (paperURL.isEmpty()) {
                paperURL = bestOaLocation.optString("landing_page_url", "");
            }
        }

        if (paperURL.isEmpty()) {
            JSONObject primaryLocation = paperJson.optJSONObject("primary_location");
            if (primaryLocation != null) {
                paperURL = primaryLocation.optString("landing_page_url", "");
            }
        }

        if (paperURL.isEmpty()) {
            paperURL = paperJson.optString("doi", "");
        }

        String abstractText = rebuildAbstract(paperJson.optJSONObject("abstract_inverted_index"));

        ResearchPaper paper = new ResearchPaper(paperId, title, authors, year, abstractText, paperURL);
        return paper;
    }

    /**
     * Rebuilds an abstract from OpenAlex's inverted index format.
     * @param invertedIndex a map of each word to the positions it appears at
     * @return the abstract as plain text, or "" if there isn't one
     */
    private String rebuildAbstract(JSONObject invertedIndex) {
        // OpenAlex gives {word: [positions]}

        if (invertedIndex == null) {
            return "";
        }

        int max = -1;
        for (String word : invertedIndex.keySet()) {
            JSONArray positions = invertedIndex.getJSONArray(word);
            for (int i = 0; i < positions.length() ; i++) {
                max = Math.max(max, positions.getInt(i));
            }
        }

        String[] words = new String[max + 1];

        for (String word : invertedIndex.keySet()) {
            JSONArray positions = invertedIndex.getJSONArray(word);
            for (int i = 0; i < positions.length(); i++) {
                words[positions.getInt(i)] = word;
            }
        }


        return String.join(" ", words);
    }



}

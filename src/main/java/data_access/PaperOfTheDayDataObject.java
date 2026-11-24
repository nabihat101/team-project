package data_access;

import entities.PaperOfTheDay;
import entities.ResearchPaper;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.LoginUseCase.LoginDataAccessInterface;
import use_case.PaperOfTheDayUseCase.PaperOfTheDayDataAccessInterface;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.time.LocalDate;

public class PaperOfTheDayDataObject implements PaperOfTheDayDataAccessInterface {

    private final File jsonFile;
    private final LoginDataAccessInterface userData;   // to get interests

    // per user: their latest pick + ids of every paper they've gotten
    private final Map<String, PaperOfTheDay> latestPicks = new HashMap<>();
    private final Map<String, List<String>> pastIds = new HashMap<>();

    public PaperOfTheDayDataObject(String jsonPath, LoginDataAccessInterface userData) {
        this.jsonFile = new File(jsonPath);
        this.userData = userData;

        // make sure file is not empty or missing
        if (jsonFile.exists() && jsonFile.length() > 0) {
            loadFile();
        }
    }

    @Override
    public PaperOfTheDay getTodaysPaper(String username) {
        PaperOfTheDay latestPick = latestPicks.get(username);

        if (latestPick != null && latestPick.isForToday()) {
            return latestPick;
        }
        // return null if it's not today's paper or it is null
        return null;
    }

    @Override
    public List<String> getPastPaperIds(String username) {
        // return pastIds for this user, or an empty list if none
       return pastIds.getOrDefault(username, new ArrayList<>());
    }

    @Override
    public List<String> getInterests(String username) {
        return userData.get(username).getInterests();
    }

    @Override
    public void save(PaperOfTheDay pick) {
        String username = pick.getUsername();
        List<String> ids = pastIds.getOrDefault(username, new ArrayList<>());
        ids.add(pick.getId());
        latestPicks.put(username, pick);
        pastIds.put(username, ids);
        saveFile();

    }

    private void loadFile() {
        String contents;

        try {
            contents = new String(Files.readAllBytes(jsonFile.toPath()));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject root = new JSONObject(contents);

        // loop over the users in the FILE (the maps are still empty here)
        for (String username : root.keySet()) {
            JSONObject userJson = root.getJSONObject(username);
            JSONObject pickJson = userJson.getJSONObject("latest");

            // rebuild the paper
            ResearchPaper paper = new ResearchPaper(
                    pickJson.getString("id"),
                    pickJson.getString("title"),
                    toList(pickJson.getJSONArray("authors")),
                    pickJson.getInt("year"),
                    pickJson.optString("abstract", ""),
                    pickJson.getString("url"));

            // rebuild the pick
            PaperOfTheDay pick = new PaperOfTheDay(paper, username,
                    LocalDate.parse(pickJson.getString("date")),
                    toList(pickJson.getJSONArray("reasons")));

            latestPicks.put(username, pick);
            pastIds.put(username, toList(userJson.getJSONArray("pastIds")));
        }
    }

    /**
     * Writes the two maps to the JSON file (so stores them from memory into file).
     */
    private void saveFile() {
        JSONObject root = new JSONObject();

        for (String username : latestPicks.keySet()) {
            PaperOfTheDay pick = latestPicks.get(username);

            // the pick - JSON
            JSONObject pickJson = new JSONObject();
            pickJson.put("id", pick.getId());
            pickJson.put("title", pick.getTitle());
            pickJson.put("authors", pick.getAuthors());
            pickJson.put("year", pick.getYear());
            pickJson.put("abstract", pick.getAbstractText());
            pickJson.put("url", pick.getUrl());
            pickJson.put("date", pick.getDate().toString());
            pickJson.put("reasons", pick.getReasons());

            // the user - JSON
            JSONObject userJson = new JSONObject();
            userJson.put("latest", pickJson);
            userJson.put("pastIds", getPastPaperIds(username));

            root.put(username, userJson);
        }

        // write once, after everyone's been added
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(root.toString(2));   // 2 = indent so it's readable
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Turns a JSON array of strings into a List.
     */
    private List<String> toList(JSONArray array) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }
}



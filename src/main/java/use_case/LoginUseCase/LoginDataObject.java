package use_case.LoginUseCase;

import entities.ContactedProfessor;
import entities.ResearchPaper;
import entities.User;
import entities.UserFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.RankedProfessorCase.UserDataAccessInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Saves users (with their saved papers and contacted professors) to a JSON file
 * so accounts stick around after the app closes.
 */
public class LoginDataObject implements LoginDataAccessInterface, UserDataAccessInterface {

    private final File jsonFile = new File("users.json");
    private final Map<String, User> users = new HashMap<>();
    private final UserFactory userFactory = new UserFactory();
    private String currentUsername;

    /**
     * Reads all saved users from the file. If there's no file yet, makes one.
     */
    public LoginDataObject() {
        if (jsonFile.length() == 0) {
            writeFile();
            return; // nothing saved yet
        }
        loadFile();
    }

    // READING

    /**
     * Reads every user from users.json into the map.
     */
    private void loadFile() {
        final String contents;
        try {
            contents = new String(Files.readAllBytes(jsonFile.toPath()));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }


        // get all users from the json
        final JSONArray usersArray = new JSONObject(contents).getJSONArray("users");
        for (int i = 0; i < usersArray.length(); i++) {
            final JSONObject userJson = usersArray.getJSONObject(i);

            final User user = userFactory.create(
                    userJson.getString("username"),
                    userJson.getString("password"),
                    toList(userJson.optJSONArray("interests")),
                    toList(userJson.optJSONArray("institutions")));

            // saved papers: collect them all into a list, then call set
            final List<ResearchPaper> savedPapers = new ArrayList<>();
            final JSONArray savedArray = userJson.optJSONArray("savedPapers");
            if (savedArray != null) {
                for (int j = 0; j < savedArray.length(); j++) {
                    savedPapers.add(jsonToPaper(savedArray.getJSONObject(j)));
                }
            }
            user.setSavedPapers(savedPapers);

            // contacted professors: same idea
            final List<ContactedProfessor> contacted = new ArrayList<>();
            final JSONArray contactedArray = userJson.optJSONArray("contactedProfessors");
            if (contactedArray != null) {
                for (int j = 0; j < contactedArray.length(); j++) {
                    contacted.add(jsonToProfessor(contactedArray.getJSONObject(j)));
                }
            }
            user.setContactedProfessors(contacted);
            users.put(user.getUsername(), user);
        }

        System.out.println("Reading: " + jsonFile.getAbsolutePath());
        System.out.println("Loaded users: " + users.keySet());
    }




    /**
     * Turns one paper JSON object into a ResearchPaper.
     */
    private ResearchPaper jsonToPaper(JSONObject json) {
        return new ResearchPaper(
                json.getString("id"),
                json.optString("title", ""),
                toList(json.optJSONArray("authors")),
                json.optInt("year", 0),
                json.optString("abstractText", ""),
                json.optString("url", ""));
    }

    /**
     * Turns one contacted professor JSON object into a ContactedProfessor.
     */
    private ContactedProfessor jsonToProfessor(JSONObject json) {
        final List<ResearchPaper> publications = new ArrayList<>();
        final JSONArray pubs = json.optJSONArray("publications");
        if (pubs != null) {
            for (int i = 0; i < pubs.length(); i++) {
                publications.add(jsonToPaper(pubs.getJSONObject(i)));
            }
        }

        return new ContactedProfessor(                                                     // CHECK order
                json.getString("id"),
                json.optString("name", ""),
                json.optString("institution", ""),
                toList(json.optJSONArray("researchAreas")),
                publications,
                LocalDate.parse(json.getString("contactedOn")),
                json.optBoolean("responded", false));
    }

    // ---------- writing ----------

    /**
     * Rewrites the whole file with every user.
     */
    private void writeFile() {
        final JSONArray usersArray = new JSONArray();
        for (User user : users.values()) {
            final JSONObject userJson = new JSONObject();
            userJson.put("username", user.getUsername());
            userJson.put("password", user.getPassword());
            userJson.put("interests", user.getInterests());
            userJson.put("institutions", user.getInstitutions());

            final JSONArray savedPapers = new JSONArray();
            for (ResearchPaper paper : user.getSavedPapers()) {                           // CHECK
                savedPapers.put(paperToJson(paper));
            }
            userJson.put("savedPapers", savedPapers);

            final JSONArray contacted = new JSONArray();
            for (ContactedProfessor professor : user.getContactedProfessors()) {         // CHECK
                contacted.put(professorToJson(professor));
            }
            userJson.put("contactedProfessors", contacted);

            usersArray.put(userJson);
        }

        final JSONObject root = new JSONObject();
        root.put("users", usersArray);

        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(root.toString(2));   // 2 = indent so it's readable
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Turns a ResearchPaper into a JSON object.
     */
    private JSONObject paperToJson(ResearchPaper paper) {
        final JSONObject json = new JSONObject();
        json.put("id", paper.getId());
        json.put("title", paper.getTitle());
        json.put("authors", paper.getAuthors());
        json.put("year", paper.getYear());
        json.put("abstractText", paper.getAbstractText());
        json.put("url", paper.getUrl());
        return json;
    }

    /**
     * Turns a ContactedProfessor into a JSON object.
     */
    private JSONObject professorToJson(ContactedProfessor professor) {
        final JSONArray publications = new JSONArray();
        for (ResearchPaper paper : professor.getPublications()) {
            publications.put(paperToJson(paper));
        }

        final JSONObject json = new JSONObject();
        json.put("id", professor.getId());
        json.put("name", professor.getName());
        json.put("institution", professor.getInstitution());
        json.put("researchAreas", professor.getResearchAreas());
        json.put("publications", publications);
        json.put("contactedOn", professor.getContactedOn().toString());                 // CHECK
        json.put("responded", professor.hasResponded());                                  // CHECK
        return json;
    }

    /**
     * Turns a JSON array of strings into a List. Missing array = empty list.
     */
    private List<String> toList(JSONArray array) {
        final List<String> list = new ArrayList<>();
        if (array == null) {
            return list;
        }
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }

    // interfacce methods

    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    /**
     * Adds the user (or updates them if they already exist) and saves.
     * @param user the user to save
     */
    public void save(User user) {
        users.put(user.getUsername(), user);
        writeFile();
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setUsername(String username) {
        currentUsername = username;
    }

    @Override
    public String getUsername() {
        return currentUsername;
    }
}
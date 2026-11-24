package data_access;

import entities.ContactedProfessor;
import entities.ResearchPaper;
import entities.User;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.UserProfile.UserProfileDataAccessInterface;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class UserProfileDataAccessObject implements UserProfileDataAccessInterface {

    private final Path filePath;
    private final JSONObject document;

    public UserProfileDataAccessObject() {
        this(Path.of("users.json"));
    }

    public UserProfileDataAccessObject(Path filePath) {
        this.filePath = filePath;
        this.document = readDocument();
        if (!document.has("users")) {
            throw new IllegalStateException("JSON profile file must contain a users array.");
        }
        document.getJSONArray("users");
    }

    private JSONObject readDocument() {
        try {
            return new JSONObject(Files.readString(filePath, StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to read user profiles from " + filePath,
                    exception);
        }
    }

    private void writeDocument() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(filePath, document.toString(2), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new UncheckedIOException("Unable to save user profiles to " + filePath,
                    exception);
        }
    }

    private JSONObject findUser(String username) {
        JSONArray users = document.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("username").equals(username)) {
                return user;
            }
        }
        return null;
    }

    private JSONObject requireUser(String username) {
        JSONObject user = findUser(username);
        if (user == null) {
            throw new NoSuchElementException("User not found: " + username);
        }
        return user;
    }

    @Override
    public User getUser(String username) {
        JSONObject profile = findUser(username);
        if (profile == null) {
            return null;
        }
        User user = new User(
                profile.getString("username"),
                profile.getString("password"),
                readStrings(profile.getJSONArray("interests")),
                readStrings(profile.getJSONArray("institutions"))
        );
        user.setContactedProfessors(readContactedProfessors(
                profile.getJSONArray("contactedProfessors")));
        user.setSavedPapers(readPapers(profile.getJSONArray("savedPapers")));
        return user;
    }

    @Override
    public List<ResearchPaper> getSavedPapers(String username) {
        return readPapers(requireUser(username).getJSONArray("savedPapers"));
    }

    @Override
    public List<ContactedProfessor> getContactedProfessors(String username) {
        return readContactedProfessors(
                requireUser(username).getJSONArray("contactedProfessors"));
    }

    @Override
    public boolean removeSavedPaper(String username, String paperId) {
        JSONObject user = findUser(username);
        if (user == null) {
            return false;
        }
        JSONArray papers = user.getJSONArray("savedPapers");
        for (int i = 0; i < papers.length(); i++) {
            if (papers.getJSONObject(i).getString("id").equals(paperId)) {
                papers.remove(i);
                writeDocument();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateUserPreferences(String username, List<String> interests,
                                         List<String> institutions) {
        JSONObject user = findUser(username);
        if (user == null) {
            return false;
        }
        user.put("interests", new JSONArray(interests));
        user.put("institutions", new JSONArray(institutions));
        writeDocument();
        return true;
    }

    @Override
    public ContactedProfessor updateContactResponseStatus(String username,
                                                          String professorName,
                                                          String institution,
                                                          boolean responded) {
        JSONObject user = requireUser(username);
        JSONArray professors = user.getJSONArray("contactedProfessors");
        for (int i = 0; i < professors.length(); i++) {
            JSONObject professor = professors.getJSONObject(i);
            if (professor.getString("name").equals(professorName)
                    && professor.getString("institution").equals(institution)) {
                professor.put("responded", responded);
                writeDocument();
                return readContactedProfessor(professor);
            }
        }
        throw new NoSuchElementException("Contacted professor not found: " + professorName);
    }

    private List<ContactedProfessor> readContactedProfessors(JSONArray professors) {
        List<ContactedProfessor> result = new ArrayList<>();
        for (int i = 0; i < professors.length(); i++) {
            result.add(readContactedProfessor(professors.getJSONObject(i)));
        }
        return result;
    }

    private ContactedProfessor readContactedProfessor(JSONObject professor) {
        return new ContactedProfessor(
                professor.getString("id"),
                professor.getString("name"),
                professor.getString("institution"),
                readStrings(professor.getJSONArray("researchAreas")),
                readPapers(professor.getJSONArray("publications")),
                LocalDate.parse(professor.getString("contactedOn")),
                professor.getBoolean("responded")
        );
    }

    private List<ResearchPaper> readPapers(JSONArray papers) {
        List<ResearchPaper> result = new ArrayList<>();
        for (int i = 0; i < papers.length(); i++) {
            JSONObject paper = papers.getJSONObject(i);
            result.add(new ResearchPaper(
                    paper.getString("id"),
                    paper.getString("title"),
                    readStrings(paper.getJSONArray("authors")),
                    paper.getInt("year"),
                    paper.getString("abstractText"),
                    paper.getString("url")
            ));
        }
        return result;
    }

    private List<String> readStrings(JSONArray values) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < values.length(); i++) {
            result.add(values.getString(i));
        }
        return result;
    }
}

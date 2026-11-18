package use_case.LoginUseCase;

import entities.User;
import entities.UserFactory;
//import use_case.LoginUseCase.LoginDataAccessInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Saves users to a CSV file so accounts stick around after the app closes.
 */
public class LoginDataObject implements LoginDataAccessInterface {

    private static final String HEADER = "username,password,interests,institutions";
    // lists are stored in one column, separated by ;
    private static final String LIST_SEPARATOR = ";";

    private final File csvFile;
    private final Map<String, User> users = new HashMap<>();

    // private String currentUsername;

    /**
     * Reads all saved users from the file. If there's no file yet, makes one.
     * @param csvPath where the CSV file is
     * @param userFactory used to turn each row back into a User
     */
    public LoginDataObject(String csvPath, UserFactory userFactory) {
        csvFile = new File(csvPath);

        if (csvFile.length() == 0) {
            writeFile();
            return; // nothing saved yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            final String header = reader.readLine();
            if (!HEADER.equals(header)) {
                throw new RuntimeException("Expected header \"" + HEADER + "\" but found \"" + header + "\"");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                // skip empty lines so they don't break the split
                if (line.isBlank()) {
                    continue;
                }
                // -1 keeps empty columns (e.g. no interests yet)
                final String[] parts = line.split(",", -1);
                final User user = userFactory.create(parts[0], parts[1],
                        toList(parts[2]), toList(parts[3]));
                users.put(parts[0], user);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Rewrites the whole file with the header + every user.
     */
    private void writeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write(HEADER);
            writer.newLine();

            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword() + ","
                        + String.join(LIST_SEPARATOR, user.getInterests()) + ","
                        + String.join(LIST_SEPARATOR, user.getInstitutions()));
                writer.newLine();
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Turns "robotics;ml" back into a list. Empty column = empty list.
     */
    private List<String> toList(String column) {
        if (column.isBlank()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(column.split(LIST_SEPARATOR)));
    }

    @Override
    public boolean existsByUsername(String username) {
        return false;
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

    }

    @Override
    public String getUsername() {
        return "";
    }

}
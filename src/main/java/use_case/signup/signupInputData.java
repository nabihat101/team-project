package use_case.signup;

import java.util.List;

/**
 * The Input Data for the Signup Use Case.
 */
public class signupInputData {

    private final String username;
    private final String password;
    private final String repeatPassword;
    private final List<String> topics;
    private final List<String> locations;


    public signupInputData(String username, String password, String repeatPassword, List<String>
                           topics, List<String> locations) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.topics = topics;
        this.locations = locations;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    List<String> getTopics() {
        return topics;
    }

    List<String> getLocations() {
        return locations;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }
}
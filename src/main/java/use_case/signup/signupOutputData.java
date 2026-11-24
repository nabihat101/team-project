package use_case.signup;

import java.util.List;

/**
 * Output Data for the Signup Use Case.
 */
public class signupOutputData {

    private final String username;
//    private final List<String> topics;
//    private final List<String> locations;

    public signupOutputData(String username, List<String> topics, List<String> locations) {
        this.username = username;
//        this.topics = topics;
//        this.locations = locations;
    }

    public String getUsername() {
        return username;
    }

//    List<String> getTopics() {
//        return topics;
//    }
//
//    List<String> getLocations() {
//        return locations;
//    }


}
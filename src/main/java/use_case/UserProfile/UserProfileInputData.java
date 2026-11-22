package use_case.UserProfile;

public class UserProfileInputData {
    private final String username;

    /**
     *
     * @param username      The user's username
     */
    public UserProfileInputData(String username) {
        this.username = username;
    }

    /**
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

}

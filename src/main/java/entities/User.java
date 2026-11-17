package entities;

import java.util.List;

public class User {

    private final String username;
    private String password;
    private List<String> interests;
    private List<String> institutions;


    /**
     * Creates a new user
     *
     * @param username      The username
     * @param password      The password
     * @param interests     The user's research interests
     * @param institutions  The user's preferred institutions
     * @throws IllegalArgumentException if the password or username are empty
     */

    public User(String username, String password, List<String> interests,
                List<String> institutions){

        if ("".equals(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        this.username = username;
        this.password = password;
        this.interests = List.copyOf(interests);
        this.institutions = List.copyOf(institutions);

    }

    /**
     * Returns the user's username
     * @return username
     */
    public String getUsername(){
        return username;
    }
    /**
     * Returns the user's password
     * @return password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Returns the user's research interests
     * @return interests
     */
    public List<String> getInterests(){
        return interests;
    }

    /**
     * Returns the user's preferred institutions
     * @return institutions
     */
    public List<String> getInstitutions(){
        return institutions;
    }


}



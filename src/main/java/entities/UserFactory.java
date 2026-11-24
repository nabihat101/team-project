
package entities;

import java.util.List;

public class UserFactory {

    /**
     * Creates a new user
     *
     * @param username      The username
     * @param password      The password
     * @param interests     The user's research interests
     * @param institutions  The user's preferred institutions
     *
     * @return User
     */
    public User create(String username, String password, List<String> interests,
                   List<String> institutions) {

    return new User(username, password, interests, institutions);
}


}

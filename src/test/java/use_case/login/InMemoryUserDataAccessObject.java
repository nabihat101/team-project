package use_case.login;

import entities.User;
import use_case.LoginUseCase.LoginDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/* USED ONLY FOR TEST FILE; THIS CLASS IS A MOCK DAO */

public class InMemoryUserDataAccessObject implements LoginDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    private String currentUsername;

    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setUsername(String username) {
        this.currentUsername = username;
    }

    @Override
    public String getUsername() {
        return this.currentUsername;
    }
}

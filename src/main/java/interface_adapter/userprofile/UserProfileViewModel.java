package interface_adapter.userprofile;

import java.util.ArrayList;
import java.util.List;

public class UserProfileViewModel {

    public static final String VIEW_NAME = "My Profile";
    private final List<UserProfileViewModelListener> listeners = new ArrayList<>();
    private UserProfileState state = new UserProfileState();

    public UserProfileState getState(){
        return state;
    }

    public void setState(UserProfileState state){
        this.state = state;
    }

    public void addListener(UserProfileViewModelListener listener){
        listeners.add(listener);
    }

    public void notifyStateChanged(){
        for (UserProfileViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}

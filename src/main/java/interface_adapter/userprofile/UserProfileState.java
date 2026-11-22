package interface_adapter.userprofile;

import entities.ContactedProfessor;
import entities.ResearchPaper;
import entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserProfileState {

    private User user;
    private List<ResearchPaper> savedPapers = new ArrayList<>();
    private List<ContactedProfessor> contactedProfessors = new ArrayList<>();
    private String error;

    public User getUser(){
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ResearchPaper> getSavedPapers() {
        return savedPapers;
    }

    public void setSavedPapers(List<ResearchPaper> savedPapers) {
        this.savedPapers = new ArrayList<>(savedPapers);
    }

    public List<ContactedProfessor> getContactedProfessors() {
        return contactedProfessors;
    }

    public void setContactedProfessors(List<ContactedProfessor> contactedProfessors) {
        this.contactedProfessors = new ArrayList<>(contactedProfessors);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

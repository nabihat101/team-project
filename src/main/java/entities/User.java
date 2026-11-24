package entities;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String username;
    private String password;
    private List<String> interests;
    private List<String> institutions;
    private List<ContactedProfessor> contactedProfessors;
    private List<ResearchPaper> savedPapers;


    /**
     * Creates a new user
     *
     * @param username      The username
     * @param password      The password
     * @param interests     The user's research interests
     * @param institutions  The user's preferred institutions
     */

    public User(String username, String password, List<String> interests,
                List<String> institutions){
        
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

    /**
     * Returns the professors the user has contacted.
     *
     * @return the user's contacted professors
     */
    public List<ContactedProfessor> getContactedProfessors() {
        return contactedProfessors;
    }

    /**
     * Returns the research papers saved by the user.
     *
     * @return the user's saved papers
     */
    public List<ResearchPaper> getSavedPapers() {
        return savedPapers;
    }

    public void setSavedPapers(List<ResearchPaper> savedPapers) {
        this.savedPapers = new ArrayList<>(savedPapers);
    }

    public void setContactedProfessors(List<ContactedProfessor> contactedProfessors) {
        this.contactedProfessors = new ArrayList<>(contactedProfessors);
    }





}



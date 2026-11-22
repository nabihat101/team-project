package interface_adapter.professor_pages;

import entities.Professor;

/**
 * State object that stores info about prof page view
 */
public class ProfessorPageState {
    private Professor professor;
    private String error;

    /**
     * Constructor for creating an empty state
     */
    public ProfessorPageState() {
    }

    /**
     * Constructor for creating copy state
     * @param copy - state
     */
    public ProfessorPageState(ProfessorPageState copy) {
        this.professor = copy.professor;
        this.error = copy.error;
    }

    /**
     * Get professor object
     * @return Professor object
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * Set a professor for state
     * @param professor - entity object
     */
    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    /**
     * Error of state
     * @return error
     */
    public String getError() {
        return error;
    }

    /**
     * Set error for state
     * @param error
     */
    public void setError(String error) {
        this.error = error;
    }

}

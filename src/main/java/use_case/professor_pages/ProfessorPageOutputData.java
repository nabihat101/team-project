package use_case.professor_pages;
import entities.Professor;

/**
 * Output data object that sends prof entity to presenter
 */
public class ProfessorPageOutputData {
    private final Professor professor;

    /**
     * Init. fields for class
     * @param professor - professor object
     */
    public ProfessorPageOutputData(Professor professor) {
        this.professor = professor;
    }

    /**
     * Gets professor object
     * @return professor - professor object
     */
    public Professor getProfessor() {
        return professor;
    }
}

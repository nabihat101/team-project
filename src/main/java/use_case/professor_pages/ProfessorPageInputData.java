package use_case.professor_pages;

/**
 * input data class to pass on input to interactor
 */
public class ProfessorPageInputData {
    private final String id;

    /**
     * Initializes the fields
     * @param id - professor id
     */
    public ProfessorPageInputData(String id) {
        this.id = id;
    }

    /**
     * Gets professor id
     * @return id - professor id
     */
    public String getProfessorId() {
        return id;
    }
}

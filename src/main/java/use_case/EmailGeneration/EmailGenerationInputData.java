package use_case.EmailGeneration;

public class EmailGenerationInputData {

    private final String professorName;
    private final String professorInstitution;
    private final String researchAreas;
    private final String userInterests;

    /**
     *
     * @param professorName           the professor's full name
     * @param professorInstitution    the current institution of the professor
     * @param researchAreas           the professor's areas of research
     * @param userInterests           the user's areas of interest
     */
    public EmailGenerationInputData(String professorName, String professorInstitution,
                                    String researchAreas, String userInterests) {
        this.professorName = professorName;
        this.professorInstitution = professorInstitution;
        this.researchAreas = researchAreas;
        this.userInterests = userInterests;
    }

    /**
     *
     * @return the professor's full name
     */
    public String getProfessorName() {
        return professorName;
    }

    /**
     *
     * @return the current institution of the professor
     */
    public String getProfessorInstitution() {
        return professorInstitution;
    }

    /**
     *
     * @return the professor's areas of research
     */
    public String getResearchAreas() {
        return researchAreas;
    }

    /**
     *
     * @return the user's areas of interest
     */
    public String getUserInterests() {
        return userInterests;
    }
}

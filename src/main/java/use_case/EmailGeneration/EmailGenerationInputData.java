package use_case.EmailGeneration;

public class EmailGenerationInputData {

    private final String professorName;
    private final String researchAreas;
    private final String userInterests;

    /**
     *
     * @param professorName    the professor's full name
     * @param researchAreas    the professor's areas of research
     * @param userInterests    the user's areas of interest
     */
    public EmailGenerationInputData(String professorName, String researchAreas,
                                    String userInterests) {
        this.professorName = professorName;
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

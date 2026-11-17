package entities;
import java.util.List;

public class Professor {

    private final String name;
    private final String institution;
    private List<String> researchAreas;
    private List<ResearchPaper> publications;


    /**
     * Creates a new professor
     * @param name          The professor's full name
     * @param institution   The institution the professor currently works at
     * @param researchAreas The research areas of the professor
     * @param publications  The published papers of the professor
     */

    public Professor(String name, String institution, List<String> researchAreas,
                     List<ResearchPaper> publications){

        this.name = name;
        this.institution = institution;
        this.researchAreas = List.copyOf(researchAreas);
        this.publications = List.copyOf(publications);
    }

    /**
     * Returns the name of the professor.
     *
     * @return the professor's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the institution the professor works at.
     *
     * @return the professor's institution (for example a university)
     */
    public String getInstitution() {
        return institution;
    }

    /**
     * Returns the research areas the professor works in.
     *
     * @return the list of the professor's research areas
     */
    public List<String> getResearchAreas() {
        return researchAreas;
    }

    /**
     * Returns the research papers the professor has published.
     *
     * @return the list of the professor's publications
     */
    public List<ResearchPaper> publications() {
        return publications;
    }
}

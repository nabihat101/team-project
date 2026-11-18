package use_case.RankedProfessorCase;
import java.util.List;

/* One row of the ranked list, as given to the output data so the presenter will see it. */

public class RankedProfessorEntry {
    
    private final String professorId;
    private final String name;
    private final String institution;
    private final List<String> matchedAreas;

    public RankedProfessorEntry(String professorId, String name, String institution,
                                List<String> matchedAreas) {
        this.professorId = professorId;
        this.name = name;
        this.institution = institution;
        this.matchedAreas = List.copyOf(matchedAreas);
    }

    /**
     *  TENTATIVE; HAVE TO CHECK API
     * Returns the ID of the professor.
     *
     * @return professorId
     */
    public String getProfessorId() {
        return professorId; 
    }

    /**
     * Returns the name of the professor.
     *
     * @return name
     */
    public String getName() {
        return name; 
    }

    /**
     * Returns the institution of the professor.
     *
     * @return institution
     */
    public String getInstitution() {
        return institution; 
    }

    /**
     * Returns the list of matched areas for the professor.
     *
     * @return matchedAreas
     */
    public List<String> getMatchedAreas() { 
        return matchedAreas; 
    }
}
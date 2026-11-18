package use_case.RankedProfessorCase;

import java.util.List;

public class RankedProfessorOutputData {
    
/** The output data for the Ranked Professors use case. */
public class RankedProfessorsOutputData {

    private final List<RankedProfessorEntry> professors;   // best match first
    private final List<String> topicsUsed;
    private final List<String> locationsUsed;

    public RankedProfessorsOutputData(List<RankedProfessorEntry> professors,
                                      List<String> topicsUsed,
                                      List<String> locationsUsed) {
        this.professors = List.copyOf(professors);
        this.topicsUsed = List.copyOf(topicsUsed);
        this.locationsUsed = List.copyOf(locationsUsed);
    }

    public List<RankedProfessorEntry> getProfessors() {
        return professors; 
    }

    public List<String> getTopicsUsed() {
        return topicsUsed; 
    }
    
    public List<String> getLocationsUsed() { 
        return locationsUsed; 
    }
}
}

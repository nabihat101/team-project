package use_case.rankedprof;

import entities.Professor;
import use_case.RankedProfessorCase.ProfessorDataAccessInterface;

import java.util.ArrayList;
import java.util.List;


/* USED ONLY FOR TESTS; THIS CLASS IS A MOCK DAO */
public class InMemoryProfessorDataAccess implements ProfessorDataAccessInterface {

    private final List<Professor> professors = new ArrayList<>();

    public void add(Professor professor) {
        professors.add(professor);
    }

    @Override
    public List<Professor> findByAreasAndLocations(List<String> areas, List<String> locations) {
        final List<Professor> result = new ArrayList<>();

        for (Professor p : professors) {
            // Does this professor share at least one area with the selected areas?
            boolean areaMatch = false;
            for (String area : p.getResearchAreas()) {
                if (areas.contains(area)) {
                    areaMatch = true;
                }
            }

            // No locations selected means no restriction.
            boolean locationMatch = locations.isEmpty() || locations.contains(p.getInstitution());

            if (areaMatch && locationMatch) {
                result.add(p);
            }
        }
        return result;
    }
}
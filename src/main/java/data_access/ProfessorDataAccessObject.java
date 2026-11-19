package data_access;
import entities.Professor;
import use_case.RankedProfessorCase.ProfessorDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/* Data Access Object for Professor. To be used in Ranked Professor Use Case. */

public class ProfessorDataAccessObject implements ProfessorDataAccessInterface{
 
    private final List<Professor> professors = new ArrayList<>();

    /**
     * Adds a professor to the data access object.
     *
     * @param professor the professor to add
     */
    public void add(Professor professor) {
        professors.add(professor);
    }

    /**
     * Finds professors by their research areas and locations.
     *
     * @param areas the list of research areas to match
     * @param locations the list of locations to match
     * @return a list of professors that match the given research areas and locations
     */
    @Override
    public List<Professor> findByAreasAndLocations(List<String> areas, List<String> locations) {
        final List<Professor> result = new ArrayList<>();

        for (Professor p : professors) {
            boolean areaMatch = false;
            for (String area : p.getResearchAreas()) {
                if (areas.contains(area)) {
                    areaMatch = true;
                }
            }

            boolean locationMatch = locations.isEmpty() || locations.contains(p.getInstitution());

            if (areaMatch && locationMatch) {
                result.add(p);
            }
        }
        return result;
    }
}
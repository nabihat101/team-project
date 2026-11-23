package use_case.RankedProfessorCase;

import java.util.List;

import entities.Professor;

public interface RankedProfessorDataAccessInterface {
        /**
     * Returns professors whose research areas overlap with at least one of the given areas
     * and who are at an institution matching one of the given locations.
     * An empty locations list means no location restriction.
     * Results are unordered: ranking is the interactor's job.
     */
    List<Professor> findByAreasAndLocations(List<String> areas, List<String> locations);
}

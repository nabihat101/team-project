package use_case.rankedprof;

import data_access.ProfessDataObject;
import entities.Professor;

import java.util.List;

/* Checks if the Professor is fetching info properly for Ranked Professor Use Case.
* This is technically not a JUnit test, just a sanity check. Ignore file. */
public class RankedProfessorDAOTest {
    public static void main(String[] args) {
        ProfessDataObject dao = new ProfessDataObject();

        List<String> topics = dao.getTopicOptions();
        List<String> locations = dao.getLocationOptions();
        System.out.println("Topics: " + topics.size() + " -> " + topics);
        System.out.println("Locations: " + locations.size() + " -> " + locations);

        // Pick the first topic and first location from the real lists to test with
        List<Professor> results = dao.findByAreasAndLocations(
                List.of(topics.get(0)), List.of(locations.get(0)));
        System.out.println("Found " + results.size() + " professors");
        for (Professor p : results) {
            System.out.println(p.getId() + " | " + p.getName() + " | "
                    + p.getInstitution() + " | " + p.getResearchAreas());
        }
    }
}
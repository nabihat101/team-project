package use_case.RankedProfessorCase;
import entities.Professor;
import entities.User;

import java.util.ArrayList;
import java.util.List;


public class RankedProfessorInteractor implements RankedProfessorInputBoundary {
    /** TENTATIVE STEPS FOR RANKED PROFESSOR USE CASE
     * 1) Loads User's Profile through DAO
     * 2) Use profiles interests
     * 3) match interests with professor
     * 4) if none, call preparefailview
     * 5) rank the matches
     * 6) call prepareSuccessView
     */

    private final UserDataAccessInterface userDataAccess;
    private final RankedProfessorDataAccessInterface professorDataAccess;
    private final RankedProfessorOutputBoundary presenter;


    /**
     * Constructor for RankedProfessorInteractor.
     *
     * @param userDataAccess the user data access interface
     * @param professorDataAccess the professor data access interface
     * @param presenter the output boundary for presenting results
     */
    public RankedProfessorInteractor(UserDataAccessInterface userDataAccess,
                                      RankedProfessorDataAccessInterface professorDataAccess,
                                      RankedProfessorOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.professorDataAccess = professorDataAccess;
        this.presenter = presenter;
    }

    /**
     * Executes the ranked professor use case.
     * @param inputData the input data for the use case
     */
    @Override
    public void execute(RankedProfessorInputData inputData) {
        final User user = userDataAccess.get(inputData.getUsername());

        // Checked boxes override the profile; nothing checked falls back to the profile.
        List<String> topics;
        if (inputData.getTopicFilters().isEmpty()) {
            topics = user.getInterests();
        }
        else {
            topics = inputData.getTopicFilters();
        }

        List<String> locations;
        if (inputData.getLocationFilters().isEmpty()) {
            locations = user.getInstitutions();
        } else {
            locations = inputData.getLocationFilters();
        }


        // Send Error Message if nothing to filter by
        if (topics.isEmpty() && locations.isEmpty()) {
            presenter.prepareFailView(
                    "No filters selected. Pick a topic or location to search.");
            return;
        }


        final List<Professor> candidates;
            try {
                candidates = professorDataAccess.findByAreasAndLocations(topics, locations);
            }
            catch (RuntimeException exception) {
                presenter.prepareFailView("Couldn't load professors right now. Please try again later.");
                return;
            }
        final List<RankedProfessorEntry> ranked = rank(candidates, topics);

        // Send Error Message if no professors matched the filters
        if (ranked.isEmpty()) {
            presenter.prepareFailView(
                    "No professors matched. Try selecting more topics or locations.");
            return;
        }

        presenter.prepareSuccessView(new RankedProfessorOutputData(ranked, topics, locations));
    }

    /**
     * Ranks the given list of professors based on the number of selected topics that appear in their research areas.
     * Professors with a higher score are ranked first. Ties are broken alphabetically by name.
     * @param professors the list of professors to rank
     * @param topics the list of selected topics to match against
     * @return a list of ranked professor entries, sorted by score, and if equal, then name
     */
    private List<RankedProfessorEntry> rank(List<Professor> professors, List<String> topics) {
        final List<RankedProfessorEntry> entries = new ArrayList<>();

        for (Professor professor : professors) {
            final List<String> matched = new ArrayList<>();
            for (String area : professor.getResearchAreas()) {
                if (topics.contains(area) && !matched.contains(area)) {
                    matched.add(area);
                }
            }

            if (!matched.isEmpty()) {
                entries.add(new RankedProfessorEntry(professor.getId(), professor.getName(),
                        professor.getInstitution(), matched));
            }
        }

        // Sort the entries first by the number of matched areas (descending), then by name (ascending)
        entries.sort((a, b) -> {
            int difference = b.getMatchedAreas().size() - a.getMatchedAreas().size();
            if (difference != 0) {
                return difference;
            }
            return a.getName().compareTo(b.getName());
        });

        return entries;
    }
    
}

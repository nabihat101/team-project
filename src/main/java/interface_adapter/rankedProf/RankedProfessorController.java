package interface_adapter.rankedProf;
import use_case.RankedProfessorCase.RankedProfessorInputBoundary;
import use_case.RankedProfessorCase.RankedProfessorInputData;

import java.util.List;

public class RankedProfessorController {
    private final RankedProfessorInputBoundary interactor;

    /**
     * initializes the fields for class
     * @param rankedProfInteractor - interactor for the use case
     */
    public RankedProfessorController(RankedProfessorInputBoundary rankedProfInteractor) {
        this.interactor = rankedProfInteractor;
    }

    /**
     * Runs the use case.
     * @param username the logged-in user
     * @param topics the checked topics (empty list if none are checked)
     * @param locations the checked locations (empty list if none are checked)
     */
    public void execute(String username, List<String> topics, List<String> locations) {
        RankedProfessorInputData inputData =
                new RankedProfessorInputData(username, topics, locations);
        interactor.execute(inputData);
    }

}

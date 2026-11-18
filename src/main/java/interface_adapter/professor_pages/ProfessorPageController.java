package interface_adapter.professor_pages;

import use_case.professor_pages.ProfessorPageInputBoundary;
import use_case.professor_pages.ProfessorPageInputData;

public class ProfessorPageController {
    private final ProfessorPageInputBoundary interactor;

    public ProfessorPageController(ProfessorPageInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String id) {
        ProfessorPageInputData inputData = new ProfessorPageInputData(id);
        interactor.execute(inputData);
    }

}

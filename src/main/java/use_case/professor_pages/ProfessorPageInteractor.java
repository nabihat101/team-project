package use_case.professor_pages;

import entities.Professor;

public class ProfessorPageInteractor implements ProfessorPageInputBoundary{
    private final ProfessorDataAccessInterface dataAccessObject;
    private final ProfessorPageOutputBoundary presenter;

    public ProfessorPageInteractor(ProfessorDataAccessInterface dataAccessObject, ProfessorPageOutputBoundary presenter) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    public void execute(ProfessorPageInputData inputData) {
        String professorId = inputData.getProfessorId();
        Professor professor = dataAccessObject.getProfessorById(professorId);
        ProfessorPageOutputData outputData = new ProfessorPageOutputData(professor);
        presenter.prepareSuccessView(outputData);
    }
}

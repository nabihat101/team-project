package use_case.professor_pages;

import entities.Professor;

/**
 * Interactor for use case
 */
public class ProfessorPageInteractor implements ProfessorPageInputBoundary{
    private final ProfessorDataAccessInterface dataAccessObject;
    private final ProfessorPageOutputBoundary presenter;

    /**
     * Init. all fields
     * @param dataAccessObject
     * @param presenter
     */
    public ProfessorPageInteractor(ProfessorDataAccessInterface dataAccessObject, ProfessorPageOutputBoundary presenter) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    /**
     * Executes use case by using the fields
     * @param inputData
     */
    public void execute(ProfessorPageInputData inputData) {
        String professorId = inputData.getProfessorId();

        try {
            Professor professor = dataAccessObject.getProfessorById(professorId);

            if (professor == null) {
                presenter.prepareFailView("Professor Not Found.");
                return;
            }

            ProfessorPageOutputData outputData = new ProfessorPageOutputData(professor);
            presenter.prepareSuccessView(outputData);
        } catch (RuntimeException e){
            presenter.prepareFailView("Unable to retrieve professor information.");
        }

    }
}

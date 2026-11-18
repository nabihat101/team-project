package use_case.professor_pages;

/**
 * Defines methods to be implemented by presenter
 */
public interface ProfessorPageOutputBoundary {
    void prepareSuccessView(ProfessorPageOutputData outputData);
    void prepareFailView(String error);
}

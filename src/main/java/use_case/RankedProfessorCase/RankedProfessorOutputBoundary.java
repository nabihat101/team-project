package use_case.RankedProfessorCase;

public interface RankedProfessorOutputBoundary {
    void prepareSuccessView(RankedProfessorOutputData outputData);
    
    /** Used for "no matches found" and any other failure; the message includes the suggestion. */
    void prepareFailView(String errorMessage);
}


    
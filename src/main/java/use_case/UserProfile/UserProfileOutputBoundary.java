package use_case.UserProfile;

import entities.ContactedProfessor;

public interface UserProfileOutputBoundary {

    /**
     *
     * @param outputData
     */
    void prepareSuccessView(UserProfileOutputData outputData);

    /**
     *
     * @param professor
     */
    void prepareContactResponseUpdated(ContactedProfessor professor);

    /**
     *
     * @param error
     */
    void prepareFailureView(String error);
}

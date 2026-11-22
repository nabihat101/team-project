package use_case.EmailGeneration;

import entities.User;
import entities.Professor;

import java.util.List;

public class EmailGenerationInteractor implements EmailGenerationInputBoundary {
    private final EmailGenerationDataAccessInterface emailDataAccessObject;
    private final EmailGenerationOutputBoundary emailPresenter;
    private final User user;
    private final Professor professor;

    /**
     * Constructor for class
     * @param emailDataAccessObject   the data access object for email generation
     * @param emailPresenter          the presenter for email generation
     * @param user                    the user who generates the email
     * @param professor               the professor whom the email is for
     */
    public EmailGenerationInteractor(EmailGenerationDataAccessInterface emailDataAccessObject,
                                     EmailGenerationOutputBoundary emailPresenter,
                                     User user, Professor professor) {
        this.emailDataAccessObject = emailDataAccessObject;
        this.emailPresenter = emailPresenter;
        this.user = user;
        this.professor = professor;
    }

    @Override
    public void execute(EmailGenerationInputData emailGenerationInputData) {
        final String emailBrief = emailGenerationInputData.getEmailBrief();
        final String professorName = professor.getName();
        final List<String> researchAreas = professor.getResearchAreas();
        final String professorInstitution = professor.getInstitution();
        final List<String> userInterests = user.getInterests();

        if (userInterests.isEmpty()) {}


    }
}

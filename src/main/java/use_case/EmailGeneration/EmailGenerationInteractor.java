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
        final String brief = emailGenerationInputData.getEmailBrief();

        try {
            if (userInterests.isEmpty()) {
                emailPresenter.prepareFailureView("The user has not provided any relevant interests.");
            } else {
                String research = String.join(",", researchAreas);
                String interest = String.join(",", userInterests);
                final String context = "Professor " + professorName + " from " + professorInstitution +
                        " with research in " + research + ", user is interested in " + interest;
                final String instruction = "Generate a cold email using the given information for the purpose of " + brief;
                ;
                final String emailContent = emailDataAccessObject.generateEmail(context, instruction).toString();

                EmailGenerationOutputData emailGenerationOutputData = new EmailGenerationOutputData(emailContent);
                emailPresenter.prepareSuccessView(emailGenerationOutputData);
            }
        } catch (RuntimeException e) {
            emailPresenter.prepareFailureView("Unable to generate email.");
        }
    }
}

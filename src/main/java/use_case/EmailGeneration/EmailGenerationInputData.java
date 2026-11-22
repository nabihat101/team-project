package use_case.EmailGeneration;

public class EmailGenerationInputData {

    private final String emailBrief;

    /**
     *
     * @param emailBrief the user's instruction for the email
     */
    public EmailGenerationInputData(String emailBrief) {
        this.emailBrief = emailBrief;
    }

    /**
     *
     * @return the email brief
     */
    public String getEmailBrief() {
        return emailBrief;
    }
}

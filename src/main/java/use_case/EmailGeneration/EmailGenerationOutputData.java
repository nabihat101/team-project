package use_case.EmailGeneration;

public class EmailGenerationOutputData {
    private final String subject;
    private final String body;

    /**
     *
     * @param subject the subject of the email
     * @param body the body of the email
     */
    public EmailGenerationOutputData(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    /**
     *
     * @return the subject of the email
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @return the body of the email
     */
    public String getBody() {
        return body;
    }
}

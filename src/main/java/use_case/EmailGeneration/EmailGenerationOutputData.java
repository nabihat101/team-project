package use_case.EmailGeneration;

public class EmailGenerationOutputData {
    private final String content;

    /**
     *
     * @param content the content of the generated email
     */
    public EmailGenerationOutputData(String content) {
        this.content = content;
    }

    /**
     *
     * @return the content of the email
     */
    public String getContent() {
        return content;
    }
}

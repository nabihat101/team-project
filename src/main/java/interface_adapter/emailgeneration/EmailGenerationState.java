package interface_adapter.emailgeneration;

public class EmailGenerationState {
    private String emailContent = "";
    private String error;

    /**
     *
     * @return the content of the email
     */
    public String getEmailContent() {
        return emailContent;
    }

    /**
     *
     * @return the error message
     */
    public String getError() {
        return error;
    }

    /**
     *
     * @param emailContent the content of the email
     */
    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    /**
     *
     * @param error the error message
     */
    public void setError(String error) {
        this.error = error;
    }
}

package use_case.PaperOfTheDayUseCase;

import java.util.List;

/**
 * Output Data for the Paper of the Day Use Case.
 */
public class PaperOfTheDayOutputData {
    private final String title;
    private final List<String> authors;
    private final int year;
    private final String url;
    private final String abstractText;
    private final List<String> reasons;


    public PaperOfTheDayOutputData(String title, List<String> authors,
                                   int year, String url, String abstractText, List<String> reasons) {
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.url = url;
        this.abstractText = abstractText;
        this.reasons = reasons;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public int getYear() {
        return year;
    }

    public String getUrl() {
        return url;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public List<String> getReasons() {
        return reasons;
    }
}

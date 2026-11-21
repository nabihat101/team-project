package interface_adapter.PaperOfTheDay;

import java.util.ArrayList;
import java.util.List;

/**
 * What the Paper of the Day screen is currently showing.
 */
public class PaperOfTheDayState {
    private String title = "";
    private List<String> authors = new ArrayList<>();
    private int year = 0;
    private String url = "";
    private String abstractText = "";
    private List<String> reasons = new ArrayList<>();
    private String error = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }




}
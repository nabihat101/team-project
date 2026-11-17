package entities;

import java.util.List;

public class ResearchPaper {
    private final String id;
    private final String title;
    private final List<String> authors;
    private final int year;
    private final String abstractText;
    private final String url;

    /**
     *
     * @param id           The id of the research paper
     * @param title        The title of the research paper
     * @param authors      The authors of the research paper
     * @param year         The year the paper was published
     * @param abstractText The abstract of the research
     * @param url          A link to the paper
     */
    public ResearchPaper(String id, String title, List<String> authors, int year,
                         String abstractText, String url) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.abstractText = abstractText;
        this.url = url;
    }

    /**
     * Returns the id of the research paper.
     *
     * @return the paper's id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the title of the research paper.
     *
     * @return the paper's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the authors of the research paper.
     *
     * @return the list of the paper's authors
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * Returns the year the research paper was published.
     *
     * @return the publication year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the abstract of the research paper.
     *
     * @return the abstract text
     */
    public String getAbstractText() {
        return abstractText;
    }

    /**
     * Returns a link to the research paper.
     *
     * @return the paper's URL
     */
    public String getUrl() {
        return url;
    }
}

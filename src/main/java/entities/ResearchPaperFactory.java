package entities;
import java.util.List;

public class ResearchPaperFactory {


    /**
     * Creates a new research paper.
     *
     * @param id            the unique id of the research paper
     * @param title         the title of the research paper
     * @param authors       the authors of the research paper
     * @param year          the year the paper was published
     * @param abstractText  the abstract of the paper
     * @param url           a link to the paper
     * @return the new research paper
     */
    public ResearchPaper create(String id, String title, List<String> authors, int year,
                                String abstractText, String url) {

        return new ResearchPaper(id, title, authors, year, abstractText, url);
    }
}
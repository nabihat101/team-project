package entities;

import java.time.LocalDate;
import java.util.List;

/** A research paper picked for a user on a specific day. */
public class PaperOfTheDay extends ResearchPaper {
    private final String username;
    private final LocalDate date;
    private final List<String> reasons;

    public PaperOfTheDay(ResearchPaper paper, String username,
                              LocalDate date, List<String> reasons) {
        super(paper.getId(), paper.getTitle(), paper.getAuthors(),
                paper.getYear(), paper.getAbstractText(), paper.getUrl());
        this.username = username;
        this.date = date;
        this.reasons = reasons;
    }

    public boolean isForToday() {
        return date.equals(LocalDate.now());
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<String> getReasons() {
        return reasons;
    }

}

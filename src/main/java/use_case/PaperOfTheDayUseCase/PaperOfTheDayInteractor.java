package use_case.PaperOfTheDayUseCase;

import entities.ResearchPaper;
import entities.PaperOfTheDay;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Picks one paper per user per day based on their interests and how recent it is. */
public class PaperOfTheDayInteractor implements PaperOfTheDayInputBoundary {
    private final PaperSearchInterface paperSearch;
    private final PaperOfTheDayDataAccessInterface paperOfTheDayData;
    private final PaperOfTheDayOutputBoundary presenter;

    public PaperOfTheDayInteractor (PaperSearchInterface paperSearch, PaperOfTheDayDataAccessInterface paperOfTheDayData,
                 PaperOfTheDayOutputBoundary presenter ) {
        this.paperSearch = paperSearch;
        this.paperOfTheDayData = paperOfTheDayData;
        this.presenter = presenter;
    }

    @Override
    public void execute(PaperOfTheDayInputData inputData) {
        String username = inputData.getUsername();
        PaperOfTheDay todays = paperOfTheDayData.getTodaysPaper(username);

        // already have one today? show it, return
        if (todays != null) {
            PaperOfTheDayOutputData outputData = new PaperOfTheDayOutputData(todays.getTitle(), todays.getAuthors(),
                    todays.getYear(), todays.getUrl(), todays.getAbstractText(), todays.getReasons());
            presenter.prepareSuccessView(outputData);
            return;
        }

        // get interests
        List<String> interests = paperOfTheDayData.getInterests(username);


        // search papers (try/catch in case OpenAlex fails)
        List<ResearchPaper> recentPapers;

        try {
            recentPapers = paperSearch.searchRecent(interests);
        }

        catch (RuntimeException e) {
            e.printStackTrace();
            presenter.prepareFailView("Couldn't load a paper right now, please try again later.");
            return;
        }

        // remove papers whose id is in getPastPaperIds
        List<ResearchPaper> newPapers = new ArrayList<>();
        List<String> pastIds = paperOfTheDayData.getPastPaperIds(username);

        for (ResearchPaper currentPaper : recentPapers) {
            if (!pastIds.contains(currentPaper.getId())) {
                newPapers.add(currentPaper);
            }

        }

        // if there are no papers print error message
        if (newPapers.isEmpty()) {
            presenter.prepareFailView("No unseen papers found.");
            return;
        }

        // score each, keep the best
        ResearchPaper bestPaper = null;
        double maxScore = -1;
        for (ResearchPaper paper: newPapers) {
            double score = score(paper, interests);
            if (score > maxScore) {
                maxScore = score;
                bestPaper = paper;
            }

        }

        // build reasons, save new PaperOfTheDay
        List<String> reasons = buildReasons(bestPaper, interests);

        // build OutputData, prepareSuccessView
        PaperOfTheDay pick = new PaperOfTheDay(bestPaper, username, LocalDate.now(), reasons);
        paperOfTheDayData.save(pick);
        PaperOfTheDayOutputData outputData = new PaperOfTheDayOutputData(pick.getTitle(), pick.getAuthors(),
                pick.getYear(), pick.getUrl(), pick.getAbstractText(), pick.getReasons());
        presenter.prepareSuccessView(outputData);
    }

    private double score(ResearchPaper paper, List<String> interests) {
        // TODO: implement citations in the scoring algorithm

        String text = ((paper.getTitle() + " " + paper.getAbstractText()).toLowerCase());
        int matches = 0;

        // check which interests match abstract or title text
        for (String interest : interests) {
            if (text.contains(interest.toLowerCase())) {
                matches += 1;
            }
        }
        double interestScore = 0;

        // get interest score an
        if (!interests.isEmpty()) {
            interestScore = (double) matches / interests.size();
        }

        int yearsOld = LocalDate.now().getYear() - paper.getYear();
        double recency = 1.0 / (1 + yearsOld);
        return 0.7 * interestScore + 0.3 * recency;
    }

    private List<String> buildReasons(ResearchPaper paper, List<String> interests) {
        List<String> reasons = new ArrayList<>();

        String text = ((paper.getTitle() + " " + paper.getAbstractText()).toLowerCase());

        for (String interest: interests) {
            if (text.contains(interest.toLowerCase())) {
                reasons.add("Matches your interest in " + interest);
            }
        }

        if (paper.getYear() == LocalDate.now().getYear()) {
            reasons.add("Published this year");
        }

        return reasons;
    }
}

package use_case.PaperOfTheDayUseCase;

import entities.PaperOfTheDay;
import java.util.List;


/**
 * DAO for the Paper of The Day use case.
 */
public interface PaperOfTheDayDataAccessInterface {

    /**
     * Recommend today's paper to the user
     * @param username the user's username
     * @return today's paper, or null if it hasn't been picked yet
     */
    PaperOfTheDay getTodaysPaper(String username);

    List<String> getPastPaperIds(String username);

    List<String> getInterests(String username);

    void save(PaperOfTheDay paperOfTheDay);

}

package use_case.PaperOfTheDayUseCase;

import entities.ResearchPaper;
import java.util.List;


/**
 * Search for research papers
 */
public interface PaperSearchInterface {
    /**
     * Gets recent papers related to the given interests!
     * @param interests the user's selected research interests
     * @return a list of recently published papers that match the interests
     */
    List<ResearchPaper> searchRecent(List<String> interests);
}

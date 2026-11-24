package use_case.RankedProfessorCase;


import java.util.List;

/**
 * The input data for the Ranked Professors use case, from the controller.
 */
public class RankedProfessorInputData {

    private final String username;
    private final List<String> topicFilters;
    private final List<String> locationFilters;

    /**
     * Constructor for RankedProfessorInputData.
     *
     * @param username the username of the user
     * @param topicFilters the list of topic filters
     * @param locationFilters the list of insttitions
     */
    public RankedProfessorInputData(String username, List<String> topicFilters,
                                     List<String> locationFilters) {
        this.username = username;
        this.topicFilters = List.copyOf(topicFilters);
        this.locationFilters = List.copyOf(locationFilters);
    }

    /**
     * Returns the username of the user.
     *
     * @return username
     */
    public String getUsername() {
        return username; 
    }

    /**
     * Returns the list of topic filters.
     *
     * @return topicFilters
     */
    public List<String> getTopicFilters() {
        return topicFilters; 
    }

    /**
     * Returns the list of location filters.
     *
     * @return locationFilters
     */
    public List<String> getLocationFilters() {
        return locationFilters; 
    }
}
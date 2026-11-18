package data_access;
import use_case.FilterOptionsDataAccessInterface;
import java.util.List;

/**
 * A data access object that provides filter options for research areas and locations. To be used in dropdown menus in the signup and ranked professors use cases.
 */

public class FilterOptionsDataAccessObject implements FilterOptionsDataAccessInterface {

    private final List<String> topics;
    private final List<String> locations;

    /**
     * Constructor for FilterOptionsDataAccessObject.
     *
     * @param topics the list of available research areas
     * @param locations the list of available locations
     */
    public FilterOptionsDataAccessObject(List<String> topics, List<String> locations) {
        this.topics = List.copyOf(topics);
        this.locations = List.copyOf(locations);
    }

    /**
     * Returns the list of available research areas.
     *
     * @return list of research areas
     */
    @Override
    public List<String> getTopicOptions() { 
        return topics; 
    }

    /**
     * Returns the list of available locations.
     *
     * @return list of locations
     */
    @Override
    public List<String> getLocationOptions() { 
        return locations; 
    }
}
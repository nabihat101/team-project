package use_case;
import java.util.List;

/**
 * INTENTIONALLY PLACED OUTSIDE OF A SINGLE USE CASE PACKAGE
 * Interface for accessing filter options for BOTH signup and ranked professors use cases. 
 * Implementations of this interface should provide the available research areas and locations that users can select from.
 */

public interface FilterOptionsDataAccessInterface {
    /** Research areas the user can pick from, e.g. OpenAlex subfield names. */
    List<String> getTopicOptions();

    /** Locations the user can pick from, e.g. Canadian institution names. */
    List<String> getLocationOptions();
}

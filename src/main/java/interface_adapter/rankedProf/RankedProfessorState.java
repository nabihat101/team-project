package interface_adapter.rankedProf;

import use_case.RankedProfessorCase.RankedProfessorEntry;

import java.util.ArrayList;
import java.util.List;

/** Holds what the ranked professors page displays, for Ranked Professor View Model
 * Contains getter and setter methods fot all components of view page. */

public class RankedProfessorState {

    private List<RankedProfessorEntry> professors = new ArrayList<>();
    private List<String> topicsUsed = new ArrayList<>();
    private List<String> locationsUsed = new ArrayList<>();
    private List<String> topicOptions = new ArrayList<>();      // checkbox choices
    private List<String> locationOptions = new ArrayList<>();   // checkbox choices
    private String error;

    public List<RankedProfessorEntry> getProfessors() {
        return professors;
    }

    public void setProfessors(List<RankedProfessorEntry> professors) {
        this.professors = professors;
    }

    public List<String> getTopicsUsed() {
        return topicsUsed;
    }

    public void setTopicsUsed(List<String> topicsUsed) {
        this.topicsUsed = topicsUsed;
    }

    public List<String> getLocationsUsed() {
        return locationsUsed;
    }

    public void setLocationsUsed(List<String> locationsUsed) {
        this.locationsUsed = locationsUsed;
    }

    public List<String> getTopicOptions() {
        return topicOptions;
    }

    public void setTopicOptions(List<String> topicOptions) {
        this.topicOptions = topicOptions;
    }

    public List<String> getLocationOptions() {
        return locationOptions;
    }

    public void setLocationOptions(List<String> locationOptions) {
        this.locationOptions = locationOptions;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
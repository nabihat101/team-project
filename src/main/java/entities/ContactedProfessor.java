package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContactedProfessor extends Professor {

    private final LocalDate contactedOn;
    private final boolean responded;

    /**
     *
     * @param id            The professor's unique ID
     * @param name          The professor's full name
     * @param institution   The institution the professor currently works at
     * @param contactedOn   The date the professor was contacted on
     * @param responded     Whether the professor has responded or not
     */
    public ContactedProfessor(String id, String name, String institution, LocalDate contactedOn,
                              boolean responded) {
        this(id, name, institution, new ArrayList<>(), new ArrayList<>(), contactedOn, responded);
    }


    /**
     *
     * @param id                The professor's unique ID
     * @param name              The professor's full name
     * @param institution       The institution the professor currently works at
     * @param researchAreas     The research areas of the professor
     * @param publications      The published papers of the professor
     * @param contactedOn       The date the professor was contacted on
     * @param responded         Whether the professor has responded or not
     */
    public ContactedProfessor(String id, String name, String institution,
                              List<String> researchAreas,
                              List<ResearchPaper> publications, LocalDate contactedOn,
                              boolean responded) {
        super(id, name, institution, researchAreas, publications);
        this.contactedOn = contactedOn;
        this.responded = responded;
    }

    /**
     *
     * @return the date the professor was contacted on
     */
    public LocalDate getContactedOn() {
        return contactedOn;
    }

    /**
     *
     * @return whether the professor has responded or not
     */
    public boolean hasResponded() {
        return responded;
    }
}
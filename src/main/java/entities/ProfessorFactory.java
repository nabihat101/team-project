package entities;

import java.util.List;

public class ProfessorFactory {

    /**
     * Creates a new professor.
     *
     * @param id            the ID of the professor
     * @param name          the full name of the professor
     * @param institution   the institution the professor works at
     * @param researchAreas the research areas the professor works in
     * @param publications  the research papers the professor has published
     * @return the new professor
     */

    public Professor create(String id, String name, String institution, List<String> researchAreas,
                            List<ResearchPaper> publications) {

        return new Professor(id, name, institution, researchAreas, publications);
    }
}

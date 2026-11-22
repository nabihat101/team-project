package use_case.professor_pages;

import entities.Professor;
import entities.ResearchPaper;

import java.util.List;

/**
 * Defines the methods for how we communicate with OpenAlex API to retrieve information
 */
public interface ProfessorDataAccessInterface {
    Professor getProfessorById(String professorId);
    List<ResearchPaper> getPublications(String professorId);
}

package use_case.rankedprof;

import entities.Professor;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.RankedProfessorCase.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* This unit tests class tests the Ranked Professor Interactor. See individual tests for more info */
class RankedProfessorInteractorTest {

    private InMemoryProfessorDataAccess professorDao;
    private TestPresenter presenter;

    /** Records whatever the interactor sends to the presenter. */
    private static class TestPresenter implements RankedProfessorOutputBoundary {
        RankedProfessorOutputData successData;
        String errorMessage;

        @Override
        public void prepareSuccessView(RankedProfessorOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    @BeforeEach
    void setUp() {
        // Leave publication empty because we won't be using in this use case
        professorDao = new InMemoryProfessorDataAccess();
        // Added out of alphabetical order on purpose, so the sorting test means something.
        professorDao.add(new Professor("P2", "Alan Turing", "York University",
                List.of("Machine Learning", "Databases"), List.of()));
        professorDao.add(new Professor("P1", "Ada Lovelace", "University of Toronto",
                List.of("Machine Learning", "Robotics"), List.of()));
        professorDao.add(new Professor("P3", "Grace Hopper", "University of Toronto",
                List.of("Databases"), List.of()));
        presenter = new TestPresenter();
    }

    /** Builds an interactor whose user DAO always returns the given user. */
    private RankedProfessorInteractor interactorFor(final User user) {
        UserDataAccessInterface userDao = new UserDataAccessInterface() {
            @Override
            public User get(String username) {
                return user;
            }
        };
        return new RankedProfessorInteractor(userDao, professorDao, presenter);
    }

    private List<String> names(RankedProfessorOutputData data) {
        List<String> result = new ArrayList<>();
        for (RankedProfessorEntry entry : data.getProfessors()) {
            result.add(entry.getName());
        }
        return result;
    }

    @Test
    void selectedTopicsOverrideProfile() {
        User user = new User("sam", "password", List.of("Databases"), List.of());
        interactorFor(user).execute(
                new RankedProfessorInputData("sam", List.of("Robotics"), List.of()));

        assertNull(presenter.errorMessage);
        assertEquals(List.of("Ada Lovelace"), names(presenter.successData));
        assertEquals(List.of("Robotics"), presenter.successData.getTopicsUsed());
    }

    @Test
    void nothingSelectedFallsBackToProfile() {
        User user = new User("sam", "password", List.of("Databases"), List.of());
        interactorFor(user).execute(
                new RankedProfessorInputData("sam", List.of(), List.of()));

        assertNull(presenter.errorMessage);
        assertEquals(List.of("Alan Turing", "Grace Hopper"), names(presenter.successData));
        assertEquals(List.of("Databases"), presenter.successData.getTopicsUsed());
    }

    @Test
    void moreMatchesRankFirst() {
        User user = new User("sam", "password", List.of("Machine Learning"), List.of());
        interactorFor(user).execute(new RankedProfessorInputData("sam",
                List.of("Machine Learning", "Robotics"), List.of()));

        // Ada matches both topics, Alan matches one.
        assertEquals(List.of("Ada Lovelace", "Alan Turing"), names(presenter.successData));
        assertEquals(List.of("Machine Learning", "Robotics"),
                presenter.successData.getProfessors().get(0).getMatchedAreas());
    }

    @Test
    void tiesAreSortedAlphabeticallyByName() {
        User user = new User("sam", "password", List.of("Machine Learning"), List.of());
        interactorFor(user).execute(new RankedProfessorInputData("sam",
                List.of("Machine Learning"), List.of()));

        // Both match one topic; "Ada" comes before "Alan".
        assertEquals(List.of("Ada Lovelace", "Alan Turing"), names(presenter.successData));
    }

    @Test
    void noMatchingProfessorsFails() {
        User user = new User("sam", "password", List.of("Machine Learning"), List.of());
        interactorFor(user).execute(new RankedProfessorInputData("sam",
                List.of("Quantum Computing"), List.of()));

        assertNull(presenter.successData);
        assertNotNull(presenter.errorMessage);
    }

    @Test
    void noInterestsAndNothingSelectedFails() {
        User user = new User("sam", "password", List.of(), List.of());
        interactorFor(user).execute(
                new RankedProfessorInputData("sam", List.of(), List.of()));

        assertNull(presenter.successData);
        assertNotNull(presenter.errorMessage);
    }
}
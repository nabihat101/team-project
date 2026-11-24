package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppBuilder appBuilder = new AppBuilder();
            JFrame application = appBuilder
                    .addLoginView()
                    .addLoginUseCase()
                    .addProfessorPageView()
                    .addProfessorPageUseCase()
                    .addPaperOfTheDayView()
                    .addPaperOfTheDayUseCase()
                    .addRankedProfPage()
                    .addRankedProfUseCase()
                    .addUserProfileView()
                    .addUserProfileUseCase()
                    .build();

            application.setVisible(true);
        });
    }
}

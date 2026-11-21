package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addLoginUseCase()
                .addPaperOfTheDayView()
                .addPaperOfTheDayUseCase()
                .build();

        // TESTING ONLY: skip login and open Paper of the Day
        appBuilder.showPaperOfTheDayFor("dana");

        application.setVisible(true);
    }
}
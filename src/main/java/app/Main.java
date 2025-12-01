package app;

import javax.swing.*;
import java.awt.*;

public class Main {
    /**
     * Main method for running the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .initializeWorkbookDataAccessObject()
                .initializeCourseRepository()
                .addSearchCoursesUseCase()
                .addMainPanel()
                .addWorkbookPersistenceDataAccessObject()
                .addSaveWorkbookUseCase()
                .addLoadWorkbookUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setExtendedState(Frame.MAXIMIZED_BOTH);
        application.setVisible(true);
    }
}